package ghastlith.whoisidentifier.identify;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ghastlith.whoisidentifier.http.HttpRequestSender;
import ghastlith.whoisidentifier.identify.exception.JsonFieldNotFoundException;

public class IdentifyIpWhoisTest {

  private final HttpRequestSender mockHttpRequestSender = mock(HttpRequestSender.class);

  private ObjectMapper objectMapper = new ObjectMapper();

  private IdentifyIpWhois identifyIpWhois = new IdentifyIpWhois(mockHttpRequestSender, objectMapper);

  @Test
  void getIPDetailedData_shouldReturnValidResponseWhenIpv4IsValid() {
    // given
    final var ip = "8.8.8.8";
    final var responseBody = "{ \"ip\": \"8.8.8.8\", \"success\": true, \"type\": \"IPv4\", \"country\": \"United States\", \"connection\": { \"isp\": \"Google LLC\" } }";
    when(this.mockHttpRequestSender.doGetRequest(any())).thenReturn(responseBody);

    // when
    final var response = this.identifyIpWhois.getIPDetailedData(ip);

    // then
    assertEquals("IPv4 8.8.8.8 is located on United States and belongs to Google LLC", response);
  }

  @Test
  void getIPDetailedData_shouldReturnValidResponseWhenIpv6IsValid() {
    // given
    final var ip = "2001:4860:4860::8888";
    final var responseBody = "{ \"ip\": \"2001:4860:4860::8888\", \"success\": true, \"type\": \"IPv6\", \"country\": \"United States\", \"connection\": { \"isp\": \"Google LLC\" } }";
    when(this.mockHttpRequestSender.doGetRequest(any())).thenReturn(responseBody);

    // when
    final var response = this.identifyIpWhois.getIPDetailedData(ip);

    // then
    assertEquals("IPv6 2001:4860:4860::8888 is located on United States and belongs to Google LLC", response);
  }

  @Test
  void getIPDetailedData_reservedIpShouldReturnInvalidIpMessage() {
    // given
    final var ip = "127.0.0.1";
    final var responseBody = "{ \"ip\": \"127.0.0.1\", \"success\": false, \"message\": \"Reserved range\" }";
    when(this.mockHttpRequestSender.doGetRequest(any())).thenReturn(responseBody);

    // when
    final var response = this.identifyIpWhois.getIPDetailedData(ip);

    // then
    assertEquals("The provided IP (" + ip + ") is invalid", response);
  }

  @Test
  void getIPDetailedData_invalidIpShouldReturnInvalidIpMessage() {
    // given
    final var ip = "127.0.0.1";
    final var responseBody = "{ \"ip\": \"4\", \"success\": false, \"message\": \"Invalid IP address\" } ";
    when(this.mockHttpRequestSender.doGetRequest(any())).thenReturn(responseBody);

    // when
    final var response = this.identifyIpWhois.getIPDetailedData(ip);

    // then
    assertEquals("The provided IP (" + ip + ") is invalid", response);
  }

  @Test
  void getIPDetailedData_shouldThrowJsonFieldNotFoundWhenResponseBodyIsEmpty() {
    // given
    final var ip = "8.8.8.8";
    when(this.mockHttpRequestSender.doGetRequest(any())).thenReturn("");

    // when
    final var throwable = catchThrowable(() -> this.identifyIpWhois.getIPDetailedData(ip));

    // then
    assertThat(throwable).isInstanceOf(JsonFieldNotFoundException.class);
  }

}
