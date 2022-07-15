package dev.extrreme.logbook.manager;

import dev.extrreme.logbook.FlightLogbook;
import dev.extrreme.logbook.dto.Aircraft;
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

public class FlightManager {
    public static final String FLIGHTS_TABLE = "flights";
    public static final String[] FLIGHTS_TABLE_COLUMNS = new String[] {
            "uuid", "flightnumber", "dep", "arr", "departuretime", "arrivaltime", "aircraft"};
    public static final String[] FLIGHTS_TABLE_COLUMNTYPES = new String[] {
            "TEXT UNIQUE NOT NULL", "TEXT", "TEXT", "TEXT", "INTEGER", "INTEGER", "TEXT"};

    /**
     * Initializes the FlightManager, creating necessary SQL tables
     * @return TRUE if the flights SQL table was successfully created in the database, FALSE otherwise
     */
    public static boolean init() {
        return getSQLManager().createTable(FLIGHTS_TABLE, FLIGHTS_TABLE_COLUMNS, FLIGHTS_TABLE_COLUMNTYPES,
                "PRIMARY KEY (`uuid`)");
    }

    /**
     * Get a list of all flights from the logbook sqlite database, will block thread it is called from until sql query
     * completion
     * @return the list of {@link Flight} data transfer objects representing found flights from the database
     */
    @NotNull
    public static List<Flight> getLoggedFlightsBlocking() {
        List<Flight> flights = new ArrayList<>();

        List<Map<Object, Object>> rows = getSQLManager().getAllRowsInTable(FLIGHTS_TABLE, FLIGHTS_TABLE_COLUMNS);

        if (rows == null || rows.isEmpty()) {
            return flights;
        }

        rows.forEach(row -> {
            UUID uuid;
            try {
                uuid = UUID.fromString((String) row.get(FLIGHTS_TABLE_COLUMNS[0]));
            } catch (IllegalArgumentException e) {
                return;
            }
            String flightNumber = (String) row.get(FLIGHTS_TABLE_COLUMNS[1]);
            String departure = (String) row.get(FLIGHTS_TABLE_COLUMNS[2]);
            String arrival = (String) row.get(FLIGHTS_TABLE_COLUMNS[3]);
            long departureTime = (long) row.get(FLIGHTS_TABLE_COLUMNS[4]);
            long arrivalTime = (long) row.get(FLIGHTS_TABLE_COLUMNS[5]);

            Aircraft aircraft = AircraftManager.getAircraftByRegistrationBlocking((String) row.get(FLIGHTS_TABLE_COLUMNS[6]));

            flights.add(new Flight(uuid, flightNumber, departure, arrival, departureTime, arrivalTime, aircraft));
        });

        return flights;
    }

    /**
     * Get a list of all flights from the logbook sqlite database, will run in a separate, asynchronous thread
     * @param callback the {@link Executable} to be executed with the retrieved list of {@link Flight} data transfer objects
     * from the database
     */
    public static void getLoggedFlights(Executable<List<Flight>> callback) {
        runAsynchronously(() -> {
            List<Flight> flights = getLoggedFlightsBlocking();
            callback.execute(flights);
        });
    }

    /**
     * Get a list of all flights from the logbook sqlite database with the specified flight number, will block thread
     * it is called from until sql query completion
     * @param flightNumber the flight number of the flights to find in database, as a string
     * @return the list of {@link Flight} data transfer objects representing found flights from the database
     */
    @NotNull
    public static List<Flight> getLoggedFlightsBlocking(String flightNumber) {
        List<Flight> flights = new ArrayList<>();

        List<Map<Object, Object>> rows = getSQLManager().getRowsInTable(FLIGHTS_TABLE, FLIGHTS_TABLE_COLUMNS[1],
                flightNumber, FLIGHTS_TABLE_COLUMNS);

        if (rows == null) {
            return flights;
        }

        for (Map<Object, Object> row : rows) {
            UUID uuid;
            try {
                uuid = UUID.fromString((String) row.get(FLIGHTS_TABLE_COLUMNS[0]));
            } catch (IllegalArgumentException e) {
                continue;
            }
            String departure = (String) row.get(FLIGHTS_TABLE_COLUMNS[2]);
            String arrival = (String) row.get(FLIGHTS_TABLE_COLUMNS[3]);
            long departureTime = (long) row.get(FLIGHTS_TABLE_COLUMNS[4]);
            long arrivalTime = (long) row.get(FLIGHTS_TABLE_COLUMNS[5]);
            String aircraftId = (String) row.get(FLIGHTS_TABLE_COLUMNS[6]);

            Aircraft aircraft = AircraftManager.getAircraftByRegistrationBlocking(aircraftId);

            flights.add(new Flight(uuid, flightNumber, departure, arrival, departureTime, arrivalTime, aircraft));
        }

        return flights;
    }

    /**
     * Get a list of all logged flights from the logbook sqlite database with the specified flight number, will run in a
     * separate, asynchronous thread
     * @param flightNumber the flight number of the logged flights to find in the database, as a string
     * @param callback the {@link Executable} to be executed with the retrieved list of {@link Flight} data transfer
     * objects from the database
     */
    public static void getLoggedFlights(String flightNumber, Executable<List<Flight>> callback) {
        runAsynchronously(() -> {
            List<Flight> flights = getLoggedFlightsBlocking(flightNumber);
            callback.execute(flights);
        });
    }

    /**
     * Get a logged flight from the logbook sqlite database, will block thread it is called from until sql query
     * completion
     * @param uuid the {@link UUID uuid} of the logged flight to find in database,
     * @return the {@link Flight} data transfer object representing the retrieved logged flight from the database
     */
    @Nullable
    public static Flight getLoggedFlightBlocking(UUID uuid) {
        List<Map<Object, Object>> rows = getSQLManager().getRowsInTable(FLIGHTS_TABLE, FLIGHTS_TABLE_COLUMNS[0],
                uuid.toString(), FLIGHTS_TABLE_COLUMNS);

        if (rows == null || rows.isEmpty()) {
            return null;
        }

        Map<Object, Object> row = rows.get(0);

        String flightNumber = (String) row.get(FLIGHTS_TABLE_COLUMNS[1]);
        String departure = (String) row.get(FLIGHTS_TABLE_COLUMNS[2]);
        String arrival = (String) row.get(FLIGHTS_TABLE_COLUMNS[3]);
        long departureTime = (long) row.get(FLIGHTS_TABLE_COLUMNS[4]);
        long arrivalTime = (long) row.get(FLIGHTS_TABLE_COLUMNS[5]);
        String aircraftId = (String) row.get(FLIGHTS_TABLE_COLUMNS[6]);

        Aircraft aircraft = AircraftManager.getAircraftByRegistrationBlocking(aircraftId);

        return new Flight(uuid, flightNumber, departure, arrival, departureTime, arrivalTime, aircraft);
    }

    /**
     * Get a logged flight from the logbook sqlite database, will run in a separate, asynchronous thread
     * @param uuid the {@link UUID uuid} of the logged flight to find in database,
     * @param callback the {@link Executable} to be executed with the retrieved logged {@link Flight} data transfer
     * object from the database
     */
    public static void getLoggedFlight(UUID uuid, Executable<Flight> callback) {
        runAsynchronously(() -> {
            Flight flight = getLoggedFlightBlocking(uuid);
            callback.execute(flight);
        });
    }

    /**
     * Add a logged flight to the logbook sqlite database, will block thread it is called from until sql query
     * completion
     * @param flight the {@link Flight} data transfer object containing all necessary data to store in the database
     * @return TRUE if the logged flight was successfully inserted into the database, FALSE if an issue occurred during
     * insertion
     */
    public static boolean addFlightBlocking(Flight flight) {
        Map<String, Object> vals = new HashMap<>();

        vals.put(FLIGHTS_TABLE_COLUMNS[1], flight.flightNumber());
        vals.put(FLIGHTS_TABLE_COLUMNS[2], flight.departure());
        vals.put(FLIGHTS_TABLE_COLUMNS[3], flight.arrival());
        vals.put(FLIGHTS_TABLE_COLUMNS[4], flight.departureTimeMillis());
        vals.put(FLIGHTS_TABLE_COLUMNS[5], flight.arrivalTimeMillis());
        vals.put(FLIGHTS_TABLE_COLUMNS[6], flight.aircraft().registration());

        return getSQLManager().setRowInTable(FLIGHTS_TABLE, FLIGHTS_TABLE_COLUMNS[0], 
                flight.uuid().toString(), vals);
    }

    /**
     * Add a logged flight to the logbook sqlite database, will run in a separate, asynchronous thread
     * @param flight the {@link Flight} data transfer object containing all necessary data to store in the database
     * @param callback the {@link Executable} to be executed with the boolean response of whether the flight was
     * successfully inserted into the database, see return options of {@link #addFlightBlocking(Flight)}
     */
    public static void addFlight(Flight flight, Executable<Boolean> callback) {
        runAsynchronously(() -> {
            boolean res = addFlightBlocking(flight);
            callback.execute(res);
        });
    }

    /**
     * Remove a logged flight from the logbook sqlite database, will block thread it is called from until sql query
     * completion
     * @param uuid the {@link UUID uuid} of the logged flight to find and delete from the database
     * @return TRUE if the logged flight was successfully removed from the database, FALSE if an issue occurred during
     * deletion
     */
    public static boolean removeFlightBlocking(UUID uuid) {
        return getSQLManager().deleteRowInTable(FLIGHTS_TABLE, FLIGHTS_TABLE_COLUMNS[0], uuid.toString());
    }

    /**
     * Remove a logged flight from the logbook sqlite database, will run in a separate, asynchronous thread
     * @param uuid the {@link UUID uuid} of the logged flight to find and delete from the database
     * @param callback the {@link Executable} to be executed with the boolean response of whether the logged flight was
     * successfully removed from the database, see return options of {@link #removeFlightBlocking(UUID)}
     */
    public static void removeFlight(UUID uuid, Executable<Boolean> callback) {
        runAsynchronously(() -> {
            boolean res = removeFlightBlocking(uuid);
            callback.execute(res);
        });
    }

    /**
     * Get the total logged flight time across all flights, will block thread it is called from until sql query
     * completion
     * @return the total logged flight time as a {@link Duration}
     */
    @NotNull
    public static Duration getTotalFlightTimeBlocking() {
        List<Duration> durations = getLoggedFlightsBlocking().stream()
                .map(Flight::getFlightTime)
                .toList();
        return DurationUtility.sum(durations);
    }

    /**
     * Get the total logged flight time across all logged flights from the logbook sqlite database, will run in a
     * separate, asynchronous thread
     * @param callback the {@link Executable} to be executed with the total logged flight time as a {@link Duration}
     */
    public static void getTotalFlightTime(Executable<Duration> callback) {
        runAsynchronously(() ->
                callback.execute(getTotalFlightTimeBlocking()));
    }

    /**
     * Get the longest logged flight from the logbook sqlite database, will block thread it is called from until sql
     * query completion
     * @return the {@link Flight} data transfer object representing the retrieved logged flight from the database
     */
    public static Flight getLongestFlightBlocking() {
        List<Flight> flights = getLoggedFlightsBlocking();

        if (flights.isEmpty()) {
            return null;
        }

        flights.sort((lhs, rhs) ->
                DurationUtility.compare(lhs.getFlightTime(), rhs.getFlightTime()));

        return flights.get(0);
    }

    /**
     * Get the longest logged flight from the logbook sqlite database, will block thread it is called from until sql
     * query completion
     * @param callback the {@link Executable} to be executed with the retrieved logged {@link Flight} data transfer
     * object from the database
     */
    public static void getLongestFlight(Executable<Flight> callback) {
        runAsynchronously(() ->
                callback.execute(getLongestFlightBlocking()));
    }

    /**
     * Get the total number of logged flights
     * @return the total number of logged flights, as an integer
     */
    public static int getTotalNumberOfFlightsBlocking() {
        return getSQLManager().getRowCount(FLIGHTS_TABLE);
    }

    /**
     * Get the total number of logged flights, will block thread it is called from until sql query completion
     * @param callback the {@link Executable} to be executed with the total number of logged flights
     */
    public static void getTotalNumberOfFlights(Executable<Integer> callback) {
        runAsynchronously(() ->
                callback.execute(getTotalNumberOfFlightsBlocking()));
    }

    /**
     * Get the most frequent departure airport
     * @return the most frequent departure airport, as a string
     */
    public static String getMostFrequentDepartureBlocking() {
        List<Object> deps = getSQLManager().getColumnInTable(FlightManager.FLIGHTS_TABLE, FlightManager.FLIGHTS_TABLE_COLUMNS[2],
                "GROUP BY `dep` ORDER BY COUNT(`dep`) DESC");

        return deps.isEmpty() ? null : (String) deps.get(0);
    }

    /**
     * Get the most frequent departure airport
     * @param callback the {@link Executable} to be executed with the most frequent departure airport, as a string
     */
    public static void getMostFrequentDeparture(Executable<String> callback) {
        runAsynchronously(() -> callback.execute(getMostFrequentDepartureBlocking()));
    }

    /**
     * Get the most frequent arrival airport
     * @return the most frequent arrival airport, as a string
     */
    public static String getMostFrequentArrivalBlocking() {
        List<Object> arrs = getSQLManager().getColumnInTable(FlightManager.FLIGHTS_TABLE, FlightManager.FLIGHTS_TABLE_COLUMNS[3],
                "GROUP BY `arr` ORDER BY COUNT(`arr`) DESC");

        return arrs.isEmpty() ? null : (String) arrs.get(0);
    }

    /**
     * Get the most frequent arrival airport
     * @param callback the {@link Executable} to be executed with the most frequent arrival airport, as a string
     */
    public static void getMostFrequentArrival(Executable<String> callback) {
        runAsynchronously(() -> callback.execute(getMostFrequentArrivalBlocking()));
    }

    /**
     * Export the logbook sqlite database flights table to a .csv file
     */
    public static void export() {
        SQLUtility.writeTableToCSV(FlightLogbook.getSQL(), FLIGHTS_TABLE);
    }

    private static SQLManager getSQLManager() {
        return FlightLogbook.getSQL().getManager();
    }
    
    private static void runAsynchronously(Runnable runnable) {
        Scheduler.getInstance().runTaskAsynchronously(runnable);
    }
}
