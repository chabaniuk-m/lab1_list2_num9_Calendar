package laba1;


import laba1.calendar.Calendar;
import laba1.calendar.GregorianCalendar;
import laba1.calendar.JulianCalendar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.TimeZone;

public class Program {

    public static void main(String[] args) throws InterruptedException {
        Calendar calendar = new GregorianCalendar();
        System.out.println(calendar);
        System.out.println("The year is " + ((calendar.isLeap(calendar.getYear())) ? "leap (366 days)" : "common (365 days)"));

        System.out.println("-------------------------------------");

        Thread.sleep(2_000);

        System.out.println("era = " + Calendar.Era.valueOf(calendar.get(Calendar.ERA)));
        System.out.println("week of year = " + calendar.get(Calendar.WEEK_OF_YEAR));
        System.out.println("week of month = " + calendar.get(Calendar.WEEK_OF_MONTH));
        System.out.println("day of year = " + calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println("seconds = " + calendar.get(Calendar.SECOND));

        Thread.sleep(5_000);

        System.out.println("-------------------------------------");

        System.out.println(" + 41 days");
        Thread.sleep(1_000);
        calendar.add(Calendar.DAY, 41);
        System.out.println(calendar);
        System.out.println("week of year = " + calendar.get(Calendar.WEEK_OF_YEAR));

        Thread.sleep(5_000);

        System.out.println("-------------------------------------");

        System.out.println(" - 73 hours");
        Thread.sleep(1_000);
        calendar.add(Calendar.HOUR, -73);
        System.out.println(calendar);

        Thread.sleep(5_000);

        System.out.println("-------------------------------------");

        System.out.println(" + days to New Year in minutes");
        Thread.sleep(1_000);
        calendar.add(Calendar.MINUTE, 60 * 24 * ((calendar.isLeap(calendar.getYear()) ? Calendar.DAYS_IN_LEAP_YEAR : Calendar.DAYS_IN_COMMON_YEAR) - calendar.get(Calendar.DAY_OF_YEAR)));
        System.out.println(calendar);

        System.out.println("-------------------------------------");

        int year;
        do {
            Thread.sleep(1_000);
            System.out.println("Enter year to check if it is leap (0 - exit): ");
            Scanner scanner = new Scanner(System.in);
            year = scanner.nextInt();
            System.out.println("The year " + year + " is " + ((calendar.isLeap(year)) ? "leap" : "common"));
        } while (year > 0);
    }
}
