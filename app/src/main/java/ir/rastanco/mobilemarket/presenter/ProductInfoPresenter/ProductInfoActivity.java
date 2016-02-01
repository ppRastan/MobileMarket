package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/10/16.
 */
public class ProductInfoActivity extends Activity {

    private ArrayList<Product> allProducts;
    private LayoutInflater inflater;
    private ViewPager viewPager;
    private float y1, y2;
    private MotionEvent touchEvent;
    private RatingBar customRatingBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_product_gallery);
        Configuration.ProductInfoActivity = this;
        Intent intent = this.getIntent();
        Bundle productBundle=new Bundle();
        productBundle=intent.getExtras();
        allProducts=productBundle.getParcelableArrayList("allProduct");
        ServerConnectionHandler sch=new ServerConnectionHandler(Configuration.ProductInfoActivity);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewPager=(ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new FullScreenImageAdapter(this, allProducts, allProducts.size()));
        viewPager.setCurrentItem(intent.getIntExtra("position", 0));
        customRatingBar = (RatingBar)findViewById(R.id.rank_of_product);

    }

    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                y2 = touchevent.getY();

                if (y1 < y2)
                {
//                    Intent intentProductInfo = new Intent(viewLayout.getContext(),ProductOptionActivity.class);
//                    intentProductInfo.putExtra("productId", products.get(position).getId());
//                    intentProductInfo.putExtra("groupId", products.get(position).getGroupId());
//                    viewLayout.getContext().startActivity(intentProductInfo);
                    Toast.makeText(getApplicationContext(),"swipeDown",Toast.LENGTH_LONG).show();
                }

                if (y1 > y2)
                {
                    Toast.makeText(getApplicationContext(),"swipeDown",Toast.LENGTH_LONG).show();

                }

                break;
            }
        }
        return false;
    }
}

