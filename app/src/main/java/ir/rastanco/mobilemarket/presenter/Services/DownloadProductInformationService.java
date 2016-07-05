package ir.rastanco.mobilemarket.presenter.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

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
        serverConnectionHandler.addAllProductToTable(serverConnectionHandler.getProductFromUrlInFirstInstall(link));
        receiver.send(STATUS_FINISHED, null);
    }
}
