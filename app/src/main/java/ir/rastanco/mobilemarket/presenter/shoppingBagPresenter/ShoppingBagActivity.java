package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
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

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_bag);
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

    }

}
