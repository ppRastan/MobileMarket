package ir.rastanco.mobilemarket.presenter;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import ir.rastanco.mobilemarket.R;

/*
created by parisa
this class created to guide user in fullScreen page
this is visible on first time you enter in activity
means that it appears once when you run application
 */
public class FullScreenPAgeUserGuider extends Activity {

    float x1,x2,y1,y2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_helper);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)

            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

    }
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();

                if (x1 < x2)
                {

                    super.onBackPressed();

                }

                // if right to left sweep event on screen
                if (x1 > x2)
                {

                    super.onBackPressed();
                }

                // if UP to Down sweep event on screen
                if (y1 < y2)
                {

                    super.onBackPressed();
                }

                if (y1 > y2)
                {

                    super.onBackPressed();
                }
                break;
            }
        }
        return false;
    }

}
