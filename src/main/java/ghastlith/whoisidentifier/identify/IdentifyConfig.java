package ghastlith.whoisidentifier.identify;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class IdentifyConfig {

  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

}
