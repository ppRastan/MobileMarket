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
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");
        ServerConnectionHandler serverConnectionHandler=ServerConnectionHandler.getInstance(this);

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        if (Configuration.getConfig().connectionStatus) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {

                String urlGetCategories= Link.getInstance().generateURLForGetAllCategories();
                serverConnectionHandler.setCategories(serverConnectionHandler.getAllCategoryInfoURL(urlGetCategories));

                String UrlGetProducts=Link.getInstance().generateUrlForGetNewProduct(this.getString(R.string.firstTimeStamp));
                serverConnectionHandler.setProducts(serverConnectionHandler.getProductFromUrlInFirstInstall(UrlGetProducts));

                serverConnectionHandler.addAllCategoryToTable(serverConnectionHandler.getCategories());
                Configuration.getConfig().emptyCategoryTable=false;
                receiver.send(STATUS_FINISHED, null);

                serverConnectionHandler.addProductInformationToDataBaseFirstInstall();
                Configuration.getConfig().emptyProductTable=false;
                //Test getting 7000 product
               //String url=Link.getInstance().generateUrlForGetNewProduct(this.getString(R.string.firstTimeStamp));
               //serverConnectionHandler.addProductInformationToDataBaseFirstInstall7000(url);
               //get all product in first install

            } catch (Exception e) {

                Log.v("unable to set this font","!");
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