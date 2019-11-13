package com.neverpile.eureka.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

@Configuration
public class GlobalAuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {
  
  @Override
  public void init(final AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
    .withUser("user")
      .password("{noop}password")
      .roles("USER")
    .and().withUser("admin")
      .password("{noop}admin")
      .roles("USER", "ADMIN");
  }

}