package edu.pdx.cs410J.jmeziere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import edu.pdx.cs410J.AirportNames;

public class MainActivity extends AppCompatActivity {
    static final String ERR_INVALID_AIRLINE = "Airline name required.";
    static final String ERR_INVALID_DEPART_DATETIME = "Departure date or time not set.";
    static final String ERR_INVALID_ARRIVE_DATETIME = "Arrival date or time not set.";
    static final String ERR_INVALID_NUMBER = "Flight number required.";
    static final String ERR_TIME_PARADOX = "Flight arrival time must come after flight departure.";
    static final String ERR_LOAD_FILE = "Error: Couldn't load airlines from file.";

    Vector<Airline> airlines;
    EditText airlineEntry;
    EditText flightNumberEntry;

    Calendar departDateTime = Calendar.getInstance();
    Calendar arriveDateTime = Calendar.getInstance();
    Spinner departAirportSpinner, arriveAirportSpinner;
    Button departDateButton, arriveDateButton, departTimeButton, arriveTimeButton;
    Button createButton, searchButton, printButton, readmeButton;

    SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            airlines = readAirlines();
        } catch (Exception ex) {
            showError(ERR_LOAD_FILE);
        }
        airlineEntry = findViewById(R.id.airlineNameEntry);
        flightNumberEntry = findViewById(R.id.flightNumberEntry);
        departDateButton = findViewById(R.id.departDateButton);
        arriveDateButton = findViewById(R.id.arriveDateButton);
        departTimeButton = findViewById(R.id.departTimeButton);
        arriveTimeButton = findViewById(R.id.arriveTimeButton);
        createButton = findViewById(R.id.createButton);
        searchButton = findViewById(R.id.searchButton);
        printButton = findViewById(R.id.printButton);
        readmeButton = findViewById(R.id.readmeButton);

        departAirportSpinner = findViewById(R.id.departAirportEntry);
        arriveAirportSpinner = findViewById(R.id.arriveAirportEntry);
        ArrayList<String> airportCodes = new ArrayList<String>(AirportNames.getNamesMap().keySet());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, airportCodes);
        departAirportSpinner.setAdapter(arrayAdapter);
        arriveAirportSpinner.setAdapter(arrayAdapter);
    }

    public void showDepartDatePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                departDateTime.set(Calendar.YEAR, year);
                departDateTime.set(Calendar.MONTH, month);
                departDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                departDateButton.setText(sdfDate.format(departDateTime.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, departDateTime.get(Calendar.YEAR), departDateTime.get(Calendar.MONTH), departDateTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Departure Date");
        datePickerDialog.getDatePicker().setMaxDate(arriveDateTime.getTime().getTime());
        datePickerDialog.show();
    }

    public void showArriveDatePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                arriveDateTime.set(Calendar.YEAR, year);
                arriveDateTime.set(Calendar.MONTH, month);
                arriveDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                arriveDateButton.setText(sdfDate.format(arriveDateTime.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, arriveDateTime.get(Calendar.YEAR), arriveDateTime.get(Calendar.MONTH), arriveDateTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Arrival Date");
        datePickerDialog.getDatePicker().setMinDate(departDateTime.getTime().getTime());
        datePickerDialog.show();
    }

    public void showDepartTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                departDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                departDateTime.set(Calendar.MINUTE, selectedMinute);
                departTimeButton.setText(sdfTime.format(departDateTime.getTime()));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, departDateTime.get(Calendar.HOUR_OF_DAY), departDateTime.get(Calendar.MINUTE), false);
        timePickerDialog.setTitle("Departure Time");
        timePickerDialog.show();
    }

    public void showArriveTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                arriveDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                arriveDateTime.set(Calendar.MINUTE, selectedMinute);
                arriveTimeButton.setText(sdfTime.format(arriveDateTime.getTime()));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, arriveDateTime.get(Calendar.HOUR_OF_DAY), arriveDateTime.get(Calendar.MINUTE), false);
        timePickerDialog.setTitle("Arrival Time");
        timePickerDialog.show();
    }

    private Airline retrieveAirline(String name) {
        Airline airline = null;
        if (name.isEmpty()) {
            showError(ERR_INVALID_AIRLINE);
        } else {
            for (Airline item : airlines) {
                if (item.getName().equalsIgnoreCase(name)) {
                    airline = item;
                    break;
                }
            }
            if (airline == null)
                airline = new Airline(name);
            airlines.add(airline);
        }
        return airline;
    }

    public void createFlight(View view) {
        String airlineName = airlineEntry.getText().toString().trim();
        Airline airline = retrieveAirline(airlineName);
        if (airline == null)
            return;

        int flightNum;
        if (flightNumberEntry.getText().toString().trim().equals("")) {
            showError(ERR_INVALID_NUMBER);
            return;
        } else {
            flightNum = Integer.parseInt(flightNumberEntry.getText().toString().trim());
        }
        String flightSrc = departAirportSpinner.getSelectedItem().toString();
        Date flightDepart = null;
        if (departDateButton.getText().toString().contains("SET") || departTimeButton.getText().toString().contains("SET")) {
            showError(ERR_INVALID_DEPART_DATETIME);
            return;
        } else {
            flightDepart = new Date(String.valueOf(departDateTime.getTime()));
        }

        String flightDest = arriveAirportSpinner.getSelectedItem().toString();
        Date flightArrive = null;
        if (arriveDateButton.getText().toString().contains("SET") || arriveTimeButton.getText().toString().contains("SET")) {
            showError(ERR_INVALID_ARRIVE_DATETIME);
            return;
        } else if (arriveDateTime.compareTo(departDateTime) < 0) {
            showError(ERR_TIME_PARADOX);
            return;
        } else {
            flightArrive = new Date(String.valueOf(arriveDateTime.getTime()));
        }

        airline.addFlight(new Flight(flightNum, flightSrc, flightDepart, flightDest, flightArrive));
        try {
            //writeAirline(airline);
            String success = "Flight #" + flightNum + " " + flightSrc + "->" + flightDest + " added to " + airlineName + ".";
            Toast.makeText(this, success, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            showError(ERR_LOAD_FILE);
        }
    }

    public void searchFlights(View view) {
        String airlineName = airlineEntry.getText().toString().trim();
        Airline airline = retrieveAirline(airlineName);
        if (airline == null)
            return;
        String flightSrc = departAirportSpinner.getSelectedItem().toString();
        String flightDest = arriveAirportSpinner.getSelectedItem().toString();

        Collection<Flight> matchedFlights = new Vector<Flight>();
        for (Flight f : airline.getFlights()) {
            if (f.getSource().equals(flightSrc) && f.getDestination().equals(flightDest)) {
                matchedFlights.add(f);
            }
        }
        if (matchedFlights.isEmpty()) {
            showError("No flights found for airline " + airlineName + " flying " + flightSrc + "->" + flightDest);
        } else {
            showFlights(airlineName, matchedFlights);
        }
    }

    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void showFlights(String airlineName, Collection<Flight> flights) {
        String message = "Airline: " + airlineName + "\n\n";
        for (Flight f : flights) {
            String prettyFlight = "  -Flight #" + f.getNumber() +
                    ":\n      Departing:  " + AirportNames.getName(f.getSource()) + "\n        " + f.getDepartureString() +
                    "\n      Arriving:      " + AirportNames.getName(f.getDestination()) + "\n        " + f.getArrivalString() + "\n\n";
            message += prettyFlight;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Flights Found");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.create().show();
    }

    public void printFlights(View view) {
        String airlineName = airlineEntry.getText().toString().trim();
        Airline airline = retrieveAirline(airlineName);
        if (airline == null)
            return;
        if (airline.getFlights().isEmpty()) {
            showError("No flights found for airline " + airlineName);
            return;
        } else {
            showFlights(airlineName, airline.getFlights());
        }
    }

    public void showReadme(View view) {
        String readme = "This app allows you to create airlines and their respective flights.\n\n" +
                "To create a flight: All airline and flight info must be filled out.\n\n" +
                "To search for flights: Only the airline name, departure airport, and arrival airport need to be entered.\n\n" +
                "To display all flights for a given airline: Only the airline name is required.";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(readme).setTitle("Readme");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.create().show();
    }

    private void writeAirline(Airline airline) throws IOException {
        String filepath = this.getFilesDir().getPath() + "/airlines/";
        File directory = new File(filepath);
        File file = new File(directory, airline.getName() + ".xml");
        file.createNewFile();
        this.openFileInput(file.getPath());
        XmlDumper dumper = new XmlDumper(file);
        dumper.dump(airline);
    }

    private Vector<Airline> readAirlines() throws IOException {
        Vector<Airline> airlines = new Vector<Airline>();
        String filepath = this.getFilesDir().getPath() + "/airlines/";
        File directory = new File(filepath);
        showError(directory.getPath());
        if (directory.exists()) {
            showError("Found directory");
            for (File f : directory.listFiles()) {
                XmlParser parser = new XmlParser(f);
                try {
                    airlines.add(parser.parse());
                } catch (Exception ex) {
                    showError(ERR_LOAD_FILE);
                }
            }
            return airlines;
        } else {
            directory.mkdir();
            return new Vector<Airline>();
        }

    }
}