package org.fastboot.redis.crud;

public interface ICurdCacheService<T> {

    /**
     * 保存到缓存
     * @param entity
     * @return
     */
    Integer save(T entity);

    /**
     * 根据key值，查找缓存记录
     * @param key 缓存key
     * @return
     */
    T findById(String key);

    /**
     * 根据key值，删除缓存记录
     * @param key 缓存key
     * @return
     */
    Integer deleteById(String key);

}
