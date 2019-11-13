package com.neverpile.eureka.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.neverpile.eureka.client.impl.feign.FeignNeverpileClient;

public class MockNeverpileRule extends WireMockRule {
  static final Logger LOGGER = LoggerFactory.getLogger(MockNeverpileRule.class);

  public NeverpileClient client;

  public MockNeverpileRule() {
    super(WireMockConfiguration.options().dynamicPort().notifier(new Slf4jNotifier(true)));
  }
  
  @Override
  protected void before() {
    super.before();

    client = FeignNeverpileClient.builder() //
        .baseURL("http://localhost:" + port()) //
//        .withOAuth2() //
//        .accessTokenUri("http://localhost:" + port + "/oauth/token") //
//        .username("admin") //
//        .password("admin") //
//        .clientId("trusted_app") //
//        .clientSecret("secret") //
//        .scope("public").scope("document") //
//        .done() //
        .build();
  }
}
