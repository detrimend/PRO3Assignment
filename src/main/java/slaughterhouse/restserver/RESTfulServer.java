package slaughterhouse.restserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "slaughterhouse")
public class RESTfulServer {

  public static void main(String[] args) {
    SpringApplication.run(RESTfulServer.class, args);
  }

}
