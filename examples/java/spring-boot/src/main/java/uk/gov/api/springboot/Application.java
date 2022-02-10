package uk.gov.api.springboot;

import org.jmolecules.architecture.onion.classical.ApplicationServiceRing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ApplicationServiceRing
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
