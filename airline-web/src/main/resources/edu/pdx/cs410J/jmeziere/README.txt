This is a README file!

Author: Jacob Meziere
Project: Project 5

This program communicates with a web server to store and retrieve flight information. It can be used to add airlines and
their flights, search for certain flights, and retrieve all flights from an airline.

Usage:
java -jar target/airline-client.jar -options -host host_name -port port_number airline_name flight_number depart_airport depart_date depart_time dest_airport arrival_date arrival_time

-options:
	-print
		Print out the flight information after program execution.
	-readme
		Display this readme. This option will not execute the rest of the program.
    -search
        Retrieve flights flying under the specified airline from one airport to the other.
        When this option is set only the airline, source airport, and destination airport must be specified.

arguments:
    host_name
        The name of the web server to connect to.
    port_name
        The port through which to connect.
	airline_name
		The name of the airline, entered either as a single word or multiple words in quotes.
	flight_number
		The number of the flight, up to 6-digits long.
	depart_airport
		The 3-letter airport code of the departing airport.
	depart_date
		The date of departure, written as mm/dd/yyyy.
	depart_time
		The time of departure, written as a 12-hour time hh:mm am/pm.
	dest_airport
		The 3-letter airport code of the destination airport.
	arrival_date
		The date of arrival written as mm/dd/yyyy.
	depart_time
		The time of arrival, written as a 12-hour time hh:mm am/pm.