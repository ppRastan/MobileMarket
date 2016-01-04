package ir.rastanco.mobilemarket.utility;

/**
 * Created by ShaisteS on 1394/10/14.
 */
public class Configuration {
    private static Configuration config = new Configuration();

    public static Configuration getConfig() {
        if (config != null) {
            return config;
        } else return new Configuration();

    }
    public static String widthDisplay;
    public static String heightDisplay;
}
