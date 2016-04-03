package ir.rastanco.mobilemarket.presenter.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/11/30.
 * This BradCastReceivers for Network Status
 * IF Network is on and new version of software is available THEN
 *              show Notification and software is on then Enable Upgrade Button
 *
 */
public class UpdateEveryConnectionOk extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ServerConnectionHandler sch = new ServerConnectionHandler(context);
        if (intent.getExtras() != null) {
            ConnectivityManager cm =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()){
                Configuration.getConfig().connectionStatus=true;
                if (sch.checkNewVersion("http://decoriss.com/app/Version.txt")) {
                    if (Configuration.getConfig().upgradeButtonMenu != null && !Configuration.getConfig().existProductInformation) {
                        Configuration.getConfig().upgradeButtonMenu.setVisible(true);
                    }
                    Intent notificationService = new Intent(context, NotificationService.class);
                    context.startService(notificationService);
                }

            }
            if (intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Configuration.getConfig().connectionStatus=false;
            }
        }
    }
}
