package dev.mpetrasin.edgeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EdgeServiceApplication {

  private static final Logger logger =
      LoggerFactory.getLogger(EdgeServiceApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(EdgeServiceApplication.class, args);
  }

}
