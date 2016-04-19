package ir.rastanco.mobilemarket.presenter.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Link;

/**
 * Created by ShaisteS on 1395/1/23.
 */
public class DownloadProductOption extends IntentService {

    public static final int STATUS_FINISHED = 3;


    public DownloadProductOption() {
        super(DownloadProductOption.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        ServerConnectionHandler serverConnectionHandler = ServerConnectionHandler.getInstance(this);
        int productId = intent.getIntExtra("productId", 0);
        int groupId = intent.getIntExtra("groupId", 0);
        String url = Link.getInstance().generateURLForGetProductOptionsOfAProduct(productId, groupId);
        ArrayList<ProductOption> options =serverConnectionHandler.getOptionsOfAProductFromURL(url);
        Bundle bundle=new Bundle();
        bundle.putSerializable("options", options);
        serverConnectionHandler.addProductOptionsToTable(productId, options);
        receiver.send(STATUS_FINISHED,bundle);


    }
}