package com.apartServer.base.config;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import net.sf.log4jdbc.DataSourceSpyInterceptor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ, proxyTargetClass = true)
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * InternalResourceViewResolver 가 없으면 tiles view 이외의 접근이 불가능함
     *
     * @return
     */
    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".jsp");
        // jsp 에서 bean 접근을 위한 선언
        resolver.setExposeContextBeansAsAttributes(true);
        return resolver;
    }

    /**
     * yml의 선언으로 대신하여 bean drop
     * https://spring.io/guides/gs/uploading-files/
     *
     * @return
     */
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(1024 * 1024 * 1024);
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login");
        registry.addViewController("/login").setViewName("login");
    }

    /**
     * 인터셉터 추가
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // interceptor 클래스 생성후 설정
        // excludePathPatterns 참고
//        registry.addInterceptor(certificationInterceptor)
//                .addPathPatterns("/**/*.do")
//                .excludePathPatterns();
    }

    /*
     * lucy-xss-filter
     *
     * */
    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new XssEscapeServletFilter());
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("*.do");    //filter를 거칠 url patterns
        return registrationBean;
    }

    /**
     * Configure ResourceHandlers to serve static resources like CSS/ Javascript etc...
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

//    @Bean
//    public TilesViewResolver tilesViewResolver() {
//        final TilesViewResolver resolver = new TilesViewResolver();
//        resolver.setViewClass(TilesView.class);
//        return resolver;
//    }

    @Bean
    public Advice dataSourceSpyInterceptor() {
        return new DataSourceSpyInterceptor();
    }

    /**
     * mybatis log interceptor
     *
     * @return
     */
    @Bean
    public BeanNameAutoProxyCreator dataSourceLog4jdbcAutoProxyCreator() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setInterceptorNames("dataSourceSpyInterceptor");
        beanNameAutoProxyCreator.setBeanNames("dataSource");
        return beanNameAutoProxyCreator;
    }

    /**
     * spring boot의 tomcat 컨테이너에서 load 되는 jar 들을 찾지 못하는 에러 해결을 위한 설정
     *
     * @return
     */
    @Bean
    public BeanPostProcessor TomcatContainerPostProcessor() {
        String[] tldSkipPatterns = new String[]{"jaxb-api.jar", "jsr173_1.0_api.jar", "jaxb1-impl.jar", "activation.jar"};
        List<String> notEmptyTldSkipPatterns = Arrays.stream(tldSkipPatterns)
                .filter(tldSkipPattern -> !tldSkipPattern.trim().isEmpty())
                .collect(toList());

        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (beanName.equals("tomcatEmbeddedServletContainerFactory") &&
                        bean instanceof TomcatEmbeddedServletContainerFactory) {
                    TomcatEmbeddedServletContainerFactory factory = (TomcatEmbeddedServletContainerFactory) bean;
                    if (!notEmptyTldSkipPatterns.isEmpty()) {
                        factory.addTldSkipPatterns(notEmptyTldSkipPatterns.toArray(new String[0]));
                    }
                }
                return bean;
            }
        };
    }

//    @Bean
//    public ServerProperties getServerProperties() {
//        return new ServerCustomization();
//    }
//    
//    public class ServerCustomization extends ServerProperties {
//		
//    	@Override
//		public void customize(ConfigurableEmbeddedServletContainer container) {
//			super.customize(container);
//			container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/WEB-INF/view/error.jsp"));
//			container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/WEB-INF/view/error500.jsp"));
//			container.addErrorPages(new ErrorPage("/WEB-INF/view/error.jsp"));
//		}
//	}
}
