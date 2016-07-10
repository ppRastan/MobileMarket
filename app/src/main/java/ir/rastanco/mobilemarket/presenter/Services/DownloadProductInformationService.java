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

    public static final int STATUS_FINISHED = 2;


    private static final String TAG = "ProductInfoService";
    private String link;

    public DownloadProductInformationService() {
        super(DownloadCategoryInformationService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        link=intent.getStringExtra("Link");
        ServerConnectionHandler serverConnectionHandler=ServerConnectionHandler.getInstance(this);
        ArrayList<Product> newProduct=serverConnectionHandler.getProductFromUrlInFirstInstall(link);
        serverConnectionHandler.addAllProductToTable(newProduct);
        Bundle bundle=new Bundle();
        bundle.putSerializable("newProduct",newProduct);
        receiver.send(STATUS_FINISHED,bundle);

    }
}
