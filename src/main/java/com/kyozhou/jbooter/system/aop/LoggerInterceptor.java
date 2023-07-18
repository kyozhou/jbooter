package com.kyozhou.jbooter.system.aop;

import com.kyozhou.jbooter.system.utils.CommonUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

public class LoggerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestId = CommonUtility.md5(System.currentTimeMillis() + "" + CommonUtility.random(1000, 9999));
        request.setAttribute("_request_id", requestId);
        request.setAttribute("_request_time", System.currentTimeMillis());
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--------------------- REQUEST").append(" [_request_id:").append(requestId).append("] ---------------------\n");
        stringBuilder.append("RequestURL: ").append(request.getRequestURL()).append("\n");
        stringBuilder.append("Method: ").append(request.getMethod()).append("\n");
        stringBuilder.append("HEADER:").append("\n");
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            stringBuilder.append(headerName).append(" -> ").append(request.getHeader(headerName)).append("\n");
        }
        stringBuilder.append("Cookies:").append("\n");
        if(request.getCookies() != null && request.getCookies().length > 0) {
            for (Cookie cookie : request.getCookies()) {
                stringBuilder.append(cookie.getName()).append("->").append(cookie.getValue()).append("\n");
            }
        }
        stringBuilder.append("Params:").append("\n");
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            stringBuilder.append(paramName).append(" -> ").append(request.getParameter(paramName)).append("\n");
        }
        stringBuilder.append("Request Body:").append("\n");
        stringBuilder.append(this.getRequestBody(request)).append("\n");
        stringBuilder.append("--------------------- REQUEST ---------------------");
        logger.info(stringBuilder.toString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {

        String requestId = request.getAttribute("_request_id").toString();
        Long requestTime = (long)request.getAttribute("_request_time");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--------------------- RESPONSE").append(" [_request_id:").append(requestId).append("] ---------------------\n");
        stringBuilder.append("Execute Time:").append(System.currentTimeMillis() - requestTime).append(" ms\n");
        String responseBody = "";
//        if (handler != null) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            if (handler instanceof String) {
//                responseBody = handler.toString();
//            } else if(handler instanceof TokenPo){
//                try {
//                    responseBody = objectMapper.writeValueAsString(handler);
//                } catch (JsonProcessingException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//        stringBuilder.append("Response Body:").append(this.getResponseBody(response)).append("\n");
        stringBuilder.append("--------------------- RESPONSE ---------------------");
        logger.info(stringBuilder.toString());
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            BufferedReader br = request.getReader();
            String str, wholeStr = "";
            while ((str = br.readLine()) != null) {
                wholeStr += str;
            }
            return wholeStr;
        } catch (IOException e) {
            return "";
        }
    }

//    private String getResponseBody(HttpServletResponse response) {
//        response.getOutputStream().
////        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(response);
//        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
//        if(wrapper != null) {
//            byte[] buf = wrapper.getContentAsByteArray();
//            if(buf.length > 0) {
//                String payload;
//                try {
//                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
//                } catch (UnsupportedEncodingException e) {
//                    payload = "[unknown]";
//                }
//                return payload;
//            }
//        }
//        return "";
//    }

}

