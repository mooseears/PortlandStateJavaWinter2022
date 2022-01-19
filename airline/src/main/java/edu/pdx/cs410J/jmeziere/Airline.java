package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AbstractAirline;

import java.util.Collection;
import java.util.Vector;

public class Airline extends AbstractAirline<Flight> {
  private final String name;
  private Collection<Flight> flights;

  public Airline(String name) {
    this.name = name;
    this.flights = new Vector<Flight>();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void addFlight(Flight flight) {
    this.flights.add(flight);
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public Collection<Flight> getFlights() {
    return this.flights;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }
}

