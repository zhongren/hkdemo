package com.example.hk.demo.util.tx;

/**
 * 哈喽顺风车 api
 */
public enum TxApiEnum {

    industry_image_defect("industry/image_defect/","单位点推理(提交样本图片检测)", "POST", true),
    ;
    private String name;
    private String desc;
    private String type;
    private Boolean auth;

    TxApiEnum(String name, String desc, String type, Boolean auth) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.auth = auth;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public Boolean getAuth() {
        return auth;
    }
}

