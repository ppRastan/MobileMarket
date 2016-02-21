package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.ProductShop;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/11/23.
 */
public class UserLastShoppingProduct extends Activity {

    private ServerConnectionHandler sch;
    private ArrayList<ProductShop> allProductsShop;
    private UserInfo user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_account);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        Configuration.UserLastShoppingContext =this;
        sch=new ServerConnectionHandler(Configuration.UserLastShoppingContext);
        allProductsShop=new ArrayList<ProductShop>();
        user=new UserInfo();
        user=sch.getUserInfo();
        if (user != null){
            allProductsShop=sch.getLastProductShop("http://decoriss.com/json/get,com=orders&uid="+
                    user.getUserId()+"&cache=false");
        }
        ListView lvLastShopping=(ListView)findViewById(R.id.lv_lastShopping);
        LastShoppingItemAdapter adapter=new LastShoppingItemAdapter(Configuration.UserLastShoppingContext,R.layout.last_shopping_item,allProductsShop);
        lvLastShopping.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
     Intent accuntManagerIntent = new Intent(UserLastShoppingProduct.this,AccountManager.class);
        startActivity(accuntManagerIntent);
    }
}
