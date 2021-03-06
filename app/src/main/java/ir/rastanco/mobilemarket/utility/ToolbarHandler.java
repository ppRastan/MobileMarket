package ir.rastanco.mobilemarket.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import ir.rastanco.mobilemarket.InfoActivity;
import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverLike;
import ir.rastanco.mobilemarket.presenter.WebView;

/**
 * Created by ParisaRashidhi on 29/03/2016.
 * this class will handel toolbar icons listener
 */
public class ToolbarHandler {
    private static ToolbarHandler toolbarHandler;
    private ImageButton cancelShareDialog;
    private Activity toolbarHandlerActivity;
    private String currentProductToShare;
    private EditText editTextToShare;

    public static ToolbarHandler getInstance() {
        if (toolbarHandler == null) {
            toolbarHandler = new ToolbarHandler();
        }
        return toolbarHandler;
    }

    public void generalShare(Activity activity, String product) {
        this.toolbarHandlerActivity = activity;
        this.currentProductToShare = product;
        final Dialog shareDialog = new Dialog(toolbarHandlerActivity);
        shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shareDialog.setContentView(R.layout.share_alert_dialog);
        Button sendBtn = (Button) shareDialog.findViewById(R.id.send_my_pm);
        sendBtn = PriceUtility.getInstance().ChangeButtonFont(sendBtn, activity);
        editTextToShare = (EditText) shareDialog.findViewById(R.id.text_to_send);
        editTextToShare = PriceUtility.getInstance().changeEditTextFont(editTextToShare, activity);
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
                        + toolbarHandlerActivity.getResources().getString(R.string.LinkDownloadApp);

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
        final boolean isAppInstalled = isAppAvailable(activity.getApplicationContext(), appName);
        if (isAppInstalled){

            final String visitProductLinkInSite = eachProduct;
            final Dialog shareByTelegram = new Dialog(activity);
            shareByTelegram.requestWindowFeature(Window.FEATURE_NO_TITLE);
            shareByTelegram.setContentView(R.layout.share_alert_dialog);
            cancelShareDialog = (ImageButton) shareByTelegram.findViewById(R.id.close_pm_to_friend);
            Button sendBtn = (Button) shareByTelegram.findViewById(R.id.send_my_pm);
            sendBtn = PriceUtility.getInstance().ChangeButtonFont(sendBtn, activity);
            editTextToShare = (EditText) shareByTelegram.findViewById(R.id.text_to_send);
            editTextToShare = PriceUtility.getInstance().changeEditTextFont(editTextToShare, activity);
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
        else {
            Toast.makeText(activity,activity.getResources().getString(R.string.message_no_telegram_in_mobile),Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isAppAvailable(Context context, String appName)
    {
        PackageManager pm = context.getPackageManager();
        try
        {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }


    public void addCurrentProductToFavorite(Activity activity, Context myContext, ImageButton likeThisProduct, Product eachProduct, ServerConnectionHandler sch) {

        if (sch.getAProduct(eachProduct.getId()).getLike() == 0) {
            if (Configuration.getConfig().userLoginStatus)
                ToastUtility.getConfig().displayToast(myContext.getResources().getString(R.string.thanks), activity);
            else
                ToastUtility.getConfig().displayToast(myContext.getResources().getString(R.string.pleaseLogin), activity);
            likeThisProduct.setImageResource(R.drawable.toolbar_add_to_favorit_fill_like);
            sch.changeProductLike(eachProduct.getId(), 1);
            if(sch.rateUsDialogShow()){
                displayAlertDialogForRateUs(myContext);
            }
        } else if (sch.getAProduct(eachProduct.getId()).getLike() == 1) {

            if (!Configuration.getConfig().userLoginStatus)
                ToastUtility.getConfig().displayToast(myContext.getResources().getString(R.string.pleaseLogin), activity);
            likeThisProduct.setImageResource(R.drawable.toolbar_add_to_favorite_border);
            sch.changeProductLike(eachProduct.getId(), 0);
        }

    }

    public void addToFavoriteInProductPage(Activity productPageActivity, ServerConnectionHandler sch, Product aProduct, ImageButton btnLike, Activity activity, int position) {

        if (sch.getAProduct(aProduct.getId()).getLike() == 0) {
            if (Configuration.getConfig().userLoginStatus)
                ToastUtility.getConfig().displayToast(activity.getResources().getString(R.string.thanks), productPageActivity);
            else
                ToastUtility.getConfig().displayToast(activity.getResources().getString(R.string.pleaseLogin), productPageActivity);
            btnLike.setImageResource(R.drawable.toolbar_add_to_favorit_fill_like);
            aProduct.setLike(1);
            sch.changeProductLike(aProduct.getId(), 1);
            ObserverLike.setLikeStatus(position);
            if(sch.rateUsDialogShow()){
               displayAlertDialogForRateUs(productPageActivity);
            }


        } else if (sch.getAProduct(aProduct.getId()).getLike() == 1) {

            if (!Configuration.getConfig().userLoginStatus)
                ToastUtility.getConfig().displayToast(activity.getResources().getString(R.string.pleaseLogin), productPageActivity);
            btnLike.setImageResource(R.drawable.toolbar_add_to_favorite_border);
            aProduct.setLike(0);
            sch.changeProductLike(aProduct.getId(), 0);
            ObserverLike.setLikeStatus(position);
        }
    }

    public void displayAlertDialogForRateUs(Context context) {
        //TODO Parisa
        final Context myContext = context;
        new MaterialDialog.Builder(myContext)
                .contentColorRes(R.color.black)
                .content(R.string.rateUsDialogTitle)
                .contentGravity(GravityEnum.CENTER)
                .negativeText(R.string.rateUsDialogNo)
                .negativeColorRes(R.color.black)
                .neutralText(R.string.rateUsDialogYes)
                .neutralColorRes(R.color.green)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                        rateIntent.setData(Uri.parse("market://details?id=" + Configuration.getConfig().applicationContext.getPackageName()));
                        myContext.startActivity(rateIntent);
                    }
                })
                .show();
    }

    public void displayInformationOfCurrentProduct(Product aProduct, Activity activity, Context context) {

        Intent intentProductInfo = new Intent(context, InfoActivity.class);
        intentProductInfo.putExtra("productId", aProduct.getId());
        intentProductInfo.putExtra("groupId", aProduct.getGroupId());
        context.startActivity(intentProductInfo);
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    public void productIndicative(String linkOfProduct, Activity activity) {
        Intent intent = new Intent(activity, WebView.class);
        intent.putExtra("url", linkOfProduct);
        activity.startActivity(intent);
       // activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkOfProduct)));
    }
}