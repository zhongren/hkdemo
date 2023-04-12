package com.example.hk.demo.util.tx;


import com.example.hk.demo.base.BaseException;
import com.example.hk.demo.util.JsonUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import okio.BufferedSink;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * 接入文档 http://b-ecs.didapinche.com/docs/jjzx/auth/token.html
 */
@Slf4j
public class TxAiHttpUtil {

    public static final MediaType JSON = MediaType.get("application/json");


    /**
     * 超时时间 单位秒
     */
    private static final int timeout = 10;

    private static TxAiConfig initApiConfig() {
        String signKey = "";
        String secret = "";
        String clientId = "";
        String apiUrl = "";
        return TxAiConfig.builder().apiUrl(apiUrl).build();
    }


    /**
     * 获取api结果
     *
     * @param txApiEnum
     * @param param
     * @param clazz
     * @param <Q>
     * @param <B>
     * @return
     */
    public static <Q extends TxAiReq, B extends TxAiResp> B getResult(TxApiEnum txApiEnum, Q param, Class<B> clazz) {
        TxAiConfig txAIConfig = initApiConfig();
        return getResult(txAIConfig, txApiEnum, param, clazz);
    }

    public static <Q extends TxAiReq, B extends TxAiResp> B getResult(TxAiConfig txAIConfig, TxApiEnum txApiEnum, Q param, Class<B> clazz) {
        // 发送请求
        String respStr = null;
        String url = txAIConfig.getApiUrl() + txApiEnum.getName();
        String jsonReq = JsonUtil.objectToJson(param);
        //post请求
        respStr = post(url, jsonReq, txApiEnum);
        if (StringUtils.isEmpty(respStr)) {
            TxAiResp didaResp = JsonUtil.jsonToBean(respStr, clazz);
            if (!TxRespCodeEnum.SUCCESS.getCode().equals(didaResp.getCode())) {
                TxRespCodeEnum errorEnum = TxRespCodeEnum.getByCode(didaResp.getCode());
                throw new BaseException(didaResp.getMessage(), String.valueOf(errorEnum.getCode()));
            }
            return JsonUtil.jsonToBean(respStr, clazz);
        }
        throw new BaseException(TxRespCodeEnum.ERROR.getMsg(), String.valueOf(TxRespCodeEnum.ERROR.getCode()));
    }

    private static String post(String url, String jsonParam, TxApiEnum txApiEnum) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS).build();
            RequestBody body = new RequestBody() {
                @Override
                public okhttp3.MediaType contentType() {
                    return JSON;
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    sink.writeUtf8(jsonParam);
                }
            };
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            StopWatch sw = new StopWatch();
            sw.start();
            Response response = client.newCall(request).execute();
            sw.stop();
            String resp=response.body().string();

            return resp;
        } catch (Exception e) {
            log.error("tx请求异常! {} {}", e.getMessage(), e);
            throw new BaseException(TxRespCodeEnum.ERROR.getMsg(), String.valueOf(TxRespCodeEnum.ERROR.getCode()));
        }
    }
}
