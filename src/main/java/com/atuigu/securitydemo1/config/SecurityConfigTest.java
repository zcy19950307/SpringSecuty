package com.atuigu.securitydemo1.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class SecurityConfigTest extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){

        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;

    }

    // 配置 制定账号密码登陆
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(password());

    }

    // 自定义
    @Override
    protected void configure(HttpSecurity  http) throws Exception {
        // 配置没有访问权限
        http.exceptionHandling().accessDeniedPage("/unauth.html");  // 当用户没有访问的权限的时候，返回这个 unauth.html 页面   不再使用 默认的返回页面
        // 配置退出、
        http.logout().logoutUrl("/logout")   // 退出地址
                .logoutSuccessUrl("/test/hello")   // 成功退出以后跳转地址
                .permitAll();


        http.formLogin()  //已定义编辑到自己表单登陆页面
            .loginPage("/login.html")  // 自定义登陆页面设置 不在使用SpringSecurity 默认带的 (默认在static下面找)
            .loginProcessingUrl("/user/login")  //登陆访问路径  这个只的 login.html中的   <form action="/user/login" method="post">  里action z  <form action="/user/login" method="post">的       action中值
            .defaultSuccessUrl("/success.html").permitAll() //登陆成功以后，跳转得路径
             .and().authorizeRequests() // 匹配路径
                .antMatchers("/","/test/hello","/user/login").permitAll()  // 访问 test/hello","user/login  可以不需要验证\
                // 只有admins权限才可以访问这个图路径 只对  /test/index"   hasAuthority 只对一个权限 支持一个admins权限
                .antMatchers("/test/index").hasAuthority("admins")
                // 只有admins权限才可以访问这个图路径 只对  /test/index"   hasAuthority 针对全部权限
               //  .antMatchers("/test/index").hasAnyAuthority("admins,manager")
                // ROLE_sale
                .antMatchers("/test/index").hasRole("sale")
                .anyRequest().authenticated()
                .and().rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60) // 设置过期时间为60秒
                .userDetailsService(userDetailsService)
                .and().csrf().disable(); // 关闭csrf

    }




    @Bean
    public PasswordEncoder password(){

        return  new BCryptPasswordEncoder();
    }

}
