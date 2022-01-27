package edu.pdx.cs410J.jmeziere;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static edu.pdx.cs410J.jmeziere.Project1.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test and can handle the calls
 * to {@link System#exit(int)} and the like.
 */
class Project1Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("This is a README file!"));
    }
  }

  @Test
  void getAirLineReturnsName() {
    String arg = "Airline";
    assertThat(getAirlineFromArgs(arg), containsString("Airline"));
  }

  @Test
  void getFlightNumberFromArgsValidNumber() {
    String validNum = "123456";
    try {
      assertThat(getFlightNumberFromArgs(validNum),  equalTo(123456));
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  @Test
  void getFlightNumberFromArgsNumberTooLong() {
    String invalidNum = "1234567";
    Exception exception = assertThrows(InvalidArgumentException.class, () ->
      getFlightNumberFromArgs(invalidNum)
    );
    assertTrue(exception.getMessage().contains(ERR_FLIGHT_NUM));
  }

  @Test
  void getFlightNumberFromArgsNotANumber() {
    String invalidNum = "ABDCE";
    Exception exception = assertThrows(InvalidArgumentException.class, () ->
      getFlightNumberFromArgs(invalidNum)
    );
    assertTrue(exception.getMessage().contains(ERR_FLIGHT_NUM));
  }

  @Test
  void getAirportCodeFromValidString() {
    String validCode = "PDX";
    try {
      assertThat(getAirportCodeFromArgs(validCode),  equalTo("PDX"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  @Test
  void getAirportCodeFromInvalidStringThrowsException() {
    String invalidCode = "1234567";
    Exception exception = assertThrows(InvalidArgumentException.class, () ->
      getAirportCodeFromArgs(invalidCode)
    );
    assertTrue(exception.getMessage().contains(ERR_AIRPORT_CODE));
  }

  @Test
  void getAirportCodeFromShortStringThrowsException() {
    String invalidCode = "PD";
    Exception exception = assertThrows(InvalidArgumentException.class, () ->
      getAirportCodeFromArgs(invalidCode)
    );
    assertTrue(exception.getMessage().contains(ERR_AIRPORT_CODE));
  }

  @Test
  void getFlightDateFromValidInputs() {
    String[] validDate = { "1/11/2002", "12:34" };
    try {
      assertThat(getFlightDateFromArgs(validDate),  equalTo("1/11/2002 12:34"));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  void getFlightDateFromInvalidInputsThrowsException() {
    String[] invalidDate = { "165/11/2002", "12:34" };
    Exception exception = assertThrows(InvalidArgumentException.class, () ->
            getFlightDateFromArgs(invalidDate));
    assertTrue(exception.getMessage().contains(ERR_FLIGHT_TIME));
  }

  @Test
  void checkArgsForFlagsFindsAllFlagsFileFirst() {
    String[] args = { "-textFile", "./airline-test.txt", "-print", "-readme" };
    assertTrue(checkArgsForFlags(args).contains("fpr"));
  }

  @Test
  void checkArgsForFlagsFindsAllFlagsPrintFirst() {
    String[] args = { "-print", "-textFile", "./airline-test.txt", "-readme" };
    assertTrue(checkArgsForFlags(args).contains("pfr"));
  }

  @Test
  void checkArgsForFlagsFindsAllFlagsReadmeFirst() {
    String[] args = { "-readme", "-print", "-textFile", "./airline-test.txt" };
    assertTrue(checkArgsForFlags(args).contains("rpf"));
  }

  @Test
  void checkArgsForFlagsFindsTwoFlags() {
    String[] args = { "-readme", "-textFile", "./airline-test.txt" };
    assertTrue(checkArgsForFlags(args).contains("rf"));
  }

  @Test
  void getFilePathFromArgsFindFilePath() {
    String[] args = { "-readme", "-textFile", "./airline-test.txt" };
    try {
      assertTrue(getFilePathFromArgs(args).contains("./airline-test.txt"));
    } catch (InvalidArgumentException ex){
      System.err.println(ex.getMessage());
    }
  }

  @Test
  void getFilePathFromArgsThrowsExceptionWhenNoArgument() {
    String[] args = { "-readme", "-textFile" };
    assertThrows(InvalidArgumentException.class, () ->
      getFilePathFromArgs(args)
    );
  }

  @Test
  void checkIfFileAlreadyExistsReturnsTrue() {
    try {
      URL resource = Project1.class.getResource("valid-airline.txt");
      String filePath = Paths.get(resource.toURI().getPath()).toString();
      assertThat(checkIfFileAlreadyExists(filePath), is(true));
    } catch (URISyntaxException | InvalidArgumentException ex) {
      System.err.println(ex.getMessage());
    }

  }
}
