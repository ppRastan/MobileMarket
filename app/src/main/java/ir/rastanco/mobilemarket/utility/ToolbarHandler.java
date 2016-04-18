package ir.rastanco.mobilemarket.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverLike;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductOptionActivity;

/**
 * Created by ParisaRashidhi on 29/03/2016.
 * this class will handel toolbar icons listener
 */
public class ToolbarHandler {
    private static ToolbarHandler toolbarHandler;
    private ImageButton cancelShareDialog;
    private Activity toolbarHandlerActivity;
    private String currentProductToShare;
    public static ToolbarHandler getInstance() {
        if (toolbarHandler == null) {
            toolbarHandler = new ToolbarHandler();
        }
        return toolbarHandler;
    }

    public void generalShare(Activity activity,String product) {
        this.toolbarHandlerActivity = activity;
        this.currentProductToShare = product;
        final Dialog shareDialog = new Dialog(toolbarHandlerActivity);
        shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shareDialog.setContentView(R.layout.share_alert_dialog);
        Button sendBtn = (Button) shareDialog.findViewById(R.id.send_my_pm);
        final EditText editTextToShare = (EditText) shareDialog.findViewById(R.id.text_to_send);
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
                String textToSend = editTextToShare.getText().toString();
                String Share = textToSend + "\n\n" +
                        currentProductToShare + "\n\n" +
                        toolbarHandlerActivity.getResources().getString(R.string.text_to_advertise) + "\n\n"
                        +toolbarHandlerActivity.getResources().getString(R.string.LinkDownloadApp);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, textToSend);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Share);
                sendIntent.setType("text/plain");
                toolbarHandlerActivity.startActivity(sendIntent);
                shareDialog.cancel();

            }
        });
        shareDialog.setCancelable(true);
        shareDialog.show();
    }

    public void shareByTelegram(final Activity activity, String eachProduct) {
        final String appName = "org.telegram.messenger";
        final String visitProductLinkInSite = eachProduct;
        final Dialog shareByTelegram = new Dialog(activity);
        shareByTelegram.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shareByTelegram.setContentView(R.layout.share_alert_dialog);
        cancelShareDialog = (ImageButton) shareByTelegram.findViewById(R.id.close_pm_to_friend);
        Button sendBtn = (Button) shareByTelegram.findViewById(R.id.send_my_pm);
        final EditText editTextToShare = (EditText) shareByTelegram.findViewById(R.id.text_to_send);
        cancelShareDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareByTelegram.dismiss();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telegramTextToSend = editTextToShare.getText().toString();
                String Share = telegramTextToSend + "\n\n" +
                        visitProductLinkInSite + "\n\n" +
                        activity.getResources().getString(R.string.text_to_advertise) + "\n\n"
                        + activity.getResources().getString(R.string.LinkDownloadApp);
                Intent telegramSendIntent = new Intent();
                telegramSendIntent.setAction(Intent.ACTION_SEND);
                telegramSendIntent.putExtra(Intent.EXTRA_SUBJECT, telegramTextToSend);
                telegramSendIntent.putExtra(Intent.EXTRA_TEXT, Share);
                telegramSendIntent.setType("text/plain");
                telegramSendIntent.setPackage(appName);
                activity.startActivity(telegramSendIntent);
                shareByTelegram.cancel();
            }
        });
        shareByTelegram.setCancelable(true);
        shareByTelegram.show();
    }


    public void addCurrentProductToFavorite(Context myContext, ImageButton likeThisProduct, Product eachProduct, Boolean isLikeButtonClicked, ServerConnectionHandler sch) {

        if (sch.getAProduct(eachProduct.getId()).getLike() == 0) {

            if (Configuration.getConfig().userLoginStatus)
                Toast.makeText(myContext, myContext.getResources().getString(R.string.thanks), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(myContext, myContext.getResources().getString(R.string.pleaseLogin), Toast.LENGTH_LONG).show();

            likeThisProduct.setImageResource(R.mipmap.ic_like_filled_toolbar);
            sch.changeProductLike(eachProduct.getId(), 1);
        } else if (sch.getAProduct(eachProduct.getId()).getLike() == 1) {

            if (!Configuration.getConfig().userLoginStatus)
                Toast.makeText(myContext, myContext.getResources().getString(R.string.pleaseLogin), Toast.LENGTH_LONG).show();

            likeThisProduct.setImageResource(R.mipmap.ic_like_toolbar);
            sch.changeProductLike(eachProduct.getId(), 0);
        }

    }

    public void addToFavoriteInProductPage(ServerConnectionHandler sch, Product aProduct, ImageButton btnLike, Activity activity, int position) {

        if (sch.getAProduct(aProduct.getId()).getLike() == 0) {

            if (Configuration.getConfig().userLoginStatus)
                Toast.makeText(activity, activity.getResources().getString(R.string.thanks), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(activity, activity.getResources().getString(R.string.pleaseLogin), Toast.LENGTH_LONG).show();

            btnLike.setImageResource(R.mipmap.ic_like_filled_toolbar);
            aProduct.setLike(1);
            sch.changeProductLike(aProduct.getId(), 1);
            ObserverLike.setLikeStatus(position);


        } else if (sch.getAProduct(aProduct.getId()).getLike() == 1) {

            if (!Configuration.getConfig().userLoginStatus)
                Toast.makeText(activity, activity.getResources().getString(R.string.pleaseLogin), Toast.LENGTH_LONG).show();

            btnLike.setImageResource(R.mipmap.ic_like_toolbar);
            aProduct.setLike(0);
            sch.changeProductLike(aProduct.getId(), 0);
            ObserverLike.setLikeStatus(position);
        }
    }

    public void displayInformationOfCurrentProduct(Product aProduct, Activity activity, Context context) {
        Intent intentProductInfo = new Intent(context, ProductOptionActivity.class);
        intentProductInfo.putExtra("productId", aProduct.getId());
        intentProductInfo.putExtra("groupId", aProduct.getGroupId());
        context.startActivity(intentProductInfo);
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    public void productIndicative(String linkOfProduct, Activity activity) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkOfProduct)));
    }
}