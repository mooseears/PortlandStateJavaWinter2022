package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.ParserException;
import jdk.jshell.spi.ExecutionControl;

import javax.swing.text.html.parser.Parser;
import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

class InvalidArgumentException extends Exception {
  public InvalidArgumentException(String str) {
    super(str);
  }
}

/**
 * The main class for the CS410J airline Project.
 */
public class Project1 {
  static final int MIN_ARGS = 6;
  static final String ERR_MISSING_ARGS = "Missing command line arguments!" +
          "\nUsage: [-options] Airline FlightNumber DepartAirport DepartDate DepartTime ArrivalAirport ArrivalDate ArrivalTime";
  static final String ERR_EXTRA_ARGS = "There are extra command line arguments: ";
  static final String ERR_FLIGHT_NUM = "Invalid flight number. Should be 1-6 digits: ";
  static final String ERR_AIRPORT_CODE = "Invalid airport code. Should be 3 letters: ";
  static final String ERR_FLIGHT_TIME = "Invalid date and time. Should be mm/dd/yyy hh:mm : ";

  public static void main(String[] args) {
    boolean doReadme = false;
    boolean doPrint = false;
    boolean doParseFile = false;

    try { // Check for at least one argument
      if (args.length < 1) {
        throw new InvalidArgumentException(ERR_MISSING_ARGS);
      }
    } catch (InvalidArgumentException ex) {
      System.err.println(ex.getMessage());
    }

    String flag = checkArgsForFlags(args);
    if (flag.contains("r")) {
      doReadme = true;
      displayReadme();
    }
    if (flag.contains("p")) {
      doPrint = true;
    }
    if (flag.contains("f")) {
      doParseFile = true;
    }

    if (!doReadme) {
      File airlineFile = null;
      String airlineName = "";
      int flightNumber = 0;
      String flightSource = "";
      String flightDeparture = "";
      String flightDestination = "";
      String flightArrival = "";
      String filePath = "";

      int currArg = 0; // Keeps track of which argument is being checked.
      if (doParseFile) { // If file flag is enabled, check for text file
        while (args[currArg].startsWith("-")) {
          if (args[currArg].equals("-textFile")) {
            filePath = args[currArg + 1];
          }
          currArg++;
        }

        airlineFile = new File(filePath);
        try {
          if (airlineFile.createNewFile()) {
            System.out.println("Airline file created at " + airlineFile.getAbsolutePath());
          }
          doParseFile = true;
        } catch (IOException ex) {
          System.err.println("Could not create file at " + airlineFile.getAbsolutePath());
          System.exit(1);
        }
      }

      currArg = 0;
      if (doPrint) { currArg++; } // If print flag is enabled, start parsing at next argument
      if (doParseFile) { currArg += 2; } // If textFile flag enabled, start parsing at next argument

      try { // Get airline and flight info from command line
        // Make sure there are enough arguments to fulfill required parameters
        if ((MIN_ARGS + currArg) >= args.length) {
          throw new ArrayIndexOutOfBoundsException(ERR_MISSING_ARGS);
        }

        airlineName = getAirlineFromArgs(args[currArg]);
        currArg++;
        flightNumber = getFlightNumberFromArgs(args[currArg]);
        currArg++;
        flightSource = getAirportCodeFromArgs(args[currArg]);
        currArg++;
        flightDeparture = getFlightDateFromArgs(Arrays.copyOfRange(args, currArg, currArg+2));
        currArg += 2;
        flightDestination = getAirportCodeFromArgs(args[currArg]);
        currArg++;
        flightArrival = getFlightDateFromArgs(Arrays.copyOfRange(args, currArg, currArg+2));
        currArg += 2;

        if (currArg < args.length) {
          throw new InvalidArgumentException(ERR_EXTRA_ARGS);
        }

      } catch (InvalidArgumentException | ArrayIndexOutOfBoundsException ex) {
        System.err.println(ex.getMessage());
        System.exit(1);
      }

      // Create airline and flight
      Airline airline = null;
      if (doParseFile) { // Get airline and flight info from file
        try {
          TextParser parser = new TextParser(new BufferedReader(new FileReader(airlineFile)));
          airline = parser.parse();
          if (!airline.getName().equals(airlineName)) {
            throw new InvalidArgumentException("Airline names do not match!");
          }
        } catch (IOException | ParserException ex) {
          System.err.println("Could not read from file " + airlineFile.getAbsolutePath());
          System.exit(1);
        } catch (InvalidArgumentException ex) {
          System.err.println(ex.getMessage());
          System.exit(1);
        }
      } else {
        airline = new Airline(airlineName);
      }

      Flight flight = new Flight(flightNumber, flightSource, flightDeparture, flightDestination, flightArrival);
      airline.addFlight(flight);

      // Print out flight info if -print option is flagged
      if (doPrint) {
        for (Flight f : airline.getFlights()) {
          System.out.println(f);
        }
      }

      if (doParseFile) { // Write airline and flight info to file
        try {
          TextDumper dumper = new TextDumper(new BufferedWriter(new FileWriter(airlineFile)));
          dumper.dump(airline);
        } catch (IOException ex) {
          System.err.println("Could not write to file " + airlineFile.getAbsolutePath());
        }
      }

    }

    System.exit(0);
  }

  /**
   * Checks for options in the command line arguments
   * @param args
   *        A <code>String </code> <code>Array</code> of command line arguments
   * @return flag
   *        A string containing characters for each flag found in args
   */
  private static String checkArgsForFlags(String[] args) {
    String flag = "";
    for (String arg : args) {
      if (arg != null && arg.startsWith("-")) {
        switch (arg) {
          case "-readme":
          case "-README":
          case "-r":
            flag += "r";
            break;
          case "-print":
          case "-p":
            flag += "p";
            break;
          case "-textFile":
          case "-f":
            flag += "f";
          default:
        }
      }
    }

    return flag;
  }

  /**
   * Displays the contents of a readme file.
   */
  private static void displayReadme() {
    try (InputStream readme = Project1.class.getResourceAsStream("README.txt")) {
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

  /**
   * Creates and returns a valid <code>String</code> for an <code>Airline</code> name.
   * @param arg
   *        A <code>String</code> input for the <code>Airline</code> name.
   * @return
   *        A <code>String</code> for the <code>Airline</code> name.
   */
  public static String getAirlineFromArgs(String arg) {
    String airlineName = "";
    // Check if the airline name is multiple strings in quotes
    /*
    if (args[currArg].startsWith("\"")) {
      while (!args[currArg].endsWith("\"")) {
        airlineName += (args[currArg] + " ");
        if (currArg < args.length - 1) {
          currArg++;
        } else {
          showErrorAndExit(args, ERR_MISSING_ARGS);
        }
      }
      airlineName += args[currArg];
    } else {
    */
      airlineName = arg;
    //}

    return airlineName;
  }

  /**
   * Creates and returns a valid <code>int</code> for a <code>Flight</code> number.
   * @param arg
   *        A <code>String</code> input for the <code>Flight</code> number.
   * @return
   *        An <code>int</code> for the <code>Flight</code> number.
   */
  public static int getFlightNumberFromArgs(String arg) throws InvalidArgumentException {
    // Make sure flightNumber is a string of 1-6 digits
    if (arg.matches("[0-9]([0-9]?){5}")) {
       return Integer.parseInt(arg);
    } else {
      throw new InvalidArgumentException(ERR_FLIGHT_NUM);
    }
  }

  /**
   * Checks and returns a valid <code>String</code> for a <code>Flight</code> airport code.
   * @param arg
   *        A <code>String</code> input for the <code>Flight</code> airport code.
   * @return
   *        A <code>String</code> for the <code>Flight</code> airport code.
   */
  public static String getAirportCodeFromArgs(String arg) throws InvalidArgumentException {
    // Make sure flightSource is a string of 3 letters
    if (arg.matches("(?i)[A-Z][A-Z][A-Z]")) {
      return arg.toUpperCase();
    } else {
      throw new InvalidArgumentException(ERR_AIRPORT_CODE);
    }
  }

  /**
   * Checks and returns a valid <code>String</code> for a <code>Flight</code> date and time.
   * @param args
   *        A <code>String</code> <code>Array</code> input for the <code>Flight</code> date and time.
   * @return
   *        A <code>String</code> for the <code>Flight</code> date and time.
   */
  public static String getFlightDateFromArgs(String[] args) throws InvalidArgumentException {
    String flightDeparture = args[0] + " " + args[1];
    // Make sure flightDeparture is a string formatted mm/dd/yyyy hh:mm
    if (flightDeparture.matches("[0-1]?[0-9]/[0-3]?[0-9]/[0-9]{4} [1-2]?[0-9]:[0-5][0-9]")) {
      return flightDeparture;
    } else {
      throw new InvalidArgumentException(ERR_FLIGHT_TIME);
    }
  }
}