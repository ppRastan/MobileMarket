package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/11/05
 */
public class ProductOptionActivity extends Activity {

    private ArrayList<ProductOption> options;
    private ServerConnectionHandler sch;
    private Product aProduct;
    private ImageButton btnBack;
    private TextView nameOfCurrentProduct;
    private boolean onBackBtnPressed = false;
    private Intent intent;
    private int productId;
    private int groupId;
    private ListView lvProductOption;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_option);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Configuration.ProductOptionContext=this;
        sch=new ServerConnectionHandler(Configuration.ProductOptionContext);

        intent = this.getIntent();
        productId=intent.getIntExtra("productId", 0);
        groupId=intent.getIntExtra("groupId",0);
        aProduct=new Product();
        aProduct=sch.getAProduct(productId);
        nameOfCurrentProduct = (TextView)findViewById(R.id.name_of_currrent_product);
        nameOfCurrentProduct.setText(aProduct.getTitle());
        btnBack = (ImageButton)findViewById(R.id.back_full_screen);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackBtnPressed = true;
                checkBackButtonState();
            }
        });
        sch=new ServerConnectionHandler(Configuration.ProductOptionContext);
        options=new ArrayList<ProductOption>();
        options=sch.getAllProductOptionOfAProduct(productId, groupId);
        lvProductOption=(ListView)findViewById(R.id.lv_productOption);
        ProductInfoItemAdapter adapter = new ProductInfoItemAdapter(Configuration.ProductOptionContext,
                R.layout.product_info_item,options);
        lvProductOption.setAdapter(adapter);

        ListView lvComment=(ListView)findViewById(R.id.lv_comments);
        ArrayList<String> commentsAProduct=new ArrayList<String>();
        commentsAProduct=sch.getContentCommentsAllProduct(productId);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                Configuration.ProductOptionContext,
                android.R.layout.simple_list_item_1,
                commentsAProduct );

        lvComment.setAdapter(arrayAdapter);

    }

    private void checkBackButtonState() {
        if(onBackBtnPressed)
        {
            super.onBackPressed();
        }
    }
}
