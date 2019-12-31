package com.belatrix.util.logger;

import static org.junit.Assert.assertTrue;

import com.belatrix.util.App;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Unit test for simple App.
 */
public class AppTest
{

    static Logger logger = Logger.getLogger(AppTest.class.getName());

    @Before
    public void init() {
        try {
            LogManager.getLogManager().readConfiguration(App.class.getClassLoader().getResourceAsStream("myLogger-test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logMessage()
    {
        logger.info("Inicio de sesión");
        logger.warning("Falta habilitar filtros adicionales");
        logger.severe("No se puedo establecer conexión");
        assertTrue( true );
    }
}
