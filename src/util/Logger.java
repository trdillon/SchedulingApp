package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;

public class Logger {

    private static final String LOGFILE = "audit_log.txt";

    public Logger() {}

    public static void log(String username, boolean success) {
        try (FileWriter fw = new FileWriter(LOGFILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
             pw.println(ZonedDateTime.now() + " " + username + " " + (success ? " Successful" : " Failed"));
        }
        catch(IOException e) {
            System.out.println("Logging Error: " + e.getMessage());
        }
    }
}
