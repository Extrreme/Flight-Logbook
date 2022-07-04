package dev.extrreme.logbook.ui;

import javax.swing.*;

public class Dialogs {

    // Flight
    public static final String INVALID_AIRCRAFT = "Invalid Input; An invalid or no aircraft is selected";
    public static final String INVALID_FLIGHT_NUMBER = "Invalid Input; An invalid flight number was provided";
    public static final String INVALID_DEPARTURE = "Invalid Input; An invalid departure ICAO code was provided";
    public static final String INVALID_ARRIVAL = "Invalid Input; An invalid arrival ICAO code was provided";
    public static final String INVALID_DEPARTURE_DATE = "Invalid Input; An invalid departure date was provided";
    public static final String INVALID_ARRIVAL_DATE = "Invalid Input; An invalid arrival date was provided";

    public static final String SUCCESS_FLIGHT = "Successfully logged flight";
    public static final String FAILED_FLIGHT = "Failed to log flight";

    public static final String NO_FLIGHT_SELECTED = "You do not have a logged flight selected";

    // Aircraft
    public static final String INVALID_REGISTRATION = "Invalid Input; An invalid aircraft registration was provided";
    public static final String INVALID_AIRFRAME = "Invalid Input; An invalid airframe was provided";
    public static final String INVALID_ENGINE = "Invalid Input; An invalid engine was provided";

    public static final String SUCCESS_ADD_AIRCRAFT = "Successfully added aircraft";
    public static final String FAILED_ADD_AIRCRAFT = "Failed to add aircraft";

    public static final String NO_AIRCRAFT_SELECTED = "You do not have an aircraft selected";

    /**
     * An enum class that serves as a wrapper for the possible integer return values of
     * {@link JOptionPane#showConfirmDialog}, using optionType {@link JOptionPane#YES_NO_OPTION}
     */
    enum ConfirmationResponse {
        YES(0),
        NO(1),
        UNKNOWN(Integer.MAX_VALUE);

        private final int responseNum;

        ConfirmationResponse(int responseNum) {
            this.responseNum = responseNum;
        }

        /**
         * Get the number value of the ConfirmationResponse as returned by {@link JOptionPane#showConfirmDialog}
         * @return The response number of the confirmation dialog response as an integer
         */
        public int getResponseNumber() {
            return responseNum;
        }

        /**
         * Get a ConfirmationResponse by the response number
         * @param responseNum The response number of the confirmation dialog response
         * @return The ConfirmationResponse associated with the provided response number, returns
         * {@link ConfirmationResponse#UNKNOWN} if an invalid response number is provided
         */
        public static ConfirmationResponse getByResponseNumber(int responseNum) {
            if (responseNum == 0) {
                return ConfirmationResponse.YES;
            } else if (responseNum == 1) {
                return ConfirmationResponse.NO;
            } else {
                return ConfirmationResponse.UNKNOWN;
            }
        }
    }

    /**
     * Shows an error dialog box with the specified message
     * @param message the message to be shown in the dialog box
     */
    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a success dialog box with the specified message
     * @param message the message to be shown in the dialog box
     */
    public static void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a confirmation dialog box with the specified message
     * @param message the message to be shown in the dialog box
     * @return the {@link ConfirmationResponse} of the confirmation dialog, used to determine whether YES or NO was
     * selected
     */
    public static ConfirmationResponse showConfirmationDialog(String message) {
        int responseNum = JOptionPane.showConfirmDialog(null, message, "Confirmation", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return ConfirmationResponse.getByResponseNumber(responseNum);
    }
}
