package com.kyozhou.jbooter.service.po;

public class HttpResultPo<T> {
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private String error = "";
    private T data;

}
