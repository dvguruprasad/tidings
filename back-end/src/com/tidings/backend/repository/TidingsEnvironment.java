package com.tidings.backend.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TidingsEnvironment {
    public static final String TIDINGS_ENVIRONMENT = "TIDINGS_ENVIRONMENT";
    public static final String DEFAULT_ENVIRONMENT = "development";

    private DBConfig dbConfig;
    private static TidingsEnvironment instance;

    private TidingsEnvironment(DBConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public static TidingsEnvironment getInstance() {
        if (instance != null)
            return instance;
        HashMap<String, String> db_connection_properties = new HashMap<String, String>();
        Properties props = new Properties();
        FileInputStream in =
                null;
        try {
            System.out.println("Using " + environment() + ".properties.");
            in = new FileInputStream(environment() + ".properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DBConfig config = null;
        try {
            props.load(in);
            config = new DBConfig(props);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TidingsEnvironment(config);
    }

    private static String environment() {
        Map<String, String> environmentVariables = System.getenv();
        return environmentVariables.containsKey(TIDINGS_ENVIRONMENT) ? environmentVariables.get(TIDINGS_ENVIRONMENT) : DEFAULT_ENVIRONMENT;
    }

    public DBConfig dbConfig() {
        return dbConfig;
    }

    public static class DBConfig {
        private HashMap<String, String> db_connection_properties = new HashMap<String, String>();

        public DBConfig(Properties props) {
            db_connection_properties.put("host", props.getProperty("host"));
            System.out.println("host: " + props.getProperty("host"));

            db_connection_properties.put("port", props.getProperty("port"));
            System.out.println("port: " + props.getProperty("port"));

            db_connection_properties.put("database", props.getProperty("database"));
            System.out.println("database: " + props.getProperty("database"));
        }

        public String host() {
            return db_connection_properties.get("host");
        }

        public int port() {
            return Integer.parseInt(db_connection_properties.get("port"));
        }

        public String database() {
            return db_connection_properties.get("database");
        }
    }
}