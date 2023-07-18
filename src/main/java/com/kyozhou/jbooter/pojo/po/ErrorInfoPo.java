package com.kyozhou.jbooter.pojo.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.HashMap;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorInfoPo<T> {

    private String error = "";
    private String errorMessage = "";
//    private String url;
    private T data;

    private HashMap<String, String> errorMap =  new HashMap<String, String>(){{
        put("beacon_cannot_access", "beacon服务不可用");
        put("beacon_error", "beacon执行错误");
        put("db_error", "数据库操作失败");
        put("data_invalid", "数据不合规");
        put("json_parse_error", "json解析失败");
        put("json_params_parse_error", "json参数解析失败");
        put("no_report", "没有报告");
        put("permission_denied", "权限不足");
        put("params_error", "参数错误");
        put("param_pattern_error", "参数格式错误");
        put("resource_not_exists", "请求资源不存在");
        put("request_params_error", "请求参数不正确");
        put("unknown_error", "未知错误");
        put("sign_auth_error", "sign签名验证失败");
        put("source_not_exists", "source不存在");
        put("source_is_registered", "source已注册");
        put("token_has_no_permission", "token权限不匹配");
        put("token_not_exists", "token不存在");
        put("token_not_specified", "token未提供");
        put("out_of_day_range", "超出日期范围");
    }};

    public static final Integer OK = 0;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
        if(errorMap.containsKey(error)) {
            this.errorMessage = errorMap.get(error).toString();
        }
    }

    @JsonProperty("error_message")
    public String getErrorMessage() {
        return errorMessage;
    }

//    public void setErrorMessage(String errorMessage) {
//        this.errorMessage = errorMessage;
//    }

//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}