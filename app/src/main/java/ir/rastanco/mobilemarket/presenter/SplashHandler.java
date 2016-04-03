package ir.rastanco.mobilemarket.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;

/**
 *  Created by ShaisteS on 1394/12/1.
 *  loading starts from here
 */
public class SplashHandler extends AppCompatActivity {

    private ServerConnectionHandler sch;
    private Context splashContext;
    private final Integer delay = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SplashHandler sPlashHandler = this;
        splashContext=this;
        sch=ServerConnectionHandler.getInstance(splashContext);
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        if (ni != null && ni.isConnectedOrConnecting()) {
                            Configuration.getConfig().connectionStatus = true;
                        }
                        if (sch.emptyUserInfo())
                            Configuration.getConfig().userLoginStatus = false; //please login
                        else Configuration.getConfig().userLoginStatus = true;//

                        if (sch.emptyDBCategory()) {
                            Configuration.getConfig().emptyCategoryTable = true;
                            sch.setCategories(sch.getAllCategoryInfoURL(Link.getInstance().generateURLForGetAllCategories()));
                        }
                        if (sch.emptyDBProduct()) {
                            Configuration.getConfig().emptyProductTable = true;
                            Configuration.getConfig().existProductInformation = false;
                            sch.setProducts(sch.getAllProductFromURL(Link.getInstance().generateUrlForGetNewProduct(splashContext.getString(R.string.firstTimeStamp))));
                            if (sch.getProducts().size() != 0)
                                Configuration.getConfig().existProductInformation = true;

                        } else {
                            Configuration.getConfig().existProductInformation = false;
                            Configuration.getConfig().emptyProductTable = false;
                        }

                        wait(delay);
                    }
                } catch (InterruptedException ex) {
                    Log.v("unable to open splash", "!");
                }
                finish();
                Intent intent = new Intent();
                intent.setClass(sPlashHandler, MainActivity.class);
                startActivity(intent);
            }
        };

        mSplashThread.start();
    }
}
