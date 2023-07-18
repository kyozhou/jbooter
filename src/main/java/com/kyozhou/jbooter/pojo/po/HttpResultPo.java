package com.kyozhou.jbooter.pojo.po;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpResultPo<T> {
    private String error = "";
    private T data;
}
