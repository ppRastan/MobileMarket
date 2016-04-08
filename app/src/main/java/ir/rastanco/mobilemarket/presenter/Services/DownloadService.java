package ir.rastanco.mobilemarket.presenter.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;

/**
 * Created by ShaisteS on 1395/1/20.
 */
public class DownloadService extends IntentService {

    public static final int STATUS_RUNNING = 0;

    private static final String TAG = "DownloadService";
    private ServerConnectionHandler serverConnectionHandler;

    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");
        serverConnectionHandler=ServerConnectionHandler.getInstance(this);
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        if (Configuration.getConfig().connectionStatus) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                //
                if (Configuration.getConfig().emptyProductTable){
                    String url=Link.getInstance().generateUrlForGetNewProduct(this.getString(R.string.firstTimeStamp));
                    serverConnectionHandler.setProducts(serverConnectionHandler.getAllProductFromURL(url, 0, 0, false));
                    serverConnectionHandler.addAllProductToTable(serverConnectionHandler.getProducts());
                    Configuration.getConfig().emptyProductTable=false;
                }

            } catch (Exception e) {

            }
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}