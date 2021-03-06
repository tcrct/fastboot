package org.fastboot.exception.nums;

/**
 * 基础异常信息
 *
 * @author Laotang
 * @version 1.0
 */
public enum ExceptionEnum implements IExceptionEnum {

    PARAM_NULL(1000, "参数为空"),
    PARAM_INCORRECT(1001, "参数验证不通过"),



    ;
    private Integer code;
    private String message;

    private ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
