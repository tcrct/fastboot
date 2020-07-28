package org.fastboot.exception.template;


import org.fastboot.common.utils.LogUtils;
import org.fastboot.common.utils.ToolsKit;
import org.fastboot.exception.common.AbstractExceptionTemplate;
import org.fastboot.exception.dto.ExceptionResultDto;
import org.fastboot.exception.utils.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * 方法参数验证异常处理模板
 *
 * @author Laotang
 * @version 1.0
 */
public class MethodArgumentNotValidExceptionTemplate extends AbstractExceptionTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodArgumentNotValidExceptionTemplate.class);

    @Override
    public Class<?> exceptionClass() {
        return MethodArgumentNotValidException.class;
    }

    @Override
    public ExceptionResultDto handle(Exception exception) {
        MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
        StringBuilder exceptionStr = new StringBuilder();
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                exceptionStr.append("[")
                        .append(fieldError.getField())
                        .append(":")
                        .append(fieldError.getDefaultMessage())
                        .append("];");
            }
        }
        ExceptionResultDto exceptionResultDto =  new ExceptionResultDto();
        exceptionResultDto.setCode(1);
        exceptionResultDto.setMessage(exceptionStr.toString());
        exceptionResultDto.setStackMsg(Exceptions.getStackTraceAsString(exception));
        LogUtils.log(LOGGER, exceptionStr.toString(), exception);
        return exceptionResultDto;
    }
}
