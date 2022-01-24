package edu.pdx.cs410J.jmeziere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The main class for the CS410J airline Project.
 */
public class Project2 {
  private static final int MIN_ARGS = 6;
  private static final String ERR_MISSING_ARGS = "Missing command line arguments!";
  private static final String ERR_EXTRA_ARGS = "There are extra command line arguments!";
  private static final String ERR_AIRLINE = "Airline name is invalid!";
  private static final String ERR_FLIGHT_NUM = "Flight number is invalid!";
  private static final String ERR_FLIGHT_SRC = "Flight source is invalid!";
  private static final String ERR_FLIGHT_DEST = "Flight destination is invalid!";
  private static final String ERR_FLIGHT_DEPART = "Flight departure is invalid!";
  private static final String ERR_FLIGHT_ARRIVAL = "Flight arrival is invalid!";

  public static void main(String[] args) {
    boolean doReadme = false;
    boolean doPrint = false;

    if (args.length < 1) {
      showErrorAndExit(args, ERR_MISSING_ARGS);
    }

    String flag = checkArgsForFlags(args);
    if (flag.contains("r")) {
      doReadme = true;
      displayReadme();
    } else {
      if (flag.contains("p")) {
        doPrint = true;
      }
    }

    if (!doReadme) {
      if (args.length < MIN_ARGS) {
        showErrorAndExit(args, ERR_MISSING_ARGS);
      } else {
        String airlineName = "";
        int flightNumber = 0;
        String flightSource = "";
        String flightDeparture = "";
        String flightDestination = "";
        String flightArrival = "";

        int currArg = 0; // Keeps track of which argument is being checked.
        if (doPrint) { currArg++; } // If print flag is enabled, start parsing at next argument

        // Check if the airline name is multiple strings in quotes
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
          airlineName = args[currArg];
        }
        currArg++;

        if (currArg < args.length - 1) {
          // Make sure flightNumber is a string of 1-6 digits
          if (args[currArg].matches("[0-9]([0-9]?){5}")) {
            flightNumber = (Integer.parseInt(args[currArg]));
          } else {
            showErrorAndExit(args, ERR_FLIGHT_NUM);
          }
        }
        currArg++;

        if (currArg < args.length - 1) {
          // Make sure flightSource is a string of 3 letters
          if (args[currArg].matches("(?i)[A-Z][A-Z][A-Z]")) {
            flightSource = args[currArg].toUpperCase();
          } else {
            showErrorAndExit(args, ERR_FLIGHT_SRC);
          }
        }
        currArg++;

        if (currArg < args.length - 2) {
          String temp = args[currArg] + " " + args[currArg + 1];
          // Make sure flightDeparture is a string formatted mm/dd/yyyy hh:mm
          if (temp.matches("[0-1]?[0-9]/[0-3]?[0-9]/[0-9]{4} [1-2]?[0-9]:[0-5][0-9]")) {
            flightDeparture = temp;
          } else {
            showErrorAndExit(args, ERR_FLIGHT_DEPART);
          }
        }
        currArg += 2;

        if (currArg < args.length - 1) {
          // Make sure flightDestination is a string of 3 letters
          if (args[currArg].matches("(?i)[A-Z][A-Z][A-Z]")) {
            flightDestination = args[currArg].toUpperCase();
          } else {
            showErrorAndExit(args, ERR_FLIGHT_DEST);
          }
        }
        currArg++;

        if (currArg < args.length - 1) {
          String temp = args[currArg] + " " + args[currArg + 1];
          // Make sure flightArrival is a string formatted mm/dd/yyyy hh:mm
          if (temp.matches("[0-1]?[0-9]/[0-3]?[0-9]/[0-9]{4} [1-2]?[0-9]:[0-5][0-9]")) {
            flightArrival = temp;
          } else {
            showErrorAndExit(args, ERR_FLIGHT_ARRIVAL);
          }
        }
        currArg += 2;

        // Check for extra arguments
        if (currArg < args.length) {
          showErrorAndExit(args, ERR_EXTRA_ARGS);
        }

        Airline airline = new Airline(airlineName);
        Flight flight = new Flight(flightNumber, flightSource, flightDeparture, flightDestination, flightArrival);
        airline.addFlight(flight);

        // Print out flight info if -print option is flagged
        if (doPrint) {
          for (Flight f : airline.getFlights()) {
            System.out.print(f);
          }
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
   * Method for handling specific input errors
   * @param args
   *        A <code>String </code> <code>Array</code> of command line arguments
   * @param error
   *        A <code>String</code> containing the error to be shown.
   */
  private static void showErrorAndExit(String[] args, String error) {
    switch (error) {
      case ERR_MISSING_ARGS:
        System.err.println(ERR_MISSING_ARGS);
        break;
      case ERR_EXTRA_ARGS:
        System.out.println(ERR_EXTRA_ARGS);
        break;
      case ERR_AIRLINE:
        System.err.println(ERR_AIRLINE);
        break;
      case ERR_FLIGHT_NUM:
        System.err.println(ERR_FLIGHT_NUM);
        break;
      case ERR_FLIGHT_SRC:
        System.err.println(ERR_FLIGHT_SRC);
        break;
      case ERR_FLIGHT_DEPART:
        System.err.println(ERR_FLIGHT_DEPART);
        break;
      case ERR_FLIGHT_DEST:
        System.err.println(ERR_FLIGHT_DEST);
        break;
      case ERR_FLIGHT_ARRIVAL:
        System.err.println(ERR_FLIGHT_ARRIVAL);
        break;
      default:
    }

    for (String arg : args) {
      System.out.println(arg);
    }
    System.exit(1);
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
}