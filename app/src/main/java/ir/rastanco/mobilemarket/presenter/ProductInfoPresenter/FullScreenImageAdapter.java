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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import ir.rastanco.mobilemarket.utility.Configuration;

public class FullScreenImageAdapter extends PagerAdapter{

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
    private float y1, y2;
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
    private double amountOfFinalPrice;
    private DecimalFormat formatter;
    private Typeface yekanFont;
    // constructor
    public FullScreenImageAdapter(Activity activity,ArrayList<Product>allProducts,int allProductSize) {
        this.activity = activity;
        this.products=allProducts;
        this.productsSize=allProductSize;
        activity =(Activity) context;
        sch=new ServerConnectionHandler(Configuration.ProductInfoActivity);
        aProduct=new Product();
    }
    public boolean onTouchEvent(MotionEvent touchevent , final int position)
    {
        switch (touchevent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                y2 = touchevent.getY();

                if (y1 < y2)
                {
                    Intent intentProductInfo = new Intent(viewLayout.getContext(),ProductOptionActivity.class);
                    intentProductInfo.putExtra("productId", products.get(position).getId());
                    intentProductInfo.putExtra("groupId", products.get(position).getGroupId());
                    viewLayout.getContext().startActivity(intentProductInfo);
                }

                if (y1 > y2)
                {

                }

                break;
            }
        }
        return false;
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
        viewLayout = inflater.inflate(R.layout.activity_product_info, container,false);
        yekanFont = Typeface.createFromAsset(activity.getAssets(), "fonts/yekan.ttf");
        nameOfCurrentProduct = (TextView)viewLayout.findViewById(R.id.name_of_photo);
        nameOfCurrentProduct.setTypeface(yekanFont);
        addToBasketBtn = (Button)viewLayout.findViewById(R.id.full_screen_add_to_basket_btn);
        numberOfFinalPrice = String.valueOf(String.valueOf(products.get(position).getPrice()));
        amountOfFinalPrice = Double.parseDouble(numberOfFinalPrice);
        formatter = new DecimalFormat("#,###,000");
        nameOfCurrentProduct.setText(products.get(position).getTitle());
        final ImageButton btnLike = (ImageButton)viewLayout.findViewById(R.id.add_to_favorite);
        addToBasketBtn.setText(formatter.format(amountOfFinalPrice) + "  " + "تومان");
        addToBasketBtn.setTypeface(yekanFont);


        if (products.get(position).getLike()==0){
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
                if(products.get(position).getLike()==0){

                    btnLike.setImageResource(R.mipmap.ic_like_filled_toolbar);
                    products.get(position).setLike(1);
                    sch.changeProductLike(products.get(position).getId(), 1);

                }
                else if(products.get(position).getLike()==1){
                    btnLike.setImageResource(R.mipmap.ic_like_toolbar);
                    products.get(position).setLike(0);
                    sch.changeProductLike(products.get(position).getId(), 0);
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

            }
        });
        btnShare = (ImageButton)viewLayout.findViewById(R.id.img_share_full_screen);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog = new Dialog(activity);
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
                        sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        if(textToSend.matches("")){
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT,activity.getResources().getString(R.string.text_to_send));
                        }
                        else {
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT,textToSend);
                        }

                        sendIntent.putExtra(Intent.EXTRA_TEXT, products.get(position).getLinkInSite());
                        sendIntent.setType("text/plain");
                        activity.startActivity(sendIntent);

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
        final ImageLoader imgLoader = new ImageLoader(Configuration.ProductInfoActivity); // important
        String picNum = products.get(position).getImagesPath().get(0);
        try {
            picNum = URLEncoder.encode(picNum, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String image_url_Main = products.get(position).getImagesMainPath() +
                picNum +
                "&size=" +
                Configuration.homeDisplaySize + "x" + Configuration.productInfoHeightSize +
                "&q=30";
        imgLoader.DisplayImage(image_url_Main, imgProduct);
        LinearLayout layout = (LinearLayout) viewLayout.findViewById(R.id.linear);
        int counter;
        if(products.get(position).getImagesPath().size()>1)
            counter=0;
        else
            counter=1;

        for (int i = counter; i < products.get(position).getImagesPath().size(); i++) {
            final ImageView imageView = new ImageView(Configuration.ProductInfoActivity);
            imageView.setId(i - 1);
            imageView.setPadding(0, 0, 0, 0);
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
                    Configuration.articleDisplaySize + "x" + Configuration.articleDisplaySize +
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
                            Configuration.homeDisplaySize + "x" + Configuration.productInfoHeightSize +
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
                    sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    if(textToSend.matches("")){
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,activity.getResources().getString(R.string.text_to_send));
                    }
                    else {
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,textToSend);
                    }

                    sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage(appName);
                    activity.startActivity(sendIntent);

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
}