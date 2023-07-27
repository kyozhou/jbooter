package com.kyozhou.jbooter.system.config;

/**
 * 系统异常（包括控制层、服务层均可抛出），最终由API对外包装成异常信息
 */
public class SysException extends Exception {

    public SysException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        //不打印此异常的错误
    }
}
