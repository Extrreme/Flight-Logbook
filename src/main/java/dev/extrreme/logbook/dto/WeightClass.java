package dev.extrreme.logbook.dto;

/**
 * Enum to represent the various weight classes an aircraft can have
 */
public enum WeightClass {

    SPECIAL("SPECIAL", 0, Integer.MAX_VALUE),
    HEAVY("Heavy", 300_000, Integer.MAX_VALUE),
    LARGE("Large", 41_000, 300_000),
    MEDIUM("Medium", 12_500, 41_000),
    SMALL("Small", 0, 12_500),
    UNKNOWN("UNKNOWN", -1, -1);

    private final String name;
    private final int minWeightLbs;
    private final int maxWeightLbs;

    WeightClass(String name, int minWeightLbs, int maxWeightLbs) {
       this.name = name;
       this.minWeightLbs = minWeightLbs;
       this.maxWeightLbs = maxWeightLbs;
    }

    /**
     * Get the name of the weight class
     * @return a string containing the name of the weight class
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the minimum weight in lbs of the weight class
     * @return the minimum weight of the weight class as an integer
     */
    public int getMinWeightLbs() {
        return this.minWeightLbs;
    }

    /**
     * Get the maximum weight in lbs of the weight class
     * @return the maximum weight of the weight class as an integer
     */
    public int getMaxWeightLbs() {
        return this.maxWeightLbs;
    }
}
