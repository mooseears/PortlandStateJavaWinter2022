package edu.pdx.cs410J.jmeziere;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class AirlineServlet extends HttpServlet {
  static final String AIRLINE_NAME_PARAMETER = "airlineName";
  static final String FLIGHT_NUMBER_PARAMETER = "flightNumber";
  static final String FLIGHT_SOURCE_PARAMETER = "flightSource";
  static final String FLIGHT_DEPART_PARAMETER = "flightDeparture";
  static final String FLIGHT_DEST_PARAMETER = "flightDestination";
  static final String FLIGHT_ARRIVAL_PARAMETER = "flightArrival";

  private final Map<String, Airline> airlines = new HashMap<>();

  /**
   * Handles an HTTP GET request from a client by writing the definition of the
   * word specified in the "word" HTTP parameter to the HTTP response.  If the
   * "word" parameter is not specified, all of the entries in the dictionary
   * are written to the HTTP response.
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/xml");

    String airlineName = getParameter(AIRLINE_NAME_PARAMETER, request);
    if (airlineName != null) {
      getOrCreateAirline(airlineName);
      dumpAirline(airlineName, response);
    } else {
      missingRequiredParameter(response, AIRLINE_NAME_PARAMETER);
    }
  }

  /**
   * Handles an HTTP POST request by storing the dictionary entry for the
   * "word" and "definition" request parameters.  It writes the dictionary
   * entry to the HTTP response.
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/plain");

    String airlineName = getParameter(AIRLINE_NAME_PARAMETER, request);
    if (airlineName == null) {
      missingRequiredParameter(response, AIRLINE_NAME_PARAMETER);
      return;
    }
    String flightNumberString = getParameter(FLIGHT_NUMBER_PARAMETER, request);
    if (flightNumberString == null) {
      missingRequiredParameter(response, FLIGHT_NUMBER_PARAMETER);
      return;
    }
    String flightSource = getParameter(FLIGHT_SOURCE_PARAMETER, request);
    if (flightSource == null) {
      missingRequiredParameter(response, FLIGHT_SOURCE_PARAMETER);
      return;
    }
    String flightDepartString = getParameter(FLIGHT_DEPART_PARAMETER, request);
    if (flightDepartString == null) {
      missingRequiredParameter(response, FLIGHT_DEPART_PARAMETER);
      return;
    }
    String flightDest = getParameter(FLIGHT_DEST_PARAMETER, request);
    if (flightDest == null) {
      missingRequiredParameter(response, FLIGHT_DEST_PARAMETER);
      return;
    }
    String flightArrivalString = getParameter(FLIGHT_ARRIVAL_PARAMETER, request);
    if (flightArrivalString == null) {
      missingRequiredParameter(response, FLIGHT_ARRIVAL_PARAMETER);
      return;
    }
    Date flightDepart = null;
    Date flightArrival = null;
    try {
      flightDepart = CommandParser.getFlightDateFromArgs(flightDepartString.split(" "));
      flightArrival = CommandParser.getFlightDateFromArgs(flightArrivalString.split(" "));
    } catch (InvalidArgumentException e) {
      response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
    }

    Airline airline = getOrCreateAirline(airlineName);
    airline.addFlight(new Flight(Integer.parseInt(flightNumberString), flightSource, flightDepart, flightDest, flightArrival));
    airlines.put(airlineName, airline);

    response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all dictionary entries.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/plain");

    this.airlines.clear();

    PrintWriter pw = response.getWriter();
    pw.println(Messages.allDictionaryEntriesDeleted());
    pw.flush();

    response.setStatus(HttpServletResponse.SC_OK);

  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   * <p>
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter(HttpServletResponse response, String parameterName)
          throws IOException {
    String message = Messages.missingRequiredParameter(parameterName);
    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes the definition of the given word to the HTTP response.
   * <p>
   * The text of the message is formatted with {@link TextDumper}
   */
  private void dumpAirline(String airlineName, HttpServletResponse response) throws IOException {
    Airline airline = getAirline(airlineName);

    if (airline == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    } else {
      PrintWriter pw = response.getWriter();

      File xmlFile = File.createTempFile("temp", null);
      XmlDumper dumper = new XmlDumper(xmlFile);
      dumper.dump(airline);
      StringBuilder sb = new StringBuilder();
      BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        sb.append(line);
      }
      String xml = sb.toString();
      pw.print(xml);
      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   * <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

  @VisibleForTesting
  Airline getOrCreateAirline(String airlineName) {
    Airline airline = getAirline(airlineName);
    if (airline == null) {
      airline = new Airline(airlineName);
      this.airlines.put(airlineName, airline);
    }
    return airline;
  }

  private Airline getAirline(String airlineName) {
    return this.airlines.get(airlineName);
  }

  private void dumpAirlineNames(HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder sb = new StringBuilder();
    for (String airlineName : airlines.keySet()) {
      sb.append(airlineName + " ");
    }
    pw.print(sb);

    response.setStatus(HttpServletResponse.SC_OK);
  }
}
