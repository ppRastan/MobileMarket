package ir.rastanco.mobilemarket.utility;
/*
created by parisa this class created to display counter icon on shopping bag
 */

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import ir.rastanco.mobilemarket.R;


public class CounterIconDisplayer {
    public static void setBadgeCount(LayerDrawable icon, int count) {

        ShoppingCounterIconCreator badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof ShoppingCounterIconCreator) {
            badge = (ShoppingCounterIconCreator) reuse;
        } else {
            badge = ShoppingCounterIconCreator.getInstance();
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}