package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.Security;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Connect;
import ir.rastanco.mobilemarket.presenter.MainActivity;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.UserActivity;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 ** Created by ShaisteS on 1394/10/30.
 */
public class ShoppingBagActivity extends Activity {


    private ImageButton closeShoppingPage;
    private ServerConnectionHandler sch;
    private Button okShop;
    private TextView totalPrice;
    private Security sec;
    private ArrayList<Integer> productsId;
    private Typeface yekanFont;
    private ListView lvShoppingBag;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Configuration.ShoppingBagActivity=this;
        sec=new Security();
        setContentView(R.layout.activity_shopping_bag);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)

            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        yekanFont= Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf");
        totalPrice = (TextView)findViewById(R.id.total_price);
        totalPrice.setTypeface(yekanFont);
        closeShoppingPage = (ImageButton)findViewById(R.id.close_shopping_page);
        closeShoppingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingBagActivity.this, MainActivity.class));
            }
        });
        Configuration.ShoppingBagActivity=this;
        sch=new ServerConnectionHandler(Configuration.ShoppingBagActivity);

        productsId=new ArrayList<Integer>();
        productsId=sch.getProductShoppingID();
        lvShoppingBag=(ListView)findViewById(R.id.lv_shoppingBag);
        shoppingBagAdapter adapter= new shoppingBagAdapter(this, R.layout.activity_shopping_bag,productsId);
        lvShoppingBag.setAdapter(adapter);

        //Total Price
        int finalPrice= 0;
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
        totalPrice.setText(formatter.format(finalPriceToolbar)+"   "+ getResources().getString(R.string.toman)+" ");

        okShop = (Button)findViewById(R.id.ok_shop);
        okShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Integer, Integer> shopInfo = new HashMap<Integer, Integer>();
                shopInfo = sch.getAllProductShopping();
                if (shopInfo.size() == 0) {
                    Toast.makeText(Configuration.ShoppingBagActivity,getResources().getString(R.string.empty_basket),
                            Toast.LENGTH_LONG).show();
                } else {
                    UserInfo user = sch.getUserInfo();
                    if (user == null) {
                        Intent shoppingBagIntent = new Intent(Configuration.ShoppingBagActivity, UserActivity.class);
                        startActivity(shoppingBagIntent);
                    } else {
                        String url = "http://decoriss.com/app,data=";
                        String urlInfo = user.getUserEmail() + "##";
                        for (Map.Entry<Integer, Integer> entry : shopInfo.entrySet())
                            urlInfo = urlInfo + entry.getKey() + "_" + entry.getValue() + "#";

                        urlInfo = sec.Base64(urlInfo);
                        url = url + urlInfo;
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        sch.emptyShoppingBag();
                        Observer.setShoppingCancel(true);
                        Connect.setMyBoolean(false);
                        productsId = sch.getProductShoppingID();
                        shoppingBagAdapter adapter = new shoppingBagAdapter(Configuration.ShoppingBagActivity,
                                R.layout.activity_shopping_bag, productsId);
                        lvShoppingBag.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });


        Observer.addShoppingCancelListener(new ShoppingCancelListener() {
            @Override
            public void ShoppingChanged() {
                Map<Integer, Integer> refreshProductsId = new Hashtable<Integer, Integer>();
                refreshProductsId = sch.getAllProductShopping();
                int finalPrice = 0;
                //Total Price
                int price;
                int off;
                for (Map.Entry<Integer, Integer> entry : refreshProductsId.entrySet()) {
                    price = 0;
                    off = 0;
                    Product product = new Product();
                    product = sch.getAProduct(entry.getKey());
                    if (product.getPriceOff() != 0)
                        off = (product.getPrice() * product.getPriceOff()) / 100;
                    price = (product.getPrice() * entry.getValue()) - (off * entry.getValue());
                    finalPrice = finalPrice + price;

                }
                totalPrice.setText(String.valueOf(finalPrice));
                String numberProductPrice = String.valueOf(totalPrice.getText());
                double finalPriceToolbar = Double.parseDouble(numberProductPrice);
                DecimalFormat formatter = new DecimalFormat("#,###,000");
                totalPrice.setText(formatter.format(finalPriceToolbar) + "   " + getResources().getString(R.string.toman)+" ");

            }
        });
    }
}
