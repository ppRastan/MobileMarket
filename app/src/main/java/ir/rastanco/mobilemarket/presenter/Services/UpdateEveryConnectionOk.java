package ir.rastanco.mobilemarket.presenter.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/11/30.
 * This BradCastReceivers for Network Status
 * IF Network is on and new version of software is available THEN
 *              show Notification and software is on then Enable Upgrade Button
 *
 */
public class UpdateEveryConnectionOk extends BroadcastReceiver {
    ServerConnectionHandler sch;

    @Override
    public void onReceive(Context context, Intent intent) {
        sch = new ServerConnectionHandler(context);
        if (intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTING) {
                Configuration.connectionStatus=true;
                if (sch.checkNewVersion("http://decoriss.com/app/Version.txt")) {
                    if (Configuration.UpgradeButtonMenu != null && !Configuration.productTableEmptyStatus) {
                        Configuration.UpgradeButtonMenu.setVisible(true);
                    }
                    Intent notificationService = new Intent(context, NotificationService.class);
                    context.startService(notificationService);
                }

            }
            if (intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            }
        }
    }
}
