package com.neverpile.eureka.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@Configuration
@Order(99)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  public SecurityConfig() {
    super(true);
  }

  @Override
  public void configure(final WebSecurity web) throws Exception {
    web.ignoring().antMatchers(HttpMethod.OPTIONS);
  }

  @Override
  public void configure(final HttpSecurity http) throws Exception {
    // @formatter:off
    http
      .addFilter(new WebAsyncManagerIntegrationFilter())
      .exceptionHandling()
        .and()
      .headers()
        .and()
      .sessionManagement()
        .and()
      .securityContext()
        .and()
      .requestCache()
        .and()
      .anonymous()
        .and()
      .authorizeRequests()
        .antMatchers("/api/**")
          .authenticated()
     ;
    // @formatter:on
  }
}
