package dev.extrreme.logbook.dto;

import dev.extrreme.logbook.utils.StringUtility;

/**
 * A record data transfer object used to hold aircraft data retrieved from the logbook sqlite database
 */
public record Aircraft(String registration, Airframe airframe, String engine) {

    /**
     * Parse the registration id of an aircraft from a string of the form returned by {@link #toString()}
     * @param aircraftString the aircraft string as provided by the {@link #toString()} method
     * @return the registration string (e.g., "C-FXCD")
     * @throws IllegalArgumentException if an invalid aircraft string was provided
     */
    public static String parseRegistration(String aircraftString) throws IllegalArgumentException {
        try {
            return aircraftString.split("\\(")[1].replace(")", "").trim();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid aircraft string provided, ensure it is of the form returned" +
                    "by the toString() method.");
        }
    }

    @Override
    public String toString() {
        return StringUtility.concatenate(" ", airframe.getFullName(), engine, "(" + registration + ")");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (registration == null ? 0 : registration.hashCode());
        hash = 31 * hash + (airframe == null ? 0 : airframe.hashCode());
        hash = 31 * hash + (engine == null ? 0 : engine.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;

        Aircraft other = (Aircraft) o;

        boolean registrationEquals = (this.registration == null && other.registration == null) ||
                (this.registration != null && this.registration.equals(other.registration));
        boolean airframeEquals = (this.airframe == null && other.airframe == null) ||
                (this.airframe != null && this.airframe.equals(other.airframe));
        boolean engineEquals = (this.engine == null && other.engine == null) ||
                (this.engine != null && this.engine.equals(other.engine));

        return registrationEquals && airframeEquals && engineEquals;

    }
}
