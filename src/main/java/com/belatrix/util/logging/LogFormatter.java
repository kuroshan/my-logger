package com.belatrix.util.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    DateFormat dateFormat;

    public LogFormatter() {
        dateFormat = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss,SSS");
    }

    @Override
    public String format(LogRecord logRecord) {
        String level = null;

        if (logRecord.getLevel() == Level.INFO) {
            level = "INFO";
        } else if (logRecord.getLevel() == Level.WARNING) {
            level = "WARNING";
        } else if (logRecord.getLevel() == Level.SEVERE) {
            level = "SEVERE";
        } else {
            level = "MESSAGE";
        }

        return String.format("%s | %s %s.%s | %s%n",
                level,
                dateFormat.format(new Date(logRecord.getMillis())),
                logRecord.getSourceClassName(),
                logRecord.getSourceMethodName(),
                logRecord.getMessage());
    }
}
