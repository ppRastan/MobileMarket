package ir.rastanco.mobilemarket.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by ParisaRashidhi on 18/01/2016.
 */
public class MyCustomTextView extends TextView {
    public MyCustomTextView(Context context) {
        super(context);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/yekan_font.ttf"));

    }
}
