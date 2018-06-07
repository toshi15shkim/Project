package com.apartServer.login.service;

import java.util.Map;

import com.apartServer.login.dao.UserLoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class UserLoginService {

	@Autowired
    UserLoginMapper userLoginMapper;
    
    public Map<String, Object> getUserInfo(String userId) {
        return userLoginMapper.selectUserInfo(userId);
    }
         
    public void modifyLoginUser(String mbrNo) {
    	userLoginMapper.updateLoginUser(mbrNo);
    }

}
