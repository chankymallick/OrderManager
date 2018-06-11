package com.ordermanager.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Chanky Mallick
 */
//@Configuration
//@EnableWebSecurity

public class LoginSecurityConfiguration{
//    extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    UtilityDAO UtilityDAO;
//
//    @Autowired
//    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(UtilityDAO.getJdbcTemplate().getDataSource())
//                .usersByUsernameQuery(
//                        "SELECT USER_ID,PASSWORD, ENABLED FROM  USERS WHERE USERNAME=?")
//                .authoritiesByUsernameQuery(
//                        "SELECT USERNAME, ROLE_NAME FROM USER_ROLES WHERE USERNAME=?");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        
//        http.authorizeRequests()
//                .antMatchers("/Home").access("hasRole('ADMIN')")
//                .anyRequest().permitAll()
//                .and()
//                .formLogin().loginPage("/Login.html")
//                .usernameParameter("username").passwordParameter("password")
//                .and()
//                .logout().logoutSuccessUrl("/Login.html")
//                .and()
//                .exceptionHandling().accessDeniedPage("/403")
//                .and()
//                .csrf();
//    }

//    protected void configureHttp(HttpSecurity httpsecurity) throws Exception {
////        httpsecurity.authorizeRequests()
////                .antMatchers("/").access("hasRole('ROLE_USER')")
////                .and()
////                .formLogin().loginPage("/Login")
////                .defaultSuccessUrl("/Home")
////                .failureUrl("/Login?error")
////                .usernameParameter("username").passwordParameter("password")
////                .and()
////                .logout().logoutSuccessUrl("/Login?logout");
//
//    }

}
