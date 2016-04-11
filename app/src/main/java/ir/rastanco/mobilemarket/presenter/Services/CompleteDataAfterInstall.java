package ir.rastanco.mobilemarket.presenter.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Link;

/**
 * Created by ShaisteS on 1395/1/23.
 */
public class CompleteDataAfterInstall extends IntentService {

    public static final int STATUS_FINISHED = 1;
    private ServerConnectionHandler serverConnectionHandler;

    public CompleteDataAfterInstall() {
        super(CompleteDataAfterInstall.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        serverConnectionHandler=ServerConnectionHandler.getInstance(this);
        int firstIndexGetProduct=serverConnectionHandler.getFirstIndexForGetProductFromJson();
        String url = Link.getInstance().generateUrlForGetNewProduct(this.getString(R.string.firstTimeStamp));
        serverConnectionHandler.setProducts(serverConnectionHandler.getAllProductFromURL(url, firstIndexGetProduct, 0, false));
        serverConnectionHandler.completeProductInformationInTable(serverConnectionHandler.getProducts());
        receiver.send(STATUS_FINISHED, null);

    }
}