package com.belatrix.util.logger;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Unit test for simple App.
 */
public class AppTest
{

    static Logger logger = Logger.getLogger(AppTest.class.getName());

    @BeforeClass
    public static void init() throws SQLException, IOException {
        LogManager.getLogManager().readConfiguration(AppTest.class.getClassLoader().getResourceAsStream("myLogger-test.properties"));
        initDatabase();
    }

    @Test
    public void logMessage()
    {
        logger.info("Inicio de sesión");
        logger.warning("Falta habilitar filtros adicionales");
        logger.severe("No se pudo establecer conexión con el servidor");
        assertTrue( true );
    }

    @AfterClass
    public static void destroy() throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery("SELECT message, level FROM Log_Values");
            if (rs.next()) {
                System.out.println(String.format("[ database:row -> %s ]", rs.getString("message")));
            }
            rs.close();
            statement.executeUpdate("DROP TABLE Log_Values");
            connection.commit();
        }
    }

    private static void initDatabase() throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.execute("CREATE TABLE Log_Values (message VARCHAR(300), level INT)");
            connection.commit();
        }
    }

    private static Connection getConnection() throws SQLException {
        String className = "com.belatrix.util.logging.DatabaseHandler";
        String user = LogManager.getLogManager().getProperty(className + ".user");
        String password = LogManager.getLogManager().getProperty(className + ".password");

        Properties connectionProps = new Properties();
        connectionProps.put("user", user);
        connectionProps.put("password", password);

        return DriverManager.getConnection("jdbc:hsqldb:mem:logger", connectionProps);

    }
}
