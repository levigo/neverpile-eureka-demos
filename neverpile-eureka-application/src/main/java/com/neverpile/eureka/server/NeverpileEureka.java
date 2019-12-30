package com.neverpile.eureka.server;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;

import com.neverpile.eureka.util.EnableNeverpileEurekaSpringApplication;

@SpringBootApplication(exclude={CassandraDataAutoConfiguration.class})
@EnableNeverpileEurekaSpringApplication
public class NeverpileEureka implements CommandLineRunner {

  @Override
  public void run(final String... arg0) throws Exception {
    if (arg0.length > 0 && arg0[0].equals("exitcode")) {
      throw new ExitException();
    }
  }

  public static void main(final String[] args) throws Exception {
    new SpringApplication(NeverpileEureka.class).run(args);
  }

  class ExitException extends RuntimeException implements ExitCodeGenerator {
    private static final long serialVersionUID = 1L;

    @Override
    public int getExitCode() {
      return 10;
    }

  }
}
