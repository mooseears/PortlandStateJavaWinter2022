package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class AirlineRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final String url;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient(String hostName, int port)
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

  /**
   * Returns all dictionary entries from the server
   */
  public Map<String, Airline> getAllAirlinesAndFlights() throws IOException, ParserException {
    Response responseList = get(this.url, Map.of());
    String[] airlineNames = responseList.getContent().split(" ");
    Map<String, Airline> airlines = new HashMap<>();

    for (String name : airlineNames) {
      Response responseAirline = get(this.url, Map.of("airlineName", name));
      File tempFile = File.createTempFile("temp", null);
      BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
      String xml = responseAirline.getContent();
      bw.write(xml);
      bw.close();
      XmlParser parser = new XmlParser(tempFile);
      Airline airline = parser.parse();
      airlines.put(airline.getName(), airline);
    }
    return airlines;
  }

  /**
   * Returns the definition for the given word
   */
  public Map<String, Airline> getAirline(String name) throws IOException, ParserException {
    Map<String, Airline> airlines = new HashMap<>();
    Response response = get(this.url, Map.of("airlineName", name));
    File tempFile = File.createTempFile("temp", null);
    BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
    String xml = response.getContent();
    if (xml.contains("<airline>")) {
      bw.write(xml);
      bw.close();
      XmlParser parser = new XmlParser(tempFile);
      Airline airline = parser.parse();
      airlines.put(airline.getName(), airline);
    } else {
      throw new ParserException("No flights found under airline " + name);
    }
    return airlines;
  }

  /**
   * Returns the definition for the given word
   */
  public Map<String, Airline> searchAirline(String name, String src, String dest) throws IOException, ParserException {
    Map<String, Airline> airlines = new HashMap<>();
    Response response = get(this.url, Map.of("airlineName", name));
    File tempFile = File.createTempFile("temp", null);
    BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
    String xml = response.getContent();
    if (xml.contains("<airline>")) {
      bw.write(xml);
      bw.close();
      XmlParser parser = new XmlParser(tempFile);
      Airline airline = parser.parse();
      Airline searchedAirline = new Airline(airline.getName());
      for (Flight flight : airline.getFlights()) {
        if (flight.getSource().equalsIgnoreCase(src) && flight.getDestination().equals(dest)) {
          searchedAirline.addFlight(flight);
        }
      }
      airlines.put(airline.getName(), searchedAirline);
    } else {
      throw new ParserException("No flights found under airline " + name+ " flying from " + src + " to " + dest + ".");
    }
    return airlines;
  }

  public void addAirlineFlightEntry(String airlineName, Airline airline) throws IOException {
    Flight flight = airline.getFlights().iterator().next();
    Response response = post(this.url, Map.of(
            "airlineName", airlineName,
            "flightNumber", String.valueOf(flight.getNumber()),
            "flightSource", flight.getSource(),
            "flightDeparture", flight.getDepartureString(),
            "flightDestination", flight.getDestination(),
            "flightArrival", flight.getArrivalString()));
    throwExceptionIfNotOkayHttpStatus(response);
  }

  private void throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
  }

}
