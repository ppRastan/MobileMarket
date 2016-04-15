package ir.rastanco.mobilemarket.utility;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by ParisaRashidhi on 15/04/2016.
 */
public class ColorUtility {

    private final static ColorUtility config = new ColorUtility();

    public static ColorUtility getConfig() {
        if (config != null) {
            return config;
        } else return new ColorUtility();
    }

    public  void setColorOfSwipeRefresh(SwipeRefreshLayout mSwipeRefreshLayout){
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.RED);
    }
}
