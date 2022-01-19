package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AbstractAirline;

import java.util.Collection;
import java.util.Vector;

/**
 * This class represents an <code>Airline</code> and its <code>Flight</code>/s.
 */
public class Airline extends AbstractAirline<Flight> {
  private final String name;
  private Collection<Flight> flights;

  /**
   * Creates a new <code>Airline</code>
   * @param name
   *        The name of the airline.
   */
  public Airline(String name) {
    this.name = name;
    this.flights = new Vector<Flight>();
  }

  /**
   * Retrieves the <code>Airline</code> name.
   * @return name
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Adds a new flight under the <code>Airline</code>.
   * @param flight
   *        The <code>Flight</code> object to be added to the <code>Airline</code>.
   */
  @Override
  public void addFlight(Flight flight) {
    this.flights.add(flight);
  }

  /**
   * Retrieves the list of <code>Flight</code>s under the <code>Airline</code>.
   * @return flights
   */
  @Override
  public Collection<Flight> getFlights() {
    return this.flights;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }
}

