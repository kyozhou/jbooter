package com.kyozhou.jbooter.config;

import com.kyozhou.jbooter.system.aop.AuthenticateInterceptor;
import com.kyozhou.jbooter.system.aop.LoggerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class HttpConfig implements WebMvcConfigurer {

    @Bean
    AuthenticateInterceptor authenticateInterceptor() {
        return new AuthenticateInterceptor();
    }
    @Bean
    LoggerInterceptor loggerInterceptor() {
        return new LoggerInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.authenticateInterceptor()).addPathPatterns("/debug/**", "/report/**", "/data/**", "/source/**");
//        registry.addInterceptor(this.loggerInterceptor()).addPathPatterns("/**/*");
    }

}

