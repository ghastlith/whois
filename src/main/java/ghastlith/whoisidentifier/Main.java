package ghastlith.whoisidentifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ghastlith.whoisidentifier.identify.IdentifyIpWhois;
import lombok.AllArgsConstructor;
import lombok.val;

@SpringBootApplication
@AllArgsConstructor
public class Main implements CommandLineRunner {

    @Autowired private ApplicationContext context;
    @Autowired private ApplicationArguments appArgs;
    @Autowired private IdentifyIpWhois identifyIpWhois;

    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(final String... args) {
        val ip = appArgs.getOptionValues("ip");

        if (null == ip || ip.get(0).isBlank()) {
            shutdown("The argument IP is required", 1);
            return;
        }

        try {
            val result = identifyIpWhois.getIPDetailedData(ip.get(0));
            shutdown(result, 0);
        } catch (Exception e) {
            shutdown(e.getMessage(), 1);
        }
    }

    private void shutdown(final String message, final int code) {
        if (code == 0) {
            System.out.println(message);
        } else {
            System.err.println("Failure: " + message);
        }

        SpringApplication.exit(context, () -> code);
    }

}
