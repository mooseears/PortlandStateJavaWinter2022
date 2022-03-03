package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PrettyPrinterTest {

  @Test
  void airlineNameIsDumpedInTextFormat() throws InvalidArgumentException {
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor));

    String airlineName = "Test Airline";
    int flightNumber = 123;
    String src = "PDX";
    Date depart = CommandParser.getFlightDateFromArgs("1/11/1111 1:11 am".split(" "));
    String dest = "SEA";
    Date arrive = CommandParser.getFlightDateFromArgs("2/22/2222 2:22 pm".split(" "));

    Airline airline = new Airline(airlineName);
    airline.addFlight(new Flight(flightNumber, src, depart, dest, arrive));

    StringWriter sw = new StringWriter();
    PrettyPrinter printer = new PrettyPrinter(sw);
    printer.dump(airline);

    String text = sw.toString();
    assertThat(outputStreamCaptor.toString(), containsString(airlineName));
  }
}
