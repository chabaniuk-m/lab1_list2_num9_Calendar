package laba1.calendar;

import jdk.jshell.spi.ExecutionControl;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * Prefer calculate value rather store it
 */
public abstract class Calendar implements Serializable {

    //************************* constants for client **************************

    /**
     * for get(int type) method
     */
    public static final int DAYS_IN_COMMON_YEAR;
    /**
     * for get(int type) method
     */
    public static final int DAYS_IN_LEAP_YEAR;
    /**
     * for get(int type) method
     */
    public static final int ERA;
    /**
     * for get(int type) method
     */
    public static final int YEAR;
    /**
     * for get(int type) method
     */
    public static final int MONTH;
    /**
     * for get(int type) method
     */
    public static final int DAY_OF_WEEK;
    /**
     * number of day in month,
     * for get(int type) method
     */
    public static final int DAY;
    /**
     * for get(int type) method
     */
    public static final int WEEK_OF_YEAR, WEEK_OF_MONTH, DAY_OF_YEAR;
    /**
     * for get(int type) method
     */
    public static final int HOUR, MINUTE, SECOND;

    /**
     * How many days have to past
     * from the beginning of the year
     * to a particular date in common year (365)
     */
    protected enum DaysUpTo {
        February(31),
        March(59),
        April(90),
        May(120),
        June(151),
        July(181),
        August(212),
        September(243),
        October(273),
        November(304),
        December(334),
        NewYear(DAYS_IN_COMMON_YEAR);

        private final int value;

        DaysUpTo(int value) {
            this.value = value;
        }

        /*public int getValue() {
            return value;
        }

        *//**
         * the same as getValue
         * @return the number of days from the beginning of the year to the current date in common year
         *//*
        public int count() {
            return value;
        }*/
    }

    protected static int counter = -1;

    static {
        DAYS_IN_COMMON_YEAR = 365;
        DAYS_IN_LEAP_YEAR = DAYS_IN_COMMON_YEAR + 1;
        ERA = counter++;
        YEAR = counter++;
        MONTH = counter++;
        DAY_OF_WEEK = counter++;
        DAY = counter++;
        WEEK_OF_YEAR = counter++;
        WEEK_OF_MONTH = counter++;
        DAY_OF_YEAR = counter++;
        HOUR = counter++;
        MINUTE = counter++;
        SECOND = counter++;
    }

    //********************* useful constants ******************************

    protected static final int UNDEFINED;
    protected static final int MILLIS_IN_MINUTE;
    protected static final int MILLIS_IN_HOUR;
    protected static final int MILLIS_IN_DAY;
    /**
     * number of year after Big Bang up to 1970
     */
    protected static final long AGE_OF_UNIVERSE;
    /**
     * needs to obtain correct timeUTC while calling method update()
     */
    protected static final long DEFAULT_RAW_OFFSET;
    protected static final DayOfWeek DAY_OF_WEEK_1_JAN_1970;
    protected static final DayOfWeek DAY_OF_WEEK_BIG_BANG;
    protected static final long AVERAGE_MILLIS_IN_YEAR;
    /**
     * time from Big Bang to 1 Jan 1970 year b.c.
     */
    protected static final long MILLIS_FROM_BIN_BANG;

    static {
        UNDEFINED = -1;
        MILLIS_IN_MINUTE = 60 * 1000;
        MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE;
        MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR;
        AVERAGE_MILLIS_IN_YEAR = (long) 363.2425 * MILLIS_IN_DAY;
        AGE_OF_UNIVERSE = 13_799_000_000L;
        DEFAULT_RAW_OFFSET = TimeZone.getDefault().getRawOffset();
        DAY_OF_WEEK_1_JAN_1970 = DayOfWeek.Thursday;
        DAY_OF_WEEK_BIG_BANG = DayOfWeek.Sunday;                                   // TODO: 9/29/2021 evaluate and set appropriate day of week
        MILLIS_FROM_BIN_BANG = AGE_OF_UNIVERSE * AVERAGE_MILLIS_IN_YEAR;
    }

    //******************** main fields ********************************

    /**
     * time millis to be added to timeUTC to get the local time
     */
    protected long rawOffset;
    /**
     * canon time can only be a year after 1 Jan 1970
     */
    protected boolean isCanonTime;
    /**
     * time passed from 1 January 1970 in milliseconds if isCanonTime,
     * time passed from Big Bang otherwise
     */
    protected long timeUTC;
    protected Era era;
    /**
     * number of year from 1970 if isCanonTime,
     * from Big Bang otherwise
     */
    protected long year = UNDEFINED;
    /**
     * time passed from the beginning of the year in milliseconds
     */
    protected long yearTime = UNDEFINED;
    /**
     * milliseconds from the start of the month
     */
    protected long monthTime = UNDEFINED;
    /**
     * to introduce local time and determine firstDayOfWeek
     */
    protected TimeZone zone;
    protected DayOfWeek firstDayOfWeek = DayOfWeek.Monday;                          //Can be changed to Sunday only in GregorianCalendar

    /**
     * sets current time and default time zone,
     * calls mandatory method
     */
    protected Calendar() {
        this(TimeZone.getDefault());
    }

    /**
     * mandatory method for construction an object
     * @param zone time zone for correct local hours
     */
    protected void initZone(@NotNull TimeZone zone) {
        this.zone = zone;
        rawOffset = zone.getRawOffset() - DEFAULT_RAW_OFFSET;
    }

    /**
     * sets current time, calls mandatory method
     * @param zone time zone for correct hours
     */
    protected Calendar(@NotNull TimeZone zone) {
        isCanonTime = true;
        initZone(zone);
        update();
    }

    /**
     * sets the current time for calendar (System.currentTimeMillis())
     */
    protected void update() {
        timeUTC = System.currentTimeMillis() - DEFAULT_RAW_OFFSET + ((isCanonTime) ? 0 : MILLIS_FROM_BIN_BANG);
        monthTime = UNDEFINED;
        yearTime = UNDEFINED;
        year = UNDEFINED;
        era = Era.AD;
    }

    /**
     * represents 2 type of era in Christian calendar
     */
    public enum Era {
        /**
         * before Chris
         */
        BC,
        /**
         * after Chris
         */
        AD;
    }

    /**
     * era according to the current year: BC or AD
     * @return current era
     */
    public Era getEra() {
        return era;
    }

    /**
     * Returns the current year according to time
     * @return current year
     */
    public long getYear() {
        getYearMillis();
        return year;
    }

    /**
     * How many milliseconds passed from the beginning of the current year.
     * Side effects:
     *      - initialize current year;
     *      - initialize yearTime;
     *      - initialize era;
     * @return milliseconds passed from the beginning of the year
     */
    protected long getYearMillis() {
        if (yearTime != UNDEFINED) return yearTime;

        long leap = (long) (DAYS_IN_LEAP_YEAR)* MILLIS_IN_DAY;              //milliseconds in leap year
        long common = (long)(DAYS_IN_COMMON_YEAR) * MILLIS_IN_DAY;            //milliseconds in the common year

        if (isCanonTime) {

            int year = 1970;
            long remained = timeUTC;

            while (true) {

                long yearTime = isLeap(year) ? leap : common;

                if (remained < yearTime) {

                    this.year = year;
                    this.era = Era.AD;
                    this.yearTime = remained;

                    return remained;
                } else {
                    remained -= yearTime;
                    ++year;
                }
            }
        } else {

            // TODO: 9/26/2021 test if the following lines working correct

            long beginOfChrisCalendar = AGE_OF_UNIVERSE - 1970;
            long years = timeUTC / AVERAGE_MILLIS_IN_YEAR;

            if (years < beginOfChrisCalendar) {                           //BC
                this.year = beginOfChrisCalendar - years;
                this.era = Era.BC;
            } else {                                                      //AD
                this.year = years - beginOfChrisCalendar;
                this.era = Era.AD;
            }

            return this.yearTime = timeUTC % AVERAGE_MILLIS_IN_YEAR;
        }
    }

    // TODO: 9/26/2021 overload in JulianCalendar
    /**
     * Checks if the passed year is leap (DAYS_IN_LEAP_YEAR days in year)
     * @param year year to be checked
     * @return true if the year is leap, false otherwise
     */
    public static boolean isLeap(long year) {
        if (year % 4 != 0) return false;

        if (year % 100 != 0) return true;

        return year % 400 == 0;
    }

    public enum Month {
        January(1),
        February(2),
        March(3),
        April(4),
        May(5),
        June(6),
        July(7),
        August(8),
        September(9),
        October(10),
        November(11),
        December(12);

        private final int value;
        Month(int value) {
            this.value = value;
        }

        /**
         * number of month, eg: May -> 5
         * @return the conventional number of month
         */
        public int getValue() {
            return value;
        }

        /**
         * Converts int value to Month. Throws IllegalArgumentException if parameter is incorrect
         * @param value value of month, can only be in range [1 - 12]
         * @return the proper month, eg: valueOf(5) -> Month.May;
         */
        public static Month valueOf(int value) {
            return switch (value) {
                case 1 -> January;
                case 2 -> February;
                case 3 -> March;
                case 4 -> April;
                case 5 -> May;
                case 6 -> June;
                case 7 -> July;
                case 8 -> August;
                case 9 -> September;
                case 10 -> October;
                case 11 -> November;
                case 12 -> December;
                default -> throw new IllegalArgumentException("Month value can only be in range [1 - 12]");
            };
        }

        /**
         * Month of the year according to the day. Throws IllegalArgumentException if the number of days is incorrect
         * @param day number of the day in the year
         * @param isLeap true if calculating month of leap year
         * @return month of the year proper to the day
         */
        public static Month valueOf(int day, boolean isLeap) {
            if (isLeap) {
                if (day < DaysUpTo.February.value) return January;
                if (day < DaysUpTo.March.value + 1) return February;
                if (day < DaysUpTo.April.value + 1) return March;
                if (day < DaysUpTo.May.value + 1) return April;
                if (day < DaysUpTo.June.value + 1) return May;
                if (day < DaysUpTo.July.value + 1) return June;
                if (day < DaysUpTo.August.value + 1) return July;
                if (day < DaysUpTo.September.value + 1) return August;
                if (day < DaysUpTo.October.value + 1) return September;
                if (day < DaysUpTo.November.value + 1) return October;
                if (day < DaysUpTo.December.value + 1) return November;
                if (day < DaysUpTo.NewYear.value + 1) return December;

                throw new IllegalArgumentException("A leap year contains 366 days, but obtained " + day);
            } else {
                if (day < DaysUpTo.February.value) return January;
                if (day < DaysUpTo.March.value) return February;
                if (day < DaysUpTo.April.value) return March;
                if (day < DaysUpTo.May.value) return April;
                if (day < DaysUpTo.June.value) return May;
                if (day < DaysUpTo.July.value) return June;
                if (day < DaysUpTo.August.value) return July;
                if (day < DaysUpTo.September.value) return August;
                if (day < DaysUpTo.October.value) return September;
                if (day < DaysUpTo.November.value) return October;
                if (day < DaysUpTo.December.value) return November;
                if (day < DaysUpTo.NewYear.value) return December;

                throw new IllegalArgumentException("Common year contains 365 days, but obtained " + day);
            }
        }

        /**
         * Return next month after count months
         * @param count number of months to be added (can be negative)
         * @return month after count months
         */
        public Month roll(int count) {

            int value = (this.value + count) % 12;

            return Month.valueOf(value > 0 ? value : value + 12);
        }

        /**
         * Next month after count days. Do not check if day is a correct number of day in current month
         * @param year is year to which current month belongs
         * @param day day of current month from which we start
         * @param count number of days to be added (can be negative)
         * @return month after count days
         */
        public Month roll(long year, int day, int count) {
            boolean leap = isLeap(year);
            int days = toDayOfYear(this, day, isLeap(year)) + count;
            int daysInYear;
            if (days <= 0) {
                do {
                    --year;
                    if (leap) {                 //before leap year it is only can be a common year
                        daysInYear = DAYS_IN_COMMON_YEAR;
                        leap = false;
                    } else {
                        daysInYear = (leap = isLeap(year)) ? DAYS_IN_LEAP_YEAR : DAYS_IN_COMMON_YEAR;
                    }
                } while ((days += daysInYear) <= 0);

                return Month.valueOf(days, leap);
            } else {
                daysInYear = leap ? DAYS_IN_LEAP_YEAR : DAYS_IN_COMMON_YEAR;
                while (days > daysInYear) {
                    days -= daysInYear;
                    ++year;
                    if (leap) {                     //after leap year can be only a common year
                        daysInYear = DAYS_IN_COMMON_YEAR;
                        leap = false;
                    } else {
                        daysInYear = (leap = isLeap(year)) ? DAYS_IN_LEAP_YEAR : DAYS_IN_COMMON_YEAR;
                    }
                }

                return Month.valueOf(days, leap);
            }
        }
    }

    /**
     * converts day of the month to day of the year. Don't check if day is a correct number of day in month
     * @param month month which holds the day
     * @param day number of the day in the month
     * @param isLeap true
     * @return day of the year proper to the day of the passed month
     */
    protected static int toDayOfYear(Month month, int day, boolean isLeap) {
        return switch(month) {
            case January -> day;
            case February -> DaysUpTo.February.value + day;
            case March -> DaysUpTo.March.value + ((isLeap) ? (day + 1) : day);
            case April -> DaysUpTo.April.value + ((isLeap) ? (day + 1) : day);
            case May -> DaysUpTo.May.value + ((isLeap) ? (day + 1) : day);
            case June -> DaysUpTo.June.value + ((isLeap) ? (day + 1) : day);
            case July -> DaysUpTo.July.value + ((isLeap) ? (day + 1) : day);
            case August -> DaysUpTo.August.value + ((isLeap) ? (day + 1) : day);
            case September -> DaysUpTo.September.value + ((isLeap) ? (day + 1) : day);
            case October -> DaysUpTo.October.value + ((isLeap) ? (day + 1) : day);
            case November -> DaysUpTo.November.value + ((isLeap) ? (day + 1) : day);
            case December -> DaysUpTo.December.value + ((isLeap) ? (day + 1) : day);
        };
    }

    // TODO: 9/28/2021 rewrite using yearTime
    /**
     * Side effect:     initialize monthTime            //TODO remove this line in realize
     * @return current month according to time
     */
    public Month getMonth() {                                                                  // one of the tasks is to ensure that monthTime is initialized
        if (monthTime != UNDEFINED) {
            return Month.January.roll(year, 1, getDayOfYear() - 1);                 // just believe that year is always initialized here
        } else {
            long millisFromStartOfTheYear = getYearMillis();
            long leap = 31L * MILLIS_IN_DAY;
            long common = 30L * MILLIS_IN_DAY;
            long february = ((isLeap(getYear())) ? 29L : 28L) * MILLIS_IN_DAY;

            // TODO: 9/27/2021 remove in realize
            if (millisFromStartOfTheYear < 0
                    || millisFromStartOfTheYear > (long) DAYS_IN_COMMON_YEAR * MILLIS_IN_DAY && isLeap(getYear())
                    || millisFromStartOfTheYear > (long) DAYS_IN_LEAP_YEAR * MILLIS_IN_DAY && isLeap(getYear())) {
                throw new RuntimeException("Incorrect value obtained from getYearMillis() method, value - " + millisFromStartOfTheYear);
            }

            if (millisFromStartOfTheYear < leap) {
                monthTime = millisFromStartOfTheYear;
                return Month.January;
            }

            if ((millisFromStartOfTheYear -= leap) < february) {
                monthTime = millisFromStartOfTheYear;
                return Month.February;
            }

            if ((millisFromStartOfTheYear -= february) < leap) {
                monthTime = millisFromStartOfTheYear;
                return Month.March;
            }

            if ((millisFromStartOfTheYear -= leap) < common) {
                monthTime = millisFromStartOfTheYear;
                return Month.April;
            }

            if ((millisFromStartOfTheYear -= common) < leap) {
                monthTime = millisFromStartOfTheYear;
                return Month.May;
            }

            if ((millisFromStartOfTheYear -= leap) < common) {
                monthTime = millisFromStartOfTheYear;
                return Month.June;
            }

            if ((millisFromStartOfTheYear -= common) < leap) {
                monthTime = millisFromStartOfTheYear;
                return Month.July;
            }

            if ((millisFromStartOfTheYear -= leap) < leap) {
                monthTime = millisFromStartOfTheYear;
                return Month.August;
            }

            if ((millisFromStartOfTheYear -= leap) < common) {
                monthTime = millisFromStartOfTheYear;
                return Month.September;
            }

            if ( (millisFromStartOfTheYear -= common) < leap) {
                monthTime = millisFromStartOfTheYear;
                return Month.October;
            }

            if ( (millisFromStartOfTheYear -= leap) < common) {
                monthTime = millisFromStartOfTheYear;
                return Month.November;
            }

            monthTime = millisFromStartOfTheYear - common;

            return Month.December;
        }
    }

    /**
     *
     * @return the number of day in the current year
     */
    public int getDayOfYear() {
        return (int) (getYearMillis() / MILLIS_IN_DAY) + 1;                         // + 1 because of 2 hours (2 * MILLIS_IN_HOUR < MILLIS_IN_DAY) from New Year is 1 Jan
    }

    /**
     *
     * @return the number of day in the current month
     */
    public int getDayOfMonth() {
        if (monthTime == UNDEFINED) getMonth();         //this method initializes monthTime as well

        return (int) (monthTime / MILLIS_IN_DAY) + 1;
    }

    /**
     *
     * @return the number of the current week in the year
     */
    public int getWeekOfYear() {
        return (int) (getYearMillis() / (7 * MILLIS_IN_DAY));
    }

    /**
     * Represent days of week conventionally.
     * First day of week is Monday with value 1
     */
    public enum DayOfWeek {
        Monday(1),
        Tuesday(2),
        Wednesday(3),
        Thursday(4),
        Friday(5),
        Saturday(6),
        Sunday(7);

        private final int value;
        DayOfWeek(int value) {
            this.value = value;
        }

        /**
         * Example: Monday - 1, Thursday - 4
         * @return the value associated with the day of week
         */
        public int getValue() {
            return value;
        }

        /**
         * convert int to DayOfWeek
         * @param value must be in range [1 - 7]
         * @return the proper DayOfWeek
         */
        public static DayOfWeek valueOf(int value) {
            return switch (value) {
                case 1 -> Monday;
                case 2 -> Tuesday;
                case 3 -> Wednesday;
                case 4 -> Thursday;
                case 5 -> Friday;
                case 6 -> Saturday;
                case 7 -> Sunday;
                default -> throw new IllegalArgumentException("The value of DayOfWeek can be only in range [1 - 7]");
            };
        }

        //TODO test
        /**
         * Next day of week after count days
         * @param count how many days will pass (can be negative)
         * @return next (previous) day of week after count days
         */
        public DayOfWeek roll(int count) {

            int value = (this.value + count) % 7;

            return DayOfWeek.valueOf(value > 0 ? value : value + 7);
        }
    }

    /**
     * Calculates day of week appropriate to current time
     * @return current day of week
     */
    public DayOfWeek getDayOfWeek() {
        // after common year day of week +1
        // after leap year day of week +2
        // after 400 year day of week does not change

        if (year == UNDEFINED) getYearMillis();

        if (isCanonTime) {                                          //counting from 1 Jan 1970
            int curr = 1970;
            int count = 0;
            while (curr != year) {
                ++curr;
                ++count;
                if (isLeap(curr)) ++curr;
            }
            //day of week has been calculated up to 1 Jan of curr year
            count += getDayOfYear() - getWeekOfYear() * 7 - 1;

            return DAY_OF_WEEK_1_JAN_1970.roll(count);
        } else {
            long year = this.year / 400 * 400;
            int count = 0;
            while (year < this.year && !isLeap(year)) {                 // <= 8 times cycle
                ++year;
                ++count;
            }
            --count;                                                   //compensate if it is the target year
            if (year < this.year - 3) {
                do {                                                   // <= 100 times cycle
                    count += (isLeap(year)) ? 2 : 1;                   //we didn't add this on the previous step
                    year += 4;
                    count += 3;                                        //do not add to calculated year because it can be equals this.year
                } while (year < this.year - 3);
            }
            while (year < this.year) {                                 // <= 4 times cycle
                count += (isLeap(year)) ? 2 : 1;
                ++year;
            }
            //day of week has been calculated up to 1 Jan of curr year
            count += getDayOfYear() - getWeekOfYear() * 7 - 1;

            return DAY_OF_WEEK_BIG_BANG.roll(count);
        }
    }

    /**
     * for get(type) method
     * @return the hour of the day in 24h format (0 - 23)
     */
    protected int _getHours() {
        return (int) (timeUTC % MILLIS_IN_DAY) / MILLIS_IN_HOUR;
    }

    /**
     * Only minutes of the current hours
     * @return current number of minutes (0 - 59)
     */
    public int getMinutes() {
        return (int) (timeUTC % MILLIS_IN_HOUR) / MILLIS_IN_MINUTE;
    }

    /**
     * In the current minute
     * @return current number of seconds (0 - 59)
     */
    public int getSecond() {
        return (int) (timeUTC % MILLIS_IN_MINUTE) / 1000;
    }

    public class Time {
        private int milliseconds;
        private int seconds;
        private int minutes;
        private int hours;
        protected boolean isAmericanFormat = false;

        Time(int totalMillisecondsInDay) {
            hours = totalMillisecondsInDay / MILLIS_IN_HOUR;
            minutes = (totalMillisecondsInDay % MILLIS_IN_HOUR) / MILLIS_IN_MINUTE;
            seconds = (totalMillisecondsInDay % MILLIS_IN_MINUTE) / 1000;
            milliseconds = totalMillisecondsInDay % 1000;
        }
    }

    @Override
    public int hashCode() {
        return (int) (timeUTC % Integer.MAX_VALUE) * ((isCanonTime) ? 1 : -1);
    }

    @Override
    public String toString() {
        // TODO: 9/29/2021 add represent type (REGEX)
        if (isCanonTime) {
            return "" + getDayOfMonth() + " " + getMonth() + " " + getYear() + " " + getDayOfWeek() + " " + _getHours() + ":" + getMinutes();
        } else {
            //TODO implement
            throw new UnsupportedOperationException("This part of toString method is not implemented yet");
        }
    }

    // TODO: 9/29/2021 REGEX IS BETTER
    /**
     * In which way to represent calendar as a String,
     * with descriptions
     */
    public enum RepresentType {

    }
}
