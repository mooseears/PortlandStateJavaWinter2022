package edu.pdx.cs410J.jmeziere;

import java.io.*;
import java.util.Arrays;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {
  private static final int MIN_ARGS = 6;

  public static void main(String[] args) {
    boolean doPrint = false;

    if (args.length < 1) {
      showErrorMissingArguments(args);
    }

    String flag = checkArgsForFlags(args);
    switch (flag) {
      case "r":
        displayReadme();
        break;
      case "p":
        doPrint = true;
        //args = Arrays.copyOfRange(args, 1, args.length - 1);
        break;
      default:
    }

    if (!flag.equals("r")) {
      if (args.length < MIN_ARGS) {
        showErrorMissingArguments(args);
      } else {
        String airlineName = "";
        int flightNumber = 0;
        String flightSource = "";
        String flightDeparture = "";
        String flightDestination = "";
        String flightArrival = "";

        int currArg = 0;
        if (doPrint) { currArg++; }

        if (args[currArg].startsWith("\"")) {
          while (!args[currArg].endsWith("\"")) {
            airlineName += (args[currArg] + " ");
            if (currArg < args.length - 1) {
              currArg++;
            } else {
              showErrorMissingArguments(args);
            }
          }
          airlineName += args[currArg];
        } else {
          airlineName = args[currArg];
        }
        currArg++;

        if (currArg < args.length - 1) {
          //try (Integer.parseInt(args[currArg])) {
            flightNumber = (Integer.parseInt(args[currArg]));

          //} catch (NumberFormatException e) {
            //System.err.println("Flight number is invalid!");
          //}
        }
        currArg++;

        if (currArg < args.length - 1) {
          if (args[currArg].matches("(?i)[A-Z][A-Z][A-Z]")) {
            flightSource = args[currArg].toUpperCase();
          } else {
            System.err.println("Flight source is invalid!");
          }
        }
        currArg++;

        if (currArg < args.length - 2) {
          String temp = args[currArg] + " " + args[currArg + 1];
          if (temp.matches("[0-1]?[0-9]/[0-3]?[0-9]/[0-9]{4} [1-2]?[0-9]:[0-5][0-9]")) {
            flightDeparture = temp;
          } else {
            System.err.println("Flight departure is invalid!");
          }
        }
        currArg += 2;

        if (currArg < args.length - 1) {
          if (args[currArg].matches("(?i)[A-Z][A-Z][A-Z]")) {
            flightDestination = args[currArg].toUpperCase();
          } else {
            System.err.println("Flight destination is invalid!");
          }
        }
        currArg++;

        if (currArg < args.length - 1) {
          String temp = args[currArg] + " " + args[currArg + 1];
          if (temp.matches("[0-1]?[0-9]/[0-3]?[0-9]/[0-9]{4} [1-2]?[0-9]:[0-5][0-9]")) {
            flightArrival = temp;
          } else {
            System.err.println("Flight arrival is invalid!");
          }
        }
        currArg += 2;

        if (currArg < args.length) {
          System.err.println("Too many arguments!");
        }

        Airline airline = new Airline(airlineName);
        Flight flight = new Flight(flightNumber, flightSource, flightDeparture, flightDestination, flightArrival);
        airline.addFlight(flight);

        if (doPrint) {
          for (Flight f : airline.getFlights()) {
            System.out.print(f);
          }
        }

      }
    }

    System.exit(0);
  }

  private static String checkArgsForFlags(String[] args) {
    String flag = "";
    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("-readme")) {
        flag = "r";
      } else if (args[0].equalsIgnoreCase("-print")) {
        flag = "p";
      }
    } else {
      if (args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")) {
        flag = "r";
      } else if (args[0].equalsIgnoreCase("-print") || args[1].equalsIgnoreCase("-print")) {
        flag = "p";
      }
    }

    return flag;
  }

  private static void showErrorMissingArguments(String[] args) {
    System.err.println("Missing command line arguments");
    for (String arg : args) {
      System.out.println(arg);
    }
    System.exit(1);
  }

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
}