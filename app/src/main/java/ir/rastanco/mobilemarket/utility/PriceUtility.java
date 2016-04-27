package ir.rastanco.mobilemarket.utility;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by ParisaRashidhi on 17/03/2016.
 * this class set font numbers and format for whole application
 * this class is singleton
 */

public class PriceUtility {
    private static DecimalFormat formatter;
    private static PriceUtility priceUtility;
    private String defaultFont = "fonts/decoriss_iransans.ttf";
    private String regularFont = "fonts/iransans_mobile_font_regular.ttf";

    public static PriceUtility getInstance() {
        if (priceUtility == null) {
            priceUtility = new PriceUtility();
            formatter = new DecimalFormat("#,###,000");
        }
        return priceUtility;

    }


    public String formatPriceCommaSeparated(int price) {
        String priceInStringFormat = String.valueOf(price);
        double amountOfFinalPrice = Double.parseDouble(priceInStringFormat);
        return String.valueOf(formatter.format(amountOfFinalPrice));
    }

    public TextView changeTextViewFont(TextView textView, Context context) {
        textView.setTypeface(FontHelper.get(context, defaultFont));
        return textView;
    }

    public EditText changeEditTextFont(EditText editText, Context context) {
        editText.setTypeface(FontHelper.get(context, regularFont));
        return editText;
    }

    public Button ChangeButtonFont(Button addToBasketBtn, Context context) {
        addToBasketBtn.setTypeface(FontHelper.get(context, defaultFont));
        return addToBasketBtn;
    }
}

