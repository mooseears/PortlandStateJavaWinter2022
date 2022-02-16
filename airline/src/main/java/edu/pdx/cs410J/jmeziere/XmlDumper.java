package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.ProjectXmlHelper;

import java.io.Writer;
import javax.xml.parsers.*;
import java.io.File;
import java.util.Date;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class XmlDumper implements AirlineDumper<Airline> {
    private File xmlFile;

    public XmlDumper(File file) {
        this.xmlFile = file;
    }

    public void dump(Airline airline) {

        try {
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            dFactory.setValidating(true);
            DocumentBuilder builder = dFactory.newDocumentBuilder();
            builder.setErrorHandler(null);
            Document doc = builder.newDocument();

            Element root = doc.createElement("airline");
            doc.appendChild(root);
            Element airlineName = doc.createElement("name");
            airlineName.setTextContent(airline.getName());
            root.appendChild(airlineName);

            for (Flight f : airline.getFlights()) {
                root.appendChild(createFlightElement(doc, f));
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transform = tFactory.newTransformer();

            transform.setOutputProperty(OutputKeys.VERSION, "1.0");
            transform.setOutputProperty(OutputKeys.ENCODING, "us-ascii");
            transform.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, AirlineXmlHelper.SYSTEM_ID);
            transform.setOutputProperty(OutputKeys.INDENT, "yes");
            transform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult file = new StreamResult(xmlFile);
            transform.transform(source, file);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public Element createFlightElement(Document doc, Flight flight) {
        Element newFlight = doc.createElement("flight");
        Element newFlightNum = doc.createElement("number");
        newFlightNum.setTextContent(String.valueOf(flight.getNumber()));
        newFlight.appendChild(newFlightNum);

        Element newFlightSrc = doc.createElement("src");
        newFlightSrc.setTextContent(flight.getSource());
        newFlight.appendChild(newFlightSrc);

        Element newFlightDepart = doc.createElement("depart");
        Element[] newFlightDepartDateTime = createFlightDateTimeElement(doc, flight.getDepartureString());
        newFlightDepart.appendChild(newFlightDepartDateTime[0]);
        newFlightDepart.appendChild(newFlightDepartDateTime[1]);
        newFlight.appendChild(newFlightDepart);

        Element newFlightDest = doc.createElement("dest");
        newFlightDest.setTextContent(flight.getDestination());
        newFlight.appendChild(newFlightDest);

        Element newFlightArrive = doc.createElement("arrive");
        Element[] newFlightArriveDateTime = createFlightDateTimeElement(doc, flight.getArrivalString());
        newFlightArrive.appendChild(newFlightArriveDateTime[0]);
        newFlightArrive.appendChild(newFlightArriveDateTime[1]);
        newFlight.appendChild(newFlightArrive);

        return newFlight;
    }

    public Element[] createFlightDateTimeElement(Document doc, String date) {
        Element[] newFlightDateTime = { doc.createElement("date"), doc.createElement("time") };
        String[] flightDateTime = date.split(" ");
        String[] flightDate = flightDateTime[0].split("/");
        newFlightDateTime[0].setAttribute("day", flightDate[1]);
        newFlightDateTime[0].setAttribute("month", flightDate[0]);
        newFlightDateTime[0].setAttribute("year", flightDate[2]);

        String[] flightTime = flightDateTime[1].split(":");
        String flightHour = flightTime[0];
        if (flightDateTime[2].equalsIgnoreCase("pm")) {
            flightHour = String.valueOf(Integer.parseInt(flightHour) + 12);
        }
        newFlightDateTime[1].setAttribute("hour", flightHour);
        newFlightDateTime[1].setAttribute("minute", flightTime[1]);

        return newFlightDateTime;
    }
}
