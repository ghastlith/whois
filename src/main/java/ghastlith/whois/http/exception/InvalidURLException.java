package ghastlith.whois.http.exception;

/**
 * {@link InvalidURLException} is thrown when the url built to be requested is
 * not valid.
 */
public class InvalidURLException extends RuntimeException {

  public InvalidURLException(final String url) {
    super("The URL " + url + " is invalid");
  }

}
