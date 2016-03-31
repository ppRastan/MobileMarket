package ir.rastanco.mobilemarket.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import ir.rastanco.mobilemarket.R;

/**
 * Created by ParisaRashidhi on 29/03/2016.
 * this class will handel toolbar icons listener
 */
    public class ToolbarHandler {
    private Dialog shareDialog;
    private Button sendBtn;
    private EditText editTextToShare;
    private ImageButton cancelShareDialog;
    private String textToSend;
    private Intent sendIntent;

    public void generalShare(final Activity activity, final String product) {
        shareDialog = new Dialog(activity);
        shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shareDialog.setContentView(R.layout.share_alert_dialog);
        sendBtn = (Button) shareDialog.findViewById(R.id.send_my_pm);
        editTextToShare = (EditText) shareDialog.findViewById(R.id.text_to_send);
        cancelShareDialog = (ImageButton) shareDialog.findViewById(R.id.close_pm_to_friend);
        cancelShareDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend = editTextToShare.getText().toString();
                String Share = textToSend + "\n\n" +
                        product + "\n\n" +
                        activity.getResources().getString(R.string.text_to_advertise) + "\n\n"
                        + activity.getResources().getString(R.string.LinkDownloadApp);

                sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, textToSend);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Share);
                sendIntent.setType("text/plain");
                activity.startActivity(sendIntent);
                shareDialog.cancel();

            }
        });
        shareDialog.setCancelable(true);
        shareDialog.show();
    }

    public void shareByTelegram(final Activity activity, String aproduct) {
        final String appName = "org.telegram.messenger";
        final String visitProductLinkInSite = aproduct;
            shareDialog = new Dialog(activity);
            shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            shareDialog.setContentView(R.layout.share_alert_dialog);
            cancelShareDialog = (ImageButton) shareDialog.findViewById(R.id.close_pm_to_friend);
            sendBtn = (Button) shareDialog.findViewById(R.id.send_my_pm);
            editTextToShare = (EditText) shareDialog.findViewById(R.id.text_to_send);
            cancelShareDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareDialog.dismiss();
                }
            });
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textToSend = editTextToShare.getText().toString();
                    String Share = textToSend + "\n\n" +
                            visitProductLinkInSite + "\n\n" +
                            activity.getResources().getString(R.string.text_to_advertise) + "\n\n"
                            + activity.getResources().getString(R.string.LinkDownloadApp);
                    sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, textToSend);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, Share);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage(appName);
                    activity.startActivity(sendIntent);
                    shareDialog.cancel();
                }
            });
            shareDialog.setCancelable(true);
            shareDialog.show();
    }

}