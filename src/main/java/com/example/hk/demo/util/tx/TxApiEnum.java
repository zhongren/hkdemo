package com.example.hk.demo.util.tx;

/**
 * 哈喽顺风车 api
 */
public enum TxApiEnum {

    industry_image_defect("industry/image_defect/","单位点推理(提交样本图片检测)", "POST", true),

    accessToken("auth/access_token", "获取token", "GET", false),
    config("carpoolweb/trip/config", "获取配置", "GET", true),
    price("carpoolweb/trip/ride/price", "询价", "GET", true),
    createOrder("carpoolweb/trip/ride/create", "下单", "POST", true),
    getOrderDetail("carpoolweb/trip/ride/detail", "订单详情", "GET", true),
    cancelOrder("carpoolweb/trip/ride/cancel", "取消订单", "POST", true),
    driverOn("carpoolweb/trip/ride/getOn", "乘客上车", "POST", true),
    driverOff("carpoolweb/trip/ride/getOff", "乘客下车", "POST", true),
    pay("carpoolweb/trip/ride/pay", "乘客支付", "POST", true),
    subsidy("carpoolweb/trip/ride/subsidy", "发送乘客感谢费", "POST", true),
    phoneNum("carpoolweb/trip/ride/phoneNum", "获取虚拟号", "GET", true),
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

