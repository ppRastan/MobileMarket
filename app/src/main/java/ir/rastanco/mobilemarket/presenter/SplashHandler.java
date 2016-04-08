package ir.rastanco.mobilemarket.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Services.DownloadResultReceiver;
import ir.rastanco.mobilemarket.presenter.Services.DownloadService;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/12/1.
 * loading starts from here
 */
public class SplashHandler extends AppCompatActivity implements DownloadResultReceiver.Receiver {

    private ServerConnectionHandler sch;
    private Context splashContext;
    private DownloadResultReceiver mReceiver;
    private final Integer delay = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SplashHandler sPlashHandler = this;
        splashContext = this;
        sch = ServerConnectionHandler.getInstance(splashContext);
        if (sch.emptyDBCategory() && sch.emptyDBProduct()) {
            Configuration.getConfig().emptyCategoryTable = true;
            Configuration.getConfig().emptyProductTable = true;
            mReceiver = new DownloadResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadService.class);
            /* Send optional extras to Download IntentService */
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("requestId", 101);
            startService(intent);
        }
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
                        } else
                            Configuration.getConfig().emptyCategoryTable = false;
                        if (sch.emptyDBProduct()) {
                            Configuration.getConfig().emptyProductTable = true;
                        } else {
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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }
}
