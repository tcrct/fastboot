package org.fastboot.db.curd;

import cn.hutool.core.util.ReflectUtil;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.core.query.Query;
import org.fastboot.common.utils.LogUtils;
import org.fastboot.common.utils.SettingKit;
import org.fastboot.common.utils.SpringKit;
import org.fastboot.db.dto.PageDto;
import org.fastboot.db.dto.SearchListDto;
import org.fastboot.db.model.BaseEntity;
import org.fastboot.db.model.IdEntity;
import org.fastboot.db.model.Update;
import org.fastboot.db.utils.DbKit;
import org.fastboot.exception.common.ServiceException;
import org.fastboot.redis.crud.ICurdCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公用的CURD方法服务基类
 *
 * @author Laotang
 * @since 1.0
 */
public class CurdService<T> implements ICurdService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurdService.class);
    private static final Map<Class, ICurdCacheService> CURD_CACHE_SERVICE_MAP = new ConcurrentHashMap<>();

    @Autowired
    private SQLManager manager;

    /** 取泛型对象类型
     * @return
     */
    protected Class<T> getGenericTypeClass() {
        return (Class<T>) DbKit.getSuperClassGenericType(getClass(), 0);
    }

    protected Boolean isCache() {
        return SettingKit.duang().key("cache.enable").defaultValue(true).getBoolean();
    }

    private ICurdCacheService getCacheService() {
        try {
            Class<T> clazz = getGenericTypeClass();
            ICurdCacheService curdCacheService = CURD_CACHE_SERVICE_MAP.get(clazz);
            if (null != curdCacheService) {
                return curdCacheService;
            }
            // 不存在则查找
            Object serviceImpl = SpringKit.getBeanByGenericType(clazz);
            Class[] interfaceCls = serviceImpl.getClass().getInterfaces();
            if (null == interfaceCls) {
                throw new NullPointerException("根据泛型[" + clazz.getName() + "]没有找到对应CacheService类，该类没有实现ICurdCacheService接口，请检查！");
            }

            if (ICurdCacheService.class.equals(interfaceCls[0])) {
                curdCacheService = (ICurdCacheService<T>) serviceImpl;
                if (null == curdCacheService) {
                    throw new NullPointerException("根据泛型[" + clazz.getName() + "]没有找到对应CurdService类，请检查！");
                }
                CURD_CACHE_SERVICE_MAP.put(clazz, curdCacheService);
                return curdCacheService;
            }
            if (null == curdCacheService) {
                throw new NullPointerException("根据泛型[" + clazz.getName() + "]没有找到对应CurdService类，请检查！");
            }
            return null;
        } catch (Exception e) {
            LogUtils.log(LOGGER,"CurdService执行getCacheService方法时: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据ID查找对象
     * @param id id主键
     * @return
     */
    @Override
    public T findById(Serializable id) {
        if (isCache()) {
            T entity = (T)getCacheService().findById(id.toString());
            if (null != entity) {
                return entity;
            }
        }
        return manager.unique(getGenericTypeClass(), id);
    }

    /**
     * 根据ID，逻辑删除记录(将表中status字段值改为0)
     * @param id 待删除的记录ID
     * @return 成功删除返回受影响的行数
     */
    @Override
    public Integer deleteById(Serializable id) {
        Object entity = ReflectUtil.newInstance(getGenericTypeClass());
        ReflectUtil.setFieldValue(entity, BaseEntity.ID_FIELD, id);
        //设置为逻辑删除
        ReflectUtil.setFieldValue(entity, BaseEntity.STATUS_FIELD, 1);
        if (isCache()) {
            getCacheService().deleteById(id.toString());
        }
        return save((T)entity);
    }

    /**
     * 保存操作
     * 以entity里是否有id值来判断是新增还是更新操作。
     * 更新操作时，会自动根据entity不为null的字段进行更新。
     *
     * @param entity 待持久化/更新的对象
     * @return
     */
    @Override
    public Integer save(T entity) {
        try {
            int count = 0;
            BaseEntity baseEntity = (BaseEntity) entity;
            if (null == baseEntity.getId() || "".equals(baseEntity.getId())) {
                DbKit.addBaseEntityValue(baseEntity);
                count =  manager.insert(getGenericTypeClass(), baseEntity);
                if (count > 0 && isCache()) {
                    getCacheService().save(entity);
                }
            } else {
                String tableName = manager.getDbStyle().getNameConversion().getTableName(entity.getClass());
                TableDesc tableDesc = manager.getMetaDataManager().getTable(tableName);
                DbKit.updatBaseEntityValue(baseEntity);
                Update update = new Update(tableDesc.getName(), baseEntity);
                count = manager.executeUpdate(new SQLReady(update.getUpdateSql(), update.getParams().toArray()));
                if (count > 0 && isCache()) {
                   getCacheService().deleteById(baseEntity.getId().toString());
                }
            }
            return count;
        } catch (Exception e) {
            LogUtils.log(LOGGER, e.getMessage(), e);
            throw new ServiceException(1, e.getMessage(), e);
        }
    }

    /**
     * 根据条件搜索记录，返回分页对象
     * @param searchListDto 搜索条件对象
     * @return PageDto
     */
    @Override
    public PageDto<T> search(SearchListDto searchListDto) {
        Class<T> genericTypeClass = getGenericTypeClass();
        PageDto<T> pageDto = new PageDto<>();
        String sqlId = searchListDto.getKey();
        List<T> resultList = null;
        Query<T> query = manager.query(genericTypeClass);
        if (null != sqlId) {
            resultList = manager.select(sqlId, genericTypeClass, searchListDto.toMap());
        } else {
            query = DbKit.createQueryCondition(query, searchListDto);
            // 默认按id desc排序
            query = query.orderBy(searchListDto.toOrderByStr());
            if (null != searchListDto.getGroupByList()) {
                query.groupBy(searchListDto.toGroupByStr());
            }
            query.limit(searchListDto.getPageNo(), searchListDto.getPageSize());
            resultList = (null == searchListDto.getFieldList()) ? query.select() :
                    query.select(searchListDto.getFieldList().toArray(new String[]{}));
        }
        pageDto.setAutoCount(true);
        pageDto.setResult(resultList);
        StringBuilder sql = query.getSql();
        sql.delete(0, sql.indexOf("WHERE"));
        query.setSql(sql);
        pageDto.setTotalCount(query.count());
        return pageDto;
    }
}
