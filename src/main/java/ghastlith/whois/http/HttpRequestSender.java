package ghastlith.whois.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import ghastlith.whois.http.exception.HttpErrorResponseException;
import ghastlith.whois.http.exception.InvalidURLException;
import lombok.RequiredArgsConstructor;

/**
 * Utility class for HTTP actions.
 */
@Component
@RequiredArgsConstructor
public class HttpRequestSender {

  private final HttpClient httpClient;

  private static final String WHOIS_URL = "http://ipwho.is/";

  /**
   * Sends an HTTP GET Request to an specified url and returns the response body.
   *
   * @param ip the desired url to retrieve information
   * @return The request response body.
   */
  public String doGetRequest(final String ip) {
    final var uri = buildUri(ip);

    final var request = HttpRequest.newBuilder()
        .uri(uri)
        .GET()
        .build();
    final var response = getResponse(httpClient, request);

    if (!HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
      throw new HttpErrorResponseException(response.statusCode());
    }

    return response.body();
  }

  private URI buildUri(final String ip) {
    final var url = WHOIS_URL + ip;

    try {
      return new URI(url);
    } catch (URISyntaxException e) {
      throw new InvalidURLException(url);
    }
  }

  private HttpResponse<String> getResponse(final HttpClient httpClient, final HttpRequest request) {
    try {
      return httpClient.send(request, BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new HttpErrorResponseException(500);
    }
  }

}
