package ir.rastanco.mobilemarket.utility;
/*
created by parisan this class created for displaing counter icon on shopping bag
 */
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import ir.rastanco.mobilemarket.R;


public class CounterIconDisplayer {
  public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

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