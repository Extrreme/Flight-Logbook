package dev.extrreme.logbook.dto;

import dev.extrreme.logbook.utils.StringUtility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/* TODO: Find an API to replace this? Very tedious to add all the airframes from various manufacturers
    Possible options:
        - https://www.back4app.com/database/back4app/aircraft-make-and-model-list
            - Limited information provided on each aircraft, would be cool to get other stuff
        - https://applications.icao.int/dataservices/default.aspx
            - Is it 100 free calls/month or total?? + Is that even enough??
            - Not sure if there is even an aircraft data API
 */

/**
 * Enum to represent the various aircraft airframes
 */
public enum Airframe {
    // Airbus
    A318("Airbus", "A318", WeightClass.LARGE),
    A319("Airbus", "A319", WeightClass.LARGE),
    A319_NEO("Airbus", "A319neo", WeightClass.LARGE),
    A320("Airbus", "A320", WeightClass.LARGE),
    A320_NEO("Airbus", "A320neo", WeightClass.LARGE),
    A321("Airbus", "A321", WeightClass.LARGE),
    A321_NEO("Airbus", "A321neo", WeightClass.LARGE),
    A330_200("Airbus", "A330-200", WeightClass.HEAVY),
    A330_300("Airbus", "A330-300", WeightClass.HEAVY),
    A330_800("Airbus", "A330-800", WeightClass.HEAVY),
    A330_900("Airbus", "A330-900", WeightClass.HEAVY),
    A350_900("Airbus", "A350-900", WeightClass.HEAVY),
    A350_1000("Airbus", "A350-1000", WeightClass.HEAVY),
    A380("Airbus", "A380", WeightClass.SPECIAL),

    // Boeing
    B737_700("Boeing", "B737-700", WeightClass.LARGE),
    B737_800("Boeing", "B737-800", WeightClass.LARGE),
    B737_900("Boeing", "B737-900", WeightClass.LARGE),
    B737_MAX_7("Boeing", "B737 MAX 7", WeightClass.LARGE),
    B737_MAX_8("Boeing", "B737 MAX 8", WeightClass.LARGE),
    B737_MAX_9("Boeing", "B737 MAX 9", WeightClass.LARGE),
    B737_MAX_10("Boeing", "B737 MAX 10", WeightClass.LARGE),
    B747_8("Boeing", "B747-8", WeightClass.HEAVY),
    B777_200LR("Boeing", "B777-200LR", WeightClass.HEAVY),
    B777_300ER("Boeing", "B777-300ER", WeightClass.HEAVY),
    B777X("Boeing", "B777X", WeightClass.HEAVY),
    B787_8("Boeing", "B787-8", WeightClass.HEAVY),
    B787_9("Boeing", "B787-9", WeightClass.HEAVY),
    B787_10("Boeing", "B787-10", WeightClass.HEAVY);

    private final String manufacturer;
    private final String name;
    private final WeightClass weightClass;

    Airframe(@NotNull String manufacturer, @NotNull String name, @NotNull WeightClass weightClass) {
        this.manufacturer = manufacturer;
        this.name = name;
        this.weightClass = weightClass;
    }

    /**
     * Get the manufacturer of the airframe (e.g., Airbus)
     * @return the aircraft manufacturer
     */
    @NotNull
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Get the name of the airframe (e.g., A320neo)
     * @return the name of the airframe
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Get the full name of the airframe (e.g., Airbus A320neo)
     * @return the full name of the aircraft
     */
    @NotNull
    public String getFullName() {
        return StringUtility.concatenate(" ", manufacturer, name);
    }

    /**
     * Get the weight class of the airframe
     * @return the {@link WeightClass weight class} of the airframe
     */
    @NotNull
    public WeightClass getWeightClass() {
        return weightClass;
    }

    /**
     * Get an airframe based on its name (e.g., A320neo)
     * @param name the name of the airframe, of the form returned by {@link #getName()}
     * @return the airframe associated with that name, or null if no airframe is found
     */
    @Nullable
    public static Airframe getByName(String name) {
        for (Airframe airframe : values()) {
            if (!airframe.getName().trim().equals(name.trim())) {
                continue;
            }
            return airframe;
        }
        return null;
    }

    /**
     * Get an airframe by its full name (e.g., Airbus A320neo)
     * @param name the full name of the airframe, of the form returned by {@link #getFullName()}
     * @return the airframe associated with that full name, or null if no airframe is found
     */
    @Nullable
    public static Airframe getByFullName(String name) {
        for (Airframe airframe : values()) {
            if (!airframe.getFullName().trim().equals(name.trim())) {
                continue;
            }
            return airframe;
        }
        return null;
    }

    /**
     * Get a list of airframe made by a specific manufacturer
     * @param manufacturer the name of the airframe manufacturer, of the form returned by {@link #getManufacturer()}
     * @return the list of airframes made by the specified manufacturer, empty if none are found
     */
    @NotNull
    public static List<Airframe> getByManufacturer(String manufacturer) {
        List<Airframe> airframes = new ArrayList<>();
        for (Airframe airframe : values()) {
            if (!airframe.getManufacturer().trim().equals(manufacturer.trim())) {
                continue;
            }
            airframes.add(airframe);
        }
        return airframes;
    }
}
