package com.example.hk.demo.util.tx;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * @description:
 * @author: ren.zhong@chebada.com
 * @date: 2021年05月11日18:11:35
 **/
@Setter
@Getter
public class TxAiResp implements Serializable {

    /**
     * 请求失败返回的错误码
     */
    private Integer code;
    /**
     * 请求失败返回的错误信息
     */
    private String message;

}