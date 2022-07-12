package dev.extrreme.logbook.ui.table;

import dev.extrreme.logbook.dto.Aircraft;
import dev.extrreme.logbook.dto.Flight;
import dev.extrreme.logbook.utils.DurationUtility;

import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * A simple {@link DefaultTableModel} implementation with custom methods to directly add and get a {@link Flight}
 * and predefined column labels
 */
public class FlightsTableModel extends DefaultTableModel {
    private static final String[] FLIGHTS_TABLE_COLUMN_LABELS = {
            "Flight Number", "Departure", "Arrival", "Departure Time", "Arrival Time", "Flight Time", "Aircraft"
    };

    private final Map<Integer, UUID> flightCache = new HashMap<>();

    public FlightsTableModel() {
        setColumnIdentifiers(FLIGHTS_TABLE_COLUMN_LABELS);
    }

    public FlightsTableModel(List<Flight> flights) {
        setColumnIdentifiers(FLIGHTS_TABLE_COLUMN_LABELS);
        flights.forEach(this::addFlight);
    }

    /**
     * Add a logged flight data transfer object to the table
     * @param flight the {@link Flight logged flight} to add to the table
     */
    public void addFlight(Flight flight) {
        String[] row = {
                flight.flightNumber(), flight.departure(), flight.arrival(),
                Flight.formatDate(flight.getDepartureDate()), Flight.formatDate(flight.getArrivalDate()),
                DurationUtility.toString(flight.getFlightTime()), flight.aircraft().toString()
        };

        super.addRow(row);
        flightCache.put(getRowCount()-1, flight.uuid());
    }

    /**
     * Get a logged flight from a row in the table
     * @param row the table row number
     * @return the {@link UUID uuid} of the flight corresponding to the provided table row
     */
    public UUID getFlight(int row) {
        return flightCache.get(row);
    }
}
