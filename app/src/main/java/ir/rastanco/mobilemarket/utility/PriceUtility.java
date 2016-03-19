package ir.rastanco.mobilemarket.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by ParisaRashidhi on 17/03/2016.
 * this class set font numbers and formatt for whole application
 */
public class PriceUtility {


    public String formatPriceCommaSeprated(int price){
        String priceInStringFormat = String.valueOf(price);
        double amountOfFinalPrice = Double.parseDouble(priceInStringFormat);
        DecimalFormat formatter = new DecimalFormat("#,###,000");
        String  finalPriceWithComma = String.valueOf(formatter.format(amountOfFinalPrice));
        return finalPriceWithComma;
            }

         public TextView changeFontToYekan(TextView textView , Context context){
         textView.setTypeface(FontHelper.get(context,"fonts/yekan.ttf"));
         return textView;
    }


    public Button ChangeButtonFont(Button addToBasketBtn, Context context) {
        addToBasketBtn.setTypeface(FontHelper.get(context,"fonts/yekan.ttf"));
        return addToBasketBtn;
    }
}

