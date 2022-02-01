package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
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
  static final int MIN_ARGS = 8;
  static final String ERR_MISSING_ARGS = "Missing command line arguments!" +
          "\nUsage: [-print] [-readme] [-textFile filepath] [-pretty (filepath|-)] Airline FlightNumber DepartAirport DepartDate DepartTime ArrivalAirport ArrivalDate ArrivalTime";
  static final String ERR_EXTRA_ARGS = "There are extra command line arguments: ";
  static final String ERR_FLIGHT_NUM = "Invalid flight number. Should be 1-6 digits: ";
  static final String ERR_INVALID_AIRPORT_CODE = "Invalid airport code. Should be 3 letters: ";
  static final String ERR_AIRPORT_CODE_NOT_FOUND = "Invalid airport code. Airport code unknown: ";
  static final String ERR_FLIGHT_TIME_FORMAT = "Invalid date and time. Should be mm/dd/yyy hh:mm am/pm: ";
  static final String ERR_FLIGHT_TIME_PARADOX = "Invalid departure and arrival times. Arrival cannot happen before departure";
  static final String TEXT_FILE_FLAG = "-textFile";
  static final String PRETTY_PRINT_FLAG = "-pretty";
  static final String PRINT_FLIGHT_FLAG = "-print";
  static final String README_FLAG = "-readme";

  public static void main(String[] args) {
    boolean doReadme = false;
    boolean doPrint = false;
    boolean doCheckFile = false;
    boolean doReadFile = false;
    boolean doPrettyPrint = false;

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
    if (flag.contains("b")) { // beauty?
      doPrettyPrint = true;
    }

    if (!doReadme) {
      File airlineFile = null;
      String airlineName = "";
      int flightNumber = 0;
      String flightSource = "";
      Date flightDeparture = null;
      String flightDestination = "";
      Date flightArrival = null;
      String textFilePath = "";

      int currArg = 0; // Keeps track of which argument is being checked.
      if (doCheckFile) { // If textFile flag is enabled, check for text file
        try {
          textFilePath = getFilePathFromArgs(args, TEXT_FILE_FLAG);
          doReadFile = checkIfFileAlreadyExists(textFilePath);
        } catch (InvalidArgumentException ex) {
          System.err.println(ex.getMessage());
          System.exit(1);
        }
        airlineFile = new File(textFilePath);
      }

      if (doPrint) { currArg++; } // If print flag is enabled, start parsing at next argument
      if (doCheckFile) { currArg += 2; } // If textFile flag enabled, start parsing at next next argument
      if (doPrettyPrint) { currArg += 2; } // If pretty printing, start parsing at next next argument

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
        flightDeparture = getFlightDateFromArgs(Arrays.copyOfRange(args, currArg, currArg+3));
        currArg += 3;
        flightDestination = getAirportCodeFromArgs(args[currArg]);
        currArg++;
        flightArrival = getFlightDateFromArgs(Arrays.copyOfRange(args, currArg, currArg+3));
        currArg += 3;

        if (flightDeparture.compareTo(flightArrival) >= 0) {
          throw new InvalidArgumentException(ERR_FLIGHT_TIME_PARADOX);
        }

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

      // Print out flight info if flagged
      if (doPrint) { System.out.println(flight); }

      // Pretty print airline info if flagged
      if (doPrettyPrint) { PrettyPrint(args, airline); }

      // Write airline and flight info to file
      if (doCheckFile) {
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
          case README_FLAG:
            flag += "r";
            break;
          case PRINT_FLIGHT_FLAG:
            flag += "p";
            break;
          case TEXT_FILE_FLAG:
            flag += "f";
            break;
          case PRETTY_PRINT_FLAG:
            flag += "b";
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
  public static String getFilePathFromArgs(String[] args, String flag) throws InvalidArgumentException {
    String filePath = "";
    int currArg = 0;
    while (currArg < args.length - 1) {
      if (args[currArg].equals(flag) && currArg+1 < args.length) {
        filePath = args[currArg + 1];
      }
      currArg++;
    }
    if (filePath.equals("")) {
      throw new InvalidArgumentException("Could not find filepath!\n" + ERR_MISSING_ARGS);
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
        // System.out.println("Airline file created at " + airlineFile.getAbsolutePath());
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
      throw new InvalidArgumentException(ERR_INVALID_AIRPORT_CODE);
    }
  }

  /**
   * Checks and returns a valid <code>String</code> for a <code>Flight</code> date and time.
   * @param args
   *        A <code>String</code> <code>Array</code> input for the <code>Flight</code> date and time.
   * @return
   *        A <code>String</code> for the <code>Flight</code> date and time.
   */
  public static Date getFlightDateFromArgs(String[] args) throws InvalidArgumentException {
    String flightTimeString = args[0] + " " + args[1] + " " + args[2];
    // Make sure flightDeparture is a string formatted mm/dd/yyyy hh:mm
    //if (flightTimeString.matches("[0-1]?[0-9]/[0-3]?[0-9]/[0-9]{4} [0-1]?[0-9]:[0-5][0-9] (am|pm)")) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
      sdf.setLenient(false);
      return sdf.parse(flightTimeString);
    } catch (ParseException ex) {
      throw new InvalidArgumentException(ERR_FLIGHT_TIME_FORMAT);
    }
  }

  public static void PrettyPrint(String[] args, Airline airline) {
    boolean doPrintToFile = false;
    File printFile = null;
    try {
      String printFilePath = getFilePathFromArgs(args, PRETTY_PRINT_FLAG);
      if (!printFilePath.equals("-")) {
        doPrintToFile = true;
        checkIfFileAlreadyExists(printFilePath);
        printFile = new File(printFilePath);
      }
    } catch (InvalidArgumentException ex) {
      System.err.println(ex.getMessage());
      System.exit(1);
    }

    if (doPrintToFile) {
      try {
        PrettyPrinter printer = new PrettyPrinter(new BufferedWriter(new FileWriter(printFile)));
        printer.dump(airline);
      } catch (IOException ex) {
        System.err.println("Could not write to file " + printFile.getAbsolutePath());
        System.exit(1);
      }
    } else {
      System.out.println("Look I'm pretty printing to stdout");
    }
  }
}