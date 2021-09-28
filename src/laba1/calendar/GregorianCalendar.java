package laba1.calendar;

public class GregorianCalendar extends Calendar implements Cloneable {

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
