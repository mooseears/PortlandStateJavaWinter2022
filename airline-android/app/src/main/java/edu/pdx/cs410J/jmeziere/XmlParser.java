package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import static edu.pdx.cs410J.jmeziere.CommandParser.*;
//import static edu.pdx.cs410J.jmeziere.Project5.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.Date;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

/**
 * Class for XmlParser
 */
public class XmlParser implements AirlineParser<Airline> {
    private File xmlFile = null;

    public XmlParser(File file) {
        this.xmlFile = file;
    }

    /**
     * Reads an <code>Airline</code> and its <code>Flight</code>s from an XML file.
     *
     */
    public Airline parse() throws ParserException {
        Airline airline = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(null);
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();

            String airlineName = root.getElementsByTagName("name").item(0).getTextContent();
            airline = new Airline(airlineName);

            NodeList flightNodes = root.getElementsByTagName("flight");
            for (int i = 0; i <flightNodes.getLength(); i++) {
                Element fElement = (Element) flightNodes.item(i);
                int flightNumber = getFlightNumberFromArgs(
                        fElement.getElementsByTagName("number")
                        .item(0).getTextContent());
                String flightSource = getAirportCodeFromArgs(
                        fElement.getElementsByTagName("src")
                        .item(0).getTextContent());

                Date flightDeparture = getDateFromXML((Element) fElement.getElementsByTagName("depart").item(0));

                String flightDestination = getAirportCodeFromArgs(
                        fElement.getElementsByTagName("dest")
                        .item(0).getTextContent());

                Date flightArrival = getDateFromXML((Element) fElement.getElementsByTagName("arrive").item(0));

                airline.addFlight(new Flight(flightNumber, flightSource, flightDeparture, flightDestination, flightArrival));
            }
            return airline;

        } catch (Exception ex) {
            throw new ParserException(ex.getMessage());
        }
    }

    public Date getDateFromXML(Element dateElement) throws ParserException {
        try {
            Element eFlightDate = (Element) dateElement.getElementsByTagName("date")
                    .item(0);
            Element eFlightTime = (Element) dateElement.getElementsByTagName("time")
                    .item(0);
            String flight = eFlightDate.getAttribute("month")
                    + "/" + eFlightDate.getAttribute("day")
                    + "/" + eFlightDate.getAttribute("year")
                    + " " + eFlightTime.getAttribute("hour")
                    + ":" + eFlightTime.getAttribute("minute");
            if (Integer.parseInt(eFlightTime.getAttribute("hour")) > 12) {
                flight += " pm";
            } else {
                flight += " am";
            }
            return getFlightDateFromArgs(flight.split(" "));
        } catch (Exception ex) {
            throw new ParserException("Can't read date from XML file.");
        }
    }
}
