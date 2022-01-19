package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AbstractFlight;

/**
 * This class represents a <code>Flight</code>.
 */
public class Flight extends AbstractFlight {
  private int flightNumber;
  private String src;
  private String depart;
  private String dest;
  private String arrive;

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
  public Flight(int flightNumber, String src, String depart, String dest, String arrive) {
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
   * Retrieves <code>Flight</code> departure date and time as a <code>String</code>.
   * @return depart
   */
  @Override
  public String getDepartureString() {
    return this.depart;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }

  /**
   * Retrieves <code>Flight</code> destination.
   * @return dest
   */
  @Override
  public String getDestination() {
    return this.dest;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }

  /**
   * Retrieves <code>Flight</code> arrival date and time as a <code>String</code>.
   * @return arrive
   */
  @Override
  public String getArrivalString() {
    return this.arrive;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }
}
