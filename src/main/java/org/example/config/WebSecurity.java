package org.example.config;

import org.example.security.enums.Permission;
import org.example.security.JwtConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Value(("${api-base-url}"))
    private String apiBaseUrl;

    private final JwtConfigurer jwtConfigurer;

    public WebSecurity(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, apiBaseUrl + "/auth/*").permitAll()
                    .antMatchers(HttpMethod.GET, apiBaseUrl + "/*", apiBaseUrl + "/*/*", apiBaseUrl + "/*/*/*").hasAuthority(Permission.DEVELOPERS_READ.getPermission())
                    .antMatchers(HttpMethod.POST, apiBaseUrl + "/*", apiBaseUrl + "/*/*").hasAuthority(Permission.DEVELOPERS_WRITE.getPermission())
                    .antMatchers(HttpMethod.DELETE, apiBaseUrl + "/*/*").hasAuthority(Permission.DEVELOPERS_WRITE.getPermission())
                    .antMatchers(HttpMethod.PUT, apiBaseUrl + "/*/*", apiBaseUrl + "/*/*/*").hasAuthority(Permission.DEVELOPERS_WRITE.getPermission())
                    .anyRequest().permitAll()
                .and()
                    .apply(jwtConfigurer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
