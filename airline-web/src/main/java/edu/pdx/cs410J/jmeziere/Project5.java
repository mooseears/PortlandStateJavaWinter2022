package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {
    public static void main(String... args) {
        CommandParser commands = new CommandParser(args);
        String hostName = commands.getHostName();
        int port = commands.getPortNum();
        String flags = commands.getFlags();

        if (flags.contains("r")){
            displayReadme();
        } else {
            AirlineRestClient client = new AirlineRestClient(hostName, port);

            try {
                StringWriter writer = new StringWriter();
                PrettyPrinter printer = new PrettyPrinter(writer);
                Airline airline = new Airline(commands.getAirlineName());

                if (flags.contains("b") || flags.contains("s")) {
                    if (flags.contains("s")) {
                        Map<String, Airline> airlines = client.searchAirline(airline.getName(), commands.getFlightSrc(), commands.getFlightDest());
                        if (airlines.get(airline.getName()).getFlights().isEmpty()) {
                            System.out.println("No flights found under airline " + airline.getName() + " flying from " + commands.getFlightSrc() + " to " + commands.getFlightDest() + ".");
                        } else {
                            for (Airline line : airlines.values()) {
                                printer.dump(line);
                            }
                        }
                    } else {
                        Map<String, Airline> airlines = client.getAirline(airline.getName());
                        if (airlines.get(airline.getName()).getFlights().isEmpty()) {
                            System.out.println("No flights found under airline " + airline.getName() + ".");
                        } else {
                            for (Airline line : airlines.values()) {
                                printer.dump(line);
                            }
                        }
                    }
                } else {
                    Flight flight = new Flight(
                            commands.getFlightNum(),
                            commands.getFlightSrc(),
                            commands.getFlightDepart(),
                            commands.getFlightDest(),
                            commands.getFlightArrive()
                    );
                    airline.addFlight(flight);
                    if (flags.contains("p"))
                        System.out.println(flight);
                    client.addAirlineFlightEntry(commands.getAirlineName(), airline);
                }
            } catch (IOException ex) {
                error("While contacting server: " + ex);
                return;
            } catch (ParserException ex) {
                error(ex.getMessage());
            } catch (Exception ex) {
                error("Couldn't parse XML from server: " + ex);
            }
        }

        System.exit(0);
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Displays the contents of a readme file.
     */
    public static void displayReadme() {
        try (InputStream readme = Project5.class.getResourceAsStream("README.txt")) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(readme))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Readme file not found\n");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Readme file issue\n");
            System.exit(1);
        }
    }
}