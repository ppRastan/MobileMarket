package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/11/23.
 */
public class UserFavouriteProduct extends Activity {

    private ServerConnectionHandler sch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Configuration.UserProfileContext =this;
        sch=new ServerConnectionHandler(Configuration.UserProfileContext);
        UserInfo aUser=sch.getUserInfo();
        ArrayList<Product> allProductLike=new ArrayList<Product>();
        allProductLike=sch.getAllProductFavourite();

        ListView lsvFavourite=(ListView) findViewById(R.id.lsv_favouriteProduct);
        UserProfileAdapter adapter= new UserProfileAdapter(Configuration.UserProfileContext,R.layout.user_profile_like_product_item,allProductLike);
        lsvFavourite.setAdapter(adapter);

    }

}

