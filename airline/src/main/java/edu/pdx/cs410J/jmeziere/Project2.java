package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.Arrays;

class InvalidArgumentException extends Exception {
  public InvalidArgumentException(String str) {
    super(str);
  }
}

/**
 * The main class for the CS410J airline Project.
 */
public class Project2 {
  static final int MIN_ARGS = 6;
  static final String ERR_MISSING_ARGS = "Missing command line arguments!" +
          "\nUsage: [-print] [-readme] [-textFile filepath] Airline FlightNumber DepartAirport DepartDate DepartTime ArrivalAirport ArrivalDate ArrivalTime";
  static final String ERR_EXTRA_ARGS = "There are extra command line arguments: ";
  static final String ERR_FLIGHT_NUM = "Invalid flight number. Should be 1-6 digits: ";
  static final String ERR_AIRPORT_CODE = "Invalid airport code. Should be 3 letters: ";
  static final String ERR_FLIGHT_TIME = "Invalid date and time. Should be mm/dd/yyy hh:mm : ";

  public static void main(String[] args) {
    boolean doReadme = false;
    boolean doPrint = false;
    boolean doCheckFile = false;
    boolean doReadFile = false;

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
      doCheckFile = true;
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
      if (doCheckFile) { // If file flag is enabled, check for text file
        try {
          filePath = getFilePathFromArgs(args);
          doReadFile = checkIfFileAlreadyExists(filePath);
        } catch (InvalidArgumentException ex) {
          System.err.println(ex.getMessage());
          System.exit(1);
        }
        airlineFile = new File(filePath);
      }

      if (doPrint) { currArg++; } // If print flag is enabled, start parsing at next argument
      if (doCheckFile) { currArg += 2; } // If textFile flag enabled, start parsing at next argument

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
      if (doReadFile) { // Get airline and flight info from file
        try {
          TextParser parser = new TextParser(new BufferedReader(new FileReader(airlineFile)));
          airline = parser.parse();
          if (!airline.getName().equals(airlineName)) {
            throw new InvalidArgumentException("Airline names do not match!");
          }
        } catch (IOException | ParserException ex) {
          System.err.println(ex.getMessage() + ": " + airlineFile.getAbsolutePath());
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

      if (doCheckFile) { // Write airline and flight info to file
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
  public static String checkArgsForFlags(String[] args) {
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
    try (InputStream readme = Project2.class.getResourceAsStream("README.txt")) {
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
   *
   * @param args
   *        A <code>String</code> <code>Array</code> of command line arguments.
   * @return
   *        The filepath as a <code>String</code>.
   * @throws InvalidArgumentException
   *        If filepath cannot be found in args
   */
  public static String getFilePathFromArgs(String[] args) throws InvalidArgumentException {
    String filePath = "";
    int currArg = 0;
    while (currArg < args.length && args[currArg].startsWith("-")) {
      if (args[currArg].equals("-textFile") && currArg+1 < args.length) {
        filePath = args[currArg + 1];
      }
      currArg++;
    }
    if (filePath.equals("")) {
      throw new InvalidArgumentException(ERR_MISSING_ARGS + " Could not find filepath!");
    } else {
      return filePath;
    }
  }

  /**
   * Checks if the chosen file exists and creates it if not.
   * @param filePath
   *        Location of file
   * @return
   *        Returns a <code>boolean</code>. True if file does already exist, False if not.
   * @throws InvalidArgumentException
   *        If file cannot be opened, read, or created.
   */
  public static boolean checkIfFileAlreadyExists(String filePath) throws InvalidArgumentException {
    File airlineFile = null;
    try {
      airlineFile = new File(filePath);

      if (airlineFile.createNewFile()) {
        System.out.println("Airline file created at " + airlineFile.getAbsolutePath());
        return false;
      }
    } catch (IOException ex) {
      throw new InvalidArgumentException("Could not create file at " + airlineFile.getAbsolutePath());
    }

    return true;
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
    if (flightDeparture.matches("[0-1]?[0-9]/[0-3]?[0-9]/[0-9]{4} [0-2]?[0-9]:[0-5][0-9]")) {
      return flightDeparture;
    } else {
      throw new InvalidArgumentException(ERR_FLIGHT_TIME);
    }
  }
}