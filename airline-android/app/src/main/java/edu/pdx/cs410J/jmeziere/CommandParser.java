package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AirportNames;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String str) {
        super(str);
    }
}

public class CommandParser {
    static final String ERR_MISSING_ARGS = "Missing command line arguments!" +
            "\nUsage: [-print] [-readme] [-search] -host hostName -port portNumber Airline FlightNumber DepartAirport DepartDate DepartTime ArrivalAirport ArrivalDate ArrivalTime";
    static final String ERR_EXTRA_ARGS = "There are extra command line arguments.";
    static final String ERR_FLIGHT_NUM = "Invalid flight number. Should be 1-6 digits.";
    static final String ERR_INVALID_AIRPORT_CODE = "Invalid airport code. Should be 3 letters.";
    static final String ERR_AIRPORT_CODE_NOT_FOUND = "Invalid airport code. Airport code unknown.";
    static final String ERR_MISSING_AIRPORT = "Missing command line arguments!" +
            "\nWhen -search option used must specify Airline, FlightSource, and FlightDestination.";
    static final String ERR_FLIGHT_TIME_FORMAT = "Invalid date and time. Should be mm/dd/yyy hh:mm am/pm.";
    static final String ERR_FLIGHT_TIME_PARADOX = "Invalid departure and arrival times. Arrival cannot happen before departure";
    static final String ERR_PORT_NUM = "Invalid port number.";
    static final String ERR_MISSING_HOST = "Missing host name.";
    static final String ERR_MISSING_PORT = "Missing port number.";

    static final int MIN_AIRLINE_ARGS = 8;
    static final int MIN_SEARCH_ARGS = 2;
    static final int INVALID_NUM = -1234567;

    static final String HOST_FLAG = "-host";
    static final String PORT_FLAG = "-port";
    static final String SEARCH_FLAG = "-search";
    static final String PRINT_FLAG = "-print";
    static final String README_FLAG = "-readme";

    private String hostName = null;
    private int portNum = INVALID_NUM;
    private String flags = null;
    private String airlineName = null;
    private int flightNum = INVALID_NUM;
    private String flightSource = null;
    private Date flightDepart = null;
    private String flightDest = null;
    private Date flightArrive = null;

    public CommandParser(String[] args) {
        try {
            if (args.length < 1)
                throw new InvalidArgumentException(ERR_MISSING_ARGS);

            // Get flags
            int flagCount = 0;
            for (int i = 0; i < args.length; i++) {
                switch (args[i].toLowerCase()) {
                    case README_FLAG:
                        flags += "r";
                        flagCount++;
                        break;
                    case PRINT_FLAG:
                        flags += "p";
                        flagCount++;
                        break;
                    case SEARCH_FLAG:
                        flags += "s";
                        flagCount++;
                        break;
                    case HOST_FLAG:
                        flags += "h";
                        if (i+1 >= args.length)
                            throw new InvalidArgumentException(ERR_MISSING_ARGS);
                        hostName = args[++i];
                        flagCount += 2;
                        break;
                    case PORT_FLAG:
                        flags += "t";
                        if (i+1 >= args.length)
                            throw new InvalidArgumentException(ERR_MISSING_ARGS);
                        try {
                            portNum = Integer.parseInt(args[++i]);
                        } catch (Exception ex) {
                            throw new InvalidArgumentException(ERR_PORT_NUM);
                        }
                        flagCount += 2;
                        break;
                    default:
                }
            }

            if (!flags.contains("r")) {
                if (!flags.contains("h"))
                    throw new InvalidArgumentException(ERR_MISSING_HOST);
                if (!flags.contains("t"))
                    throw new InvalidArgumentException(ERR_MISSING_PORT);
                int currArg = flagCount;
                if (currArg >= args.length) {
                    throw new InvalidArgumentException(ERR_MISSING_ARGS);
                }
                airlineName = args[currArg];
                currArg++;
                if (currArg == args.length) {
                    flags += "b"; // stop parsing commands and set flag to print airline
                } else {
                    if (args.length - currArg < MIN_AIRLINE_ARGS && !flags.contains("s"))
                        throw new InvalidArgumentException(ERR_MISSING_ARGS);
                    if (args.length - currArg < MIN_SEARCH_ARGS && flags.contains("s"))
                        throw new InvalidArgumentException(ERR_MISSING_AIRPORT);
                    if (!flags.contains("s")) {
                        flightNum = getFlightNumberFromArgs(args[currArg]);
                        currArg++;
                    }
                    flightSource = getAirportCodeFromArgs(args[currArg]);
                    currArg++;
                    if (!flags.contains("s")) {
                        flightDepart = getFlightDateFromArgs(Arrays.copyOfRange(args, currArg, currArg + 3));
                        currArg += 3;
                    }
                    flightDest = getAirportCodeFromArgs(args[currArg]);
                    currArg++;
                    if (!flags.contains("s")) {
                        flightArrive = getFlightDateFromArgs(Arrays.copyOfRange(args, currArg, currArg + 3));
                        currArg += 3;
                    }

                    if (currArg < args.length)
                        throw new InvalidArgumentException(ERR_EXTRA_ARGS);
                }
            }
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    public String getHostName() { return hostName; }
    public int getPortNum() { return portNum; }
    public String getFlags() { return flags; }
    public String getAirlineName() { return airlineName; }
    public int getFlightNum() { return flightNum; }
    public String getFlightSrc() { return flightSource; }
    public Date getFlightDepart() { return flightDepart; }
    public String getFlightDest() { return flightDest; }
    public Date getFlightArrive() { return flightArrive; }

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
            if (AirportNames.getNamesMap().containsKey(arg.toUpperCase())) {
                return arg.toUpperCase();
            } else {
                throw new InvalidArgumentException(ERR_AIRPORT_CODE_NOT_FOUND);
            }
        }
        throw new InvalidArgumentException(ERR_INVALID_AIRPORT_CODE);
    }

    /**
     * Checks and returns a valid <code>String</code> for a <code>Flight</code> date and time.
     * @param args
     *        A <code>String</code> <code>Array</code> input for the <code>Flight</code> date and time.
     * @return
     *        A <code>String</code> for the <code>Flight</code> date and time.
     */
    public static Date getFlightDateFromArgs(String[] args) throws InvalidArgumentException {
        String[] timeSubstring = args[1].split(":");
        int hour = Integer.parseInt(timeSubstring[0]);
        String minute = ":" + timeSubstring[1];
        if (hour > 12)
            hour -= 12;
        String flightTimeString = args[0] + " " + hour + minute + " " + args[2];
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
}
