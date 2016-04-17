package ir.rastanco.mobilemarket.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 *  Created by ShaisteS on 1394/12/1.
 *  loading starts from here
 */
public class SplashHandler extends AppCompatActivity{

    private ServerConnectionHandler sch;
    private final Integer delay = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SplashHandler sPlashHandler = this;
        Context splashContext=this;
        sch=ServerConnectionHandler.getInstance(splashContext);
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(delay);
                    }
                } catch (InterruptedException ex) {
                    Log.v("unable to open splash", "!");
                }
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni != null && ni.isConnectedOrConnecting()) {
                    Configuration.getConfig().connectionStatus = true;
                }
                if (sch.emptyUserInfo())
                    Configuration.getConfig().userLoginStatus = false; //please login
                else Configuration.getConfig().userLoginStatus = true;//

                if (sch.emptyDBCategory()) {
                    Configuration.getConfig().emptyCategoryTable=true;
                }
                else
                    Configuration.getConfig().emptyCategoryTable=false;
                if (sch.emptyDBProduct()) {
                    Configuration.getConfig().emptyProductTable = true;
                }
                else {
                    Configuration.getConfig().emptyProductTable = false;
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
