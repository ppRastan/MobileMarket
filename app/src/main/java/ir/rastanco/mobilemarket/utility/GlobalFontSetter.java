package ir.rastanco.mobilemarket.utility;

/*
created by parisa
this class created to set whole application font
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

public final class GlobalFontSetter {

    public static void setDefaultFont(Context context, String fontAssetName) {

        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont("MONOSPACE", regular);
    }

    private static void replaceFont(String staticTypefaceFieldName,
                                    final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
            Log.v("can not set this font", "!");
        }
    }
}
