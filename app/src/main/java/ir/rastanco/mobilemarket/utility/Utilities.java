package ir.rastanco.mobilemarket.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.MemoryCache;

/**
 * Created by ShaisteS on 1395/1/8.
 * This class include utility Method
 */
public class Utilities {

    private final int fiveMillion = 5000000;
    private final int tenMillion=10000000;
    private final int overTenMillion = 10000001;
    private final int numberOfArticlesAfterRefresh = 100;
    private final int atLeastArticleInFirstTime = 25;
    private final int calculatePercent = 100;
    private final String imageQuality = "30";
    private static Utilities utility = new Utilities();


    public static Utilities getInstance() {
        if (utility != null) {
            return utility;
        } else return new Utilities();
    }

    public ArrayList<String> getPriceFilterTitle() {
        ArrayList<String> priceFilter = new ArrayList<>();
        priceFilter.add(Configuration.getConfig().mainActivityContext.getResources().getString(R.string.up_to1million));
        priceFilter.add(Configuration.getConfig().mainActivityContext.getResources().getString(R.string.up_to5million));
        priceFilter.add(Configuration.getConfig().mainActivityContext.getResources().getString(R.string.up_to10million));
        priceFilter.add(Configuration.getConfig().mainActivityContext.getResources().getString(R.string.over10million));
        return priceFilter;
    }

    public int convertPriceTitleToInt(String priceTitle) {
        int price;
        int oneMillion = 1000000;
        if (priceTitle.equals(Configuration.getConfig().mainActivityContext.getResources().getString(R.string.up_to1million)))
            price = oneMillion;
        else if (priceTitle.equals(Configuration.getConfig().mainActivityContext.getResources().getString(R.string.up_to5million)))
            price = fiveMillion;
        else if (priceTitle.equals(Configuration.getConfig().mainActivityContext.getResources().getString(R.string.up_to10million)))
            price = tenMillion;
        else
            price = overTenMillion; //1 is sign for price is above
        return price;
    }

    public int getAtLeastHighestPrice() {
        return overTenMillion;
    }

    public int getStartArticleNumber() {
        return 0;
    }

    public int getAtLeastArticleInFirstTime() {
        return atLeastArticleInFirstTime;
    }

    public int getArticleNumberWhenRefresh() {
        return numberOfArticlesAfterRefresh;
    }

    public String getImageQuality() {
        return imageQuality;
    }

    public int calculatePriceOffProduct(int price, int priceOff) {
        int off = (price * priceOff) / calculatePercent;
        return price - off;
    }


    public Drawable ResizeImage(int imageID, Context context, int deviceWidth) {
        MemoryCache memoryCache = new MemoryCache();
        try{
            BitmapDrawable bd = (BitmapDrawable) ContextCompat.getDrawable(context, imageID);
            double imageHeight = bd.getBitmap().getHeight();
            double imageWidth = bd.getBitmap().getWidth();

            double ratio = deviceWidth / imageWidth;
            int newImageHeight = (int) (imageHeight * ratio);

            Bitmap bMap = BitmapFactory.decodeResource(context.getResources(), imageID);
            return new BitmapDrawable(context.getResources(), getResizedBitmap(bMap, newImageHeight, deviceWidth));

        }catch (OutOfMemoryError e){
                memoryCache.clear();
            return null;
        }
    }

    //Resize Bitmap
    private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public  void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            //Log.e(TabbedActivity.TAG, "Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }

    public String deleteSpaceFromUrl(String url){
        url=url.replace("roots/cube.php?code=","")
        .replace("+%28","%20(").replace("%29",")").replace("%2F","/");
        return url;
    }
}
