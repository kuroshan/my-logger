package com.belatrix.util;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    static Logger logger = Logger.getLogger(App.class.getName());

    static {
        try {
            LogManager.getLogManager().readConfiguration(App.class.getClassLoader().getResourceAsStream("myLogger.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args )
    {
        logger.info("Inicio de sesión");
        logger.warning("Falta habilitar filtros adicionales");
        logger.severe("No se puedo establecer conexión");
    }
}

