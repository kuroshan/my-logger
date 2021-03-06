package com.belatrix.util.logging;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFilter implements Filter {

    @Override
    public boolean isLoggable(LogRecord logRecord) {
        Level level = logRecord.getLevel();
        return level == Level.INFO || level == Level.WARNING || level == Level.SEVERE;
    }
}
