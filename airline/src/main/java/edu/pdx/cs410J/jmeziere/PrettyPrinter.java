package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Pretty prints an <code>Airline</code> and its flights to a file.
 */
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
                String prettyFlight = "  -Flight #" + f.getNumber() +
                        ":\n    Departing:\t" + AirportNames.getName(f.getSource()) + "\t" + f.getDepartureString() +
                        "\n    Arriving:\t" + AirportNames.getName(f.getDestination()) + "\t" + f.getArrivalString();
                pw.println(prettyFlight);
            }
            pw.flush();
        }
    }
}
