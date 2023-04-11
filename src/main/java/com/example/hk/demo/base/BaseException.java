package com.example.hk.demo.base;



/**
 * Created by zhongr on 2017/8/25.
 */
public class BaseException extends RuntimeException {
    private String code;


    public BaseException(BusinessEnum businessEnum) {
        this(businessEnum.getMsg(), businessEnum.getCode());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BaseException(String message) {
        this.code = code;
    }

    public BaseException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BaseException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public BaseException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
