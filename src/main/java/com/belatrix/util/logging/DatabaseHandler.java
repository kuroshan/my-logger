package com.belatrix.util.logging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.*;

public class DatabaseHandler extends StreamHandler {

    private LogManager logManager;
    private Connection connection;
    private String sql;
    private ConsoleHandler c;

    public DatabaseHandler() throws SQLException {
        this.configure();
    }

    @Override
    public void publish(LogRecord logRecord) {
        Filter filter = this.getFilter();
        Level logLevel = this.getLevel();
        Level level = logRecord.getLevel();

        if (!filter.isLoggable(logRecord)) {
            return;
        }

        if (logLevel == Level.SEVERE) {
            if (level == Level.INFO || level == Level.WARNING){
                return;
            }
        } else if (logLevel == Level.WARNING) {
            if (level == Level.INFO) {
                return;
            }
        } else if (logLevel != Level.INFO) {
            return;
        }

        executeSql(logRecord);
    }

    @Override
    public void close() throws SecurityException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("connection closing error");
            }
        }
    }

    private void configure() throws SQLException {
        logManager = LogManager.getLogManager();
        String className = this.getClass().getName();
        connection = createConnection(className);
        this.setLevel(getLevelProperty(className + ".level", Level.ALL));
        this.setFilter(getFilterProperty(className + ".filter", (Filter)null));
        this.setFormatter(getFormatterProperty(className + ".formatter", new XMLFormatter()));
    }

    Filter getFilterProperty(String attribute, Filter filterDefault) {
        String classNameFilter = logManager.getProperty(attribute);

        try {
            if (classNameFilter != null) {
                Class classFilter = ClassLoader.getSystemClassLoader().loadClass(classNameFilter);
                return (Filter)classFilter.newInstance();
            }
        } catch (Exception var5) {
        }

        return filterDefault;
    }

    Formatter getFormatterProperty(String attribute, Formatter formatterDefault) {
        String classNameFormatter = logManager.getProperty(attribute);

        try {
            if (classNameFormatter != null) {
                Class classFormatter = ClassLoader.getSystemClassLoader().loadClass(classNameFormatter);
                return (Formatter)classFormatter.newInstance();
            }
        } catch (Exception var5) {
        }

        return formatterDefault;
    }

    Level getLevelProperty(String attribute, Level levelDefault) {
        String classNameLevel = logManager.getProperty(attribute);
        if (classNameLevel == null) {
            return levelDefault;
        } else {
            Level level = null;
            if (classNameLevel.equals("INFO")) {
                level = Level.INFO;
            } else if (classNameLevel.equals("WARNING")) {
                level = Level.WARNING;
            } else if (classNameLevel.equals("SEVERE")) {
                level = Level.SEVERE;
            }

            return level != null ? level : levelDefault;
        }
    }

    Connection createConnection(String className) throws SQLException {

        String driver = logManager.getProperty(className + ".driver");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String user = logManager.getProperty(className + ".user");
        String password = logManager.getProperty(className + ".password");
        String URL = logManager.getProperty(className + ".URL");
        sql = logManager.getProperty(className + ".sql");

        Properties connectionProps = new Properties();
        connectionProps.put("user", user);
        connectionProps.put("password", password);

        Connection connection = DriverManager.getConnection(URL, connectionProps);

        return connection;
    }

    void executeSql(LogRecord logRecord) {
        int t = 0;

        Level level = logRecord.getLevel();

        if (level == Level.INFO) {
            t = 1;
        }

        if (level == Level.WARNING) {
            t = 2;
        }

        if (level == Level.SEVERE) {
            t = 3;
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            Formatter formatter = this.getFormatter();
            String msg = formatter.format(logRecord).replaceAll("\n", "");
            ps.setString(1, msg);
            ps.setInt(2, t);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("database error execute prepare statement");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException("database error close prepare statement");
                }
            }
        }
    }

}