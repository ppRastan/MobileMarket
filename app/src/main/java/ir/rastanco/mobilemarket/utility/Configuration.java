package ir.rastanco.mobilemarket.utility;


import android.content.Context;

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
    public static Context activityContext;
    public static String homeDisplaySize;
    public static String shopDisplaySize;
    public static String productInfoHeightSize;
    public static String articleDisplaySize;
    public static Context superACFragment;
    public static Context MainActivityFragment;
    public static Context ProductInfoActivity;
    public static Context AplicationCOntext;
    public static String FirstTimeStamp;
}
