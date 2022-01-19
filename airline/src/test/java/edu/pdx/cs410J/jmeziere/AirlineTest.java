package edu.pdx.cs410J.jmeziere;


import java.util.Collection;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Airline} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class AirlineTest {
    Flight createTestFlight() {
        return new Flight(64, "PDX", "1/23/4567 12:34", "LAX", "2/11/4567 15:45");
    }

    Airline createTestAirline() {
        return new Airline("Test");
    }

    @Test
    void canGetAirlineName() {
        Airline airline = createTestAirline();
        assertThat(airline.getName(), is("Test"));
    }

    @Test
    void canAddFlightToFlightCollectionAndGetFlightsFromAirline() {
        Airline airline = createTestAirline();
        Flight testFlight = createTestFlight();
        airline.addFlight(testFlight);
        Collection<Flight> flightList = airline.getFlights();
        assertThat(flightList.contains(testFlight), is(true));
    }
}
