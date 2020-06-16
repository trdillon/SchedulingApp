package util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateFormatter {

    public static String getDTFNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault());

        Instant instant = Instant.now();
        String DTF = formatter.format(instant);

        return DTF;
    }
}