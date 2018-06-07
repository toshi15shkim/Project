package com.apartServer.login.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("authService")
public class UserCertificationService implements UserDetailsService {

    @Autowired
    private UserLoginService userLoginService;


    private boolean isEnalbe(Map<String, Object> userInfoMap) {
        return true;

    }


    private boolean isNonExpired(Map<String, Object> userInfoMap) {
        return true;
    }


    private boolean isNonLock(Map<String, Object> userInfoMap) {

        return true;
    }


    private boolean isCredentialsNonExpired(Map<String, Object> userInfoMap) {
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        if (StringUtils.isEmpty(userId)) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        Map<String, Object> userInfoMap = userLoginService.getUserInfo(userId);

        if (MapUtils.isEmpty(userInfoMap)) {
            // 사용자 계정이 존재하지 않음
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // 그외에 인증할 수 없는 경우들에 대해 처리한다.(계정 블럭/세션만료 등등)

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        grantList.add(new SimpleGrantedAuthority(MapUtils.getString(userInfoMap, "AUTH_ID")));

        return new User(userId,
                MapUtils.getString(userInfoMap, "USER_PWD"),
                isEnalbe(userInfoMap),
                isNonExpired(userInfoMap),
                isCredentialsNonExpired(userInfoMap),
                isNonLock(userInfoMap),
                grantList);
    }

}
