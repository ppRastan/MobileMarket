package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.Security;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.MainActivity;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShoppingCancel;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShoppingCancelListener;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.LoginPage;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.PriceUtility;
import ir.rastanco.mobilemarket.utility.Utilities;

/**
 ** Created by ShaisteS on 1394/10/30.
 * this class will display whole price and whole offer price in shopping bag
 */
public class ShoppingBagActivity extends Activity {


    private ImageButton closeShoppingPage;
    private ServerConnectionHandler sch;
    private Button confirmShopping;
    private TextView totalPriceTextView;
    private Security security;
    private ArrayList<Integer> productsId;
    private ListView lvShoppingBag;
    protected void onCreate(Bundle savedInstanceState) {

        Configuration.getConfig().ShoppingBagContext =this;
        security =new Security();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_bag);
        this.shoppingBagRTLizer();
        this.setYekanFont();
        this.closeShoppingBag();
        sch=new ServerConnectionHandler(Configuration.getConfig().ShoppingBagContext);
        productsId = new ArrayList<Integer>();
        productsId = sch.getProductShoppingID();
        this.shoppingListViewCreator();
        int finalPrice= 0;
        int price;
        for(int i=0;i<productsId.size();i++){
            Product product=sch.getAProduct(productsId.get(i));
            if(product.getPriceOff()!=0)
                price= Utilities.getInstance().calculatePriceOffProduct(product.getPrice(),product.getPriceOff());
            else
                price=product.getPrice();
            finalPrice=finalPrice+price;
        }

        totalPriceTextView.setText(String.valueOf(PriceUtility.getInstance().formatPriceCommaSeprated(finalPrice)+"  "+"تومان"));
        confirmShopping = (Button)findViewById(R.id.ok_shop);
        confirmShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Integer, Integer> shopInfo = new HashMap<Integer, Integer>();
                shopInfo = sch.getAllProductShopping();
                if (shopInfo.size() == 0) {
                    Toast.makeText(Configuration.getConfig().ShoppingBagContext, getResources().getString(R.string.empty_basket),
                            Toast.LENGTH_LONG).show();
                } else {
                    UserInfo user = sch.getUserInfo();
                    if (user == null) {
                        Intent shoppingBagIntent = new Intent(Configuration.getConfig().ShoppingBagContext, LoginPage.class);
                        startActivity(shoppingBagIntent);
                    } else {
                        String url = Link.getInstance().generateURLForSendShoppingProductsToServer(user.getUserEmail(),shopInfo);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        sch.emptyShoppingBag();
                        ObserverShoppingCancel.setShoppingCancel(true);
                        ObserverShopping.setMyBoolean(false);
                        productsId = sch.getProductShoppingID();
                        shoppingBagAdapter adapter = new shoppingBagAdapter(Configuration.getConfig().ShoppingBagContext,
                                R.layout.activity_shopping_bag, productsId);
                        lvShoppingBag.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });


        ObserverShoppingCancel.addShoppingCancelListener(new ObserverShoppingCancelListener() {
            @Override
            public void ShoppingChanged() {
                Map<Integer, Integer> refreshProductsId = new Hashtable<Integer, Integer>();
                refreshProductsId = sch.getAllProductShopping();
                int finalPrice = 0;
                int price;
                for (Map.Entry<Integer, Integer> entry : refreshProductsId.entrySet()) {
                    Product product = sch.getAProduct(entry.getKey());
                    if(product.getPriceOff()!=0) {
                        price = Utilities.getInstance().calculatePriceOffProduct(product.getPrice(), product.getPriceOff());
                        price=price*entry.getValue();
                    }

                    else {
                        price = product.getPrice();
                        price=price*entry.getValue();
                    }
                    finalPrice=finalPrice+price;
                }
                totalPriceTextView.setText(PriceUtility.getInstance().formatPriceCommaSeprated(finalPrice));
            }
        });
    }

    private void shoppingListViewCreator() {
        lvShoppingBag =(ListView)findViewById(R.id.lv_shoppingBag);
        shoppingBagAdapter adapter= new shoppingBagAdapter(this, R.layout.activity_shopping_bag,productsId);
        lvShoppingBag.setAdapter(adapter);

    }

    private void closeShoppingBag() {

        closeShoppingPage = (ImageButton)findViewById(R.id.close_shopping_page);
        closeShoppingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingBagActivity.this, MainActivity.class));
            }
        });
    }

    private void shoppingBagRTLizer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    private void setYekanFont() {
        totalPriceTextView = (TextView)findViewById(R.id.total_price);
        totalPriceTextView = PriceUtility.getInstance().changeFontToYekan(totalPriceTextView, this);

    }

}
