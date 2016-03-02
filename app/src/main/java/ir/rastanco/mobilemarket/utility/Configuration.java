package ir.rastanco.mobilemarket.utility;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

/**
 * Created by ShaisteS on 1394/10/14.
 */
public class Configuration {
    private static Configuration config = new Configuration();
    public static boolean IstheFirtTimeGoingToThisPage;

    public static Configuration getConfig() {
        if (config != null) {
            return config;
        } else return new Configuration();

    }

    public static boolean userLoginStatus;

    public static String homeDisplaySizeForURL;
    public static int homeDisplaySizeForShow;
    public static String shopDisplaySizeForURL;
    public static int shopDisplaySizeForShow;
    public static String productInfoHeightSize;
    public static String articleDisplaySizeForURL;
    public static int articleDisplaySizeForShow;
    public static int progressBarSize;

    public static Context activityContext;
    public static Context MainActivityContext;
    public static Context ProductInfoContext;
    public static Context AplicationContext;
    public static Context UserLoginContext;
    public static Context UserProfileContext;
    public static Context ShopFragmentContext;
    public static Context UserActivity;
    public static Context ManageUserPage;
    public static Context ShoppingBagContext;
    public static Context UserLastShoppingContext;
    public static Context ProductOptionContext;
    public static Context AccountManagerContext;
    public static Context SpecialLoadingContext;

    public static MenuItem UpgradeButtonMenu;

    public static ViewPager UserProfileViewPager;
    public static ViewPager MainPager;

    public static Boolean RTL;
    public static Boolean productTableEmptyStatus; //true is empty and false is full


}
