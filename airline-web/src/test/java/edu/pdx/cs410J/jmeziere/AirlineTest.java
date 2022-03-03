package edu.pdx.cs410J.jmeziere;


import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Airline} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class AirlineTest {
    Flight createTestFlight() {
        Flight flight = null;
        try {
            flight = new Flight(64, "PDX", new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("1/23/4567 12:34 pm"), "LAX", new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("2/11/4567 3:45 pm"));
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }
        return flight;
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
