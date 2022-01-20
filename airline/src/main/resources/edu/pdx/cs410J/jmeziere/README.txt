This is a README file!

Author: Jacob Meziere
Project: Project 1

This program takes command line arguments to create a flight and the airline under which it is flying.

Usage: 

java -jar target/airline-2022.0.0.jar -options airline_name flight_number depart_airport depart_date depart_time dest_airport arrival_date arrival_time

-options: 

	-print
		Print out the flight information after program execution.
	-readme
		Display this readme. This option will not execute the rest of the program.

arguments:

	airline_name
		The name of the airline, entered either as a single word or multiple words in quotes.

	flight_number
		The number of the flight, up to 6-digits long.

	depart_airport
		The 3-letter airport code of the departing airport.

	depart_date
		The date of departure, written as mm/dd/yyyy.

	depart_time
		The time of departure, written as a 24-hour time hh:mm.

	dest_airport
		The 3-letter airport code of the destination airport.

	arrival_date
		The date of arrival written as mm/dd/yyyy.

	depart_time
		The time of arrival, written as a 24-hour time hh:mm.

