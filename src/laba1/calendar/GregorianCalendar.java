package laba1.calendar;

import org.jetbrains.annotations.NotNull;

import java.util.TimeZone;

public class GregorianCalendar extends Calendar implements Cloneable {

    public GregorianCalendar() {
        this(TimeZone.getDefault());
    }

    public GregorianCalendar(@NotNull TimeZone zone) {
        super(zone);
    }

    /**
     * Is it a daylight time, depending on zone and timeUTC + rawOffset
     * @return number of hours to be added to the time
     */
    private int daylightTimeOffset() {
        // TODO: 10/4/2021 implement +- correctly
        return 1 * MILLIS_IN_HOUR;
    }

    /**
     * For get(type) method
     * @return conventional index of dayOfWeek
     */
    private int _getDayOfWeek() {
        if (firstDayOfWeek == DayOfWeek.Sunday) {
            return getDayOfWeek().getValue() % 7 + 1;
        } else {
            return getDayOfWeek().getValue();
        }
    }
}
