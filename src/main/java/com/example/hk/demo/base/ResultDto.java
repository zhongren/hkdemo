package com.example.hk.demo.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;

public class ResultDto implements Serializable {
    private static final long serialVersionUID = 1L;
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 状态码
    private String code;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    // 通用状态码
    public static String SUCCESS = "1";
    public static String FAIL = "0";
    public static String SYS_ERROR = "-999";

    public static ResultDto build(String status, String msg, Object data) {
        return new ResultDto(status, msg, data);
    }

    public static ResultDto success(Object data) {
        return new ResultDto(data);
    }

    public static ResultDto success(String msg, Object data) {
        return build(SUCCESS, msg, data);
    }

    public static ResultDto success() {
        return new ResultDto(null);
    }

    public static ResultDto fail(String msg, Object data) {
        return build(FAIL, msg, data);
    }

    public static ResultDto fail(String msg) {
        return build(FAIL, msg, null);
    }

    public ResultDto() {

    }

    public ResultDto(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ResultDto(Object data) {
        this.code = SUCCESS;
        this.msg = "操作成功";
        this.data = data;
    }

    public ResultDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 将json结果集转化为ResultModel对象
     *
     * @param jsonData json数据
     * @param clazz    TaotaoResult中的object类型
     * @return
     */
    public static ResultDto formatToBean(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, ResultDto.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isObject()) {
                obj = MAPPER.readValue(data.traverse(), clazz);
            } else if (data.isTextual()) {
                obj = MAPPER.readValue(data.asText(), clazz);
            }
            return build(jsonNode.get("code").asText(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static ResultDto format(String json) {
        try {
            return MAPPER.readValue(json, ResultDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合转化
     *
     * @param jsonData json数据
     * @param clazz    集合中的类型
     * @return
     */
    public static ResultDto formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("code").asText(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

}
