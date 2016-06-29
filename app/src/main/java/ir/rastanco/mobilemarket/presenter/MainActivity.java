package ir.rastanco.mobilemarket.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Home.HorizontalAdapter;
import ir.rastanco.mobilemarket.presenter.Services.DownloadResultReceiver;
import ir.rastanco.mobilemarket.presenter.Services.DownloadService;
import ir.rastanco.mobilemarket.utility.Configuration;

public class MainActivity extends AppCompatActivity implements DownloadResultReceiver.Receiver {

    private Context context;
    private RecyclerView horizontal_recycler_view;
    private ArrayList<String> horizontalList;
    private HorizontalAdapter horizontalAdapter;
    private ServerConnectionHandler serverConnectionHandler;
    private DownloadResultReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        serverConnectionHandler = ServerConnectionHandler.getInstance(context);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            Configuration.getConfig().connectionStatus = true;
        }
        if (serverConnectionHandler.emptyUserInfo())
            Configuration.getConfig().userLoginStatus = false; //please login
        else Configuration.getConfig().userLoginStatus = true;//

        if (serverConnectionHandler.emptyDBCategory()) {
            Configuration.getConfig().emptyCategoryTable=true;
            mReceiver = new DownloadResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadService.class);
            /* Send optional extras to Download IntentService */
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("requestId", 101);
            startService(intent);

        }
        else
            Configuration.getConfig().emptyCategoryTable=false;
        if (serverConnectionHandler.emptyDBProduct()) {
            Configuration.getConfig().emptyProductTable = true;
        }
        else {
            Configuration.getConfig().emptyProductTable = false;
        }

        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view_special_product);
        horizontalList = new ArrayList<>();
        horizontalList.add("horizontal 1");
        horizontalList.add("horizontal 2");
        horizontalList.add("horizontal 3");
        horizontalList.add("horizontal 4");
        horizontalList.add("horizontal 5");
        horizontalList.add("horizontal 6");
        horizontalList.add("horizontal 7");
        horizontalList.add("horizontal 8");
        horizontalList.add("horizontal 9");
        horizontalList.add("horizontal 10");
        horizontalAdapter = new HorizontalAdapter(horizontalList,context);

        LinearLayoutManager horizontalLayoutManager= new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }
}