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
        } else return new Configuration();
    }


    public Context mainActivityContext;
    public Context productInfoActivityContext;
    public Context applicationContext;
    public Context userLoginActivityContext;
    public Context userProfileActivityContext;
    public Context shopFragmentContext;
    public Context userLastShoppingActivityContext;
    public Context productOptionActivityContext;
    public Context accountManagerContext;
    public Context specialProductManagementContext;


    public String filterOption;
    public String filterPriceTitle;
    public String filterBrand;
    public String filterAll;
    public String productInfoHeightForURL;
    public String homeDisplaySizeForURL;
    public String shopDisplaySizeForURL;
    public String articleDisplaySizeForURL;
    public String filterCategoryTitle;


    public Boolean userLoginStatus;
    public Boolean RTL;
    public Boolean emptyProductTable; //true is empty and false is full
    public Boolean emptyCategoryTable;//true is empty and false is full
    public Boolean connectionStatus = false;


    public Integer filterCategoryId = 0; //It is important that filterCategoryId=0
    public Integer homeDisplaySizeForShow;
    public Integer shopDisplaySizeForShow;
    public Integer productInfoHeightForShow;
    public Integer articleDisplaySizeForShow;
    public Integer progressBarSize;
    public Integer mainTabCount;
    public Integer firstIndexGetProduct;
    public Integer numberAllProducts;
    public Integer numberOfProductMustBeTaken = 100;


    public MenuItem upgradeButtonMenu;
    public ViewPager mainPager;
    public FloatingActionButton customerSupportFloatingActionButton;


}
