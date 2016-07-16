package ir.rastanco.mobilemarket.presenter.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;

/**
 * Created by ShaisteS on 7/4/2016.
 */
public class DownloadProductInformationService extends IntentService {

    public static final int STATUS_FINISHED_FIRST_ENTER_TAB=2;
    public static final int STATUS_FINISHED_WHEN_SCROLL = 3;
    public static final int STATUS_FINISHED_FILTER=4;


    private static final String TAG = "ProductInfoService";
    private String link;
    private int code;

    public DownloadProductInformationService() {
        super(DownloadProductInformationService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        link=intent.getStringExtra("Link");
        code=intent.getIntExtra("code",0);
        ServerConnectionHandler serverConnectionHandler=ServerConnectionHandler.getInstance(this);
        ArrayList<Product> newProduct=serverConnectionHandler.getProductFromUrlInFirstInstall(link);
        serverConnectionHandler.addAllProductToTable(newProduct);
        Bundle bundle=new Bundle();
        bundle.putSerializable("newProduct",newProduct);
        if (code==200)
            receiver.send(STATUS_FINISHED_FIRST_ENTER_TAB,bundle);
        if (code==201)
            receiver.send(STATUS_FINISHED_WHEN_SCROLL,bundle);
        if (code==202)
            receiver.send(STATUS_FINISHED_FILTER,bundle);

    }
}
