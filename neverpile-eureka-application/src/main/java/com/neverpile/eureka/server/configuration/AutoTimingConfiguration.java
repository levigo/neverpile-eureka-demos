package com.neverpile.eureka.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
@EnableAspectJAutoProxy
public class AutoTimingConfiguration {
   @Bean
   public TimedAspect timedAspect(final MeterRegistry registry) {
      return new TimedAspect(registry);
   }
}