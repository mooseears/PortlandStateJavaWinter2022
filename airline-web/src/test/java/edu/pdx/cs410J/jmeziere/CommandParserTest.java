package edu.pdx.cs410J.jmeziere;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

import static edu.pdx.cs410J.jmeziere.CommandParser.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandParserTest {
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
    assertTrue(exception.getMessage().contains(ERR_INVALID_AIRPORT_CODE));
  }

  @Test
  void getAirportCodeFromShortStringThrowsException() {
    String invalidCode = "PD";
    Exception exception = assertThrows(InvalidArgumentException.class, () ->
            getAirportCodeFromArgs(invalidCode)
    );
    assertTrue(exception.getMessage().contains(ERR_INVALID_AIRPORT_CODE));
  }

  @Test
  void getFlightDateFromValidInputs() {
    String[] validDate = { "1/11/2002", "12:34", "pm" };
    try {
      assertThat(new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(getFlightDateFromArgs(validDate)),  equalTo("01/11/2002 12:34 PM"));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  void getFlightDateFromInvalidInputsThrowsException() {
    String[] invalidDate = { "165/11/2002", "12:34", "pm" };
    Exception exception = assertThrows(InvalidArgumentException.class, () ->
            getFlightDateFromArgs(invalidDate));
    assertTrue(exception.getMessage().contains(ERR_FLIGHT_TIME_FORMAT));
  }

  @Test
  void checkArgsForFlagsFindsAllFlagsFileFirst() {
    String[] args = { "-search", "airline", "PDX", "SEA", "-print", "-readme", "-host", "localhost", "-port", "8080" };
    CommandParser parser = new CommandParser(args);
    assertTrue(parser.getFlags().contains("sprht"));
  }
}
