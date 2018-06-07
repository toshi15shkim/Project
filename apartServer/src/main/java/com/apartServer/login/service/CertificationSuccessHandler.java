package com.apartServer.login.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import com.apartServer.certification.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class CertificationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserLoginService userLoginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        String accept = request.getHeader("accept");
        Map<String, Object> mbrInfo = userLoginService.getUserInfo(authentication.getName());
        modifyLoginUser(MapUtils.getString(mbrInfo, "USER_ID"));
        setSessionLoanInfo(mbrInfo, request.getSession());

        if (StringUtils.indexOf(accept, "html") > -1) {
            setAlwaysUseDefaultTargetUrl(true);
            setDefaultTargetUrl(getTargetUrl(request, response));
            super.onAuthenticationSuccess(request, response, authentication);
        } else if (StringUtils.indexOf(accept, "json") > -1) {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            LoginVo loginVo = new LoginVo();
            loginVo.setErrorMsg("");
            loginVo.setLogin(true);
            loginVo.setRefererUrl(getTargetUrl(request, response));
            loginVo.setPwdExpiry(false);
            PrintWriter out = response.getWriter();
            out.print(JSON.toJSONString(loginVo));
            out.flush();
            out.close();
        }
    }

    /**
     * session 에 기본정보를 설정한다.
     *
     * @param mbrInfo
     * @param session
     */
    private void setSessionLoanInfo(Map<String, Object> mbrInfo, HttpSession session) {
        session.setAttribute("userId", MapUtils.getString(mbrInfo, "USER_ID", ""));
        session.setAttribute("userNm", MapUtils.getString(mbrInfo, "USER_NAME", ""));
        session.setAttribute("roleId", MapUtils.getString(mbrInfo, "AUTH_ID", ""));
    }

    private void modifyLoginUser(String mbrNo) {
        userLoginService.modifyLoginUser(mbrNo);
    }

    private String getTargetUrl(HttpServletRequest request, HttpServletResponse response) {

        if (isAlwaysUseDefaultTargetUrl()) {
            return getDefaultTargetUrl(request.getServletPath());
        }

        if (request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST") != null) {
            SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

            String requestUrl = savedRequest.getRedirectUrl();

            if (StringUtils.isNotBlank(requestUrl) && !StringUtils.startsWith(requestUrl, "/login") && !StringUtils.equals(requestUrl, "/")) {
                return requestUrl;
            } else {
                return getDefaultTargetUrl(request.getServletPath());
            }
        } else {
            return getDefaultTargetUrl(request.getServletPath());
        }
    }

    private String getDefaultTargetUrl(String servletPath) {

        return "/default.do";
    }

}
