package com.kyozhou.jbooter.system.aop;

import com.kyozhou.jbooter.pojo.po.PermissionEnum;
import com.kyozhou.jbooter.pojo.po.TokenPo;
import com.kyozhou.jbooter.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticateInterceptor implements HandlerInterceptor {

    @Autowired
    private OrganizationService organizationService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = request.getParameter("token");
        if (token == null) {
            response.sendRedirect("/custom_error?error=token_not_specified");
            return false;
        }
        TokenPo tokenPo = organizationService.getToken(token);
        if (tokenPo == null) {
            response.setStatus(403);
            response.sendRedirect("/custom_error?error=token_not_exists");
            return false;
        }
        if (!this.checkOrganizationService(request, tokenPo.getOrgUUID())) {
            response.setStatus(403);
            response.sendRedirect("/custom_error?error=token_has_no_permission");
            return false;
        }
        request.setAttribute("_org_uuid", tokenPo.getOrgUUID());
        return true;
    }

    private boolean checkOrganizationService(HttpServletRequest request, String orgUUID) {
        Map<String, PermissionEnum> serviceMap = new HashMap<>();
        serviceMap.put("report", PermissionEnum.REPORT_SERVICE);
        serviceMap.put("data", PermissionEnum.DATA_SERVICE);
        serviceMap.put("source", PermissionEnum.SOURCE_SERVICE);
        String servletPath = request.getServletPath();
        String[] strings = servletPath.split("/");
        if (strings.length <= 1) {
            return false;
        }

        for (String stringTemp : strings) {
            if (stringTemp.equals("/") || stringTemp.length() < 3) {
                continue;
            }
            if (stringTemp.equals("debug")) {
                return true;
            }
            return organizationService.checkOrganizationPermission(orgUUID, serviceMap.get(stringTemp));
        }
        return false;
    }


}
