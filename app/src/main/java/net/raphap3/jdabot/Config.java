package net.raphap3.jdabot;

public class Config {

//    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return System.getenv(key.toUpperCase());
    }

}
