package org.sdase.commons.server.dropwizard.bundles;

import io.dropwizard.Configuration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.sdase.commons.server.dropwizard.bundles.scanner.JacksonTypeScanner;

public class GenericLookupConfigCommand<T extends Configuration> extends ConfiguredCommand<T> {

  private static final String HELP_TEMPLATE =
      // next major: make it a multiline String when Java 11 is not supported anymore
      String.join(
          "%n",
          "Configuration can be manipulated dynamically from environment variables.",
          "Some types use a special syntax for the value:",
          "- Duration: '<int><unit>' with unit as:",
          "  - ns: nanoseconds",
          "  - us: microseconds",
          "  - ms: milliseconds",
          "  - s: seconds",
          "  - m: minutes",
          "  - h: hours",
          "  - d: days",
          "- DataSize: <int><unit>, with unit as",
          "  - B: bytes",
          "  - KB: kilobytes",
          "  - KiB: kibibytes",
          "  - MB: megabytes",
          "  - MiB: mebibytes",
          "  - GB: gigabytes",
          "  - GiB: gibibytes",
          "  - T: terabytes",
          "  - TiB: tebibytes",
          "  - PB: petabytes",
          "  - PiB: pebibytes",
          "",
          "All supported environment variable keys:",
          "",
          "%s",
          "");

  public GenericLookupConfigCommand() {
    super("config", "Shows available generic environment variables for configuration.");
  }

  @Override
  public void configure(Subparser subparser) {
    // nothing to configure
  }

  @Override
  @SuppressWarnings("java:S106") // using System.out to avoid clutter in output
  protected void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration)
      throws Exception {
    String configurationHints =
        new JacksonTypeScanner(bootstrap.getObjectMapper())
            .createConfigurationHints(configuration.getClass());
    System.out.printf(HELP_TEMPLATE, configurationHints);
  }
}
