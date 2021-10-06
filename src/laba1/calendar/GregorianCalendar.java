package laba1.calendar;

import org.jetbrains.annotations.NotNull;

import java.util.TimeZone;

/**
 * Represents Gregorian calendar - the most used calendar nowadays
 */
public class GregorianCalendar extends Calendar {

    /**
     * Uses default time zone and current time
     */
    public GregorianCalendar() {
        this(TimeZone.getDefault());
    }

    /**
     * Uses current time
     * @param zone time zone
     */
    public GregorianCalendar(@NotNull TimeZone zone) {
        super(zone);
    }

    /**
     * Uses default time zone, but can be changed using set
     * @param time time in milliseconds
     * @param isCanon true milliseconds is after 1 Jan 1970,
     *                false if earlier (time must be after Big Bang)
     */
    public GregorianCalendar(long time, boolean isCanon) {
        isCanonTime = isCanon;
        initZone(TimeZone.getDefault());
        update(time);
    }

    @Override
    public boolean isLeap(long year) {
        return Calendar.isLeapYear(year);
    }

    @Override
    public void setSundayFirstDayOfWeek(boolean sunday) {
        firstDayOfWeek = sunday ? DayOfWeek.Sunday : DayOfWeek.Monday;
    }

    /**
     * For get(type) method
     * @return conventional index of dayOfWeek
     */
    protected int _getDayOfWeek() {
        if (firstDayOfWeek == DayOfWeek.Sunday) {
            return getDayOfWeek().getValue() % 7 + 1;
        } else {
            return getDayOfWeek().getValue();
        }
    }

    @Override
    public Calendar clone() {
        return new GregorianCalendar(timeUTC, isCanonTime);
    }
}
