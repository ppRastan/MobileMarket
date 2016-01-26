package ir.rastanco.mobilemarket.presenter.photoPresenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
 * A Customize Adapter For Home Grid view
 *
 */
public class PictureProductPhotoItemAdapter extends BaseAdapter{

    private Context context;
    private static LayoutInflater inflater=null;
    private ArrayList<Product> allProduct;


    public PictureProductPhotoItemAdapter(FragmentActivity mainActivity,ArrayList<Product> products) {

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
        ImageView imgP;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();

        final View rowView;
        rowView = inflater.inflate(R.layout.picture_product_item_photo, null);
        ImageLoader imgLoader = new ImageLoader(Configuration.superACFragment); // important
        holder.imgP=(ImageView) rowView.findViewById(R.id.imgb_picProduct);
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

        holder.imgP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(rowView.getContext(), ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct",allProduct);
                intent.putExtra("position",position);
                rowView.getContext().startActivity(intent);
            }
        });
        return rowView;
    }
}