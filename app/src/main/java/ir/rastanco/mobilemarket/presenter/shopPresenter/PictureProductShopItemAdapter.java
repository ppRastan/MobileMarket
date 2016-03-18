package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.CounterIconDisplayer;
import ir.rastanco.mobilemarket.utility.PriceUtility;

/**
 * Created by ShaisteS on 12/28/2015.
 * A Customize Adapter For Shop Grid view
 */
public class PictureProductShopItemAdapter extends RecyclerView.Adapter<PictureProductShopItemAdapter.Holder>{

    private static LayoutInflater inflater=null;
    private ArrayList<Product> allProduct;
    private ProgressBar imageLoading;
    private boolean isLikeButtonClicked = true;
    private boolean isSelectedForShop=false;
    private ServerConnectionHandler sch;
    private CounterIconDisplayer ciu;
    private  Context myContext;
    private String textToSend = null;
    private Dialog shareDialog;
    private Intent sendIntent;

    public PictureProductShopItemAdapter(FragmentActivity mainActivity,ArrayList<Product> products) {

        myContext =mainActivity;
        inflater = ( LayoutInflater ) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        allProduct =products;
        sch=new ServerConnectionHandler(myContext);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView;
        rowView = inflater.inflate(R.layout.picture_produc_item_shop, null);
        return new Holder(rowView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        final Product aProduct=allProduct.get(position);
        if (aProduct.getPriceOff()==0){
            holder.priceForYou.setVisibility(View.INVISIBLE);
            holder.originalPrice.setTextColor(Color.BLACK);
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }
        else {
            holder.originalPrice.setTextColor(Color.RED);
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            int price= aProduct.getPrice();
            int offPrice= (price*aProduct.getPriceOff())/100;
            String priceForYou = String.valueOf(price-offPrice);
            double amountOfFinalPrice = Double.parseDouble(priceForYou);
            DecimalFormat formatter = new DecimalFormat("#,###,000");
            holder.priceForYou.setText(String.valueOf(formatter.format(amountOfFinalPrice)+" "+"تومان"));
            holder.priceForYou.setVisibility(View.VISIBLE);
        }
        if(Configuration.RTL)
        {

            holder.offerLeft.setVisibility(View.GONE);
            if(aProduct.getPriceOff() != 0)
            {
                holder.offerRight.setVisibility(View.VISIBLE);
            }
            else {
                holder.offerRight.setVisibility(View.GONE);
            }
        }


        if (! Configuration.RTL)
        {
            holder.offerRight.setVisibility(View.GONE);
            if(aProduct.getPriceOff() != 0) {

                holder.offerLeft.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.offerLeft.setVisibility(View.GONE);
            }
        }

        if (aProduct.getPrice()==0)
            holder.basketToolbar.setVisibility(View.GONE);
        else
            holder.basketToolbar.setVisibility(View.VISIBLE);

        if (sch.checkSelectProductForShop(aProduct.getId()))
            holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
        else
            holder.basketToolbar.setImageResource(R.mipmap.bye_toolbar);

        holder.basketToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSelectedForShop==false) {
                    holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
                    isSelectedForShop=true;
                    sch.addProductToShoppingBag(aProduct.getId(),1);
                    myContext.startActivity(new Intent(myContext, ShoppingBagActivity.class));
                    ObserverShopping.setMyBoolean(true);
                    isSelectedForShop = true;

                }

                else if (isSelectedForShop==true)
                {
                    holder.basketToolbar.setImageResource(R.mipmap.bye_toolbar);
                    isSelectedForShop=false;
                    sch.deleteAProductShopping(aProduct.getId());
                    ObserverShopping.setMyBoolean(false);
                    isSelectedForShop = false;
                }
            }
        });

        holder.shareToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog = new Dialog(myContext);
                shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                shareDialog.setContentView(R.layout.share_alert_dialog);
                ImageButton cancelShareDialog = (ImageButton) shareDialog.findViewById(R.id.close_pm_to_friend);
                final Button sendBtn = (Button)shareDialog.findViewById(R.id.send_my_pm);
                final EditText editTextToShare = (EditText)shareDialog.findViewById(R.id.text_to_send);
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
                        String share=textToSend+"\n\n"+
                                aProduct.getLinkInSite()+ "\n\n"+
                                myContext.getResources().getString(R.string.text_to_advertise)+"\n\n"
                                + myContext.getResources().getString(R.string.LinkDownloadApp) ;
                        sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,textToSend);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,textToSend);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,share);
                        sendIntent.setType("text/plain");
                        myContext.startActivity(sendIntent);
                        shareDialog.cancel();
                    }
                });
                shareDialog.setCancelable(true);
                shareDialog.show();

            }
        });

        if (sch.getAProduct(aProduct.getId()).getLike()==0){
            //this Product No Favorite
            holder.likeToolBar.setImageResource(R.mipmap.ic_like_toolbar);
            isLikeButtonClicked=false;
        }
        else{

            holder.likeToolBar.setImageResource(R.mipmap.ic_like_filled_toolbar);
            isLikeButtonClicked=true;
        }

        holder.likeToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sch.getAProduct(aProduct.getId()).getLike() == 0) {

                    if(Configuration.userLoginStatus)
                        Toast.makeText(myContext, myContext.getResources().getString(R.string.thanks), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(myContext,myContext.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_filled_toolbar);
                    isLikeButtonClicked = true;
                    sch.changeProductLike(aProduct.getId(), 1);
                } else if (sch.getAProduct(aProduct.getId()).getLike() == 1) {

                    if(!Configuration.userLoginStatus)
                        Toast.makeText(myContext,myContext.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_toolbar);
                    isLikeButtonClicked = false;
                    sch.changeProductLike(aProduct.getId(), 0);
                }
            }
        });

        //get main picture from server or cache
        String picCounter;
        if(aProduct.getImagesPath().size()==0)
            picCounter="no_image_path";
        else
            picCounter = aProduct.getImagesPath().get(0);

        holder.imgP.getLayoutParams().width=Configuration.shopDisplaySizeForShow;
        holder.imgP.getLayoutParams().height=Configuration.shopDisplaySizeForShow;
        try {
            picCounter= URLEncoder.encode(picCounter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_1 =aProduct.getImagesMainPath()+
                picCounter+
                "&size="+
                Configuration.shopDisplaySizeForURL +"x"+Configuration.shopDisplaySizeForURL +
                "&q=30";
        holder.imgLoader.DisplayImage(image_url_1, holder.imgP);

        //Drawable d=ResizeImage(R.drawable.loadingholder,holder.rowView,Configuration.shopDisplaySizeForShow);
        //Picasso.with(Configuration.ShopFragmentContext).load(image_url_1).into(holder.imgP);
        /*Drawable d=ResizeImage(R.drawable.loadingholder,rowView,Configuration.shopDisplaySizeForShow);
        final ProgressBar progressBar=(ProgressBar)rowView.findViewById(R.id.prograssBar);
        progressBar.getLayoutParams().height=Configuration.progressBarSize;
        progressBar.getLayoutParams().width=Configuration.progressBarSize;
        Glide.with(myContext)
                .load(image_url_1).override(Configuration.shopDisplaySizeForShow, Configuration.shopDisplaySizeForShow)
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
                //.placeholder(d)
                .error(d)
                .into(holder.imgP);*/

        holder.infoP.setText(aProduct.getTitle());
        String priceOfCurrentGood = String.valueOf(aProduct.getPrice());
        double amountOfFinalPrice = Double.parseDouble(priceOfCurrentGood);
        DecimalFormat formatter = new DecimalFormat("#,###,000");
        if (aProduct.getPrice()==0) {
            holder.originalPrice.setText("به زودی ");
        }
        else
            holder.originalPrice.setText(formatter.format(amountOfFinalPrice) + "  " + "تومان");
        holder.imgP.setImageBitmap(holder.image);
        holder.imgP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.rowView.getContext(), ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct", allProduct);
                intent.putExtra("position", position);
                holder.rowView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return allProduct.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView infoP;
        TextView originalPrice;
        TextView priceForYou;
        ImageView imgP;
        ProgressBar progressBar;
        ImageButton shareToolBar;
        ImageButton basketToolbar;
        ImageButton likeToolBar;
        ImageButton offerLeft;
        ImageButton offerRight;
        ImageLoader imgLoader;
        Bitmap image;
        View rowView;

        public Holder(View itemView) {
            super(itemView);
            PriceUtility priceUtility = new PriceUtility();
            Activity activity;
            activity =(Activity) myContext;
            infoP=(TextView) itemView.findViewById(R.id.txt_infoProduct);
            infoP = priceUtility.changeFontToYekan(infoP,activity);
            originalPrice =(TextView) itemView.findViewById(R.id.txt_priceProduct);
            originalPrice = priceUtility.changeFontToYekan(originalPrice,activity);
            priceForYou = (TextView)itemView.findViewById(R.id.txt_price_for_you);
            priceForYou = priceUtility.changeFontToYekan(priceForYou,activity);
            imgP=(ImageView) itemView.findViewById(R.id.imbt_picProduct);
            progressBar=(ProgressBar)itemView.findViewById(R.id.prograssBar);
            offerLeft = (ImageButton)itemView.findViewById(R.id.ic_offer_left);
            offerRight = (ImageButton)itemView.findViewById(R.id.ic_offer_right);
            shareToolBar = (ImageButton)itemView.findViewById(R.id.share_toolbar_in_main_page);
            likeToolBar = (ImageButton)itemView.findViewById(R.id.empty_like_toolbar);
            basketToolbar = (ImageButton)itemView.findViewById(R.id.basket_toolbar);
            rowView=itemView;
            image=null;
            imgLoader= new ImageLoader(myContext,itemView,Configuration.shopDisplaySizeForShow); // important
        }
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