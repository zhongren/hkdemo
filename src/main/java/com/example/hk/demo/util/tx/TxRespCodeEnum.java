package com.example.hk.demo.util.tx;

public enum TxRespCodeEnum {
    SUCCESS(0, "SUCCESS"),
    ERROR1(-1001, "未知错误"),
    ERROR2(-1002, "参数不合法"),
    ERROR3(-1003, "GPU资源不足"),
    ERROR4(-1004, "数据不存在"),
    ERROR(-1, "其他错误（需要对方配合）"),;

    private Integer code;
    private String msg;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    TxRespCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static TxRespCodeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TxRespCodeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return ERROR;
    }
}
