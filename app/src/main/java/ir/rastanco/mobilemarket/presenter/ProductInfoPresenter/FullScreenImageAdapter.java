package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

/**
 * Created by ShaisteS on 1394/10/23.
 * A pagerAdapter for show product information and picture
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverLike;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Links;
import ir.rastanco.mobilemarket.utility.PriceUtility;
import ir.rastanco.mobilemarket.utility.Utilities;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<Product> products;
    private LayoutInflater inflater;
    private ServerConnectionHandler sch;
    private int productsSize;
    private ImageButton btnInfo;
    private ImageButton btnShareByTelegram;
    private ImageButton btnShare;
    private View viewLayout;
    private TextView nameOfCurrentProduct;
    private String textToSend = null;
    private Dialog shareDialog;
    private ImageButton cancelShareDialog;
    private Button sendBtn;
    private EditText editTextToShare;
    private Intent sendIntent;
    private Button addToBasketBtn;
    private String numberOfFinalPrice;

    public FullScreenImageAdapter(Activity activity,ArrayList<Product>allProducts,int allProductSize) {
        this.activity = activity;
        this.products=allProducts;
        this.productsSize=allProductSize;
        sch=new ServerConnectionHandler(Configuration.getConfig().ProductInfoContext);
    }

    @Override
    public int getCount() {
        return productsSize;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewLayout = inflater.inflate(R.layout.activity_product_info, container, false);
        final Product aProduct=products.get(position);
        addToBasketBtn = (Button)viewLayout.findViewById(R.id.full_screen_add_to_basket_btn);
        nameOfCurrentProduct = (TextView)viewLayout.findViewById(R.id.name_of_photo);
        nameOfCurrentProduct.setText(aProduct.getTitle());

        setProductQuality(aProduct.getQualityRank());
        if (aProduct.getPrice()==0){
            addToBasketBtn.setText(activity.getString(R.string.coming_soon));
            addToBasketBtn.setCompoundDrawables(null,null,null,null);
            addToBasketBtn.setEnabled(false);

        }

        if (aProduct.getPriceOff()==0 && aProduct.getPrice()!=0){
            int price=aProduct.getPrice();
            numberOfFinalPrice = String.valueOf(price);
            addToBasketBtn.setText(activity.getString(R.string.productPrice)+" "+
                    PriceUtility.getInstance().formatPriceCommaSeprated(Integer.valueOf(numberOfFinalPrice)) + "  " +
                    activity.getString(R.string.currency));
        }
        if (aProduct.getPriceOff()!=0 && aProduct.getPrice()!=0)
        {
            int price=aProduct.getPrice();
            int priceOff=aProduct.getPriceOff();
            int priceForYou= Utilities.getInstance().calculatePriceOffProduct(price,priceOff);
            numberOfFinalPrice = String.valueOf(priceForYou);
            addToBasketBtn.setText(activity.getString(R.string.price_for_you)+" "
                    +PriceUtility.getInstance().formatPriceCommaSeprated(Integer.valueOf(numberOfFinalPrice)) + "  " +
                    activity.getString(R.string.currency));
            //addToBasketBtn.invalidateDrawable(null);

        }

        addToBasketBtn = PriceUtility.getInstance().ChangeButtonFont(addToBasketBtn,activity);
        addToBasketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sch.addProductToShoppingBag(aProduct.getId(), 1);
                Configuration.getConfig().ProductInfoContext.startActivity(new Intent(Configuration.getConfig().ProductInfoContext, ShoppingBagActivity.class));
                ObserverShopping.setMyBoolean(true);
            }
        });

        sch.getAllProductOptionOfAProduct(aProduct.getId(),
                aProduct.getGroupId());

        final ImageButton btnLike = (ImageButton)viewLayout.findViewById(R.id.add_to_favorite);

        if (sch.getAProduct(aProduct.getId()).getLike()==0){
            //this Product No Favorite
            btnLike.setImageResource(R.mipmap.ic_like_toolbar);
        }
        else{

            btnLike.setImageResource(R.mipmap.ic_like_filled_toolbar);
        }
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(sch.getAProduct(aProduct.getId()).getLike()==0){

                    if(Configuration.getConfig().userLoginStatus)
                        Toast.makeText(activity,activity.getResources().getString(R.string.thanks),Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(activity,activity.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    btnLike.setImageResource(R.mipmap.ic_like_filled_toolbar);
                    aProduct.setLike(1);
                    sch.changeProductLike(aProduct.getId(), 1);
                    ObserverLike.setLikeStatus(position);


                }
                else if(sch.getAProduct(aProduct.getId()).getLike()==1){

                    if(!Configuration.getConfig().userLoginStatus)
                        Toast.makeText(activity,activity.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    btnLike.setImageResource(R.mipmap.ic_like_toolbar);
                    aProduct.setLike(0);
                    sch.changeProductLike(aProduct.getId(), 0);
                    ObserverLike.setLikeStatus(position);
                }
            }
        });


        btnInfo=(ImageButton)viewLayout.findViewById(R.id.img_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProductInfo = new Intent(viewLayout.getContext(),ProductOptionActivity.class);
                intentProductInfo.putExtra("productId", aProduct.getId());
                intentProductInfo.putExtra("groupId", aProduct.getGroupId());
                viewLayout.getContext().startActivity(intentProductInfo);
                activity.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

            }
        });
        btnShare = (ImageButton)viewLayout.findViewById(R.id.img_share_full_screen);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog = new Dialog(activity);
                shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                shareDialog.setContentView(R.layout.share_alert_dialog);
                cancelShareDialog = (ImageButton) shareDialog.findViewById(R.id.close_pm_to_friend);
                sendBtn = (Button)shareDialog.findViewById(R.id.send_my_pm);
                editTextToShare = (EditText)shareDialog.findViewById(R.id.text_to_send);
                cancelShareDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                    }
                });

                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sendBtn.setTextColor(Color.parseColor("#EB4D2A"));
                        textToSend = editTextToShare.getText().toString();
                        String Share=textToSend+"\n\n"+
                                aProduct.getLinkInSite()+ "\n\n"+
                                activity.getResources().getString(R.string.text_to_advertise)+"\n\n"
                                +activity.getResources().getString(R.string.LinkDownloadApp);

                        sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,textToSend);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, Share);
                        sendIntent.setType("text/plain");
                        activity.startActivity(sendIntent);
                        shareDialog.cancel();

                    }
                });
                shareDialog.setCancelable(true);
                shareDialog.show();
            }
        });


        btnShareByTelegram = (ImageButton)viewLayout.findViewById(R.id.telegram_share);
        btnShareByTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareByTelegram(aProduct.getLinkInSite());
            }
        });
        final ImageView imgProduct = (ImageView) viewLayout.findViewById(R.id.img_productInfo);
        imgProduct.getLayoutParams().width=Configuration.getConfig().homeDisplaySizeForShow;
        imgProduct.getLayoutParams().height=Configuration.getConfig().productInfoHeightForShow;
        final ImageLoader imgLoader = new ImageLoader(Configuration.getConfig().ProductInfoContext,viewLayout,Configuration.getConfig().homeDisplaySizeForShow); // important

        String imageNumberPath;
        if(aProduct.getImagesPath().size()==0)
            imageNumberPath="no_image_path";
        else
            imageNumberPath = aProduct.getImagesPath().get(0);

        try {
            imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_Main = Links.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(),imageNumberPath,Configuration.getConfig().homeDisplaySizeForURL,Configuration.getConfig().productInfoHeightForURL);
        imgLoader.DisplayImage(image_url_Main, imgProduct);
        LinearLayout layout = (LinearLayout) viewLayout.findViewById(R.id.linear);
        int counter;
        if(aProduct.getImagesPath().size()>1)
            counter=0;
        else
            counter=1;

        for (int i = counter; i < aProduct.getImagesPath().size(); i++) {
            final ImageView imageView = new ImageView(Configuration.ProductInfoContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Configuration.articleDisplaySizeForShow,Configuration.articleDisplaySizeForShow);
            imageView.setLayoutParams(layoutParams);
            imageView.setId(i - 1);
            imageView.setPadding(1, 1, 1, 0);
            layout.addView(imageView);
            imageNumberPath = aProduct.getImagesPath().get(i);
            try {
                imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String image_url_otherPic =Links.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(),imageNumberPath,Configuration.getConfig().articleDisplaySizeForURL,Configuration.getConfig().articleDisplaySizeForURL);
            imgLoader.DisplayImage(image_url_otherPic, imageView);

            final int clickImageNum=i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String imageNumberPath = aProduct.getImagesPath().get(clickImageNum);
                    try {
                        imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String image_url_otherPic = Links.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(),imageNumberPath,Configuration.getConfig().homeDisplaySizeForURL,Configuration.getConfig().productInfoHeightForURL);
                    imgLoader.DisplayImage(image_url_otherPic, imgProduct);

                }
            });

        }
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }

    private void shareByTelegram(String productLinkInSite) {

        final String appName = "org.telegram.messenger";
        final String visitProductLinkInSite=productLinkInSite;
        final boolean isAppInstalled = isAppAvailable(appName);
        if (isAppInstalled) {
            shareDialog = new Dialog(activity);
            shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            shareDialog.setContentView(R.layout.share_alert_dialog);
            cancelShareDialog = (ImageButton) shareDialog.findViewById(R.id.close_pm_to_friend);
            sendBtn = (Button)shareDialog.findViewById(R.id.send_my_pm);
            editTextToShare = (EditText)shareDialog.findViewById(R.id.text_to_send);
            cancelShareDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareDialog.dismiss();
                }
            });
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendBtn.setTextColor(Color.parseColor("#EB4D2A"));
                    textToSend = editTextToShare.getText().toString();
                    String Share=textToSend+"\n\n"+
                            visitProductLinkInSite+ "\n\n"+
                            activity.getResources().getString(R.string.text_to_advertise)+"\n\n"
                            +activity.getResources().getString(R.string.LinkDownloadApp);
                    sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT,textToSend);
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
        else
        {
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.telegram_not_installed), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAppAvailable(String appName) {
        PackageManager pm = activity.getPackageManager();
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }

    private void setProductQuality(String quality){
        ImageView imgProductQuality=(ImageView)viewLayout.findViewById(R.id.img_ProductQuality);
        switch (quality){
            case "a":
                imgProductQuality.setImageResource(R.drawable.darajeha);
                break;
            case "b":
                imgProductQuality.setImageResource(R.drawable.darajehb);
                break;
            case "c":
                imgProductQuality.setImageResource(R.drawable.darajehc);
                break;
            case "d":
                imgProductQuality.setImageResource(R.drawable.darajehd);
                break;
            case "e":
                imgProductQuality.setImageResource(R.drawable.darajehe);
                break;
            case "f":
                imgProductQuality.setImageResource(R.drawable.darajehf);
                break;
        }


    }
}