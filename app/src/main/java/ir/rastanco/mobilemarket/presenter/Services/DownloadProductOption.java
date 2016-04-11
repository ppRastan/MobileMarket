package ir.rastanco.mobilemarket.presenter.Services;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Link;

/**
 * Created by ShaisteS on 1395/1/23.
 */
public class DownloadProductOption extends IntentService {

    private ServerConnectionHandler serverConnectionHandler;
    private int productId;
    private int groupId;
    private ArrayList<ProductOption> options;

    public DownloadProductOption() {
        super(DownloadProductOption.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        serverConnectionHandler=ServerConnectionHandler.getInstance(this);
        productId=intent.getIntExtra("productId",0);
        groupId=intent.getIntExtra("groupId", 0);
        String url= Link.getInstance().generateURLForGetProductOptionsOfAProduct(productId, groupId);
        options= new ArrayList<ProductOption>();
        options=serverConnectionHandler.getOptionsOfAProductFromURL(url);
        serverConnectionHandler.addProductOptionsToTable(productId,options);


    }
}