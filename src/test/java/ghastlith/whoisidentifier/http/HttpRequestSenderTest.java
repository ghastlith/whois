package ghastlith.whoisidentifier.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;

import ghastlith.whoisidentifier.http.exception.HttpErrorResponseException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HttpRequestSenderTest {

  private final HttpClient mockHttpClient = mock(HttpClient.class);
  private final HttpResponse mockHttpResponse = mock(HttpResponse.class);

  private final HttpRequestSender mockHttpRequestSender = new HttpRequestSender(mockHttpClient);

  private static final String IP = "168.124.24.32";

  @Test
  void doGetRequest_shouldReturnResponseBodyAsStringWhenRequestIs2xxSuccessful() throws IOException, InterruptedException {
    // given
    final var mockResponseBody = "{ \"success\": true }";

    when(this.mockHttpResponse.statusCode()).thenReturn(200);
    when(this.mockHttpResponse.body()).thenReturn(mockResponseBody);
    when(this.mockHttpClient.send(any(), any())).thenReturn(this.mockHttpResponse);

    // when
    final var response = this.mockHttpRequestSender.doGetRequest(IP);

    // then
    assertEquals(mockResponseBody, response);
  }

  @Test
  void doGetRequest_shouldThrowHttpErrorResponseExceptionWhenStatusIsNot2xxSuccessful() throws IOException, InterruptedException {
    // given
    when(this.mockHttpResponse.statusCode()).thenReturn(403);
    when(this.mockHttpClient.send(any(), any())).thenReturn(this.mockHttpResponse);

    // when
    final var throwable = catchThrowable(() -> this.mockHttpRequestSender.doGetRequest(IP));

    // then
    assertThat(throwable).isInstanceOf(HttpErrorResponseException.class);
  }

}
