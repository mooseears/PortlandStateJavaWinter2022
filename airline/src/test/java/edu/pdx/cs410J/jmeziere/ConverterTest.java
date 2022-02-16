package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class ConverterTest extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Converter} with the given arguments.
     */
    private InvokeMainTestCase.MainMethodResult invokeMain(String... args) {
        return invokeMain( Converter.class, args );
    }

    /**
     * Tests that invoking the main method with no arguments issues an error
     */
    @Test
    void testNoCommandLineArguments() {
        InvokeMainTestCase.MainMethodResult result = invokeMain();
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid command line arguments"));
    }

    @Test
    void testConvertsTextToXmlSuccessfully(@TempDir File tempDir) {
        File xmlFile = new File(tempDir, "xml-test.xml");
        String textPath = "./src/test/resources/edu/pdx/cs410J/jmeziere/valid-airline.txt";
        InvokeMainTestCase.MainMethodResult result = invokeMain(textPath, xmlFile.getPath());

        Airline airline = null;
        try {
            assertThat(xmlFile, notNullValue());

            XmlParser parser = new XmlParser(xmlFile);
            airline = parser.parse();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        ArrayList<Flight> flights = (ArrayList) airline.getFlights();
        Flight f1 = flights.get(0);
        assertThat(airline.getName(), Matchers.equalTo("Test Airline"));
        assertThat(f1.getNumber(), Matchers.equalTo(9420));
        assertThat(f1.getSource(), Matchers.equalTo("PDX"));
        assertThat(f1.getDepartureString(), Matchers.equalTo("01/22/1993 12:35 AM"));
        assertThat(f1.getDestination(), Matchers.equalTo("LAX"));
        assertThat(f1.getArrivalString(), Matchers.equalTo("12/01/1998 02:06 PM"));
    }
}
