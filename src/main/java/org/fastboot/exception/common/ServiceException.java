package org.fastboot.exception.common;

/**
 * Created by laotang on 2020/5/31.
 */
public class ServiceException extends RuntimeException {

    private Integer code;
    private String message;
    private Throwable throwable;

    public ServiceException() {
        super();
    }

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServiceException(Integer code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        this.message = message;
        this.throwable = throwable;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
