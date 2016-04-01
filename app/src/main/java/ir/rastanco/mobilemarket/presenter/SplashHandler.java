package ir.rastanco.mobilemarket.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 *  Created by ShaisteS on 1394/12/1.
 *  loading starts from here
 */
public class SplashHandler extends AppCompatActivity {

    private Thread mSplashThread;
    private ServerConnectionHandler sch;
    private final Integer delay = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SplashHandler sPlashHandler = this;
        sch=new ServerConnectionHandler(this);
        mSplashThread = new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch

                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        if (ni != null && ni.isConnected()) {
                            Configuration.getConfig().connectionStatus=true;
                        }
                        if (sch.emptyUserInfo())
                            Configuration.getConfig().userLoginStatus=false; //please login
                        else Configuration.getConfig().userLoginStatus=true;//

                        if (sch.emptyDBProduct())
                            Configuration.getConfig().productTableEmptyStatus=true;
                        else
                            Configuration.getConfig().productTableEmptyStatus=false;

                        wait(delay);
                    }
                }
                catch(InterruptedException ex){
                   Log.v("unable to open splash screen","!");
                }
                finish();
                Intent intent = new Intent();
                intent.setClass(sPlashHandler, MainActivity.class);
                startActivity(intent);
            }
        };

        mSplashThread.start();
    }
    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }
}
