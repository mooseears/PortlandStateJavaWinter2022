package edu.pdx.cs410J.jmeziere;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class XmlDumperTest {

    @Test
    void xmlDumperDumpsAirline(@TempDir File tempDir) {
        Airline originalAirline = null;
        Airline duplicateAirline = null;

        try {
            URL resource = Project5.class.getResource("valid-airline.xml");
            File xmlReadFile = new File(Paths.get(resource.toURI().getPath()).toString());
            assertThat(xmlReadFile, notNullValue());
            XmlParser parser = new XmlParser(xmlReadFile);
            originalAirline = parser.parse();

            File xmlDumpFile = new File(tempDir,"xml-test.xml");
            XmlDumper dumper = new XmlDumper(xmlDumpFile);
            dumper.dump(originalAirline);

            XmlParser parser2 = new XmlParser(xmlDumpFile);
            duplicateAirline = parser2.parse();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        ArrayList<Flight> flights = (ArrayList) duplicateAirline.getFlights();
        Flight f1 = flights.get(0);
        Flight f2 = flights.get(1);
        assertThat(duplicateAirline.getName(), equalTo("Valid Airlines"));
        assertThat(f1.getNumber(), equalTo(1437));
        assertThat(f1.getSource(), equalTo("BJX"));
        assertThat(f1.getDepartureString(), equalTo("09/25/2020 05:00 PM"));
        assertThat(f1.getDestination(), equalTo("CMN"));
        assertThat(f1.getArrivalString(), equalTo("09/26/2020 03:56 AM"));
        assertThat(f2.getNumber(), equalTo(7865));
        assertThat(f2.getSource(), equalTo("JNB"));
        assertThat(f2.getDepartureString(), equalTo("05/15/2020 07:24 AM"));
        assertThat(f2.getDestination(), equalTo("XIY"));
        assertThat(f2.getArrivalString(), equalTo("05/16/2020 09:07 AM"));
    }
}
