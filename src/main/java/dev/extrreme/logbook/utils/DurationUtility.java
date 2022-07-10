package dev.extrreme.logbook.utils;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

public class DurationUtility {

    /**
     * Converts a duration to a readable string
     * @param duration the {@link Duration} to be converted to a string
     * @return the readable string that represents the duration
     */
    @NotNull
    public static String toString(Duration duration) {
        long HH = duration.toHours();
        long MM = duration.toMinutesPart();
        long SS = duration.toSecondsPart();

        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }

    /**
     * Sums a group of durations
     * @param durations the {@link Duration durations} to be summed
     * @return A duration whose value is the sum of the provided durations
     */
    @NotNull
    public static Duration sum(@NotNull Duration... durations) {
        Duration sum = Duration.ZERO;

        for (Duration duration : durations) {
            sum = sum.plus(duration);
        }

        return sum;
    }

    /**
     * Sums a group of durations
     * @param durations the {@link Duration durations} to be summed
     * @return A duration whose value is the sum of the provided durations
     */
    @NotNull
    public static Duration sum(@NotNull Collection<Duration> durations) {
        Duration sum = Duration.ZERO;

        for (Duration duration : durations) {
            sum = sum.plus(duration);
        }

        return sum;
    }

    /**
     * Compares two durations, can be used as sorting comparator for {@link Duration durations}
     * @param duration1 the first duration
     * @param duration2 the second duration
     * @return 0 if both durations are the same, -1 if duration1 > duration2, 1 if duration2 > duration1
     */
    public static int compare(@NotNull Duration duration1, @NotNull Duration duration2) {
        return Integer.compare(0, duration1.compareTo(duration2));
    }
}
