package dev.extrreme.logbook.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {

    /**
     * Concatenate a group of strings
     * @param args the strings to concatenate, as an array
     * @param start the starting index to concatenate from
     * @param end the ending index to concatenate to
     * @param between the separator between concatenated strings
     * @return the concatenated string
     */
    public static String concatenate(int start, int end, String between, String... args) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length && i <= end; i++) {
            sb.append(args[i]);
            if (i != end) {
                sb.append(between);
            }
        }

        return sb.toString();
    }

    /**
     * Concatenate a group of strings
     * @param args the strings to concatenate, as an array
     * @param between the separator between concatenated strings
     * @return the concatenated string
     */
    public static String concatenate(String between, String... args) {
        return concatenate(0, args.length-1, between, args);
    }

    /**
     * Check if a specified string is null or empty
     * @param str the string to be checked
     * @return TRUE if the string is null or empty, FALSE if it contains characters
     */
    public static boolean isEmptyOrNull(String str) {
        return str == null || str.equals("");
    }

    /**
     * Check if any string in a specified group of strings is null or empty
     * @param strs the group of strings to be checked, as an array
     * @return TRUE if any of the strings are null or empty, FALSE if all strings contain characters
     */
    public static boolean isEmptyOrNull(String... strs) {
        for (String str : strs) {
            if (isEmptyOrNull(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces the last substring of this string that matches the given regular expression with the given replacement.
     * @param input the string to replace the last substring in
     * @param regex the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for the first match
     * @return the resulting string
     */
    public static String replaceLast(String input, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (!matcher.find()) {
            return input;
        }

        int lastMatchStart;
        do {
            lastMatchStart = matcher.start();
        } while (matcher.find());

        IgnoredResult.ignore(matcher.find(lastMatchStart));

        StringBuilder sb = new StringBuilder(input.length());
        matcher.appendReplacement(sb, replacement);
        matcher.appendTail(sb);
        return sb.toString();
    }
}
