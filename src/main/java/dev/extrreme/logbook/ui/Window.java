package dev.extrreme.logbook.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import dev.extrreme.logbook.FlightLogbook;
import dev.extrreme.logbook.manager.AircraftManager;
import dev.extrreme.logbook.manager.FlightManager;
import dev.extrreme.logbook.dto.Aircraft;
import dev.extrreme.logbook.dto.Airframe;
import dev.extrreme.logbook.dto.Flight;
import dev.extrreme.logbook.ui.table.AircraftsTableModel;
import dev.extrreme.logbook.ui.table.FlightsTableModel;
import dev.extrreme.logbook.utils.ImageUtility;
import dev.extrreme.logbook.utils.StringUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.EventObject;
import java.util.UUID;

public class Window extends JFrame {

    // Title to be used as window title
    private static final String TITLE = "My Logbook";

    // File to be used as the image for the application (obtained from /resources/)
    private static final String IMAGE_PATH = "icon.png";

    // Initial window dimensions
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    private JPanel mainPanel;

    // Buttons
    private JButton addAircraftButton, logFlightButton, removeAircraftButton, removeFlightButton;

    // Dropdowns
    private JComboBox<String> airframeSelectionComboBox, aircraftSelectionComboBox;

    // Text Fields
    private JTextField flightNumberTextField, departureTextField, arrivalTextField, departureTimeTextField,
            arrivalTimeTextField,registrationTextField, engineTextField;

    // Tables
    private JTable flightsTable, aircraftTable;

    private String selectedAircraft = null;
    private UUID selectedFlight = null;

    public Window() {
        initUI();
        initButtons();
        start();
    }

    /**
     * Initialize all Swing UI components
     * Method generated using IntelliJ IDEA GUI Designer
     */
    private void initUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        JTabbedPane tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Flights", panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("<HTML><U><B>Flight Logbook:</B></U></HTML>");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        flightsTable = new JTable() {
            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }
        };
        scrollPane1.setViewportView(flightsTable);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("<HTML><U><B>Log Flight:</B></U></HTML>");
        panel4.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Flight Number:");
        panel5.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Departure");
        panel5.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Arrival");
        panel5.add(label5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        arrivalTextField = new JTextField();
        panel5.add(arrivalTextField, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        departureTextField = new JTextField();
        panel5.add(departureTextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        flightNumberTextField = new JTextField();
        panel5.add(flightNumberTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Departure Time:");
        panel6.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Arrival Time:");
        panel6.add(label7, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        arrivalTimeTextField = new JTextField();
        panel6.add(arrivalTimeTextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        departureTimeTextField = new JTextField();
        panel6.add(departureTimeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel6.add(spacer2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Aircraft:");
        panel7.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        aircraftSelectionComboBox = new JComboBox<>();
        panel7.add(aircraftSelectionComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel7.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        logFlightButton = new JButton();
        logFlightButton.setText("Log Flight");
        panel8.add(logFlightButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel8.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel8.add(spacer5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        removeFlightButton = new JButton();
        removeFlightButton.setText("Remove Flight");
        removeFlightButton.setVisible(false);
        panel8.add(removeFlightButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel3.add(spacer6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Aircraft", panel9);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel9.add(panel10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("<HTML><U><B>All Aircraft:</B></U></HTML>");
        panel10.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel10.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        aircraftTable = new JTable() {
            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }
        };
        scrollPane2.setViewportView(aircraftTable);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel9.add(panel11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(2, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel11.add(panel12, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("<HTML><U><B>Add Aircraft:</B></U></HTML>");
        panel12.add(label10, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Airframe:");
        panel12.add(label11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Engine:");
        panel12.add(label12, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Registration:");
        panel12.add(label13, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registrationTextField = new JTextField();
        registrationTextField.setText("");
        panel12.add(registrationTextField, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        engineTextField = new JTextField();
        engineTextField.setText("");
        panel12.add(engineTextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        airframeSelectionComboBox = new JComboBox<>();
        panel12.add(airframeSelectionComboBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel12.add(spacer7, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel11.add(panel13, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addAircraftButton = new JButton();
        addAircraftButton.setText("Add Aircraft");
        panel13.add(addAircraftButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel13.add(spacer8, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel13.add(spacer9, new GridConstraints(0, 3, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel13.add(spacer10, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        removeAircraftButton = new JButton();
        removeAircraftButton.setText("Remove Aircraft");
        removeAircraftButton.setVisible(false);
        panel13.add(removeAircraftButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * Initialize all the buttons with their functions
     */
    private void initButtons() {
        addAircraftButton.addActionListener(e -> {
            String registration = registrationTextField.getText();
            String airframeStr = (String) airframeSelectionComboBox.getSelectedItem();
            String engine = engineTextField.getText();

            if (StringUtility.isEmptyOrNull(registration)) {
                Dialogs.showErrorDialog(Dialogs.INVALID_REGISTRATION);
                return;
            } else if (StringUtility.isEmptyOrNull(airframeStr)) {
                Dialogs.showErrorDialog(Dialogs.INVALID_AIRFRAME);
                return;
            } else if (StringUtility.isEmptyOrNull(engine)) {
                Dialogs.showErrorDialog(Dialogs.INVALID_ENGINE);
                return;
            }

            Airframe airframe;
            try {
                airframe = Airframe.valueOf(airframeStr);
            } catch (IllegalArgumentException ex) {
                Dialogs.showErrorDialog(Dialogs.INVALID_AIRFRAME);
                return;
            }

            final Aircraft aircraft = new Aircraft(registration, airframe, engine);
            AircraftManager.addAircraft(aircraft, res -> {
                if (res) {
                    refreshContent();
                    resetAircraftTextFields();
                    Dialogs.showSuccessDialog(Dialogs.SUCCESS_ADD_AIRCRAFT);
                } else {
                    Dialogs.showErrorDialog(Dialogs.FAILED_ADD_AIRCRAFT);
                }
            });
        });
        removeAircraftButton.addActionListener(e -> {
            if (selectedAircraft == null) {
                Dialogs.showErrorDialog(Dialogs.NO_AIRCRAFT_SELECTED);
                return;
            }

            Dialogs.ConfirmationResponse resp = Dialogs.showConfirmationDialog("Are you sure you would like to delete " +
                    "this aircraft? (" + selectedAircraft + ")");

            if (resp != Dialogs.ConfirmationResponse.YES) {
                return;
            }

            AircraftManager.removeAircraft(selectedAircraft, res -> {
                if (res) {
                    Dialogs.showSuccessDialog("Successfully deleted " + selectedAircraft);
                    refreshContent();
                } else {
                    Dialogs.showErrorDialog("An error occurred trying to delete " + selectedAircraft + " please try again");
                }

            });
        });

        aircraftTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        aircraftTable.getSelectionModel().addListSelectionListener(e -> {
            if (!(aircraftTable.getModel() instanceof AircraftsTableModel model)) {
                return;
            }

            if (aircraftTable.getSelectionModel().isSelectionEmpty()) {
                selectedAircraft = null;
                removeAircraftButton.setVisible(false);
                return;
            }

            selectedAircraft = model.getAircraft(aircraftTable.getSelectedRow());
            removeAircraftButton.setVisible(true);
        });

        logFlightButton.addActionListener(e -> {
            String aircraftStr = (String) aircraftSelectionComboBox.getSelectedItem();

            String flightNumber = flightNumberTextField.getText();

            String departure = departureTextField.getText();
            String arrival = arrivalTextField.getText();

            String depDateStr = departureTimeTextField.getText();
            String arrDateStr = arrivalTimeTextField.getText();

            if (StringUtility.isEmptyOrNull(aircraftStr)) {
                Dialogs.showErrorDialog(Dialogs.INVALID_AIRCRAFT);
                return;
            } else if (StringUtility.isEmptyOrNull(flightNumber)) {
                Dialogs.showErrorDialog(Dialogs.INVALID_FLIGHT_NUMBER);
                return;
            } else if (StringUtility.isEmptyOrNull(departure)) {
                Dialogs.showErrorDialog(Dialogs.INVALID_DEPARTURE);
                return;
            } else if (StringUtility.isEmptyOrNull(arrival)) {
                Dialogs.showErrorDialog(Dialogs.INVALID_ARRIVAL);
                return;
            } else if (StringUtility.isEmptyOrNull(depDateStr)) {
                Dialogs.showErrorDialog(Dialogs.INVALID_DEPARTURE_DATE);
                return;
            } else if (StringUtility.isEmptyOrNull(arrDateStr)) {
                Dialogs.showErrorDialog(Dialogs.INVALID_DEPARTURE);
                return;
            }

            OffsetDateTime depTime;
            try {
                depTime = Flight.parseDate(depDateStr);
            } catch (DateTimeParseException ex) {
                Dialogs.showErrorDialog(Dialogs.INVALID_DEPARTURE_DATE);
                return;
            }
            OffsetDateTime arrTime;
            try {
                arrTime = Flight.parseDate(arrDateStr);
            } catch (DateTimeParseException ex) {
                Dialogs.showErrorDialog(Dialogs.INVALID_ARRIVAL_DATE);
                return;
            }

            String aircraftRegistration = Aircraft.parseRegistration(aircraftStr);

            AircraftManager.getAircraftByRegistration(aircraftRegistration, aircraft -> {
                if (aircraft == null) {
                    Dialogs.showErrorDialog(Dialogs.INVALID_AIRCRAFT);
                    return;
                }

                final Flight flight = new Flight(UUID.randomUUID(), flightNumber, departure, arrival,
                        depTime.toInstant().toEpochMilli(), arrTime.toInstant().toEpochMilli(), aircraft);
                FlightManager.addFlight(flight, res -> {
                    if (res) {
                        refreshContent();
                        resetFlightTextFields();
                        Dialogs.showSuccessDialog(Dialogs.SUCCESS_FLIGHT);
                    } else {
                        Dialogs.showErrorDialog(Dialogs.FAILED_FLIGHT);
                    }
                });
            });
        });
        removeFlightButton.addActionListener(e -> {
            if (selectedFlight == null) {
                Dialogs.showErrorDialog(Dialogs.NO_FLIGHT_SELECTED);
                return;
            }

            Dialogs.ConfirmationResponse resp = Dialogs.showConfirmationDialog("Are you sure you would like to delete " +
                    "the selected flight log?");

            if (resp != Dialogs.ConfirmationResponse.YES) {
                return;
            }

            FlightManager.removeFlight(selectedFlight, res -> {
                if (res) {
                    Dialogs.showSuccessDialog("Successfully deleted the selected flight log");
                    refreshContent();
                } else {
                    Dialogs.showErrorDialog("An error occurred trying to delete the selected flight log please try again");
                }
            });
        });

        flightsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!(flightsTable.getModel() instanceof FlightsTableModel model)) {
                return;
            }

            if (flightsTable.getSelectionModel().isSelectionEmpty()) {
                selectedFlight = null;
                removeFlightButton.setVisible(false);
                return;
            }

            selectedFlight = model.getFlight(flightsTable.getSelectedRow());
            removeFlightButton.setVisible(true);
        });

    }

    /**
     * Initialize and input all the content from the database into the tables
     */
    private void initContent() {
        airframeSelectionComboBox.removeAllItems();
        for (Airframe airframe : Airframe.values()) {
            airframeSelectionComboBox.addItem(airframe.name());
        }

        aircraftSelectionComboBox.removeAllItems();
        AircraftManager.getAllAircraft(aircrafts -> {
            AircraftsTableModel model = new AircraftsTableModel();

            aircrafts.forEach(aircraft -> {
                aircraftSelectionComboBox.addItem(aircraft.toString());
                model.addAircraft(aircraft);
            });

            aircraftTable.setModel(model);
        });

        FlightManager.getLoggedFlights(flights -> {
            FlightsTableModel model = new FlightsTableModel();

            Collections.reverse(flights); // Reverse so we can see most recently logged flights at top of table
            flights.forEach(model::addFlight);

            flightsTable.setModel(model);
        });
    }

    /**
     * Refresh the content in the tables with all content from the database
     */
    public void refreshContent() {
        initContent();
        selectedFlight = null;
        removeFlightButton.setVisible(false);
        selectedAircraft = null;
        removeAircraftButton.setVisible(false);
    }

    /**
     * Initialize frame and display it to user
     */
    private void start() {
        setTitle(TITLE);

        BufferedImage icon = ImageUtility.getImage(IMAGE_PATH);
        if (icon != null) {
            setIconImage(icon);
        }

        setSize(WIDTH, HEIGHT);

        initContent();
        setContentPane(mainPanel);

        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Window.this.dispose();
                FlightLogbook.close();
            }
        });

        setVisible(true);
    }

    /**
     * Reset all input text fields used to log a flight
     */
    private void resetFlightTextFields() {
        flightNumberTextField.setText("");
        departureTextField.setText("");
        arrivalTextField.setText("");
        departureTimeTextField.setText("");
        arrivalTimeTextField.setText("");
    }

    /**
     * Reset all input text fields used to add an aircraft
     */
    private void resetAircraftTextFields() {
        engineTextField.setText("");
        registrationTextField.setText("");
    }
}
