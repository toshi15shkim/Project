package com.apartServer.certification.vo;

import lombok.Data;

@Data
public class LoginVo {
    boolean isLogin;
    String errorMsg;
    String refererUrl;
    boolean isPwdExpiry;
    String passwordValidity;
    String passwordExpiredDate;
    
    
}
