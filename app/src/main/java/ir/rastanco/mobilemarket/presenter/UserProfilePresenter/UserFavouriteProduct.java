package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/11/23.
 * This class Show favorite Product of user
 */
public class UserFavouriteProduct extends Activity {

    private ServerConnectionHandler sch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Configuration.getConfig().userProfileActivityContext =this;
        sch=ServerConnectionHandler.getInstance(Configuration.getConfig().userProfileActivityContext);
        ArrayList<Product>allProductLike=sch.getAllProductFavourite();
        ListView lsvFavourite=(ListView) findViewById(R.id.lsv_favouriteProduct);
        UserFavouriteProductItemAdapter adapter= new UserFavouriteProductItemAdapter(Configuration.getConfig().userProfileActivityContext,allProductLike);
        lsvFavourite.setAdapter(adapter);
        lsvFavourite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int productId = sch.getProductIdWithTitle((String) parent.getItemAtPosition(position));
                Product aProduct = sch.getAProduct(productId);
                ArrayList<Product> product = new ArrayList<>();
                product.add(aProduct);
                Intent intent = new Intent(Configuration.getConfig().mainActivityContext, ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct", product);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        }
        );

    }
    @Override
    public void onBackPressed() {
        Intent UserFavoriteProduct = new Intent(UserFavouriteProduct.this,AccountManagerActivity.class);
        startActivity(UserFavoriteProduct);
        this.finish();
    }
}

