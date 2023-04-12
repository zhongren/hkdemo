package com.example.hk.demo;

import com.example.hk.demo.util.JsonUtil;
import com.example.hk.demo.util.tx.TxAiConfig;
import com.example.hk.demo.util.tx.TxAiHttpUtil;
import com.example.hk.demo.util.tx.TxApiEnum;
import com.example.hk.demo.util.tx.req.TxImageDefectReq;
import com.example.hk.demo.util.tx.resp.TxImageDefectResp;
import org.junit.Test;

/**
 * @description:
 * @author: ren.zhong@chebada.com
 * @date: 2023年04月12日10:11:52
 **/
public class TxTest {
    private static String testUrl = "";
    private static String prdUrl = "";

    public TxAiConfig testConfig(String apiUrl) {
        TxAiConfig appConfig = TxAiConfig.builder().apiUrl(apiUrl).build();
        return appConfig;
    }

    public TxAiConfig prdConfig(String apiUrl) {
        TxAiConfig appConfig = TxAiConfig.builder().apiUrl(apiUrl).build();
        return appConfig;
    }

    @Test
    public void imageDefect() {
        try {
            TxAiConfig appConfig = testConfig(testUrl);
            TxAiHttpUtil txAiHttpUtil = new TxAiHttpUtil();
            TxImageDefectReq req = new TxImageDefectReq();
            TxImageDefectResp txImageDefectResp = txAiHttpUtil.getResult(appConfig, TxApiEnum.industry_image_defect, req, TxImageDefectResp.class);
            // System.out.println(openCityResp.getData().getOpenCitysList().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}