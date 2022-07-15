package dev.extrreme.logbook.manager;

import dev.extrreme.logbook.FlightLogbook;
import dev.extrreme.logbook.dto.Aircraft;
import dev.extrreme.logbook.dto.Airframe;
import dev.extrreme.logbook.dto.Flight;
import dev.extrreme.logbook.scheduling.Scheduler;
import dev.extrreme.logbook.sql.SQLManager;
import dev.extrreme.logbook.utils.DurationUtility;
import dev.extrreme.logbook.utils.SQLUtility;
import dev.extrreme.logbook.utils.executable.Executable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;

public class AircraftManager {
    private static final String AIRCRAFT_TABLE = "aircraft";
    private static final String[] AIRCRAFT_TABLE_COLUMNS = new String[] {
            "registration", "airframe", "engine"};
    private static final String[] AIRCRAFT_TABLE_COLUMNTYPES = new String[] {
            "TEXT NOT NULL UNIQUE", "TEXT", "TEXT"};

    /**
     * Initializes the AircraftManager, creating necessary SQL tables
     * @return TRUE if the aircraft SQL table was successfully created in the database, FALSE otherwise
     */
    public static boolean init() {
        return getSQLManager().createTable(AIRCRAFT_TABLE, AIRCRAFT_TABLE_COLUMNS, AIRCRAFT_TABLE_COLUMNTYPES,
                "PRIMARY KEY (`registration`)");
    }

    /**
     * Get a list of all aircraft from the logbook sqlite database, will block thread it is called from until sql query
     * completion
     * @return The list of {@link Aircraft} data transfer objects representing found aircraft data from the database
     */
    @NotNull
    public static List<Aircraft> getAllAircraftBlocking() {
        List<Aircraft> aircraft = new ArrayList<>();

        List<Map<Object, Object>> rows = getSQLManager().getAllRowsInTable(AIRCRAFT_TABLE, AIRCRAFT_TABLE_COLUMNS);

        if (rows == null || rows.isEmpty()) {
            return aircraft;
        }

        rows.forEach(row -> {
            String registration = (String) row.get(AIRCRAFT_TABLE_COLUMNS[0]);
            String airframe = (String) row.get(AIRCRAFT_TABLE_COLUMNS[1]);
            String engine = (String) row.get(AIRCRAFT_TABLE_COLUMNS[2]);

            aircraft.add(new Aircraft(registration, Airframe.valueOf(airframe), engine));
        });

        return aircraft;
    }

    /**
     * Get a list of all aircraft from the logbook sqlite database, will run in a separate, asynchronous thread
     * @param callback The {@link Executable} to be executed with the retrieved list of {@link Aircraft} data transfer
     * objects from the database
     */
    public static void getAllAircraft(Executable<List<Aircraft>> callback) {
        Scheduler.getInstance().runTaskAsynchronously(() ->
                callback.execute(getAllAircraftBlocking()));
    }

    /**
     * Get an aircraft from the logbook sqlite database, will block thread it is called from until sql query completion
     * @param registration The registration of the aircraft to find in database, as a string
     * @return The {@link Aircraft} data transfer object representing the aircraft data in the database
     */
    @Nullable
    public static Aircraft getAircraftByRegistrationBlocking(String registration) {
        if (registration == null) {
            return null;
        }
        
        List<Map<Object, Object>> rows = getSQLManager().getRowsInTable(AIRCRAFT_TABLE, AIRCRAFT_TABLE_COLUMNS[0],
                registration, AIRCRAFT_TABLE_COLUMNS);

        if (rows == null || rows.isEmpty()) {
            return null;
        }

        Map<Object, Object> row = rows.get(0);

        String airframe = (String) row.get(AIRCRAFT_TABLE_COLUMNS[1]);
        String engine = (String) row.get(AIRCRAFT_TABLE_COLUMNS[2]);

        return new Aircraft(registration, Airframe.valueOf(airframe), engine);
    }

    /**
     * Get an aircraft from the logbook sqlite database, will run in a separate, asynchronous thread
     * @param registration The registration of the aircraft to find in the database, as a string
     * @param callback The {@link Executable} to be executed with the retrieved {@link Aircraft} data transfer object
     * from the database
     */
    public static void getAircraftByRegistration(String registration, Executable<Aircraft> callback) {
        Scheduler.getInstance().runTaskAsynchronously(() -> 
                callback.execute(getAircraftByRegistrationBlocking(registration)));
    }

    /**
     * Add an aircraft to the logbook sqlite database, will block thread it is called from until sql query completion
     * @param aircraft The {@link Aircraft} data transfer object containing all necessary data to store in the database
     * @return TRUE if the aircraft was successfully inserted into the database, FALSE if an issue occurred adding the aircraft
     */
    public static boolean addAircraftBlocking(Aircraft aircraft) {
        Map<String, Object> vals = new HashMap<>();

        vals.put(AIRCRAFT_TABLE_COLUMNS[0], aircraft.registration());
        vals.put(AIRCRAFT_TABLE_COLUMNS[1], aircraft.airframe().name());
        vals.put(AIRCRAFT_TABLE_COLUMNS[2], aircraft.engine());

        return getSQLManager().setRowInTable(AIRCRAFT_TABLE, AIRCRAFT_TABLE_COLUMNS[0],
                aircraft.registration(), vals);
    }

    /**
     * Add an aircraft to the logbook sqlite database, will run in a separate, asynchronous thread
     * @param aircraft The {@link Aircraft} data transfer object containing all necessary data to store in the database
     * @param callback The {@link Executable} to be executed with the boolean response of whether the aircraft was
     * successfully inserted into the database, see return options of {@link #addAircraftBlocking(Aircraft)}
     */
    public static void addAircraft(Aircraft aircraft, Executable<Boolean> callback) {
        Scheduler.getInstance().runTaskAsynchronously(() -> 
                callback.execute(addAircraftBlocking(aircraft)));
    }

    /**
     * Remove an aircraft from the logbook sqlite database, will block thread it is called from until sql query
     * completion
     * @param registration The registration of the aircraft to find and delete in the database, as a string
     * @return TRUE if the aircraft was successfully removed from the database, FALSE if an issue occurred removing the
     * aircraft
     */
    public static boolean removeAircraftBlocking(String registration) {
        return getSQLManager().deleteRowInTable(AIRCRAFT_TABLE, AIRCRAFT_TABLE_COLUMNS[0], registration);
    }

    /**
     * Remove an aircraft from the logbook sqlite database, will run in a separate, asynchronous thread
     * @param registration The registration of the aircraft to find and delete in the database, as a string
     * @param callback The {@link Executable} to be executed with the boolean response of whether the aircraft was
     * successfully removed from the database, see return options of {@link #removeAircraftBlocking(String)
     */
    public static void removeAircraft(String registration, Executable<Boolean> callback) {
        Scheduler.getInstance().runTaskAsynchronously(() -> 
                callback.execute(removeAircraftBlocking(registration)));
    }

    /**
     * Get the total logged flight time of an aircraft from the logbook sqlite database, will block thread it is called
     * from until sql query completion
     * @param registration The registration of the aircraft to find in the database and get the flight ime of, as a
     * string
     * @return the flight time of the found aircraft, returns {@link Duration#ZERO} if the aircraft is not valid
     */
    @NotNull
    public static Duration getFlightTimeBlocking(String registration) {
        List<Duration> durations = FlightManager.getLoggedFlightsBlocking().stream()
                .filter(flight -> flight.aircraft().registration().equals(registration))
                .map(Flight::getFlightTime)
                .toList();

        return DurationUtility.sum(durations);
    }

    /**
     * Get the total logged flight time of an aircraft from the logbook sqlite database, will run in a separate,
     * asynchronous thread
     * @param registration The registration of the aircraft to find in the database and get the flight ime of, as a
     * string
     * @param callback The {@link Executable} to be executed with the flight time of the found aircraft as a
     * {@link Duration}, see return options of {@link #getFlightTimeBlocking(String)}
     */
    public static void getFlightTime(String registration, Executable<Duration> callback) {
        Scheduler.getInstance().runTaskAsynchronously(() ->
                callback.execute(getFlightTimeBlocking(registration)));
    }

    /**
     * Get the most used aircraft (based on flight time) from the logbook sqlite database, will block thread it is
     * called from until sql query completion
     * @return The {@link Aircraft} data transfer object representing the found aircraft data in the database
     */
    @Nullable
    public static Aircraft getMostUsedAircraftBlocking() {
        TreeMap<Duration, Aircraft> flightTimes = new TreeMap<>();

        getAllAircraftBlocking().forEach(aircraft ->
                flightTimes.put(getFlightTimeBlocking(aircraft.registration()), aircraft));

        return flightTimes.isEmpty() ? null : flightTimes.lastEntry().getValue();

    }

    /**
     * Get the most used aircraft (based on flight time) from the logbook sqlite database, will block thread it is
     * called from until sql query completion
     * @param callback The {@link Executable} to be executed with the found aircraft, see return options of
     * {@link #getMostUsedAircraftBlocking()}
     */
    public static void getMostUsedAircraft(Executable<Aircraft> callback) {
        Scheduler.getInstance().runTaskAsynchronously(() ->
                callback.execute(getMostUsedAircraftBlocking()));
    }

    /**
     * Export the logbook sqlite database aircraft table to a .csv file
     */
    public static void export() {
        SQLUtility.writeTableToCSV(FlightLogbook.getSQL(), AIRCRAFT_TABLE);
    }

    private static SQLManager getSQLManager() {
        return FlightLogbook.getSQL().getManager();
    }
}
