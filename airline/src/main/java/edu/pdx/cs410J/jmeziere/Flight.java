package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {
  private int flightNumber;
  private String src;
  private String depart;
  private String dest;
  private String arrive;

  public Flight(int flightNumber, String src, String depart, String dest, String arrive) {
    this.flightNumber = flightNumber;
    this.src = src;
    this.depart = depart;
    this.dest = dest;
    this.arrive = arrive;
  }

  @Override
  public int getNumber() {
    return this.flightNumber;
    //return 42;
  }

  @Override
  public String getSource() {
    return this.src;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getDepartureString() {
    return this.depart;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getDestination() {
    return this.dest;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getArrivalString() {
    return this.arrive;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }
}
