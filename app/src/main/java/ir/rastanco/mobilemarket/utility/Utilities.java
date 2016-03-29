package ir.rastanco.mobilemarket.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;

/**
 * Created by ShaisteS on 1395/1/8.
 * This class include utility Method
 */
public class Utilities {

    private int oneMillion = 1000000 ;
    private int fiveMillion = 5000000 ;
    private int overOneMillion = 10000001 ;
    private static Utilities utility = new Utilities();

    public static Utilities getInstance() {
        if (utility != null) {
            return utility;
        }
        else return new Utilities();
    }

    public ArrayList<String> getPriceFilterTitle(){
        ArrayList<String> priceFilter = new ArrayList<String>();
        priceFilter.add(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto1million));
        priceFilter.add(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto5million));
        priceFilter.add(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto10million));
        priceFilter.add(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.over10million));
        return priceFilter;
    }
    public int convertPriceTitleToInt(String priceTitle){
        int price=0;
        if(priceTitle.equals(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto1million)))
            price= oneMillion ;
        else if (priceTitle.equals(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto5million)))
            price=fiveMillion ;
        else if (priceTitle.equals(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto10million)))
            price=getAtLeastHighestPrice();
        else
            price=overOneMillion ; //1 is sign for price is above
        return price;
    }
    public int getAtLeastHighestPrice(){
        return oneMillion;
    }
    public int getStartArticleNumber(){return 0;}
    public int getAtLeastArticleInFirstTime(){return 25;}
    public int getArticleNumberWhenRefresh(){return 100;}
    public String getImageQuality(){
        String quality="30";
        return quality;
    }
    public int calculatePriceOffProduct(int price,int priceOff){
        int off=(price*priceOff)/100;
        int priceForYou=price-off;
        return priceForYou;
    }


    public Drawable ResizeImage (int imageID,Context context,int deviceWidth) {

        BitmapDrawable bd=(BitmapDrawable) context.getResources().getDrawable(imageID);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(context.getResources(), imageID);
        Drawable drawable = new BitmapDrawable(context.getResources(),getResizedBitmap(bMap,newImageHeight,(int) deviceWidth));

        return drawable;
    }
    //Resize Bitmap
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }



}
