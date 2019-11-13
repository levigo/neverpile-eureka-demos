package com.neverpile.eureka.client.impl.feign;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import feign.RequestInterceptor;

public class OAuth2ClientBuilder {
  private final ClientBuilder parent;
  private String username;
  private String password;
  private String clientId;
  private String clientSecret;
  private final List<String> scopes = new ArrayList<>();
  private String accessTokenUri;

  OAuth2ClientBuilder(final ClientBuilder clientBuilder) {
    this.parent = clientBuilder;
  }

  public OAuth2ClientBuilder accessTokenUri(final String accessTokenUri) {
    this.accessTokenUri = accessTokenUri;
    return this;
  }
  
  public OAuth2ClientBuilder username(final String username) {
    this.username = username;
    return this;
  }

  public OAuth2ClientBuilder password(final String password) {
    this.password = password;
    return this;
  }

  public OAuth2ClientBuilder clientId(final String clientId) {
    this.clientId = clientId;
    return this;
  }

  public OAuth2ClientBuilder clientSecret(final String clientSecret) {
    this.clientSecret = clientSecret;
    return this;
  }

  public OAuth2ClientBuilder scope(final String scope) {
    scopes.add(scope);
    return this;
  }

  private RequestInterceptor oauth2FeignRequestInterceptor() {
    return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), getAuthenticationDetails());
  }

  private OAuth2ProtectedResourceDetails getAuthenticationDetails() {
    ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
    resourceDetails.setClientSecret(clientSecret);
    resourceDetails.setClientId(clientId);
    resourceDetails.setAccessTokenUri(accessTokenUri);
    resourceDetails.setPassword(password);
    resourceDetails.setUsername(username);
    resourceDetails.setGrantType("password");
    resourceDetails.setScope(scopes);
    return resourceDetails;
  }

  public ClientBuilder done() {
    parent.requestInterceptor(oauth2FeignRequestInterceptor());
    return parent;
  }
}
