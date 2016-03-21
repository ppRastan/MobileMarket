package ir.rastanco.mobilemarket.utility;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by ParisaRashidhi on 17/03/2016.
 * this class set font numbers and formatt for whole application
 * this class is singletone
 */

   public class PriceUtility{
          private String priceInStringFormat;
          private double amountOfFinalPrice;
          private String  finalPriceWithComma;
          private DecimalFormat formatter;
          private static PriceUtility priceUtility;
              public static PriceUtility getInstance() {
        if(priceUtility == null)
        {
        priceUtility = new PriceUtility();

        }
        return priceUtility;

              }


    public String formatPriceCommaSeprated(int price)
    {
        priceInStringFormat = String.valueOf(price);
        amountOfFinalPrice = Double.parseDouble(priceInStringFormat);
        formatter = new DecimalFormat("#,###,000");
        finalPriceWithComma = String.valueOf(formatter.format(amountOfFinalPrice));
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

