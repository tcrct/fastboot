package org.fastboot.exception.template;

import org.fastboot.exception.common.AbstractExceptionTemplate;
import org.fastboot.exception.dto.ExceptionResultDto;
import org.fastboot.exception.utils.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 空指针异常处理模板
 *
 * @author Laotang
 * @version 1.0
 */
public class NullPointerExceptionTemplate extends AbstractExceptionTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(NullPointerExceptionTemplate.class);

    @Override
    public Class<?> exceptionClass() {
        return NullPointerException.class;
    }

    @Override
    public ExceptionResultDto handle(Exception exception) {
        LOGGER.info("空指针异常");
        NullPointerException nullPointerException = (NullPointerException) exception;
        ExceptionResultDto exceptionResultDto =  new ExceptionResultDto();
        exceptionResultDto.setCode(1);
        exceptionResultDto.setMessage("空指针异常");
        exceptionResultDto.setStackMsg(Exceptions.getStackTraceAsString(exception));
        LOGGER.info(exceptionResultDto.getStackMsg());
        return exceptionResultDto;
    }

}
