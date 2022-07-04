package dev.extrreme.logbook.dto;

import dev.extrreme.logbook.utils.StringUtility;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * A record data transfer object used to hold flight data retrieved from the logbook sqlite database
 */
public record Flight(UUID uuid, String flightNumber, String departure, String arrival, long departureTimeMillis,
                     long arrivalTimeMillis, Aircraft aircraft) {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm xxx");

    /**
     * Get the departure date and time of the flight (UTC)
     * @return the departure date and time as an {@link OffsetDateTime}
     */
    public OffsetDateTime getDepartureDate() {
        return epochMillisToODT(departureTimeMillis);
    }

    /**
     * Get the arrival date and time of the flight (UTC)
     * @return the arrival date and time as an {@link OffsetDateTime}
     */
    public OffsetDateTime getArrivalDate() {
        return epochMillisToODT(arrivalTimeMillis);
    }

    /**
     * Get the duration of the flight
     * @return the {@link Duration duration} of the flight
     */
    public Duration getFlightTime() {
        return Duration.ofMillis(arrivalTimeMillis-departureTimeMillis);
    }

    /**
     * Formats a date to UTC using the Flight class internal DateTimeFormatter
     * @param date the {@link OffsetDateTime date} to be formatted
     * @return the date as a string provided by the Flight class {@link DateTimeFormatter}
     */
    public static String formatDate(OffsetDateTime date) {
        return date.format(dtf);
    }

    /**
     * Parse a date out of a string as provided by the {@link #formatDate(OffsetDateTime)} method
     * @param dateStr the date string to be parsed
     * @return the date as an {@link OffsetDateTime} provided by the {@link Flight} class {@link DateTimeFormatter}
     * @throws DateTimeParseException if the date string is not a valid date as per the {@link Flight} class
     * {@link DateTimeFormatter}
     */
    public static OffsetDateTime parseDate(String dateStr) throws DateTimeParseException {
        return OffsetDateTime.parse(dateStr, dtf);
    }

    private static OffsetDateTime epochMillisToODT(long millis) {
        return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC);
    }

    @Override
    public String toString() {
        return StringUtility.concatenate(",", flightNumber, departure + " -> " + arrival,
                formatDate(getDepartureDate()) + "-" + formatDate(getArrivalDate()), aircraft.toString());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (uuid == null ? 0 : uuid.hashCode());
        hash = 31 * hash + (flightNumber == null ? 0 : flightNumber.hashCode());
        hash = 31 * hash + (departure == null ? 0 : departure.hashCode());
        hash = 31 * hash + (arrival == null ? 0 : arrival.hashCode());
        hash = 31 * hash + (aircraft == null ? 0 : aircraft.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;

        Flight other = (Flight) o;

        boolean uuidEquals = (this.uuid == null && other.uuid == null) ||
                (this.uuid != null && this.uuid.equals(other.uuid));
        boolean flightNumberEquals = (this.flightNumber == null && other.flightNumber == null) ||
                (this.flightNumber != null && this.flightNumber.equals(other.flightNumber));
        boolean departureEquals = (this.departure == null && other.departure == null) ||
                (this.departure != null && this.departure.equals(other.departure));
        boolean arrivalEquals = (this.arrival == null && other.arrival == null) ||
                (this.arrival != null && this.arrival.equals(other.arrival));
        boolean departureTimeEquals = this.departureTimeMillis == other.departureTimeMillis;
        boolean arrivalTimeEquals = this.arrivalTimeMillis == other.arrivalTimeMillis;
        boolean aircraftEquals = (this.aircraft == null && other.aircraft == null) ||
                (this.aircraft != null && this.aircraft.equals(other.aircraft));

        return uuidEquals && flightNumberEquals && departureEquals && arrivalEquals && departureTimeEquals &&
                arrivalTimeEquals && aircraftEquals;

    }
}
