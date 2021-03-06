package ir.rastanco.mobilemarket.presenter.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.presenter.SplashHandler;

/**
 * Created by ShaisteS on 1394/11/30.
 * A Notification Service
 */
public class NotificationService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, SplashHandler.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), notificationIntent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(getApplicationContext().getString(R.string.decoriss))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentText(getApplicationContext().getString(R.string.update_decoriss))
                .setAutoCancel(true).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(9999, notification);
        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
