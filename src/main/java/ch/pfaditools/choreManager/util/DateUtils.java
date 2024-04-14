package ch.pfaditools.choreManager.util;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {

    public static String formatDateShort(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd.MM"));
    }

    public static String formatDateWeekday(LocalDate date) {
        DateFormatSymbols symbols = new DateFormatSymbols(new Locale("de"));
        String[] dayNames = symbols.getWeekdays();
        int weekDayOrdinal = date.getDayOfWeek().ordinal();
        // Sunday
        if (weekDayOrdinal == 6) {
           return dayNames[1];
        }
        return dayNames[weekDayOrdinal + 2];
    }
}
