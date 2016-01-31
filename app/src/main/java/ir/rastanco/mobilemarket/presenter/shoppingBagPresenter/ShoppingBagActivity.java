package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.MainActivity;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 ** Created by ShaisteS on 1394/10/30.
 */
public class ShoppingBagActivity extends Activity {


    private ImageButton closeShoppingPage;
    private ServerConnectionHandler sch;
    private Button okShop;
    private TextView totalPrice;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_bag);
        Typeface btraffic= Typeface.createFromAsset(getAssets(),"fonts/B Traffic.ttf");
        totalPrice = (TextView)findViewById(R.id.total_price);
        closeShoppingPage = (ImageButton)findViewById(R.id.close_shopping_page);
        closeShoppingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingBagActivity.this, MainActivity.class));
            }
        });
        okShop = (Button)findViewById(R.id.ok_shop);
        okShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Configuration.ShoppingBagActivity=this;
        sch=new ServerConnectionHandler(Configuration.ShoppingBagActivity);

        ArrayList<Integer> productsId=new ArrayList<Integer>();
        productsId=sch.getProductForShopping();

        ListView lvShoppingBag=(ListView)findViewById(R.id.lv_shoppingBag);
        shoppingBagAdapter adapter= new shoppingBagAdapter(this, R.layout.activity_shopping_bag,productsId);
        lvShoppingBag.setAdapter(adapter);

        //Total Price
        int finalPrice=0;
        int price;
        int off;
        for(int i=0;i<productsId.size();i++){
            price=0;
            off=0;
            Product product=new Product();
            product=sch.getAProduct(productsId.get(i));
            if(product.getPriceOff()!=0)
                off=(product.getPrice()*product.getPriceOff())/100;
            price=product.getPrice()-off;
            finalPrice=finalPrice+price;
        }
        totalPrice.setText(String.valueOf(finalPrice));
        String numberProductPrice = String.valueOf(totalPrice.getText());
        double finalPriceToolbar = Double.parseDouble(numberProductPrice);
        DecimalFormat formatter = new DecimalFormat("#,###,000");
        totalPrice.setTypeface(btraffic);
        totalPrice.setText(formatter.format(finalPriceToolbar));


    }

}
