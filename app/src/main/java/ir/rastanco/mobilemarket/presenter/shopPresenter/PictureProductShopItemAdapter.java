package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Connect;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.CounterIconUtils;

import static android.app.PendingIntent.getActivity;

/**
 * Created by ShaisteS on 12/28/2015.
 * A Customize Adapter For Shop Grid view
 */
public class PictureProductShopItemAdapter extends BaseAdapter{

    private static LayoutInflater inflater=null;
    private ArrayList<Product> allProduct;
    private ProgressBar imageLoading;
    private boolean isLikeButtonClicked = false;
    private boolean isSelectedForShop=false;
    private ServerConnectionHandler sch;
    private CounterIconUtils ciu;
    private Typeface brafficFont;
    private  Context context;

    public PictureProductShopItemAdapter(FragmentActivity mainActivity,ArrayList<Product> products) {

        context=mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        allProduct =products;
        sch=new ServerConnectionHandler(Configuration.superACFragment);

    }
    public PictureProductShopItemAdapter(Context context)
    {
        this.context = context;
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
        holder.priceP=(TextView) rowView.findViewById(R.id.txt_priceProduct);
        holder.priceP.setTypeface(brafficFont);
        holder.imgP=(ImageView) rowView.findViewById(R.id.imbt_picProduct);
        holder.offerLeft = (ImageButton)rowView.findViewById(R.id.ic_offer_left);
        holder.offerRight = (ImageButton)rowView.findViewById(R.id.ic_offer_right);
        holder.offerLeft.setVisibility(View.INVISIBLE);
        //if(holder.mProduct.getPriceOff() != 0)
        //{
            holder.offerRight.setVisibility(View.VISIBLE);
        //}
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1))
        {
            holder.offerRight.setVisibility(View.INVISIBLE);
         //  if(holder.mProduct.getPriceOff() != 0) {

                holder.offerLeft.setVisibility(View.VISIBLE);
           // }
           // else
           // {
             //   holder.offerLeft.setVisibility(View.INVISIBLE);
            //}
        }
        holder.shareToolBar = (ImageButton)rowView.findViewById(R.id.share_toolbar_in_main_page);
        holder.basketToolbar = (ImageButton)rowView.findViewById(R.id.basket_toolbar);
        holder.likeToolBar = (ImageButton)rowView.findViewById(R.id.empty_like_toolbar);
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
                    context.startActivity(new Intent(context,ShoppingBagActivity.class));
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

                final Dialog shareDialog = new Dialog(context);
                shareDialog.setContentView(R.layout.share_alert_dialog);
                ImageButton cancelShareDialog = (ImageButton) shareDialog.findViewById(R.id.close_pm_to_friend);
                final Button sendBtn = (Button)shareDialog.findViewById(R.id.send_my_pm);
                final EditText textToShare = (EditText)shareDialog.findViewById(R.id.text_to_send);
                cancelShareDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                    }
                });
                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String TextToSend = String.valueOf(textToShare.getText());
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,TextToSend);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://cafebazaar.ir/app/?id=com.Arvand.HundredPercent");
                        sendIntent.setType("text/plain");
                        context.startActivity(sendIntent);

                    }
                });
                shareDialog.setCancelable(true);
                shareDialog.show();

            }
        });

        if (allProduct.get(position).getLike()==0){
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
                if(isLikeButtonClicked == false){
                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_filled_toolbar);
                    isLikeButtonClicked = true;
                    sch.changeProductLike(allProduct.get(position).getId(), 1);
                }
                else if(isLikeButtonClicked){
                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_toolbar);
                    isLikeButtonClicked = false;
                    sch.changeProductLike(allProduct.get(position).getId(), 0);
                }
            }
        });

        ImageLoader imgLoader = new ImageLoader(Configuration.superACFragment); // important
        String picCounter = allProduct.get(position).getImagesPath().get(0);
        try {
            picCounter= URLEncoder.encode(picCounter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_1 = allProduct.get(position).getImagesMainPath()+
                picCounter+
                "&size="+
                Configuration.shopDisplaySize+"x"+Configuration.shopDisplaySize+
                "&q=30";
        imgLoader.DisplayImage(image_url_1, holder.imgP);
        holder.infoP.setText(allProduct.get(position).getTitle());
        String priceOfCurrentGood = String.valueOf(allProduct.get(position).getPrice());
        double amountOfFinalPrice = Double.parseDouble(priceOfCurrentGood);
        DecimalFormat formatter = new DecimalFormat("#,###,000");
        holder.priceP.setText(formatter.format(amountOfFinalPrice)+"  "+"تومان");
        holder.imgP.setImageBitmap(image);

        holder.imgP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rowView.getContext(), ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct",allProduct);
                intent.putExtra("position", position);
                rowView.getContext().startActivity(intent);
            }
        });
        return rowView;
    }

}