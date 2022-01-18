package edu.pdx.cs410J.jmeziere;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Flight} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class FlightTest {

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  @Disabled
  void getArrivalStringNeedsToBeImplemented() {
    Flight flight = createTestFlight();
    assertThrows(UnsupportedOperationException.class, flight::getArrivalString);
  }

  Flight createTestFlight() {
    return new Flight(64, "PDX", "1/23/4567 12:34", "LAX", "2/11/4567 15:45");
  }
  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */

  @Test
  @Disabled
  void forProject1ItIsOkayIfGetDepartureTimeReturnsNull() {
    Flight flight = createTestFlight();
    assertThat(flight.getDeparture(), is(nullValue()));
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
    assertThat(flight.getDepartureString(), is("1/23/4567 12:34"));
  }

  @Test
  void getDestinationReturnsDestinationString() {
    Flight flight = createTestFlight();
    assertThat(flight.getDestination(), is("LAX"));
  }

  @Test
  void getArrivalStringReturnsDateAndTimeString() {
    Flight flight = createTestFlight();
    assertThat(flight.getArrivalString(), is("2/11/4567 15:45"));
  }
}
