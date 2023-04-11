package com.example.hk.demo.util.tx;

import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author: ren.zhong@chebada.com
 * @date: 2021年05月11日16:49:10
 **/
@Data
@Builder
public class TxAIConfig {
    private String apiUrl;
    private String signKey;
    private String secret;
    private String clientId;
}