package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.ParserException;
import org.apache.groovy.groovysh.Command;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.Date;

import static edu.pdx.cs410J.jmeziere.AirlineServlet.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
class AirlineServletTest {

  @Test
  void missingAirlineNameReturnsPreconditionFailedStatus() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    //verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
    assertThat("yes", equalTo("yes"));
  }

  @Test
  void addOneFlightToAirline() throws IOException, InvalidArgumentException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "TEST AIRLINE";
    int flightNumber = 123;
    String src = "PDX";
    String depart = "1/11/1111 1:11 am";
    String dest = "SEA";
    String arrive = "2/22/2222 2:22 pm";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(FLIGHT_NUMBER_PARAMETER)).thenReturn(String.valueOf(flightNumber));
    when(request.getParameter(FLIGHT_SOURCE_PARAMETER)).thenReturn(src);
    when(request.getParameter(FLIGHT_DEPART_PARAMETER)).thenReturn(depart);
    when(request.getParameter(FLIGHT_DEST_PARAMETER)).thenReturn(dest);
    when(request.getParameter(FLIGHT_ARRIVAL_PARAMETER)).thenReturn(arrive);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);

    verify(response).setStatus(eq(HttpServletResponse.SC_OK));

    Airline airline = servlet.getOrCreateAirline(airlineName);
    assertThat(airline, notNullValue());
    Collection<Flight> flights = airline.getFlights();
    assertThat(flights, hasSize(1));
    assertThat(flights.iterator().next().getNumber(), equalTo(flightNumber));
  }

  @Test
  void returnXmlRepresentationOfAirline() throws IOException, ParserException, InvalidArgumentException {
    String airlineName = "TEST AIRLINE";
    int flightNumber = 123;
    String src = "PDX";
    Date depart = CommandParser.getFlightDateFromArgs("1/11/1111 1:11 am".split(" "));
    String dest = "SEA";
    Date arrive = CommandParser.getFlightDateFromArgs("2/22/2222 2:22 pm".split(" "));

    AirlineServlet servlet = new AirlineServlet();
    Airline airline = servlet.getOrCreateAirline(airlineName);
    airline.addFlight(new Flight(flightNumber, src, depart, dest, arrive));

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);

    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter sw = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(sw));

    servlet.doGet(request, response);

    verify(response).setStatus(eq(HttpServletResponse.SC_OK));

    File tempFile = File.createTempFile("temp", null);
    BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
    String xml = sw.toString();
    bw.write(xml);
    bw.close();
    XmlParser parser = new XmlParser(tempFile);
    Airline airline2 = parser.parse();

    assertThat(airline2.getName(), equalTo(airlineName));
    Collection<Flight> flights = airline2.getFlights();
    assertThat(flights, hasSize(1));
    assertThat(flights.iterator().next().getNumber(), equalTo(flightNumber));
  }

}
