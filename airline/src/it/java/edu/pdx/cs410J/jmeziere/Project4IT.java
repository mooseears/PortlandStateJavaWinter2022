package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project4} main class.
 */
class Project4IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project4} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project4.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  @Test
  void testPrintsInputFlightSuccessfully() {
      MainMethodResult result = invokeMain(
                "-print",
                "Project4", "9420", "PDX", "01/22/1993", "02:25", "am", "LAX", "1/01/2019", "15:07", "pm"
      );
      assertThat(result.getExitCode(), equalTo(0));
      assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 9420 departs PDX at 01/22/1993 02:25 AM arrives LAX at 01/01/2019 03:07 PM"));
  }

  @Test
  void testParsesXMLSuccessfully() throws URISyntaxException {
      URL resource = Project4.class.getResource("valid-airline.xml");
      File xmlFile = new File(Paths.get(resource.toURI().getPath()).toString());
      MainMethodResult result = invokeMain(
                "-xmlFile", xmlFile.getPath(),
                "-pretty", "-",
                "Valid Airlines", "9420", "PDX", "01/22/1993", "02:25", "am", "LAX", "1/01/2019", "15:07", "pm"
      );
      assertThat(result.getExitCode(), equalTo(0));
      assertThat(result.getTextWrittenToStandardOut(), containsString("-Flight #1437"));
  }

  @Test
  void testParsesXMLAndPrettyPrintsToFileSuccessfully(@TempDir File tempDir) throws URISyntaxException {
      URL resource = Project4.class.getResource("valid-airline.xml");
      File xmlFile = new File(Paths.get(resource.toURI().getPath()).toString());
      File prettyFile = new File(tempDir, "pretty-airline.txt");
      MainMethodResult result = invokeMain(
                "-xmlFile", xmlFile.getPath(),
                "-pretty", prettyFile.getPath(),
                "Valid Airlines", "9420", "PDX", "01/22/1993", "02:25", "am", "LAX", "1/01/2019", "15:07", "pm"
      );
      assertThat(result.getExitCode(), equalTo(0));
      try {
          BufferedReader br = new BufferedReader(new FileReader(prettyFile.getPath()));
          String line;
          String out = "";
          while ((line = br.readLine()) != null) {
              out += line;
          }
          assertThat(out, containsString("-Flight #1437"));
      } catch (Exception ex) {
          System.out.println(ex.getMessage());
      }
  }
}