package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

/**
 * Created by ShaisteS on 1394/10/23.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
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
import java.text.DecimalFormat;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverLike;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.PriceUtility;

public class FullScreenImageAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<Product> products;
    private ArrayList<ProductOption> options;
    private LayoutInflater inflater;
    private ServerConnectionHandler sch;
    private int productsSize;
    private ImageButton btnInfo;
    private ImageButton btnShareByTelegram;
    private ImageButton btnShare;
    private ArrayList<Product> allProduct;
    private Context context;
    private View viewLayout;
    private TextView nameOfCurrentProduct;
    private Product aProduct;
    private String textToSend = null;
    private Dialog shareDialog;
    private ImageButton cancelShareDialog;
    private Button sendBtn;
    private EditText editTextToShare;
    private Intent sendIntent;
    private Button addToBasketBtn;
    private String numberOfFinalPrice;
    private PriceUtility priceUtility;
    public FullScreenImageAdapter(Activity activity,ArrayList<Product>allProducts,int allProductSize) {
        this.activity = activity;
        this.products=allProducts;
        this.productsSize=allProductSize;
        activity =(Activity) context;
        sch=new ServerConnectionHandler(Configuration.ProductInfoContext);
        aProduct=new Product();
        priceUtility = new PriceUtility();

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

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewLayout = inflater.inflate(R.layout.activity_product_info, container, false);
        addToBasketBtn = (Button)viewLayout.findViewById(R.id.full_screen_add_to_basket_btn);
        nameOfCurrentProduct = (TextView)viewLayout.findViewById(R.id.name_of_photo);
        nameOfCurrentProduct.setText(products.get(position).getTitle());

        setProductQuality(products.get(position).getQualityRank());
        //امحصولات با عنوان به زودی
        if(products.get(position).getPrice()==0){
            addToBasketBtn.setText("به زودی");
            addToBasketBtn.setCompoundDrawables(null,null,null,null);
            addToBasketBtn.setEnabled(false);

        }

        //این محصول تخفیف ندارد
        if (products.get(position).getPriceOff()==0 && products.get(position).getPrice()!=0){
            int price=products.get(position).getPrice();
            numberOfFinalPrice = String.valueOf(price);
            addToBasketBtn.setText("قیمت : "+" "+priceUtility.formatPriceCommaSeprated(Integer.valueOf(numberOfFinalPrice)) + "  " + "تومان");
        }
        //این محصول تخفیف دارد
        if (products.get(position).getPriceOff()!=0 && products.get(position).getPrice()!=0)
        {
            int price=products.get(position).getPrice();
            int off=(price*products.get(position).getPriceOff())/100;
            int priceForYou=price-off;
            numberOfFinalPrice = String.valueOf(priceForYou);
            addToBasketBtn.setText("قیمت برای شما:"+" "+priceUtility.formatPriceCommaSeprated(Integer.valueOf(numberOfFinalPrice)) + "  " + "تومان");
            //addToBasketBtn.invalidateDrawable(null);

        }

       addToBasketBtn = priceUtility.ChangeButtonFont(addToBasketBtn,activity);
        addToBasketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sch.addProductToShoppingBag(products.get(position).getId(), 1);
                Configuration.ProductInfoContext.startActivity(new Intent(Configuration.ProductInfoContext, ShoppingBagActivity.class));
                ObserverShopping.setMyBoolean(true);
            }
        });

        sch.getProductOption(products.get(position).getId(),
                products.get(position).getGroupId());

        final ImageButton btnLike = (ImageButton)viewLayout.findViewById(R.id.add_to_favorite);

        if (sch.getAProduct(products.get(position).getId()).getLike()==0){
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
                if(sch.getAProduct(products.get(position).getId()).getLike()==0){

                    if(Configuration.userLoginStatus)
                        Toast.makeText(activity,activity.getResources().getString(R.string.thanks),Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(activity,activity.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    btnLike.setImageResource(R.mipmap.ic_like_filled_toolbar);
                    products.get(position).setLike(1);
                    sch.changeProductLike(products.get(position).getId(), 1);
                    ObserverLike.setLikeStatus(position);


                }
                else if(sch.getAProduct(products.get(position).getId()).getLike()==1){

                    if(!Configuration.userLoginStatus)
                        Toast.makeText(activity,activity.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    btnLike.setImageResource(R.mipmap.ic_like_toolbar);
                    products.get(position).setLike(0);
                    sch.changeProductLike(products.get(position).getId(), 0);
                    ObserverLike.setLikeStatus(position);


                }
            }
        });


        btnInfo=(ImageButton)viewLayout.findViewById(R.id.img_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProductInfo = new Intent(viewLayout.getContext(), ProductOptionActivity.class);
                intentProductInfo.putExtra("productId", products.get(position).getId());
                intentProductInfo.putExtra("groupId", products.get(position).getGroupId());
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
                                products.get(position).getLinkInSite()+ "\n\n"+
                                Configuration.ProductInfoContext.getResources().getString(R.string.text_to_advertise)+"\n\n"
                                +Configuration.ProductInfoContext.getResources().getString(R.string.LinkDownloadApp);

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

                shareByTelegram(position);

            }
        });
        final ImageView imgProduct = (ImageView) viewLayout.findViewById(R.id.img_productInfo);
        imgProduct.getLayoutParams().width=Configuration.homeDisplaySizeForShow;
        imgProduct.getLayoutParams().height=Configuration.productInfoHeightForShow;
        final ImageLoader imgLoader = new ImageLoader(Configuration.ProductInfoContext,viewLayout,Configuration.homeDisplaySizeForShow); // important
        String picNum = products.get(position).getImagesPath().get(0);
        try {
            picNum = URLEncoder.encode(picNum, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String image_url_Main = products.get(position).getImagesMainPath() +
                picNum +
                "&size=" +
                Configuration.homeDisplaySizeForURL + "x" + Configuration.productInfoHeightForURL +
                "&q=30";
        imgLoader.DisplayImage(image_url_Main, imgProduct);
        LinearLayout layout = (LinearLayout) viewLayout.findViewById(R.id.linear);
        int counter;
        if(products.get(position).getImagesPath().size()>1)
            counter=0;
        else
            counter=1;

        for (int i = counter; i < products.get(position).getImagesPath().size(); i++) {
            final ImageView imageView = new ImageView(Configuration.ProductInfoContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Configuration.articleDisplaySizeForShow,Configuration.articleDisplaySizeForShow);
            imageView.setLayoutParams(layoutParams);
            imageView.setId(i - 1);
            imageView.setPadding(1, 1, 1, 0);
            layout.addView(imageView);
            picNum = products.get(position).getImagesPath().get(i);
            try {
                picNum = URLEncoder.encode(picNum, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String image_url_otherPic = products.get(position).getImagesMainPath() +
                    picNum +
                    "&size=" +
                    Configuration.articleDisplaySizeForURL + "x" + Configuration.articleDisplaySizeForURL +
                    "&q=30";
            imgLoader.DisplayImage(image_url_otherPic, imageView);

            final int parentClickImage=position;
            final int clickImageNum=i;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String picNum = products.get(parentClickImage).getImagesPath().get(clickImageNum);
                    try {
                        picNum = URLEncoder.encode(picNum, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String image_url_otherPic = products.get(parentClickImage).getImagesMainPath() +
                            picNum +
                            "&size=" +
                            Configuration.homeDisplaySizeForURL + "x" + Configuration.productInfoHeightForURL +
                            "&q=30";
                    imgLoader.DisplayImage(image_url_otherPic, imgProduct);

                }
            });

        }
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }

    private void shareByTelegram(final int position) {

        final String appName = "org.telegram.messenger";
        final String msg = products.get(position).getLinkInSite();
        final boolean isAppInstalled = isAppAvailable(activity.getApplicationContext(), appName);
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
                            products.get(position).getLinkInSite()+ "\n\n"+
                            Configuration.ProductInfoContext.getResources().getString(R.string.text_to_advertise)+"\n\n"
                            +Configuration.ProductInfoContext.getResources().getString(R.string.LinkDownloadApp);

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

    private boolean isAppAvailable(Context applicationContext, String appName) {
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