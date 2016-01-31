package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/11/05
 */
public class ProductOptionActivity extends Activity {

    private ArrayList<ProductOption> options;
    private ServerConnectionHandler sch;
    private Product aProduct;
    private RatingBar rankOfProduct;
    private ImageButton btnBack;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_option);
        Configuration.ProductOptionFragment=this;

        Intent intent = this.getIntent();
        int productId=intent.getIntExtra("productId", 0);
        int groupId=intent.getIntExtra("groupId",0);
        rankOfProduct = (RatingBar)findViewById(R.id.rank_of_product);
        rankOfProduct.setRating(2);
        btnBack = (ImageButton)findViewById(R.id.back_info);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(ProductOptionActivity.this,FullScreenImageAdapter.class));

            }
        });
        sch=new ServerConnectionHandler(Configuration.ProductOptionFragment);
        options=new ArrayList<ProductOption>();
        options=sch.getProductOption(productId,groupId);
        ListView lvProductOption=(ListView)findViewById(R.id.lv_productOption);
        ProductInfoItemAdapter adapter = new ProductInfoItemAdapter(Configuration.ProductOptionFragment,
                R.layout.product_info_item,options);
        lvProductOption.setAdapter(adapter);
    }
}
