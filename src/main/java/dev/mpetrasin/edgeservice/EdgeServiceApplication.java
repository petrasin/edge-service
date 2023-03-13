package dev.mpetrasin.edgeservice;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;

@SpringBootApplication
public class EdgeServiceApplication {

  private static final Logger logger = LoggerFactory.getLogger(EdgeServiceApplication.class);

  private final RouteLocator routeLocator;

  public EdgeServiceApplication(RouteLocator routeLocator) {
    this.routeLocator = routeLocator;
  }

  public static void main(String[] args) {
    SpringApplication.run(EdgeServiceApplication.class, args);
  }


  @PostConstruct
  public void printRouteInfo() {
    routeLocator.getRoutes().subscribe(route -> {
      var id = route.getId();
      var predicate = route.getPredicate().toString();
      var uri = route.getUri().toString();

      logger.info("Registered route{id: {}, predicate: {}, target: {}}", id, predicate, uri);
    });
  }
}
