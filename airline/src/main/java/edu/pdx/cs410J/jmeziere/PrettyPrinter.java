package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class PrettyPrinter implements AirlineDumper<Airline> {
    private final Writer writer;

    public PrettyPrinter(Writer writer) {
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
