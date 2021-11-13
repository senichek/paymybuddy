package com.open.paymybuddy.configs;

import com.open.paymybuddy.security.UserPrincipalDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {        
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/transfer").permitAll()
               // .antMatchers("/admin/**", "/rest/admin/**").hasRole("ADMIN")
                .antMatchers("/rest/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login") //custom login page, login view must be also returned by controller
                .successForwardUrl("/home") //there should be postMapping on the redirect page
                //.failureUrl("/error")
                .permitAll()
                .and()
                .csrf().disable()
                //.csrf().ignoringAntMatchers("/rest/**")
                .logout()
                .permitAll()
               // .and()
               // .exceptionHandling().accessDeniedPage("/accessDenied")
                .and()
                .httpBasic();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider () {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder () {return new BCryptPasswordEncoder();
    }
}
