package org.fastboot.exception.template;

import org.fastboot.common.utils.LogUtils;
import org.fastboot.exception.common.AbstractExceptionTemplate;
import org.fastboot.exception.dto.ExceptionResultDto;
import org.fastboot.exception.utils.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * BeetlSQL 异常处理
 *
 * @author Laotang
 * @version 1.0
 */
public class HttpRequestMethodNotSupportedExceptionTemplate extends AbstractExceptionTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestMethodNotSupportedExceptionTemplate.class);

    @Override
    public Class<?> exceptionClass() {
        return HttpRequestMethodNotSupportedException.class;
    }

    @Override
    public ExceptionResultDto handle(Exception exception) {
        HttpRequestMethodNotSupportedException e = (HttpRequestMethodNotSupportedException) exception;
        ExceptionResultDto exceptionResultDto =  new ExceptionResultDto();
        exceptionResultDto.setCode(1);
        exceptionResultDto.setMessage(e.getMessage());
        exceptionResultDto.setStackMsg(Exceptions.getStackTraceAsString(exception));
        LogUtils.log(LOGGER, e.getMessage(), exception);
        return exceptionResultDto;
    }

}
