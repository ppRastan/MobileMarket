package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_product_gallery);
        Configuration.getConfig().productInfoContext = this;
        Intent intent = this.getIntent();
        Bundle productBundle = new Bundle();
        productBundle = intent.getExtras();
        ArrayList<Product> allProducts = productBundle.getParcelableArrayList("allProduct");
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new FullScreenImageAdapter(this, allProducts, allProducts.size()));
        viewPager.setCurrentItem(intent.getIntExtra("position", 0));
        if (Configuration.getConfig().isTheFirstTimeOpeningThisPage)
            startActivity(new Intent(ProductInfoActivity.this, FullScreenPAgeUserGuider.class));
    }

    @Override
    public void onBackPressed() {
        Configuration.getConfig().isTheFirstTimeOpeningThisPage = false;
        finish();
    }
}

