package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AbstractFlight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents a <code>Flight</code>.
 */
public class Flight extends AbstractFlight implements Comparable<Flight> {
  private int flightNumber;
  private String src;
  private Date depart;
  private String dest;
  private Date arrive;

  /**
   * Creates a new <code>Flight</code>.
   * @param flightNumber
   *        The number of the flight, up to 6 digits long.
   * @param src
   *        The 3-letter airport code for where the flight starts.
   * @param depart
   *        The date and 24-hour time of the flight's departure
   *        as "mm/dd/yyyy hh:mm".
   * @param dest
   *        The 3-letter airport code for where the flight ends.
   * @param arrive
   *        The date and 24-hour time of the flight's arrival
   *        as "mm/dd/yyyy hh:mm".
   */
  public Flight(int flightNumber, String src, Date depart, String dest, Date arrive) {
    this.flightNumber = flightNumber;
    this.src = src;
    this.depart = depart;
    this.dest = dest;
    this.arrive = arrive;
  }

  /**
   * Retrieves <code>Flight</code> number.
   * @return flightNumber
   */
  @Override
  public int getNumber() {
    return this.flightNumber;
  }

  /**
   * Retrieves <code>Flight</code> source.
   * @return src
   */
  @Override
  public String getSource() {
    return this.src;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }

  /**
   * Retrieves <code>Flight</code> departure time.
   * @return depart
   */
  @Override
  public Date getDeparture() {
    return this.depart;
  }

  /**
   * Retrieves <code>Flight</code> departure date and time as a <code>String</code>.
   * @return depart as <code>String</code>.
   */
  @Override
  public String getDepartureString() {
    return new SimpleDateFormat("MM/dd/yyy hh:mm aa").format(this.depart);
  }

  /**
   * Retrieves <code>Flight</code> destination.
   * @return dest
   */
  @Override
  public String getDestination() {
    return this.dest;
  }

  /**
   * Retrieves <code>Flight</code> arrival time.
   * @return arrive
   */
  @Override
  public Date getArrival() {
    return this.arrive;
  }

  /**
   * Retrieves <code>Flight</code> arrival date and time as a <code>String</code>.
   * @return arrive as <code>String</code>.
   */
  @Override
  public String getArrivalString() {
    return new SimpleDateFormat("MM/dd/yyy hh:mm aa").format(this.arrive);
  }

  /**
   * Comparator for <code>Flight</code> in order of airport code, then departure time.
   * @param flight
   * @return 1 if flight has lower airport code and/or later time, 0 if equal, -1 otherwise
   */
  @Override
  public int compareTo(Flight flight) {
    if (this.src.compareTo(flight.getSource()) > 0) {
      return 1;
    } else if (this.src.compareTo(flight.getSource()) == 0) {
      return this.depart.compareTo(flight.getDeparture());
    } else {
      return -1;
    }
  }
}
