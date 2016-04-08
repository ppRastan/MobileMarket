package ir.rastanco.mobilemarket.utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Map;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;

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
    private static ToolbarHandler toolbarHandler;

    public static ToolbarHandler getInstance() {
        if (toolbarHandler == null) {
            toolbarHandler = new ToolbarHandler();
        }
        return toolbarHandler;
        // Supply num input as an argument.
    }

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

    public void shareByTelegram(final Activity activity, String eachProduct) {
        final String appName = "org.telegram.messenger";
        final String visitProductLinkInSite = eachProduct;
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


    public void addCurrentProductToFavorite(Context myContext, ImageButton likeThisProduct, Product eachProduct, Boolean isLikeButtonClicked, ServerConnectionHandler sch) {

        if (sch.getAProduct(eachProduct.getId()).getLike() == 0) {

            if (Configuration.getConfig().userLoginStatus)
                Toast.makeText(myContext, myContext.getResources().getString(R.string.thanks), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(myContext, myContext.getResources().getString(R.string.pleaseLogin), Toast.LENGTH_LONG).show();

            likeThisProduct.setImageResource(R.mipmap.ic_like_filled_toolbar);
            isLikeButtonClicked = true;
            sch.changeProductLike(eachProduct.getId(), 1);
        } else if (sch.getAProduct(eachProduct.getId()).getLike() == 1) {

            if (!Configuration.getConfig().userLoginStatus)
                Toast.makeText(myContext, myContext.getResources().getString(R.string.pleaseLogin), Toast.LENGTH_LONG).show();

            likeThisProduct.setImageResource(R.mipmap.ic_like_toolbar);
            isLikeButtonClicked = false;
            sch.changeProductLike(eachProduct.getId(), 0);
        }

    }
}