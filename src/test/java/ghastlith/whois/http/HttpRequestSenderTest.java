package ghastlith.whois.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ghastlith.whois.http.exception.HttpErrorResponseException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HttpRequestSenderTest {

  @Mock private HttpClient mockHttpClient;
  @Mock private HttpResponse mockHttpResponse;
  @InjectMocks private HttpRequestSender mockHttpRequestSender;

  private static final String IP = "168.124.24.32";

  @Test
  void doGetRequest_shouldReturnResponseBodyAsStringWhenRequestIs2xxSuccessful() throws IOException, InterruptedException {
    // given
    final var mockResponseBody = "{ \"success\": true }";

    when(mockHttpResponse.statusCode()).thenReturn(200);
    when(mockHttpResponse.body()).thenReturn(mockResponseBody);
    when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);

    // when
    final var response = mockHttpRequestSender.doGetRequest(IP);

    // then
    assertThat(response).isEqualTo(mockResponseBody);
  }

  @Test
  void doGetRequest_shouldThrowHttpErrorResponseExceptionWhenStatusIsNot2xxSuccessful() throws IOException, InterruptedException {
    // given
    when(mockHttpResponse.statusCode()).thenReturn(403);
    when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);

    // when
    final var throwable = catchThrowable(() -> mockHttpRequestSender.doGetRequest(IP));

    // then
    assertThat(throwable).isInstanceOf(HttpErrorResponseException.class);
  }

}
