package ir.rastanco.mobilemarket.presenter.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Link;

/**
 * Created by ShaisteS on 7/4/2016.
 */
public class DownloadProductInformationService extends IntentService {

    public static final int STATUS_FINISHED = 2;


    private static final String TAG = "ProductInfoService";
    private int requestForCategoryId;
    private int minLimitedProductNumber;
    private int maxLimitedProductNumber;

    public DownloadProductInformationService() {
        super(DownloadCategoryInformationService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        requestForCategoryId=intent.getIntExtra("categoryId",0);
        minLimitedProductNumber=intent.getIntExtra("minLimited",0);
        maxLimitedProductNumber=intent.getIntExtra("maxLimited",10);
        ServerConnectionHandler serverConnectionHandler=ServerConnectionHandler.getInstance(this);
        String UrlGetProducts= Link.getInstance().
               generateForGetLimitedProductOfAMainCategory(requestForCategoryId,
               minLimitedProductNumber,maxLimitedProductNumber);
        serverConnectionHandler.addAllProductToTable(serverConnectionHandler.getProductFromUrlInFirstInstall(UrlGetProducts));
        receiver.send(STATUS_FINISHED, null);
    }
}
