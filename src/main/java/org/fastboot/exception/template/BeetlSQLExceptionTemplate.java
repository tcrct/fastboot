package org.fastboot.exception.template;

import org.fastboot.common.utils.LogUtils;
import org.fastboot.exception.common.AbstractExceptionTemplate;
import org.fastboot.exception.dto.ExceptionResultDto;
import org.beetl.sql.core.BeetlSQLException;
import org.fastboot.exception.utils.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BeetlSQL 异常处理
 *
 * @author Laotang
 * @version 1.0
 */
public class BeetlSQLExceptionTemplate extends AbstractExceptionTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeetlSQLExceptionTemplate.class);

    @Override
    public Class<?> exceptionClass() {
        return BeetlSQLException.class;
    }

    @Override
    public ExceptionResultDto handle(Exception exception) {
        BeetlSQLException sqlException = (BeetlSQLException) exception;
        ExceptionResultDto exceptionResultDto =  new ExceptionResultDto();
        exceptionResultDto.setCode(sqlException.getCode());
        exceptionResultDto.setMessage(sqlException.getMessage());
        exceptionResultDto.setStackMsg(Exceptions.getStackTraceAsString(exception));
        LogUtils.log(LOGGER, sqlException.getMessage(), exception);
        return exceptionResultDto;
    }

}
