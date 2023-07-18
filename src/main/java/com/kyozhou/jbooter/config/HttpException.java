package com.kyozhou.jbooter.config;

public class HttpException extends Exception {

    public HttpException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        //不打印此异常的错误
    }
}
