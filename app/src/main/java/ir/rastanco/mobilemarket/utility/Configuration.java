package ir.rastanco.mobilemarket.utility;


import android.content.Context;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Product;

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
    public static String homeDisplaySize;
    public static String shopDisplaySize;
    public static Context superACFragment;
    public static Context MainActivityFragment;
    public static Context ProductInfoActivity;
}
