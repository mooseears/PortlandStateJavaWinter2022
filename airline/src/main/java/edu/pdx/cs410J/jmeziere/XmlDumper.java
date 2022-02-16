package edu.pdx.cs410J.jmeziere;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.ProjectXmlHelper;

import java.io.Writer;

public class XmlDumper implements AirlineDumper<Airline> {
    private final Writer writer;

    public XmlDumper(Writer writer) {
        this.writer = writer;
    }

    public void dump(Airline airline) {

    }
}
