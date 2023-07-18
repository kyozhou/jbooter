package com.kyozhou.jbooter.controller;

import com.kyozhou.jbooter.pojo.po.ErrorInfoPo;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class MyErrorController implements ErrorController {

    @RequestMapping("error")
    public ErrorInfoPo<String> handleError(HttpServletRequest request){
        ErrorInfoPo<String> errorInfoPo = new ErrorInfoPo<>();
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == 401){
        }else if(statusCode == 404){
            errorInfoPo.setError("resource_not_exists");
        }else if(statusCode == 403){
            errorInfoPo.setError("permission_denied");
        }else{
        }
        return errorInfoPo;
    }

    @RequestMapping("custom_error")
    public ErrorInfoPo<String> customError(
            @RequestParam("error") String error
    ) {
        ErrorInfoPo<String> errorInfoPo = new ErrorInfoPo<>();
        errorInfoPo.setError(error);
        return errorInfoPo;
    }
}
