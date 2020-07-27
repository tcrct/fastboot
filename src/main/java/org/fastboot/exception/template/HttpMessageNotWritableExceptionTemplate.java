package org.fastboot.exception.template;

import org.fastboot.exception.common.AbstractExceptionTemplate;
import org.fastboot.exception.dto.ExceptionResultDto;
import org.fastboot.exception.utils.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotWritableException;

/**
 * HttpMessageNotWritable 异常处理
 * 抛出该异常，一般是Entity/Dto里没有get/set方法
 *
 * @author Laotang
 * @version 1.0
 */
public class HttpMessageNotWritableExceptionTemplate extends AbstractExceptionTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpMessageNotWritableExceptionTemplate.class);

    @Override
    public Class<?> exceptionClass() {
        return HttpMessageNotWritableException.class;
    }

    @Override
    public ExceptionResultDto handle(Exception exception) {
        HttpMessageNotWritableException e = (HttpMessageNotWritableException) exception;
        ExceptionResultDto exceptionResultDto =  new ExceptionResultDto();
        exceptionResultDto.setCode(1);
        exceptionResultDto.setMessage("请确保Entity/Dto类已经正确设置get/set方法，异常信息: " + e.getMessage());
        exceptionResultDto.setStackMsg(Exceptions.getStackTraceAsString(exception));
        LOGGER.info(exceptionResultDto.getStackMsg());
        return exceptionResultDto;
    }

}
