package com.example.hk.demo.util.tx;


import com.example.hk.demo.base.BaseException;
import com.example.hk.demo.util.JsonUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 接入文档 http://b-ecs.didapinche.com/docs/jjzx/auth/token.html
 */
@Slf4j
public class TXALHttpUtil {


    /**
     * 超时时间 单位毫秒
     */
    private static final int timeout = 1000 * 5;

    private static TxAIConfig initApiConfig() {
        String signKey = "";
        String secret = "";
        String clientId = "";
        String apiUrl = "";
        return TxAIConfig.builder().signKey(signKey).apiUrl(apiUrl).secret(secret).clientId(clientId).build();
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
    public static <Q extends BaseTxAiReq, B extends BaseDidaResp> B getResult(TxApiEnum txApiEnum, Q param, Class<B> clazz, String token) {
        TxAIConfig txAIConfig = initApiConfig();
        return getResult(txAIConfig, txApiEnum, param, clazz, token);
    }

    public static <Q extends BaseTxAiReq, B extends BaseDidaResp> B getResult(TxAIConfig txAIConfig, TxApiEnum txApiEnum, Q param, Class<B> clazz, String token) {
        // 发送请求
        String respStr = null;
        Map<String, Object> signMap = object2TreeMap(param);
        String paramStr = getParamUrl(signMap);
        long unixtimestamp = System.currentTimeMillis();
        //String sign = StringUtils.lowerCase(MD5.encrypt(paramStr + "&timestamp=" + unixtimestamp + "&sign_key=" + txAIConfig.getSignKey())) + " " + unixtimestamp + " " + txAIConfig.getClientId();
        String sign ="";
        String url = txAIConfig.getApiUrl() + txApiEnum.getName();
        //post请求
        respStr = post(url, signMap, txApiEnum, sign, token, txAIConfig.getClientId());
        if (StringUtils.isEmpty(respStr)) {
            BaseDidaResp didaResp = JsonUtil.jsonToBean(respStr, clazz);
            if (!TxRespCodeEnum.SUCCESS.getCode().equals(didaResp.getCode()) ) {
                TxRespCodeEnum errorEnum = TxRespCodeEnum.getByCode(didaResp.getCode());
                throw new BaseException( didaResp.getMessage(),String.valueOf(errorEnum.getCode()));
            }
            return JsonUtil.jsonToBean(respStr, clazz);
        }
        throw new BaseException(TxRespCodeEnum.ERROR.getMsg(),String.valueOf(TxRespCodeEnum.ERROR.getCode()));
    }

    /**
     * 对象转 TreeMap 默认升序排序, 过滤空值
     *
     * @param obj
     * @return
     */
    private static Map<String, Object> object2TreeMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new TreeMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : null;
                // 过滤掉空值属性
                if (value == null || "".equals(value.toString())) {
                    continue;
                }
                map.put(key, value);
            }
        } catch (Exception e) {
            log.warn("对象转换异常{}{}", e.getMessage(), e);
        }
        return map;
    }


    /**
     * 对象参数转url参数
     *
     * @param signMap
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String getParamUrl(Map<String, Object> signMap) {
        List<String> list = Lists.newArrayList();
        if (!ObjectUtils.isEmpty(signMap)) {
            signMap.forEach((k, v) -> {
                StringBuilder sb = new StringBuilder();
                list.add(sb.append(k).append("=").append(v).toString());
            });
            return String.join("&", list);
        }
        return "";
    }



    private static String post(String url, Map<String, Object> paramsMap, TxApiEnum txApiEnum, String sign, String token, String clientId) {
        try {

            Request.Builder request = new Request.Builder();
            request.header("Content-type", "application/x-www-form-urlencoded");
            if (txApiEnum.getAuth()) {
                request.header("Authorization", "Bearer " + token);
                request.header("sign", sign);
                request.header("client_id", clientId);

            }
            request.url(url).post();
            log.info("Http Post Start, URL:{} Sign:{} Authorization:{} Req:{}", url, sign, "Bearer " + token, JsonUtil.objectToJson(paramsMap));
            //TimeTicker.start();
            Response response = new OkHttpClient.Builder().build().newCall(request.build()).execute();
            //TimeTicker.stop();
            ResponseBody resBody = response.body();
            assert resBody != null;
            String bodyString = resBody.string();
            log.info("Http Post Done, URL:{} Req:{} Res:{} ", url, JsonUtil.objectToJson(paramsMap), bodyString);
            return bodyString;
        } catch (Exception e) {
            log.error("tx请求异常! {} {}", e.getMessage(), e);
            throw new BaseException(TxRespCodeEnum.ERROR.getMsg(),String.valueOf(TxRespCodeEnum.ERROR.getCode()));
        }
    }
}
