package laba1.calendar;

import org.jetbrains.annotations.NotNull;

import java.util.TimeZone;

/**
 * Simpler than GregorianCalendar.
 * The main difference is realization of method isLeap
 */
public class JulianCalendar extends Calendar {

    /**
     * Uses default time zone and current time
     */
    public JulianCalendar() {
        this(TimeZone.getDefault());
    }

    /**
     * Uses current time
     * @param zone time zone
     */
    public JulianCalendar(@NotNull TimeZone zone) {
        super(zone);
    }

    /**
     * Uses default time zone, but can be changed using set
     * @param time time in milliseconds
     * @param isCanon true milliseconds is after 1 Jan 1970,
     *                false if earlier (time must be after Big Bang)
     */
    public JulianCalendar(long time, boolean isCanon) {
        isCanonTime = isCanon;
        initZone(TimeZone.getDefault());
        update(time);
    }

    @Override
    protected void update() {
        update(System.currentTimeMillis() + rawOffset + ((isCanonTime) ? 0 : MILLIS_FROM_BIN_BANG) + 13L * MILLIS_IN_DAY);
    }

    @Override
    public boolean isLeap(long year) {
        return year % 4 == 0;
    }

    @Override
    public void setSundayFirstDayOfWeek(boolean sunday) {
        if (!sunday) return;

        throw new UnsupportedOperationException("JulianCalendar does not support changing the first day of week");
    }

    @Override
    protected int _getDayOfWeek() {
        return getDayOfWeek().getValue();
    }

    @Override
    public Calendar clone() {
        return new JulianCalendar(timeUTC, isCanonTime);
    }
}
