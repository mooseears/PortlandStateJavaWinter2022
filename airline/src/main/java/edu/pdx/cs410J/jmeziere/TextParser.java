package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

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
        int flightNumber = Integer.parseInt(line);
        String flightSource = br.readLine();
        String flightDeparture = br.readLine();
        String flightDestination = br.readLine();
        String flightArrival = br.readLine();
        airline.addFlight(new Flight(flightNumber, flightSource, flightDeparture, flightDestination, flightArrival));
      }

      return airline;

    } catch (IOException e) {
      throw new ParserException("While parsing airline text", e);
    }
  }
}
