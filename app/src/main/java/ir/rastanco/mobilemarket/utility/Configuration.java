package ir.rastanco.mobilemarket.utility;


import android.content.Context;
import android.support.v4.view.ViewPager;

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
    public static String productInfoHeightSize;
    public static String articleDisplaySize;

    public static Context activityContext;
    public static Context superACFragment;
    public static Context MainActivityFragment;
    public static Context ProductInfoActivity;
    public static Context AplicationCOntext;
    public static Context UserLoginFragment;
    public static Context UserProfileFragment;
    public static Context UserActivity;
    public static Context ShoppingBagActivity;
    public static Context UserAccountFragment;
    public static Context ProductOptionFragment;

    public static ViewPager UserProfileViewPager;
    public static ViewPager MainPager;

    public static Boolean RTL;


}
