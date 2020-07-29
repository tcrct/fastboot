package org.fastboot.exception.template;

import org.beetl.sql.core.BeetlSQLException;
import org.fastboot.common.utils.LogUtils;
import org.fastboot.exception.common.AbstractExceptionTemplate;
import org.fastboot.exception.dto.ExceptionResultDto;
import org.fastboot.exception.utils.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * BeetlSQL 异常处理
 *
 * @author Laotang
 * @version 1.0
 */
public class HttpMessageNotReadableExceptionTemplate extends AbstractExceptionTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpMessageNotReadableExceptionTemplate.class);

    @Override
    public Class<?> exceptionClass() {
        return HttpMessageNotReadableException.class;
    }

    @Override
    public ExceptionResultDto handle(Exception exception) {
        HttpMessageNotReadableException e = (HttpMessageNotReadableException) exception;
        ExceptionResultDto exceptionResultDto =  new ExceptionResultDto();
        exceptionResultDto.setCode(5000);
        exceptionResultDto.setMessage(e.getMessage());
        exceptionResultDto.setStackMsg(Exceptions.getStackTraceAsString(exception));
        LogUtils.log(LOGGER, e.getMessage(), exception);
        return exceptionResultDto;
    }

}
