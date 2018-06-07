package com.apartServer.base.config;

import com.apartServer.aop.AjaxSessionTimeoutFilter;
import com.apartServer.login.service.CertificationFailureHandler;
import com.apartServer.login.service.CertificationProviderService;
import com.apartServer.login.service.CertificationSuccessHandler;
import com.apartServer.login.service.UserCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private UserCertificationService userCertificationService;

        @Autowired
        private CertificationSuccessHandler certificationSuccessHandler;

        @Autowired
        private CertificationFailureHandler certificationFailureHandler;

        /**
         * Spring Security 의 multiple forward slashes(//) 관련 처리를 위해 http 방화벽 허용
         *
         * @param web
         * @throws Exception
         */
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/resources/**", "*.js");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            /**
             * 빌더 메소드를 통해 작성된 값들은 먼저 빌드된 순서에 따라 우선순위가 정해짐
             * 먼저 정해진 값은 그 뒤에 permitAll()이라도 불변
             *
             * WebSecurity configure 에 설정되지 않은 인증에 대한 처리
             */
            http
                    .authorizeRequests()
                    // web ignoring을 제외한 전체가 기본 인증 페이지
                    .anyRequest().authenticated()   // 어떤 요청에 대해서도 인증을 받도록 설정
                    .and()
                    .formLogin()
                    //                    .loginProcessingUrl("loginProcessing")
                    .loginPage("/login")
                    .failureHandler(certificationFailureHandler)
                    .successHandler(certificationSuccessHandler)
                    // 로그인 실패 페이지
                    //                    .failureUrl("/login?error")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout")               // 기본적으로 post 로 던진다.
                    .logoutSuccessUrl("/logout")      // logout 성공시 호출될 페이지
//                        .deleteCookies("remember-me", "JSESSIONID")
//                    .permitAll()
                    .and()
                    // TODO : ajax 세션만료에 대한 exception 처리. 현재 에러발생 추후 수정필요
                    .addFilterAfter(ajaxSessionTimeoutFilter(), ExceptionTranslationFilter.class)
                    .authenticationProvider(daoAuthenticationProvider())
                    .csrf().disable()       // cross site request forgery, 사용안함
                    .httpBasic();
        }

        //        @Bean
        public AjaxSessionTimeoutFilter ajaxSessionTimeoutFilter() {
            AjaxSessionTimeoutFilter sessionTimeoutFilter = new AjaxSessionTimeoutFilter();
            sessionTimeoutFilter.setAjaxHeader("AJAX");
            return sessionTimeoutFilter;
        }


        @Bean(name = "certificationProviderService")
        public AuthenticationProvider daoAuthenticationProvider() {
            CertificationProviderService certificationProviderService = new CertificationProviderService();
            certificationProviderService.setUserDetailsService(userCertificationService);
            return certificationProviderService;
        }
    }
}