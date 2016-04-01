package ir.rastanco.mobilemarket.utility;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

/**
 * Created by ShaisteS on 1394/10/14.
 * A Singleton Class For Access Static Parameter
 */
public class Configuration {
    private final static Configuration config = new Configuration();
    public static Configuration getConfig() {
        if (config != null) {
            return config;
        }
        else return new Configuration();
    }

    public  boolean userLoginStatus;
    public  boolean isTheFirstTimeOpeningThisPage;
    public  String homeDisplaySizeForURL;
    public  int homeDisplaySizeForShow;
    public  String shopDisplaySizeForURL;
    public  int shopDisplaySizeForShow;
    public  String productInfoHeightForURL;
    public  int productInfoHeightForShow;
    public  String articleDisplaySizeForURL;
    public  int articleDisplaySizeForShow;
    public  int progressBarSize;

    public  Context mainActivityContext;
    public  Context productInfoContext;
    public  Context ApplicationContext;
    public  Context UserLoginContext;
    public  Context UserProfileContext;
    public  Context ShopFragmentContext;
    public  Context ShoppingBagContext;
    public  Context UserLastShoppingContext;
    public  Context ProductOptionContext;
    public  Context accountManagerContext;
    public  Context specialProductManagementContext;
    public  MenuItem upgradeButtonMenu;
    public  ViewPager mainPager;
    public  Boolean RTL;
    public  Boolean productTableEmptyStatus; //true is empty and false is full
    public  Boolean connectionStatus = false;
    public  int mainTabCount;
    public  FloatingActionButton telephoneFloatingActionButton;
    public  String FilterCategoryTitle;
    public  int FilterCategoryId;
    public  String FilterOption;
    public  String FilterPriceTitle;
    public  String FilterBrand;
    public  String FilterAll;

}
