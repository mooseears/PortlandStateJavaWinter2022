package edu.pdx.cs410J.jmeziere;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static edu.pdx.cs410J.jmeziere.Project4.checkIfFileAlreadyExists;

/**
 * The main class for Converter
 */
public class Converter {
    /**
     * Main method of Converter
     * @param args
     *        arg[0]: textFile to convert
     *        arg[1]: xmlFile to create
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Invalid command line arguments!\nUsage: textFile xmlFile");
            System.exit(1);
        } else {
            try {
                File textFile = new File(args[0]);
                if ((textFile.length() == 0)) {
                    throw new InvalidArgumentException("File does not exist: " + args[0]);
                }
                checkIfFileAlreadyExists(args[1]);
                File xmlFile = new File(args[1]);

                TextParser parser = new TextParser(new BufferedReader(new FileReader(textFile)));
                XmlDumper dumper = new XmlDumper(xmlFile);
                Airline airline = parser.parse();
                dumper.dump(airline);

            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
    }
}
