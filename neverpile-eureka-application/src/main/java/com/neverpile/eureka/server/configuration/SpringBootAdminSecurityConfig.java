package com.neverpile.eureka.server.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import de.codecentric.boot.admin.client.registration.metadata.MetadataContributor;

@Configuration
@Order(98)
public class SpringBootAdminSecurityConfig extends WebSecurityConfigurerAdapter implements MetadataContributor {
  private static final String SBA_USER_NAME = "spring-boot-admin";

  protected static final Logger logger = LoggerFactory.getLogger(SpringBootAdminSecurityConfig.class);
  
  private final String randomPassword;

  public SpringBootAdminSecurityConfig() {
    super(true);
    randomPassword = java.util.UUID.randomUUID().toString();
  }

  @PostConstruct
  public void init() {
     logger.info("Secured actuator endpoints with basic authentication: {}/{}", SBA_USER_NAME, randomPassword);
  }
  
  @Override
  public void configure(final WebSecurity web) throws Exception {
    web.ignoring().antMatchers(HttpMethod.OPTIONS);
  }

  @Override
  public void configure(final HttpSecurity http) throws Exception {
    http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests() //
        .anyRequest().hasRole("SBA") //
        .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint());
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
    entryPoint.setRealmName("admin realm");
    return entryPoint;
  }

  @Bean
  GlobalAuthenticationConfigurerAdapter authConfigurer() {
    return new GlobalAuthenticationConfigurerAdapter() {
      @Override
      public void init(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(SBA_USER_NAME).password("{noop}" + randomPassword).roles("SBA");
      }
    };
  }

  @Override
  public Map<String, String> getMetadata() {
    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("user.name", SBA_USER_NAME);
    metadata.put("user.password", randomPassword);
    return metadata;
  }
}
