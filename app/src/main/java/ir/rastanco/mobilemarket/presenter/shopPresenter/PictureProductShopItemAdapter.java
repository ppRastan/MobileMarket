package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 12/28/2015.
 * A Customize Adapter For Shop Grid view
 */
public class PictureProductShopItemAdapter extends BaseAdapter{

    private Context context;
    private static LayoutInflater inflater=null;
    private ArrayList<Product> allProduct;
    private ProgressBar imageLoading;
    private Typeface font;
    private boolean isLikeButtonClicked = true;
    public PictureProductShopItemAdapter(FragmentActivity mainActivity,ArrayList<Product> products) {

        context=mainActivity;
        font = Typeface.createFromAsset(mainActivity.getAssets(),"yekan_font.ttf");
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        allProduct =products;

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
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder=new Holder();
        Bitmap image=null;

        final View rowView;
        rowView = inflater.inflate(R.layout.picture_produc_item_shop, null);
        holder.infoP=(TextView) rowView.findViewById(R.id.txt_infoProduct);
        holder.priceP=(TextView) rowView.findViewById(R.id.txt_priceProduct);
        holder.imgP=(ImageView) rowView.findViewById(R.id.imbt_picProduct);
        holder.shareToolBar = (ImageButton)rowView.findViewById(R.id.share_toolbar);
        holder.basketToolbar = (ImageButton)rowView.findViewById(R.id.basket_toolbar);
        holder.likeToolBar = (ImageButton)rowView.findViewById(R.id.empty_like_toolbar);
        holder.shareToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Configuration.getConfig().activityContext,"share this product",Toast.LENGTH_SHORT).show();
            }
        });
        holder.likeToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLikeButtonClicked == false){

                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_fill_toolbar);
                    isLikeButtonClicked = true;
                    //TODO display area here
                }
                else if(isLikeButtonClicked){
                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_toolbar);
                    isLikeButtonClicked = false;
                    //TODO exit area mode
                }
            }
        });
        holder.basketToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.basketToolbar.setImageResource(R.mipmap.ic_shop_green_toolbar);
            }
        });
        holder.infoP.setTypeface(font);
        holder.priceP.setTypeface(font);
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
        holder.priceP.setText(String.valueOf(allProduct.get(position).getPrice()));
        holder.imgP.setImageBitmap(image);

        holder.imgP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle bundle=new Bundle();
                bundle.putSerializable("allProducts",allProduct);*/
                Intent intent=new Intent(rowView.getContext(), ProductInfoActivity.class);
                //intent.putExtras(bundle);
                intent.putExtra("position",position);
                rowView.getContext().startActivity(intent);
            }
        });
        return rowView;
    }

}