package ir.rastanco.mobilemarket.utility;

/**
 * Created by ShaisteS on 1394/12/27.
 * A Singleton Class for Access Static parameter when filtering Product Information
 */
public class DataFilter {

    private static DataFilter data = new DataFilter();

    public static DataFilter getConfig() {
        if (data != null) {
            return data;
        } else return new DataFilter();
    }
    public static String FilterCategoryTitle;
    public static int FilterCategoryId;
    public static String FilterOption;
    public static String FilterPriceTitle;
    public static String FilterBrand;
    public static String FilterAll;
}
