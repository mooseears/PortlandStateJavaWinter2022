package edu.pdx.cs410J.jmeziere;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Flight} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class FlightTest {
  Flight createTestFlight() {
    Flight flight = null;
    try {
      flight = new Flight(64, "PDX", new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("1/23/4567 12:34 pm"), "LAX", new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("2/11/4567 3:45 pm"));
    } catch (ParseException ex) {
      System.err.println(ex.getMessage());
    }
    return flight;
  }

  @Test
  void getSourceReturnsSourceString() {
    Flight flight = createTestFlight();
    assertThat(flight.getSource(), is("PDX"));
  }

  @Test
  void getNumberReturnsFlightNumber() {
    Flight flight = createTestFlight();
    assertThat(flight.getNumber(), is(64));
  }

  @Test
  void getDepartureStringReturnsDateAndTimeString() {
    Flight flight = createTestFlight();
    assertThat(flight.getDepartureString(), is("01/23/4567 12:34 PM"));
  }

  @Test
  void getDestinationReturnsDestinationString() {
    Flight flight = createTestFlight();
    assertThat(flight.getDestination(), is("LAX"));
  }

  @Test
  void getArrivalStringReturnsDateAndTimeString() {
    Flight flight = createTestFlight();
    assertThat(flight.getArrivalString(), is("02/11/4567 03:45 PM"));
  }
}
