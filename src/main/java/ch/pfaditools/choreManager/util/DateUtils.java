package ch.pfaditools.choreManager.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String formatDateShort(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd.MM"));
    }
}
