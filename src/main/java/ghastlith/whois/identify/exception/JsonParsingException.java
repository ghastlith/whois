package ghastlith.whois.identify.exception;

/**
 * {@link JsonParsingException} is thrown when parsing JSON encountered any
 * problem.
 */
public class JsonParsingException extends RuntimeException {

  public JsonParsingException() {
    super("There was a problem parsing the whois response into a JSON");
  }

}
