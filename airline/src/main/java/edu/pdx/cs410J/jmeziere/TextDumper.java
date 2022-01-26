package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class TextDumper implements AirlineDumper<Airline> {
  private final Writer writer;

  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  @Override
  public void dump(Airline airline) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
      ) {
      pw.println(airline.getName());

      for (Flight f : airline.getFlights()) {
        pw.println(f.getNumber());
        pw.println(f.getSource());
        pw.println(f.getDepartureString());
        pw.println(f.getDestination());
        pw.println(f.getArrivalString());
      }
      pw.flush();
    }
  }
}
