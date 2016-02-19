package ir.rastanco.mobilemarket.presenter.homePresenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverSimilarProduct;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 12/27/2015.
 * A Customize Adapter For Home List view
 */
public class PictureProductHomeItemAdapter extends ArrayAdapter<Product>  {

    private Activity myContext;
    private ArrayList<Product> allProduct;
    private ServerConnectionHandler sch;
    private ImageButton shareBtn;
    private String textToSend = null;
    private Dialog shareDialog;
    private ImageButton cancelShareDialog;
    private ImageButton basketToolbar;
    private Button sendBtn;
    private EditText editTextToShare;
    private Intent sendIntent;
    private boolean isSelectedForShop=false;

    public PictureProductHomeItemAdapter(Context context, int resource, ArrayList<Product> products) {
        super(context, resource,products);
        myContext=(Activity)context;
        allProduct=products;
        sch=new ServerConnectionHandler(context);

    }

    public View getView(final int position, View convertView, ViewGroup parent){

        Bitmap image=null;
        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.picture_product_item_home, null);

        //Special Icon
        //ImageButton offerLeft = (ImageButton)rowView.findViewById(R.id.ic_offer_left);
        ImageButton offerRight = (ImageButton)rowView.findViewById(R.id.ic_offer_right);
        if(Configuration.RTL)
        {
            //offerLeft.setVisibility(View.GONE);
            if(allProduct.get(position).getPriceOff() != 0)
            {
                offerRight.setVisibility(View.VISIBLE);
            }
            else {
                offerRight.setVisibility(View.GONE);
            }
        }
        /*if (! Configuration.RTL)
        {
            offerRight.setVisibility(View.GONE);
            if(allProduct.get(position).getPriceOff() != 0) {

                offerLeft.setVisibility(View.VISIBLE);
            }
            else
            {
                offerLeft.setVisibility(View.GONE);
            }
        }*/

        basketToolbar = (ImageButton)rowView.findViewById(R.id.basket_toolbar);
        if (sch.checkSelectProductForShop(allProduct.get(position).getId()))
            basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
        else
            basketToolbar.setImageResource(R.mipmap.bye_toolbar);

        basketToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (isSelectedForShop==false) {
                basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
                isSelectedForShop=true;
                sch.addProductToShoppingBag(allProduct.get(position).getId(),1);
                myContext.startActivity(new Intent(myContext,ShoppingBagActivity.class));
                myContext.overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
                ObserverShopping.setMyBoolean(true);
                isSelectedForShop = true;

            }

            else if (isSelectedForShop==true){
                basketToolbar.setImageResource(R.mipmap.bye_toolbar);
                isSelectedForShop=false;
                sch.deleteAProductShopping(allProduct.get(position).getId());
                ObserverShopping.setMyBoolean(false);
                isSelectedForShop = false;

            }
            }
        });

        Button btnSimilar=(Button) rowView.findViewById(R.id.btn_similar);
        btnSimilar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Integer> catNumber=new ArrayList<Integer>();
                       catNumber= sch.getPageNumForSimilarProduct(allProduct.get(position).getGroupId());
                        Configuration.MainPager.setCurrentItem(catNumber.get(0));
                        ObserverSimilarProduct.setSimilarProduct(catNumber.get(1));
                   }
               });


        shareBtn = (ImageButton) rowView.findViewById(R.id.imbt_share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareDialog = new Dialog(myContext);
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
                        String Share = textToSend + "\n\n" +
                                allProduct.get(position).getLinkInSite() + "\n\n" +
                                myContext.getResources().getString(R.string.text_to_advertise) + "\n\n"
                                + myContext.getResources().getString(R.string.LinkDownloadApp) ;

                        sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, textToSend);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, Share);
                        sendIntent.setType("text/plain");
                        myContext.startActivity(sendIntent);
                        shareDialog.cancel();
                    }
                });

                shareDialog.setCancelable(true);
                shareDialog.show();
//                shareDialog.cancel();
            }
        });
        ImageLoader imgLoader = new ImageLoader(Configuration.superACFragment,rowView,Configuration.homeDisplaySizeForShow); // important
        final  ImageView PicProductImage = (ImageView) rowView.findViewById(R.id.img_picProduct);
        PicProductImage.getLayoutParams().width= Configuration.homeDisplaySizeForShow;
        PicProductImage.getLayoutParams().height=Configuration.homeDisplaySizeForShow;


        String picCounter = allProduct.get(position).getImagesPath().get(0);
        try {
            picCounter= URLEncoder.encode(picCounter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_1 = allProduct.get(position).getImagesMainPath()+picCounter+
                "&size="+
                Configuration.homeDisplaySizeForURL +"x"+Configuration.homeDisplaySizeForURL +
                "&q=30";
        imgLoader.DisplayImage(image_url_1, PicProductImage);

        /*Drawable d=ResizeImage(R.drawable.loadingholder,rowView,Configuration.homeDisplaySizeForShow);
        final ProgressBar progressBar=(ProgressBar)rowView.findViewById(R.id.prograssBar);
        progressBar.getLayoutParams().height=Configuration.progressBarSize;
        progressBar.getLayoutParams().width=Configuration.progressBarSize;
        Glide.with(myContext)
               .load(image_url_1).override(Configuration.homeDisplaySizeForShow, Configuration.homeDisplaySizeForShow)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(d)
               .into(PicProductImage);*/

        ImageButton shareImgB=(ImageButton)rowView.findViewById(R.id.imbt_share);
        shareImgB.setBackgroundColor(Color.TRANSPARENT);
        PicProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> newAllProduct=new ArrayList<Product>();
                newAllProduct=sch.getSpecialProduct();
                Intent intent = new Intent(rowView.getContext(), ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct",newAllProduct);
                intent.putExtra("position",position);
                rowView.getContext().startActivity(intent);
            }
        });
        return rowView;
    }

    public Drawable ResizeImage (int imageID,View rowView,int deviceWidth) {

        BitmapDrawable bd=(BitmapDrawable) rowView.getResources().getDrawable(imageID);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(rowView.getResources(), imageID);
        Drawable drawable = new BitmapDrawable(rowView.getResources(),getResizedBitmap(bMap,newImageHeight,(int) deviceWidth));

        return drawable;
    }

    /************************ Resize Bitmap *********************************/
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

}
