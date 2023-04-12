package com.example.hk.demo.base;


/**
 * Created by zhongr on 2017/8/25.
 */
public enum BizEnum {

    BIZ_ERROR("-1001", "服务异常"),
    SERVICE_ERROR(ResultDto.SYS_ERROR, "服务异常");
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    BizEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
