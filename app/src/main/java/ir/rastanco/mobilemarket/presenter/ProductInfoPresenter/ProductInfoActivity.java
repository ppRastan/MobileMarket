package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.presenter.FullScreenPAgeUserGuider;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/10/16.
 * A Activity For show Product Information
 **/
public class ProductInfoActivity extends Activity {
    private String isItFirstTimeRunningApplication = "firstTime";
    private String allProduct = "allProduct";
    private String position = "position";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_product_gallery);
        Configuration.getConfig().productInfoActivityContext = this;
        Intent intent = this.getIntent();
        Bundle productBundle = intent.getExtras();
        ArrayList<Product> allProducts = productBundle.getParcelableArrayList(allProduct);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new FullScreenImageAdapter(this, allProducts, allProducts != null ? allProducts.size() : 0));
        viewPager.setCurrentItem(intent.getIntExtra(position, 0));
        //if it is the first time running this application i will show guide page else just display product page
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean(isItFirstTimeRunningApplication, false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(isItFirstTimeRunningApplication, true);
            startActivity(new Intent(ProductInfoActivity.this, FullScreenPAgeUserGuider.class));
            editor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

