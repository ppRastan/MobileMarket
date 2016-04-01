package ir.rastanco.mobilemarket.utility;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/**
 * Created by ParisaRashidhi on 22/01/2016.
 * this class declares how we can have a circle paint above shopping bag using paint class
 */
public class ShoppingCounterIconCreator extends Drawable {

    private final Paint mBadgePaint;
    private final Paint mTextPaint;
    private final Rect mTxtRect = new Rect();
    private String mCount = "";
    private boolean mWillDraw = false;
    private final static ShoppingCounterIconCreator shoppingCounterIconCreator = new ShoppingCounterIconCreator();

    public static ShoppingCounterIconCreator getInstance() {
        if (shoppingCounterIconCreator != null) {
            return shoppingCounterIconCreator;
        } else return new ShoppingCounterIconCreator();
    }

    private ShoppingCounterIconCreator() {
        //سایز عدد روی سبد خرید را مشخص میکند
        float mTextSize = 24F;
        mBadgePaint = new Paint();
        //رنگ دایره ی سبد خرید را مشخص میکند که فعلا سبز است
        mBadgePaint.setColor(Color.parseColor("#31a140"));
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);
        mTextPaint = new Paint();
        //رنگ عدد داخل دایره ی روی سبد خرید را مشخص میکند که فعلا سفید است
        mTextPaint.setColor(Color.WHITE);
        //ضخامت عدد نوشته شده توی دایره ی سبد خرید را مشخص میکند
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mWillDraw) {
            return;
        }

        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        // Position the badge in the top-right quadrant of the icon.
        float radius = ((Math.min(width, height) / 2) - 1) / 2;
        float centerX = width - radius - 1;
        float centerY = radius + 1;

        // Draw badge circle.
        canvas.drawCircle(centerX, centerY, radius, mBadgePaint);

        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
        float textHeight = mTxtRect.bottom - mTxtRect.top;
        float textY = centerY + (textHeight / 2f);
        canvas.drawText(mCount, centerX, textY, mTextPaint);
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    public void setCount(int count) {
        mCount = Integer.toString(count);

        // Only draw a badge if there are notifications.
        mWillDraw = count > 0;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
