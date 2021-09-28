package laba1;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Program {

    public static void main(String[] args) {
        System.out.println(choice(1));
        System.out.println(choice(2));
        System.out.println(choice(3));
        Calendar calendar = new GregorianCalendar()
    }

    public static String choice(int i) {
        return switch(i) {
            case 1 -> "January";
            case 2 -> "February";
            default -> {throw new IllegalArgumentException("We don't know month with id - " + i);}
        };
    }
}
