package laba1;


import laba1.calendar.Calendar;
import laba1.calendar.GregorianCalendar;

import java.util.TimeZone;

public class Program {

    public static void main(String[] args) {
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        System.out.println(calendar.toString());
    }
}
