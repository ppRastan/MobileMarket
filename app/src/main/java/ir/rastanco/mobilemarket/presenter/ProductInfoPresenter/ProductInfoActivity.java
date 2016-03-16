package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.FullScreenPageUserGuider;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/10/16.
 **/
public class ProductInfoActivity extends Activity {

    private ArrayList<Product> allProducts;
    private LayoutInflater inflater;
    private ViewPager viewPager;
    private Intent intent;
    private Bundle productBundle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_product_gallery);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }*/
        Configuration.ProductInfoContext = this;
        intent = this.getIntent();
        productBundle = new Bundle();
        productBundle = intent.getExtras();
        allProducts = productBundle.getParcelableArrayList("allProduct");
        ServerConnectionHandler sch = new ServerConnectionHandler(Configuration.ProductInfoContext);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new FullScreenImageAdapter(this, allProducts, allProducts.size()));
        viewPager.setCurrentItem(intent.getIntExtra("position", 0));
        if (Configuration.IstheFirtTimeGoingToThisPage)
            startActivity(new Intent(ProductInfoActivity.this, FullScreenPageUserGuider.class));
    }

    @Override
    public void onBackPressed() {
        Configuration.IstheFirtTimeGoingToThisPage = false;
        finish();
    }
}

