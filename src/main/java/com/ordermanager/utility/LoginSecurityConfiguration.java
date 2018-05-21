package com.ordermanager.utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
/**
  @author Chanky Mallick
 */
//@Configuration
//@EnableWebSecurity

public class LoginSecurityConfiguration {
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
////        authManagerBuilder.inMemoryAuthentication().
////                withUser("").
////                password("").
////                authorities("");
//
//    }

    protected void configureHttp(HttpSecurity httpsecurity) throws Exception {
//        httpsecurity.authorizeRequests()
//                .antMatchers("/").access("hasRole('ROLE_USER')")
//                .and()
//                .formLogin().loginPage("/Login")
//                .defaultSuccessUrl("/Home")
//                .failureUrl("/Login?error")
//                .usernameParameter("username").passwordParameter("password")
//                .and()
//                .logout().logoutSuccessUrl("/Login?logout");

    }

}
