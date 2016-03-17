package ir.rastanco.mobilemarket.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by ParisaRashidhi on 17/03/2016.
 * this class set font numbers and formatt for whole application
 */
public class PriceUtility {

   private Typeface font;

    public String formatPriceCommaSeprated(String persionPriceWithoutFormatt){
        double amountOfFinalPrice = Double.parseDouble(persionPriceWithoutFormatt);
        DecimalFormat formatter = new DecimalFormat("#,###,000");
        String  finalPriceWithComma = String.valueOf(formatter.format(amountOfFinalPrice));
        return finalPriceWithComma;
            }

         public TextView changeFontToYekan(TextView textView , Activity activity){
         font= Typeface.createFromAsset(activity.getAssets(), "fonts/yekan.ttf");
         textView.setTypeface(font);
         return textView;
    }

    public TextView lineDisplayerthroughNumber(TextView textViewWithLine , String price){
        textViewWithLine.setPaintFlags(textViewWithLine.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        return textViewWithLine;
    }
}

