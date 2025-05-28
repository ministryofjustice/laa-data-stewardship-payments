package uk.gov.justice.laa.datastewardship.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Data Stewardship Payments application.
 */
@SpringBootApplication
public class DataStewardshipPaymentsApplication {

  /**
   * The application main method.
   *
   * @param args the application arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(DataStewardshipPaymentsApplication.class, args);
  }
}
