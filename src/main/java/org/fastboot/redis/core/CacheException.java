package org.fastboot.redis.core;

/**
 * 缓存异常
 *
 * @author Laotang
 * @since 1.0
 */
public class CacheException extends RuntimeException {

    public CacheException(String errMessage) {
           super(errMessage);
    }

    public CacheException(String errMessage, Throwable e) {
        super(errMessage, e);
    }
}
