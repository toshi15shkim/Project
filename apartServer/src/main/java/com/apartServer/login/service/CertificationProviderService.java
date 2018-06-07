package com.apartServer.login.service;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

 
public class CertificationProviderService extends DaoAuthenticationProvider{
	
    @Override
    public Authentication authenticate(Authentication authentication)  {

        try {
            Authentication auth = super.authenticate(authentication);
            return auth;
        }
         
        catch (BadCredentialsException e) {
            throw e;
        }
         
        catch (LockedException e){
            throw e;
        }
         
        catch(AccountExpiredException e) {
            throw e;
        }    
        catch (CredentialsExpiredException e) {
            throw e;
        }

    }
	
}
