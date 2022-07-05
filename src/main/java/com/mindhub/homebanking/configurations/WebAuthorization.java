package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/web/login.html").permitAll()
                .antMatchers("/web/pics/**").permitAll()
                .antMatchers("/web/scripts/**").permitAll()
                .antMatchers("/web/styles/**").permitAll()
                .antMatchers("/api/transactions/**").permitAll()
                .antMatchers("/api/loan/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/api/loans").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/api/loans/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/api/clients/current/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/api/clients").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/api/accounts").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/api/accounts/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/api/transactions").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/web/cards.html").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/rest/**", "/h2-console", "/api/**").hasAuthority("ADMIN")
                .antMatchers("/**").hasAnyAuthority("CLIENT", "ADMIN");


        http.formLogin()

                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login") // app/login
                .defaultSuccessUrl("/web/accounts.html");


        http.logout().logoutUrl("/api/logout"); // app/logout

        // turn off checking for CSRF tokens

        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()); // agregado

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable(); // httpSecurity

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }

}