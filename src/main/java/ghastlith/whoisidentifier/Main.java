package ghastlith.whoisidentifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ghastlith.whoisidentifier.identify.IdentifyIpWhois;
import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class Main implements ApplicationRunner {

  @Autowired private ApplicationContext context;
  @Autowired private IdentifyIpWhois identifyIpWhois;

  public static void main(final String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Override
  public void run(final ApplicationArguments args) {
    final var ip = args.getOptionValues("ip");

    if (ip == null || ip.get(0).isBlank()) {
      shutdown("the argument IP is required", 1);
      return;
    }

    try {
      final var result = identifyIpWhois.getIPDetailedData(ip.get(0));
      shutdown(result, 0);
    } catch (Exception e) {
      shutdown(e.getMessage(), 1);
    }
  }

  private void shutdown(final String message, final int code) {
    if (code == 0) {
      System.out.println(message);
    } else {
      System.err.println("failure: " + message);
    }

    SpringApplication.exit(context, () -> code);
  }

}
