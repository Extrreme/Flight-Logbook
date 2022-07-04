package dev.extrreme.logbook.utils;

import java.time.Duration;

public class DurationUtility {

    /**
     * Converts a duration to a readable string
     * @param duration the {@link Duration} to be converted to a string
     * @return the readable string that represents the duration
     */
    public static String toString(Duration duration) {
        long HH = duration.toHours();
        long MM = duration.toMinutesPart();
        long SS = duration.toSecondsPart();

        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }
}
