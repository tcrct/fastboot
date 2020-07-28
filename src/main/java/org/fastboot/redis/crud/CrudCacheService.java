package org.fastboot.redis.crud;

/**
 * 通用CURD Cache层
 *
 * @author Laotang
 * @since 1.0
 */
public class CrudCacheService<T> implements ICurdCacheService<T> {

    @Override
    public Integer save(T entity) {
        return 0;
    }

    @Override
    public T findById(String key) {
        return null;
    }

    @Override
    public Integer deleteById(String key) {
        return 0;
    }
}
