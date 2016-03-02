package ir.rastanco.mobilemarket.presenter;

import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import ir.rastanco.mobilemarket.utility.Configuration;


/**
 * Created by Emertat on 01/15/2016.
 */
public class PhoneCallListener extends PhoneStateListener {

    private boolean isPhoneCalling = false;

    String LOG_TAG = "LOGGING 123";

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        if (TelephonyManager.CALL_STATE_RINGING == state) {
            // phone ringing
            Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
        }

        if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
            // active
            Log.i(LOG_TAG, "OFFHOOK");

            isPhoneCalling = true;
        }

        if (TelephonyManager.CALL_STATE_IDLE == state) {
            // run when class initial and phone call ended,
            // need detect flag from CALL_STATE_OFFHOOK
            Log.i(LOG_TAG, "IDLE");

            if (isPhoneCalling) {

                Log.i(LOG_TAG, "restart app");

                // restart app
                Intent i = Configuration.AplicationContext.getPackageManager()
                        .getLaunchIntentForPackage(
                                Configuration.AplicationContext.getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setClass(Configuration.AplicationContext, MainActivity.class);
                Configuration.AplicationContext.startActivity(i);
                isPhoneCalling = false;
            }

        }
    }
}
