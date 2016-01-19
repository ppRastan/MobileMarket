package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        Bitmap image=null;

        final View rowView;
        rowView = inflater.inflate(R.layout.picture_produc_item_shop, null);
        holder.infoP=(TextView) rowView.findViewById(R.id.txt_infoProduct);
        holder.priceP=(TextView) rowView.findViewById(R.id.txt_priceProduct);
        holder.imgP=(ImageView) rowView.findViewById(R.id.imbt_picProduct);
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