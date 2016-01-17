package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ShaisteS on 1394/10/16.
 */
public class ProductInfoActivity extends Activity {

    private ArrayList<Product> allProducts;
    private LayoutInflater inflater;
    private ViewPager viewPager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_product_gallery);
        Configuration.ProductInfoActivity = this;

        Intent intent = this.getIntent();
        /*Bundle bundle = intent.getExtras();
        allProducts = (ArrayList<Product>) bundle.getSerializable("allProducts");*/
        ServerConnectionHandler sch=new ServerConnectionHandler(Configuration.ProductInfoActivity);
        allProducts=sch.getAllProductFromTable();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewPager=(ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new FullScreenImageAdapter(this,allProducts));
        viewPager.setCurrentItem(intent.getIntExtra("position",0));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}

