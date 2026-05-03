package ghastlith.whoisidentifier;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;

import ghastlith.whoisidentifier.identify.IdentifyIpWhois;

class MainTest {

  private final ApplicationContext context = mock(ApplicationContext.class);
  private final ApplicationArguments arguments = mock(ApplicationArguments.class);
  private final IdentifyIpWhois identifyIpWhois = mock(IdentifyIpWhois.class);

  private final Main application = new Main(context, identifyIpWhois);

  private final PrintStream standardOut = System.out;
  private final PrintStream standardErr = System.err;
  private final ByteArrayOutputStream outStreamCaptor = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errStreamCaptor = new ByteArrayOutputStream();

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outStreamCaptor));
    System.setErr(new PrintStream(errStreamCaptor));
  }

  @AfterEach
  public void tearDown() {
    System.setOut(standardOut);
    System.setErr(standardErr);
  }

  @Test
  void run_shouldReturnValidMessageWhenArgumentIpExists() {
    // given
    final var ip = singletonList("127.0.0.1");
    when(identifyIpWhois.getIPDetailedData(any())).thenReturn("mocked result");
    when(arguments.getOptionValues("ip")).thenReturn(ip);

    // when
    application.run(arguments);

    // then
    verify(identifyIpWhois, times(1)).getIPDetailedData(any());
    assertThat(outStreamCaptor.toString()).contains("mocked result");
  }

  @Test
  void run_shouldThrowReturnValidMessageWhenArgumentIpDoesntExists() {
    // given
    final var ip = singletonList("");
    when(identifyIpWhois.getIPDetailedData(any())).thenReturn("mocked result");
    when(arguments.getOptionValues("ip")).thenReturn(ip);

    // when
    application.run(arguments);

    // then
    verify(identifyIpWhois, times(0)).getIPDetailedData(any());
    assertThat(errStreamCaptor.toString()).contains("failure: the argument IP is required");
  }

}
