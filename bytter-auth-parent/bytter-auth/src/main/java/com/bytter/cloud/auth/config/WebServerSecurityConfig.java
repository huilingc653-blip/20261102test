package com.bytter.cloud.auth.config;

import com.bytter.cloud.common.web.format.StanderJackson2ObjectMapperBuilderCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * web容器配置
 *
 * @author zhoul 2021-1-19
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class WebServerSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 注入自定义的userDetailsService实现，获取用户信息，设置密码加密方式
     *
     * @param authenticationManagerBuilder 授权管理建造器
     * @throws Exception 未知异常信息
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.removeConfigurer(DefaultLoginPageConfigurer.class);
        http.authorizeRequests()
                .antMatchers("/actuator", "/actuator/**").denyAll()
                .antMatchers(
                "/oauth/**",
                "/oauth2/**",
                "/token/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
    	.anyRequest().authenticated()
        .and()
        .csrf().disable();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
    }


    /**
     * AuthenticationManager对象在OAuth2认证服务中要使用，提前放入IOC容器中
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BytterPasswordEncoder();
    }
    
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return new StanderJackson2ObjectMapperBuilderCustomizer();
    }
    
    @Bean
    public FilterRegistrationBean permitAuthenticationCrossFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CrossOriginConfig());
        registrationBean.setOrder(-9999);
        registrationBean.addUrlPatterns("/oauth/*","/oauth2/*");
        return registrationBean;
    }
    

    public FilterRegistrationBean permitAuthenticationFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new PermitAuthenticationFilter());
        registrationBean.setOrder(-9998);
        registrationBean.addUrlPatterns("/oauth/*","/oauth2/*");
        return registrationBean;
    }
    
    @Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/statics/**","/sso/**");
	}
}
