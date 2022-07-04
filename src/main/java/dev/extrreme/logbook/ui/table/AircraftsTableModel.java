package dev.extrreme.logbook.ui.table;

import dev.extrreme.logbook.dto.Aircraft;
import dev.extrreme.logbook.dto.Flight;
import dev.extrreme.logbook.utils.DurationUtility;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link DefaultTableModel} implementation with custom methods to directly add and get an {@link Aircraft}
 * and predefined column labels
 */
public class AircraftsTableModel extends DefaultTableModel {
    private static final String[] AIRCRAFT_TABLE_COLUMN_LABELS = {
            "Registration", "Airframe", "Engine"
    };

    public AircraftsTableModel() {
        setColumnIdentifiers(AIRCRAFT_TABLE_COLUMN_LABELS);
    }

    /**
     * Add an aircraft data transfer object to the table
     * @param aircraft the {@link Aircraft aircraft} to add to the table
     */
    public void addAircraft(Aircraft aircraft) {
        String[] row = { aircraft.registration(), aircraft.airframe().getFullName(), aircraft.engine() };
        super.addRow(row);
    }

    /**
     * Get an aircraft from a row in the table
     * @param row the table row number
     * @return the registration of the aircraft corresponding to the provided table row
     */
    public String getAircraft(int row) {
        return (String) getValueAt(row, 0);
    }
}
