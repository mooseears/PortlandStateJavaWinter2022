package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;

import static edu.pdx.cs410J.jmeziere.Project4.*;

public class TextParser implements AirlineParser<Airline> {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  @Override
  public Airline parse() throws ParserException {
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {

      String airlineName = br.readLine();
      if (airlineName == null) {
        throw new ParserException("Missing airline name");
      }
      Airline airline = new Airline(airlineName);

      String line;
      while ((line = br.readLine()) != null) {
        try {
          int flightNumber = getFlightNumberFromArgs(line);
          String flightSource = getAirportCodeFromArgs(br.readLine());
          Date flightDeparture = getFlightDateFromArgs(br.readLine().split(" "));
          String flightDestination = getAirportCodeFromArgs(br.readLine());
          Date flightArrival = getFlightDateFromArgs(br.readLine().split(" "));
          airline.addFlight(new Flight(flightNumber, flightSource, flightDeparture, flightDestination, flightArrival));
        } catch (IOException | InvalidArgumentException ex) {
          throw new ParserException("Cannot read from file.");
        }
      }

      return airline;

    } catch (IOException e) {
      throw new ParserException("While parsing airline text", e);
    }
  }
}
