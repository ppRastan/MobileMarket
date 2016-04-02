package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.ProductShop;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;


/**
 * Created by ShaisteS on 1394/11/23.
 * This class Show user past purchases from site
 */
public class UserLastShoppingProduct extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_account);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        Configuration.getConfig().userLastShoppingActivityContext =this;
        ServerConnectionHandler sch=new ServerConnectionHandler(Configuration.getConfig().userLastShoppingActivityContext);
        ArrayList<ProductShop> allProductsShop=new ArrayList<>();

        UserInfo user=sch.getUserInfo();
        if (user != null){
            allProductsShop=sch.getLastProductShop(Link.getInstance().generateURLForGetUserLasShopping(user.getUserId()));
        }
        ListView lvLastShopping=(ListView)findViewById(R.id.lv_lastShopping);
        UserLastShoppingProductItemAdapter adapter=new UserLastShoppingProductItemAdapter(Configuration.getConfig().userLastShoppingActivityContext,allProductsShop);
        lvLastShopping.setAdapter(adapter);
    }

        @Override
        public void onBackPressed() {
            Intent userPreviousShopping = new Intent(UserLastShoppingProduct.this,AccountManagerActivity.class);
            startActivity(userPreviousShopping);
            this.finish();
        }
}
