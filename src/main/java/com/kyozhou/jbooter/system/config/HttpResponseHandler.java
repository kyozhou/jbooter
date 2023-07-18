package com.kyozhou.jbooter.system.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyozhou.jbooter.pojo.po.HttpResultPo;
import com.kyozhou.jbooter.pojo.po.ErrorInfoPo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

@ControllerAdvice
public class HttpResponseHandler implements ResponseBodyAdvice {

//    @ExceptionHandler(value = Exception.class)
//    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("exception", e);
//        mav.addObject("url", req.getRequestURL());
//        mav.setViewName("error");
//        return mav;
//    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, org.springframework.http.server.ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResultPo<Object> httpResultPo = new HttpResultPo<>();
        if(body instanceof ErrorInfoPo) {
            return body;
        } else if (body instanceof String) {
            httpResultPo.setData(body);
            try {
                return objectMapper.writeValueAsString(httpResultPo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return this.httpError("json_parse_error");
            }
//        } else if (body instanceof Integer) {
        } else {
            httpResultPo.setData(body);
            return httpResultPo;
        }
    }

    @ExceptionHandler(value = HttpException.class)
    @ResponseBody
    public ErrorInfoPo<String> jsonErrorHandler(HttpServletRequest req, HttpException e) {
        return this.httpError(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ErrorInfoPo<String> requestFieldErrorHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        return this.httpError(violation.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfoPo<String> notFoundErrorHandler(Exception e) {
        e.printStackTrace();
        return this.httpError("request_not_exists");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    @ResponseBody
    public ErrorInfoPo<String> badRequestErrorHandler(Exception e) {
        e.printStackTrace();
        return this.httpError("request_params_error");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfoPo<String> InternalServerErrorHandler(Exception e) {
        e.printStackTrace();
        return this.httpError("unknown_error");
    }

    private ErrorInfoPo<String> httpError(String errorMessage) {
        ErrorInfoPo<String> errorInfoPo = new ErrorInfoPo<>();
        errorInfoPo.setError(errorMessage);
        return errorInfoPo;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }
}

