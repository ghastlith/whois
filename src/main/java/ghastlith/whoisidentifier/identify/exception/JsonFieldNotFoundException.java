package ghastlith.whoisidentifier.identify.exception;

/**
 * {@link JsonFieldNotFoundException} is thrown when trying to access an
 * inexistent field on a JSON.
 */
public class JsonFieldNotFoundException extends RuntimeException {

  public JsonFieldNotFoundException(final String field) {
    super("The field " + field + " does not exist on the Whois response");
  }

}
