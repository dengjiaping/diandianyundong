/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fox.exercise.bitmap.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.util.Log;


/**
 * <p>Duration formatting utilities and constants. The following table describes the tokens
 * used in the pattern language for formatting. </p>
 * <table border="1">
 * <tr><th>character</th><th>duration element</th></tr>
 * <tr><td>y</td><td>years</td></tr>
 * <tr><td>M</td><td>months</td></tr>
 * <tr><td>d</td><td>days</td></tr>
 * <tr><td>H</td><td>hours</td></tr>
 * <tr><td>m</td><td>minutes</td></tr>
 * <tr><td>s</td><td>seconds</td></tr>
 * <tr><td>S</td><td>milliseconds</td></tr>
 * </table>
 *
 * @version $Id: DurationFormatUtils.java 1144993 2011-07-11 00:51:16Z ggregory $
 * @since 2.1
 */
public class DurationFormatUtils {

    /**
     * <p>DurationFormatUtils instances should NOT be constructed in standard programming.</p>
     * <p/>
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public DurationFormatUtils() {
        super();
    }

    /**
     * <p>Pattern used with <code>FastDateFormat</code> and <code>SimpleDateFormat</code>
     * for the ISO8601 period format used in durations.</p>
     *
     * @see org.apache.commons.lang3.time.FastDateFormat
     * @see java.text.SimpleDateFormat
     */
    public static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
    /**
     * The empty String {@code ""}.
     *
     * @since 2.0
     */
    public static final String EMPTY = "";

    /**
     * Represents a failed index search.
     *
     * @since 2.1
     */
    public static final int INDEX_NOT_FOUND = -1;
    //-----------------------------------------------------------------------
    /**
     * <p>Formats the time gap as a string.</p>
     * <p/>
     * <p>The format used is ISO8601-like:
     * <i>H</i>:<i>m</i>:<i>s</i>.<i>S</i>.</p>
     *
     * @param durationMillis  the duration to format
     * @return the formatted duration, not null
     */
    private static final int PAD_LIMIT = 8192;
    private static final String TAG = "DurationFormatUtils";

    public static String formatDurationHMS(long durationMillis) {
        return formatDuration(durationMillis, "H:mm:ss.SSS");
    }

    /**
     * <p>Formats the time gap as a string.</p>
     * <p/>
     * <p>The format used is the ISO8601 period format.</p>
     * <p/>
     * <p>This method formats durations using the days and lower fields of the
     * ISO format pattern, such as P7D6TH5M4.321S.</p>
     *
     * @param durationMillis the duration to format
     * @return the formatted duration, not null
     */
    public static String formatDurationISO(long durationMillis) {
        return formatDuration(durationMillis, ISO_EXTENDED_FORMAT_PATTERN, false);
    }

    /**
     * <p>Formats the time gap as a string, using the specified format, and padding with zeros and
     * using the default timezone.</p>
     * <p/>
     * <p>This method formats durations using the days and lower fields of the
     * format pattern. Months and larger are not used.</p>
     *
     * @param durationMillis the duration to format
     * @param format         the way in which to format the duration, not null
     * @return the formatted duration, not null
     */
    public static String formatDuration(long durationMillis, String format) {
        return formatDuration(durationMillis, format, true);
    }

    /**
     * <p>Formats the time gap as a string, using the specified format.
     * Padding the left hand side of numbers with zeroes is optional and
     * the timezone may be specified.</p>
     * <p/>
     * <p>This method formats durations using the days and lower fields of the
     * format pattern. Months and larger are not used.</p>
     *
     * @param durationMillis the duration to format
     * @param format         the way in which to format the duration, not null
     * @param padWithZeros   whether to pad the left hand side of numbers with 0's
     * @return the formatted duration, not null
     */
    public static String formatDuration(long durationMillis, String format, boolean padWithZeros) {

        Token[] tokens = lexx(format);
        Log.d("format tokens:", "" + tokens.toString());

        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        int milliseconds = 0;

        if (Token.containsTokenWithValue(tokens, d)) {
            days = (int) (durationMillis / DateUtils.MILLIS_PER_DAY);
            durationMillis = durationMillis - (days * DateUtils.MILLIS_PER_DAY);
        }
        if (Token.containsTokenWithValue(tokens, H)) {
            hours = (int) (durationMillis / DateUtils.MILLIS_PER_HOUR);
            durationMillis = durationMillis - (hours * DateUtils.MILLIS_PER_HOUR);
        }
        if (Token.containsTokenWithValue(tokens, m)) {
            minutes = (int) (durationMillis / DateUtils.MILLIS_PER_MINUTE);
            durationMillis = durationMillis - (minutes * DateUtils.MILLIS_PER_MINUTE);
        }
        if (Token.containsTokenWithValue(tokens, s)) {
            seconds = (int) (durationMillis / DateUtils.MILLIS_PER_SECOND);
            durationMillis = durationMillis - (seconds * DateUtils.MILLIS_PER_SECOND);
        }
        if (Token.containsTokenWithValue(tokens, S)) {
            milliseconds = (int) durationMillis;
        }

        return format(tokens, 0, 0, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }

    /**
     * <p>Formats an elapsed time into a plurialization correct string.</p>
     * <p/>
     * <p>This method formats durations using the days and lower fields of the
     * format pattern. Months and larger are not used.</p>
     *
     * @param durationMillis               the elapsed time to report in milliseconds
     * @param suppressLeadingZeroElements  suppresses leading 0 elements
     * @param suppressTrailingZeroElements suppresses trailing 0 elements
     * @return the formatted text in days/hours/minutes/seconds, not null
     */
    public static String formatDurationWords(
            long durationMillis,
            boolean suppressLeadingZeroElements,
            boolean suppressTrailingZeroElements) {

        // This method is generally replacable by the format method, but 
        // there are a series of tweaks and special cases that require 
        // trickery to replicate.
        String duration = formatDuration(durationMillis, "d' days 'H' hours 'm' minutes 's' seconds'");
        if (suppressLeadingZeroElements) {
            // this is a temporary marker on the front. Like ^ in regexp.
            duration = " " + duration;
            String tmp = replaceOnce(duration, " 0 days", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = replaceOnce(duration, " 0 hours", "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = replaceOnce(duration, " 0 minutes", "");
                    duration = tmp;
                    if (tmp.length() != duration.length()) {
                        duration = replaceOnce(tmp, " 0 seconds", "");
                    }
                }
            }
            if (duration.length() != 0) {
                // strip the space off again
                duration = duration.substring(1);
            }
        }
        if (suppressTrailingZeroElements) {
            String tmp = replaceOnce(duration, " 0 seconds", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = replaceOnce(duration, " 0 minutes", "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = replaceOnce(duration, " 0 hours", "");
                    if (tmp.length() != duration.length()) {
                        duration = replaceOnce(tmp, " 0 days", "");
                    }
                }
            }
        }
        // handle plurals
        duration = " " + duration;
        duration = replaceOnce(duration, " 1 seconds", " 1 second");
        duration = replaceOnce(duration, " 1 minutes", " 1 minute");
        duration = replaceOnce(duration, " 1 hours", " 1 hour");
        duration = replaceOnce(duration, " 1 days", " 1 day");
        return duration.trim();
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Formats the time gap as a string.</p>
     * <p/>
     * <p>The format used is the ISO8601 period format.</p>
     *
     * @param startMillis the start of the duration to format
     * @param endMillis   the end of the duration to format
     * @return the formatted duration, not null
     */
    public static String formatPeriodISO(long startMillis, long endMillis) {
        return formatPeriod(startMillis, endMillis, ISO_EXTENDED_FORMAT_PATTERN, false, TimeZone.getDefault());
    }

    /**
     * <p>Formats the time gap as a string, using the specified format.
     * Padding the left hand side of numbers with zeroes is optional.
     *
     * @param startMillis the start of the duration
     * @param endMillis   the end of the duration
     * @param format      the way in which to format the duration, not null
     * @return the formatted duration, not null
     */
    public static String formatPeriod(long startMillis, long endMillis, String format) {
        return formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
    }

    /**
     * <p>Formats the time gap as a string, using the specified format.
     * Padding the left hand side of numbers with zeroes is optional and
     * the timezone may be specified. </p>
     * <p/>
     * <p>When calculating the difference between months/days, it chooses to
     * calculate months first. So when working out the number of months and
     * days between January 15th and March 10th, it choose 1 month and
     * 23 days gained by choosing January->February = 1 month and then
     * calculating days forwards, and not the 1 month and 26 days gained by
     * choosing March -> February = 1 month and then calculating days
     * backwards. </p>
     * <p/>
     * <p>For more control, the <a href="http://joda-time.sf.net/">Joda-Time</a>
     * library is recommended.</p>
     *
     * @param startMillis  the start of the duration
     * @param endMillis    the end of the duration
     * @param format       the way in which to format the duration, not null
     * @param padWithZeros whether to pad the left hand side of numbers with 0's
     * @param timezone     the millis are defined in
     * @return the formatted duration, not null
     */
    public static String formatPeriod(long startMillis, long endMillis, String format, boolean padWithZeros,
                                      TimeZone timezone) {

        // Used to optimise for differences under 28 days and 
        // called formatDuration(millis, format); however this did not work 
        // over leap years. 
        // TODO: Compare performance to see if anything was lost by 
        // losing this optimisation. 

        Token[] tokens = lexx(format);

        // timezones get funky around 0, so normalizing everything to GMT 
        // stops the hours being off
        Calendar start = Calendar.getInstance(timezone);
        start.setTime(new Date(startMillis));
        Calendar end = Calendar.getInstance(timezone);
        end.setTime(new Date(endMillis));
        Log.d(TAG, "start.getTimeInMillis():" + start.getTimeInMillis());
        Log.d(TAG, "end.getTimeInMillis():" + end.getTimeInMillis());
        Log.d(TAG, "start.get(Calendar.YEAR):" + start.get(Calendar.YEAR));
        Log.d(TAG, "end.get(Calendar.YEAR):" + end.get(Calendar.YEAR));

        // initial estimates
        int milliseconds = end.get(Calendar.MILLISECOND) - start.get(Calendar.MILLISECOND);
        int seconds = end.get(Calendar.SECOND) - start.get(Calendar.SECOND);
        int minutes = end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE);
        int hours = end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY);
        int days = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);
        int months = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        Log.d(TAG, "years first caculate:" + years);
        // each initial estimate is adjusted in case it is under 0
        while (milliseconds < 0) {
            milliseconds += 1000;
            seconds -= 1;
        }
        while (seconds < 0) {
            seconds += 60;
            minutes -= 1;
        }
        while (minutes < 0) {
            minutes += 60;
            hours -= 1;
        }
        while (hours < 0) {
            hours += 24;
            days -= 1;
        }

        if (Token.containsTokenWithValue(tokens, M)) {
            while (days < 0) {
                days += start.getActualMaximum(Calendar.DAY_OF_MONTH);
                months -= 1;
                start.add(Calendar.MONTH, 1);
            }

            while (months < 0) {
                months += 12;
                years -= 1;
            }

            if (!Token.containsTokenWithValue(tokens, y) && years != 0) {
                while (years != 0) {
                    months += 12 * years;
                    years = 0;
                }
            }
        } else {
            // there are no M's in the format string

            if (!Token.containsTokenWithValue(tokens, y)) {
                int target = end.get(Calendar.YEAR);
                if (months < 0) {
                    // target is end-year -1
                    target -= 1;
                }

                while ((start.get(Calendar.YEAR) != target)) {
                    days += start.getActualMaximum(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);

                    // Not sure I grok why this is needed, but the brutal tests show it is
                    if (start instanceof GregorianCalendar &&
                            start.get(Calendar.MONTH) == Calendar.FEBRUARY &&
                            start.get(Calendar.DAY_OF_MONTH) == 29) {
                        days += 1;
                    }

                    start.add(Calendar.YEAR, 1);

                    days += start.get(Calendar.DAY_OF_YEAR);
                }

                years = 0;
            }

            while (start.get(Calendar.MONTH) != end.get(Calendar.MONTH)) {
                days += start.getActualMaximum(Calendar.DAY_OF_MONTH);
                start.add(Calendar.MONTH, 1);
            }

            months = 0;

            while (days < 0) {
                days += start.getActualMaximum(Calendar.DAY_OF_MONTH);
                months -= 1;
                start.add(Calendar.MONTH, 1);
            }

        }

        // The rest of this code adds in values that 
        // aren't requested. This allows the user to ask for the 
        // number of months and get the real count and not just 0->11.

        if (!Token.containsTokenWithValue(tokens, d)) {
            hours += 24 * days;
            days = 0;
        }
        if (!Token.containsTokenWithValue(tokens, H)) {
            minutes += 60 * hours;
            hours = 0;
        }
        if (!Token.containsTokenWithValue(tokens, m)) {
            seconds += 60 * minutes;
            minutes = 0;
        }
        if (!Token.containsTokenWithValue(tokens, s)) {
            milliseconds += 1000 * seconds;
            seconds = 0;
        }
        Log.d(TAG, "years:" + years);
        Log.d(TAG, "months:" + months);
        Log.d(TAG, "days:" + days);

        return format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>The internal method to do the formatting.</p>
     *
     * @param tokens       the tokens
     * @param years        the number of years
     * @param months       the number of months
     * @param days         the number of days
     * @param hours        the number of hours
     * @param minutes      the number of minutes
     * @param seconds      the number of seconds
     * @param milliseconds the number of millis
     * @param padWithZeros whether to pad
     * @return the formatted string
     */
    static String format(Token[] tokens, int years, int months, int days, int hours, int minutes, int seconds,
                         int milliseconds, boolean padWithZeros) {
        StringBuffer buffer = new StringBuffer();
        boolean lastOutputSeconds = false;
        int sz = tokens.length;
        for (int i = 0; i < sz; i++) {
            Token token = tokens[i];
            Object value = token.getValue();
            int count = token.getCount();
            if (value instanceof StringBuffer) {
                buffer.append(value.toString());
            } else {
                if (value == y) {
                    buffer.append(padWithZeros ? leftPad(Integer.toString(years), count, '0') : Integer
                            .toString(years));
                    lastOutputSeconds = false;
                } else if (value == M) {
                    buffer.append(padWithZeros ? leftPad(Integer.toString(months), count, '0') : Integer
                            .toString(months));
                    lastOutputSeconds = false;
                } else if (value == d) {
                    buffer.append(padWithZeros ? leftPad(Integer.toString(days), count, '0') : Integer
                            .toString(days));
                    lastOutputSeconds = false;
                } else if (value == H) {
                    buffer.append(padWithZeros ? leftPad(Integer.toString(hours), count, '0') : Integer
                            .toString(hours));
                    lastOutputSeconds = false;
                } else if (value == m) {
                    buffer.append(padWithZeros ? leftPad(Integer.toString(minutes), count, '0') : Integer
                            .toString(minutes));
                    lastOutputSeconds = false;
                } else if (value == s) {
                    buffer.append(padWithZeros ? leftPad(Integer.toString(seconds), count, '0') : Integer
                            .toString(seconds));
                    lastOutputSeconds = true;
                } else if (value == S) {
                    if (lastOutputSeconds) {
                        milliseconds += 1000;
                        String str = padWithZeros
                                ? leftPad(Integer.toString(milliseconds), count, '0')
                                : Integer.toString(milliseconds);
                        buffer.append(str.substring(1));
                    } else {
                        buffer.append(padWithZeros
                                ? leftPad(Integer.toString(milliseconds), count, '0')
                                : Integer.toString(milliseconds));
                    }
                    lastOutputSeconds = false;
                }
            }
        }
        return buffer.toString();
    }

    static final Object y = "y";
    static final Object M = "M";
    static final Object d = "d";
    static final Object H = "H";
    static final Object m = "m";
    static final Object s = "s";
    static final Object S = "S";

    /**
     * Parses a classic date format string into Tokens
     *
     * @param format the format to parse, not null
     * @return array of Token[]
     */
    static Token[] lexx(String format) {
        char[] array = format.toCharArray();
        ArrayList<Token> list = new ArrayList<Token>(array.length);

        boolean inLiteral = false;
        StringBuffer buffer = null;
        Token previous = null;
        int sz = array.length;
        for (int i = 0; i < sz; i++) {
            char ch = array[i];
            if (inLiteral && ch != '\'') {
                buffer.append(ch); // buffer can't be null if inLiteral is true
                continue;
            }
            Object value = null;
            switch (ch) {
                // TODO: Need to handle escaping of '
                case '\'':
                    if (inLiteral) {
                        buffer = null;
                        inLiteral = false;
                    } else {
                        buffer = new StringBuffer();
                        list.add(new Token(buffer));
                        inLiteral = true;
                    }
                    break;
                case 'y':
                    value = y;
                    break;
                case 'M':
                    value = M;
                    break;
                case 'd':
                    value = d;
                    break;
                case 'H':
                    value = H;
                    break;
                case 'm':
                    value = m;
                    break;
                case 's':
                    value = s;
                    break;
                case 'S':
                    value = S;
                    break;
                default:
                    if (buffer == null) {
                        buffer = new StringBuffer();
                        list.add(new Token(buffer));
                    }
                    buffer.append(ch);
            }

            if (value != null) {
                if (previous != null && previous.getValue() == value) {
                    previous.increment();
                } else {
                    Token token = new Token(value);
                    list.add(token);
                    previous = token;
                }
                buffer = null;
            }
        }
        return list.toArray(new Token[list.size()]);
    }

    //-----------------------------------------------------------------------

    /**
     * Element that is parsed from the format pattern.
     */
    static class Token {

        /**
         * Helper method to determine if a set of tokens contain a value
         *
         * @param tokens set to look in
         * @param value  to look for
         * @return boolean <code>true</code> if contained
         */
        static boolean containsTokenWithValue(Token[] tokens, Object value) {
            int sz = tokens.length;
            for (int i = 0; i < sz; i++) {
                if (tokens[i].getValue() == value) {
                    return true;
                }
            }
            return false;
        }

        private final Object value;
        private int count;

        /**
         * Wraps a token around a value. A value would be something like a 'Y'.
         *
         * @param value to wrap
         */
        Token(Object value) {
            this.value = value;
            this.count = 1;
        }

        /**
         * Wraps a token around a repeated number of a value, for example it would
         * store 'yyyy' as a value for y and a count of 4.
         *
         * @param value to wrap
         * @param count to wrap
         */
        Token(Object value, int count) {
            this.value = value;
            this.count = count;
        }

        /**
         * Adds another one of the value
         */
        void increment() {
            count++;
        }

        /**
         * Gets the current number of values represented
         *
         * @return int number of values represented
         */
        int getCount() {
            return count;
        }

        /**
         * Gets the particular value this token represents.
         *
         * @return Object value
         */
        Object getValue() {
            return value;
        }

        /**
         * Supports equality of this Token to another Token.
         *
         * @param obj2 Object to consider equality of
         * @return boolean <code>true</code> if equal
         */
        @Override
        public boolean equals(Object obj2) {
            if (obj2 instanceof Token) {
                Token tok2 = (Token) obj2;
                if (this.value.getClass() != tok2.value.getClass()) {
                    return false;
                }
                if (this.count != tok2.count) {
                    return false;
                }
                if (this.value instanceof StringBuffer) {
                    return this.value.toString().equals(tok2.value.toString());
                } else if (this.value instanceof Number) {
                    return this.value.equals(tok2.value);
                } else {
                    return this.value == tok2.value;
                }
            }
            return false;
        }

        /**
         * Returns a hash code for the token equal to the
         * hash code for the token's value. Thus 'TT' and 'TTTT'
         * will have the same hash code.
         *
         * @return The hash code for the token
         */
        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * Represents this token as a String.
         *
         * @return String representation of the token
         */
        @Override
        public String toString() {
            return repeat(this.value.toString(), this.count);
        }
    }

    public static String repeat(String str, int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= PAD_LIMIT) {
            return repeat(str.charAt(0), repeat);
        }

        int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1:
                return repeat(str.charAt(0), repeat);
            case 2:
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                StringBuilder buf = new StringBuilder(outputLength);
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                return buf.toString();
        }
    }
    // Replacing
    //-----------------------------------------------------------------------

    /**
     * <p>Replaces a String with another String inside a larger String, once.</p>
     * <p/>
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     * <p/>
     * <pre>
     * replaceOnce(null, *, *)        = null
     * replaceOnce("", *, *)          = ""
     * replaceOnce("any", null, *)    = "any"
     * replaceOnce("any", *, null)    = "any"
     * replaceOnce("any", "", *)      = "any"
     * replaceOnce("aba", "a", null)  = "aba"
     * replaceOnce("aba", "a", "")    = "ba"
     * replaceOnce("aba", "a", "z")   = "zba"
     * </pre>
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace with, may be null
     * @return the text with any replacements processed,
     * {@code null} if null String input
     * @see #replace(String text, String searchString, String replacement, int max)
     */
    public static String replaceOnce(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    /**
     * <p>Replaces all occurrences of a String within another String.</p>
     * <p/>
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     * <p/>
     * <pre>
     * replace(null, *, *)        = null
     * replace("", *, *)          = ""
     * replace("any", null, *)    = "any"
     * replace("any", *, null)    = "any"
     * replace("any", "", *)      = "any"
     * replace("aba", "a", null)  = "aba"
     * replace("aba", "a", "")    = "b"
     * replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @return the text with any replacements processed,
     * {@code null} if null String input
     * @see #replace(String text, String searchString, String replacement, int max)
     */
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first {@code max} values of the search String.</p>
     * <p/>
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     * <p/>
     * <pre>
     * replace(null, *, *, *)         = null
     * replace("", *, *, *)           = ""
     * replace("any", null, *, *)     = "any"
     * replace("any", *, null, *)     = "any"
     * replace("any", "", *, *)       = "any"
     * replace("any", *, *, 0)        = "any"
     * replace("abaa", "a", null, -1) = "abaa"
     * replace("abaa", "a", "", -1)   = "b"
     * replace("abaa", "a", "z", 0)   = "abaa"
     * replace("abaa", "a", "z", 1)   = "zbaa"
     * replace("abaa", "a", "z", 2)   = "zbza"
     * replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max          maximum number of values to replace, or {@code -1} if no maximum
     * @return the text with any replacements processed,
     * {@code null} if null String input
     */
    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p/>
     * <p>
     * A {@code null} reference passed to this method is a no-op, or if
     * any "search string" or "string to replace" is null, that replace will be
     * ignored. This will not repeat. For repeating replaces, call the
     * overloaded method.
     * </p>
     * <p/>
     * <pre>
     *  replaceEach(null, *, *)        = null
     *  replaceEach("", *, *)          = ""
     *  replaceEach("aba", null, null) = "aba"
     *  replaceEach("aba", new String[0], null) = "aba"
     *  replaceEach("aba", null, new String[0]) = "aba"
     *  replaceEach("aba", new String[]{"a"}, null)  = "aba"
     *  replaceEach("aba", new String[]{"a"}, new String[]{""})  = "b"
     *  replaceEach("aba", new String[]{null}, new String[]{"a"})  = "aba"
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"})  = "wcte"
     *  (example of how it does not repeat)
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"})  = "dcte"
     * </pre>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @return the text with any replacements processed, {@code null} if
     * null String input
     * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
     *                                  and/or size 0)
     * @since 2.4
     */
    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p/>
     * <p>
     * A {@code null} reference passed to this method is a no-op, or if
     * any "search string" or "string to replace" is null, that replace will be
     * ignored.
     * </p>
     * <p/>
     * <pre>
     *  replaceEach(null, *, *, *) = null
     *  replaceEach("", *, *, *) = ""
     *  replaceEach("aba", null, null, *) = "aba"
     *  replaceEach("aba", new String[0], null, *) = "aba"
     *  replaceEach("aba", null, new String[0], *) = "aba"
     *  replaceEach("aba", new String[]{"a"}, null, *) = "aba"
     *  replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
     *  replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
     *  (example of how it repeats)
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, true) = IllegalStateException
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, false) = "dcabe"
     * </pre>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @return the text with any replacements processed, {@code null} if
     * null String input
     * @throws IllegalStateException    if the search is repeating and there is an endless loop due
     *                                  to outputs of one being inputs to another
     * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
     *                                  and/or size 0)
     * @since 2.4
     */
    public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
        // timeToLive should be 0 if not used or nothing to replace, else it's
        // the length of the replace array
        int timeToLive = searchList == null ? 0 : searchList.length;
        return replaceEach(text, searchList, replacementList, true, timeToLive);
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p/>
     * <p>
     * A {@code null} reference passed to this method is a no-op, or if
     * any "search string" or "string to replace" is null, that replace will be
     * ignored.
     * </p>
     * <p/>
     * <pre>
     *  replaceEach(null, *, *, *) = null
     *  replaceEach("", *, *, *) = ""
     *  replaceEach("aba", null, null, *) = "aba"
     *  replaceEach("aba", new String[0], null, *) = "aba"
     *  replaceEach("aba", null, new String[0], *) = "aba"
     *  replaceEach("aba", new String[]{"a"}, null, *) = "aba"
     *  replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
     *  replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
     *  (example of how it repeats)
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
     *  replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, *) = IllegalStateException
     * </pre>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @param repeat          if true, then replace repeatedly
     *                        until there are no more possible replacements or timeToLive < 0
     * @param timeToLive      if less than 0 then there is a circular reference and endless
     *                        loop
     * @return the text with any replacements processed, {@code null} if
     * null String input
     * @throws IllegalStateException    if the search is repeating and there is an endless loop due
     *                                  to outputs of one being inputs to another
     * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
     *                                  and/or size 0)
     * @since 2.4
     */
    private static String replaceEach(
            String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {

        // mchyzer Performance note: This creates very few new objects (one major goal)
        // let me know if there are performance requests, we can create a harness to measure

        if (text == null || text.length() == 0 || searchList == null ||
                searchList.length == 0 || replacementList == null || replacementList.length == 0) {
            return text;
        }

        // if recursing, this shouldn't be less than 0
        if (timeToLive < 0) {
            throw new IllegalStateException("Aborting to protect against StackOverflowError - " +
                    "output of one loop is the input of another");
        }

        int searchLength = searchList.length;
        int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: "
                    + searchLength
                    + " vs "
                    + replacementLength);
        }

        // keep track of which still have matches
        boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                    searchList[i].length() == 0 || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesn't have to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their corresponding text being replaced
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i] == null || replacementList[i] == null) {
                continue;
            }
            int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length() / 5);

        StringBuilder buf = new StringBuilder(text.length() + increase);

        while (textIndex != -1) {

            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);

            start = textIndex + searchList[replaceIndex].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                        searchList[i].length() == 0 || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        String result = buf.toString();
        if (!repeat) {
            return result;
        }

        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }

    // Replace, character based
    //-----------------------------------------------------------------------

    /**
     * <p>Replaces all occurrences of a character in a String with another.
     * This is a null-safe version of {@link String#replace(char, char)}.</p>
     * <p/>
     * <p>A {@code null} string input returns {@code null}.
     * An empty ("") string input returns an empty string.</p>
     * <p/>
     * <pre>
     * replaceChars(null, *, *)        = null
     * replaceChars("", *, *)          = ""
     * replaceChars("abcba", 'b', 'y') = "aycya"
     * replaceChars("abcba", 'z', 'y') = "abcba"
     * </pre>
     *
     * @param str         String to replace characters in, may be null
     * @param searchChar  the character to search for, may be null
     * @param replaceChar the character to replace, may be null
     * @return modified String, {@code null} if null string input
     * @since 2.0
     */
    public static String replaceChars(String str, char searchChar, char replaceChar) {
        if (str == null) {
            return null;
        }
        return str.replace(searchChar, replaceChar);
    }

    /**
     * <p>Replaces multiple characters in a String in one go.
     * This method can also be used to delete characters.</p>
     * <p/>
     * <p>For example:<br />
     * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>.</p>
     * <p/>
     * <p>A {@code null} string input returns {@code null}.
     * An empty ("") string input returns an empty string.
     * A null or empty set of search characters returns the input string.</p>
     * <p/>
     * <p>The length of the search characters should normally equal the length
     * of the replace characters.
     * If the search characters is longer, then the extra search characters
     * are deleted.
     * If the search characters is shorter, then the extra replace characters
     * are ignored.</p>
     * <p/>
     * <pre>
     * replaceChars(null, *, *)           = null
     * replaceChars("", *, *)             = ""
     * replaceChars("abc", null, *)       = "abc"
     * replaceChars("abc", "", *)         = "abc"
     * replaceChars("abc", "b", null)     = "ac"
     * replaceChars("abc", "b", "")       = "ac"
     * replaceChars("abcba", "bc", "yz")  = "ayzya"
     * replaceChars("abcba", "bc", "y")   = "ayya"
     * replaceChars("abcba", "bc", "yzx") = "ayzya"
     * </pre>
     *
     * @param str          String to replace characters in, may be null
     * @param searchChars  a set of characters to search for, may be null
     * @param replaceChars a set of characters to replace, may be null
     * @return modified String, {@code null} if null string input
     * @since 2.0
     */
    public static String replaceChars(String str, String searchChars, String replaceChars) {
        if (isEmpty(str) || isEmpty(searchChars)) {
            return str;
        }
        if (replaceChars == null) {
            replaceChars = EMPTY;
        }
        boolean modified = false;
        int replaceCharsLength = replaceChars.length();
        int strLength = str.length();
        StringBuilder buf = new StringBuilder(strLength);
        for (int i = 0; i < strLength; i++) {
            char ch = str.charAt(i);
            int index = searchChars.indexOf(ch);
            if (index >= 0) {
                modified = true;
                if (index < replaceCharsLength) {
                    buf.append(replaceChars.charAt(index));
                }
            } else {
                buf.append(ch);
            }
        }
        if (modified) {
            return buf.toString();
        }
        return str;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>Left pad a String with spaces (' ').</p>
     * <p/>
     * <p>The String is padded to the size of {@code size}.</p>
     * <p/>
     * <pre>
     * leftPad(null, *)   = null
     * leftPad("", 3)     = "   "
     * leftPad("bat", 3)  = "bat"
     * leftPad("bat", 5)  = "  bat"
     * leftPad("bat", 1)  = "bat"
     * leftPad("bat", -1) = "bat"
     * </pre>
     *
     * @param str  the String to pad out, may be null
     * @param size the size to pad to
     * @return left padded String or original String if no padding is necessary,
     * {@code null} if null String input
     */
    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    /**
     * <p>Left pad a String with a specified character.</p>
     * <p/>
     * <p>Pad to a size of {@code size}.</p>
     * <p/>
     * <pre>
     * leftPad(null, *, *)     = null
     * leftPad("", 3, 'z')     = "zzz"
     * leftPad("bat", 3, 'z')  = "bat"
     * leftPad("bat", 5, 'z')  = "zzbat"
     * leftPad("bat", 1, 'z')  = "bat"
     * leftPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     the String to pad out, may be null
     * @param size    the size to pad to
     * @param padChar the character to pad with
     * @return left padded String or original String if no padding is necessary,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    /**
     * <p>Left pad a String with a specified String.</p>
     * <p/>
     * <p>Pad to a size of {@code size}.</p>
     * <p/>
     * <pre>
     * leftPad(null, *, *)      = null
     * leftPad("", 3, "z")      = "zzz"
     * leftPad("bat", 3, "yz")  = "bat"
     * leftPad("bat", 5, "yz")  = "yzbat"
     * leftPad("bat", 8, "yz")  = "yzyzybat"
     * leftPad("bat", 1, "yz")  = "bat"
     * leftPad("bat", -1, "yz") = "bat"
     * leftPad("bat", 5, null)  = "  bat"
     * leftPad("bat", 5, "")    = "  bat"
     * </pre>
     *
     * @param str    the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     * @return left padded String or original String if no padding is necessary,
     * {@code null} if null String input
     */
    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    public static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }
}
