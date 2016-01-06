package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverHandler.DownloadImage;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 12/28/2015.
 * A Customize Adapter For Shop Grid view
 */
public class PictureProductShopItemAdapter extends BaseAdapter{

    private Context context;
    private static LayoutInflater inflater=null;
    private ArrayList<Product> allProduct;

    public PictureProductShopItemAdapter(FragmentActivity mainActivity,ArrayList<Product> products) {

        context=mainActivity;
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
        View rowView;
        Bitmap image=null;

        try {
            image=new DownloadImage()
                    .execute(allProduct.get(position).getImagesMainPath()+
                            allProduct.get(position).getImagesPath().get(0)+
                            "&size="+
                            Configuration.shopDisplaySize+"x"+Configuration.shopDisplaySize+
                            "&q=30").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        rowView = inflater.inflate(R.layout.picture_produc_item_shop, null);
        holder.infoP=(TextView) rowView.findViewById(R.id.txt_infoProduct);
        holder.priceP=(TextView) rowView.findViewById(R.id.txt_priceProduct);
        holder.imgP=(ImageView) rowView.findViewById(R.id.imbt_picProduct);

        holder.infoP.setText(allProduct.get(position).getTitle());
        holder.priceP.setText(String.valueOf(allProduct.get(position).getPrice()));
        holder.imgP.setImageBitmap(image);

        return rowView;
    }

}