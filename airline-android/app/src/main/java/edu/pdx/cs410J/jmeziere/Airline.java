package edu.pdx.cs410J.jmeziere;

import java.util.ArrayList;
import java.util.Collection;

import edu.pdx.cs410J.AbstractAirline;

/**
 * This class represents an <code>Airline</code> and its <code>Flight</code>/s.
 */
public class Airline extends AbstractAirline<Flight> {
  private final String name;
  private ArrayList<Flight> flightList;

  /**
   * Creates a new <code>Airline</code>
   * @param name
   *        The name of the airline.
   */
  public Airline(String name) {
    this.name = name;
    this.flightList = new ArrayList<>();
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
    if (flightList.isEmpty()) {
      flightList.add(flight);
    } else {
      for (Flight f : flightList) {
        if (flight.compareTo(f) < 0) {
          flightList.add(flightList.indexOf(f), flight);
          return;
        }
      }
      flightList.add(flight);
    }
  }

  /**
   * Retrieves the list of <code>Flight</code>s under the <code>Airline</code>.
   * @return flights
   */
  @Override
  public Collection<Flight> getFlights() {
    return this.flightList;
  }
}

