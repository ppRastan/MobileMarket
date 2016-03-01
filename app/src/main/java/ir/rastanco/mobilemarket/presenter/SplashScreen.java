package ir.rastanco.mobilemarket.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/12/1.
 */
public class SplashScreen extends AppCompatActivity{

    private Thread mSplashThread;
    private ServerConnectionHandler sch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SplashScreen sPlashScreen = this;
        sch=new ServerConnectionHandler(this);
        mSplashThread = new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        checkDbState();
                        wait(10);
                    }
                }
                catch(InterruptedException ex){
                }
                finish();
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, MainActivity.class);
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

    private void checkDbState() {

        ArrayList<Category> categories=new ArrayList<Category>();
        ArrayList<Article> articles=new ArrayList<Article>();

        //for add brandName to DataBase then update brandName filed
        //last version in server 1.3.9
        //version app that install in mobile is 1.0.0

        if(sch.getLastVersionInDB().equals("1.0.0")){
            sch.reloadProduct("1352689345");
            sch.updateVersionApp("1.0.0.1");
        }
        if(sch.getLastVersionInDB().equals("1.3.9")){
            sch.reloadProduct("1352689345");
            sch.updateVersionApp("1.3.9.1");
            sch.setLastUpdateTimeStamp();
        }

        if (sch.emptyUserInfo())
            Configuration.userLoginStatus=false; //please login
        else Configuration.userLoginStatus=true;//

        if (sch.emptyDBCategory()){
            categories=sch.getAllCategoryInfoURL("http://decoriss.com/json/get,com=allcats&cache=false");
            sch.addAllCategoryToTable(categories);
        }
        else
            sch.refreshCategories("http://decoriss.com/json/get,com=allcats&cache=false");

        if (sch.emptyDBProduct())
            Configuration.productTableEmptyStatus=true;
        else
            Configuration.productTableEmptyStatus=false;

    }


}
