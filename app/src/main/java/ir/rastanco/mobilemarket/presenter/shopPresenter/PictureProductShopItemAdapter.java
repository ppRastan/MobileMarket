package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Connect;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.CounterIconUtils;

/**
 * Created by ShaisteS on 12/28/2015.
 * A Customize Adapter For Shop Grid view
 */
public class PictureProductShopItemAdapter extends BaseAdapter{

    private static LayoutInflater inflater=null;
    private ArrayList<Product> allProduct;
    private ProgressBar imageLoading;
    private boolean isLikeButtonClicked = true;
    private boolean isSelectedForShop=false;
    private ServerConnectionHandler sch;
    private CounterIconUtils ciu;
    private  Context myContext;
    private Typeface yekanFont;
    private String textToSend = null;
    private Dialog shareDialog;
    private ImageButton cancelShareDialog;
    private Button sendBtn;
    private EditText editTextToShare;
    private Intent sendIntent;
    public PictureProductShopItemAdapter(FragmentActivity mainActivity,ArrayList<Product> products) {

        myContext =mainActivity;
        inflater = ( LayoutInflater ) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        allProduct =products;
        sch=new ServerConnectionHandler(Configuration.superACFragment);
        yekanFont = Typeface.createFromAsset(myContext.getAssets(), "fonts/yekan.ttf");

    }
    public PictureProductShopItemAdapter(Context context)
    {
        this.myContext = context;
    }

    @Override
    public int getCount() {
        return allProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView infoP;
        TextView priceP;
        ImageView imgP;
        ImageButton shareToolBar;
        ImageButton basketToolbar;
        ImageButton likeToolBar;
        ImageButton offerLeft;
        ImageButton offerRight;
        Product mProduct;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder=new Holder();
        final Bitmap image=null;
        holder.mProduct = new Product();

        final View rowView;
        rowView = inflater.inflate(R.layout.picture_produc_item_shop, null);

        holder.infoP=(TextView) rowView.findViewById(R.id.txt_infoProduct);
        holder.infoP.setTypeface(yekanFont);
        holder.priceP=(TextView) rowView.findViewById(R.id.txt_priceProduct);
        holder.priceP.setTypeface(yekanFont);

        holder.imgP=(ImageView) rowView.findViewById(R.id.imbt_picProduct);
        holder.imgP.getLayoutParams().width=Configuration.shopDisplaySizeForShow;
        holder.imgP.getLayoutParams().height=Configuration.shopDisplaySizeForShow;

        holder.offerLeft = (ImageButton)rowView.findViewById(R.id.ic_offer_left);
        holder.offerRight = (ImageButton)rowView.findViewById(R.id.ic_offer_right);
       if(Configuration.RTL)
       {

            holder.offerLeft.setVisibility(View.GONE);
            if(allProduct.get(position).getPriceOff() != 0)
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
              if(allProduct.get(position).getPriceOff() != 0) {

                holder.offerLeft.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.offerLeft.setVisibility(View.GONE);
            }
        }
        holder.shareToolBar = (ImageButton)rowView.findViewById(R.id.share_toolbar_in_main_page);
        holder.likeToolBar = (ImageButton)rowView.findViewById(R.id.empty_like_toolbar);
        holder.basketToolbar = (ImageButton)rowView.findViewById(R.id.basket_toolbar);

        if (allProduct.get(position).getPrice()==0)
            holder.basketToolbar.setVisibility(View.GONE);

        if (sch.checkSelectProductForShop(allProduct.get(position).getId()))
            holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
        else
            holder.basketToolbar.setImageResource(R.mipmap.bye_toolbar);

        holder.basketToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSelectedForShop==false) {
                    holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
                    isSelectedForShop=true;
                    sch.addProductToShoppingBag(allProduct.get(position).getId(),1);
                    myContext.startActivity(new Intent(myContext, ShoppingBagActivity.class));
                    Connect.setMyBoolean(true);
                    isSelectedForShop = true;

                }

                else if (isSelectedForShop==true){
                    holder.basketToolbar.setImageResource(R.mipmap.bye_toolbar);
                    isSelectedForShop=false;
                    sch.deleteAProductShopping(allProduct.get(position).getId());
                    Connect.setMyBoolean(false);
                    isSelectedForShop = false;

                }
            }
        });


        holder.shareToolBar.setOnClickListener(new View.OnClickListener() {
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
                        String share=textToSend+"\n\n"+
                                allProduct.get(position).getLinkInSite()+ "\n\n"+
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

        if (sch.getAProduct(allProduct.get(position).getId()).getLike()==0){
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
                if (sch.getAProduct(allProduct.get(position).getId()).getLike() == 0) {
                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_filled_toolbar);
                    isLikeButtonClicked = true;
                    sch.changeProductLike(allProduct.get(position).getId(), 1);
                } else if (sch.getAProduct(allProduct.get(position).getId()).getLike() == 1) {
                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_toolbar);
                    isLikeButtonClicked = false;
                    sch.changeProductLike(allProduct.get(position).getId(), 0);
                }
            }
        });

//        ImageLoader imgLoader = new ImageLoader(Configuration.superACFragment); // important
        String picCounter = allProduct.get(position).getImagesPath().get(0);
        try {
            picCounter= URLEncoder.encode(picCounter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_1 = allProduct.get(position).getImagesMainPath()+
                picCounter+
                "&size="+
                Configuration.shopDisplaySizeForURL +"x"+Configuration.shopDisplaySizeForURL +
                "&q=30";
//       Picasso.with(Configuration.superACFragment).load(image_url_1).into(holder.imgP);
//       imgLoader.DisplayImage(image_url_1, holder.imgP);

        Drawable d=ResizeImage(R.drawable.loadingholder,rowView,Configuration.shopDisplaySizeForShow);
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
                .into(holder.imgP);

        holder.infoP.setText(allProduct.get(position).getTitle());
        String priceOfCurrentGood = String.valueOf(allProduct.get(position).getPrice());
        double amountOfFinalPrice = Double.parseDouble(priceOfCurrentGood);
        DecimalFormat formatter = new DecimalFormat("#,###,000");
        if (allProduct.get(position).getPrice()==0)
            holder.priceP.setText("به زودی ");
        else
            holder.priceP.setText(formatter.format(amountOfFinalPrice) + "  " + "تومان");
        holder.imgP.setImageBitmap(image);
        holder.imgP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rowView.getContext(), ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct", allProduct);
                intent.putExtra("position", position);
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