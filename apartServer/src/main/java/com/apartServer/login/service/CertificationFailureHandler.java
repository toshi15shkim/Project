package com.apartServer.login.service;

import com.alibaba.fastjson.JSON;
import com.apartServer.certification.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Service
public class CertificationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    private final static String SPRING_FAIL_URL = "/login?error";

    @Autowired
    private UserLoginService userLoginService;
    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

    public CertificationFailureHandler() {
        super(SPRING_FAIL_URL);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException authenticationException)
            throws IOException, ServletException {

        String accept = request.getHeader("accept");
        if (StringUtils.indexOf(accept, "html") > -1) {
            super.onAuthenticationFailure(request, response, authenticationException);
        } else if (StringUtils.indexOf(accept, "json") > -1) {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            LoginVo loginVo = new LoginVo();

            if (authenticationException.getClass().equals(BadCredentialsException.class)
                    || authenticationException.getClass().equals(UsernameNotFoundException.class)) {
                loginVo.setErrorMsg("사용자가 없거나 패스워드가 일치하지 않습니다.");
            } else if (authenticationException.getClass().equals(LockedException.class)) {
                loginVo.setErrorMsg("계정이 잠겼습니다. \r\n 패스워드 찾기로  패스워드를 재설정해주세요.");
            }

            loginVo.setLogin(false);
            loginVo.setPwdExpiry(false);
            PrintWriter out = response.getWriter();
            out.print(JSON.toJSONString(loginVo));
            out.flush();
            out.close();
        }

    }

}
