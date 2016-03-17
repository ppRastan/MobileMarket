package ir.rastanco.mobilemarket.presenter;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import ir.rastanco.mobilemarket.R;

/*
created by parisan
this class created to guide user in fullScreen page
this is visible on first time you enter in activity
means that it appears once when you run application
 */
public class UserGuide extends Activity {

    private ImageButton goToRight;
    private ImageButton goToLeft;
    float x1,x2,y1,y2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_helper);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)

            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        goToLeft = (ImageButton)findViewById(R.id.go_to_left);
        goToRight = (ImageButton)findViewById(R.id.go_to_right);
        goToLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // overridePendingTransition(R.anim.animation_enter,
                 //       R.anim.animation_leave);
                onBackPressed();
            }
        });
        goToRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // overridePendingTransition(R.anim.animation_leave,
                //        R.anim.animation_enter);
                onBackPressed();
            }
        });
    }
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

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