package org.fastboot.exception.template;

import org.fastboot.common.utils.LogUtils;
import org.fastboot.exception.common.AbstractExceptionTemplate;
import org.fastboot.exception.common.ServiceException;
import org.fastboot.exception.dto.ExceptionResultDto;
import org.fastboot.exception.utils.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常枚举处理模板
 *
 * @author Laotang
 * @version 1.0
 */
public class ServiceExceptionTemplate extends AbstractExceptionTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionTemplate.class);

    @Override
    public Class<?> exceptionClass() {
        return ServiceException.class;
    }

    @Override
    public ExceptionResultDto handle(Exception exception) {
        LOGGER.info("异常枚举抛出异常");
        ServiceException serviceException = (ServiceException) exception;
        ExceptionResultDto exceptionResultDto =  new ExceptionResultDto();
        exceptionResultDto.setCode(serviceException.getCode());
        exceptionResultDto.setMessage(serviceException.getMessage());
        exceptionResultDto.setStackMsg(Exceptions.getStackTraceAsString(exception));
        LogUtils.log(LOGGER, serviceException.getMessage(), exception);
        return exceptionResultDto;
    }

}
