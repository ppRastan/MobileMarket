package ir.rastanco.mobilemarket.presenter.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Link;

/**
 * Created by ShaisteS on 04/10/2016.
 */
public class UpdateService extends IntentService {

    public static final int STATUS_FINISHED = 1;

    public UpdateService() {
        super(UpdateService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        ServerConnectionHandler serverConnectionHandler=ServerConnectionHandler.getInstance(this);
        serverConnectionHandler.refreshCategories(Link.getInstance().generateURLForGetAllCategories());
        serverConnectionHandler.getNewProducts();
        serverConnectionHandler.getEditProducts();
        receiver.send(STATUS_FINISHED, null);

    }
}
