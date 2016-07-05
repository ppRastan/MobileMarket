package ir.rastanco.mobilemarket.utility;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

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


    public Boolean userLoginStatus=false;
    public Boolean RTL=false;
    public Boolean emptyProductTable = false; //true is empty and false is full
    public Boolean emptyCategoryTable = false;//true is empty and false is full
    public Boolean connectionStatus = false;
    public Boolean filterCategoryDialogShowStatus = false; //false= no show dialog and true= show dialog


    public Integer filterCategoryId = 0; //It is important that filterCategoryId=0
    public Integer homeDisplaySizeForShow = 0;//solve exception java.lang.RuntimeException: Unable to start activity
    public Integer shopDisplaySizeForShow = 0;//solve exception java.lang.RuntimeException: Unable to start activity
    public Integer productInfoHeightForShow = 0;//solve exception java.lang.RuntimeException: Unable to start activity
    public Integer articleDisplaySizeForShow = 0;//solve exception java.lang.RuntimeException: Unable to start activity
    public Integer progressBarSize;
    public Integer mainTabCount;
    public Integer firstIndexGetProduct;
    public Integer numberAllProducts;
    public Integer numberOfProductMustBeTaken =100;
    public Integer someOfFewProductNumberForGetEveryTab =10;
    public Integer someOfFewSpecialProductNumber=30;

    public int requestIdForGetCategoryInformationInFirstInstall=101;


    public ImageButton upgradeButtonMenu;
    public ViewPager mainPager;
    public FloatingActionButton customerSupportFloatingActionButton;
    public View mainActivityView;


}
