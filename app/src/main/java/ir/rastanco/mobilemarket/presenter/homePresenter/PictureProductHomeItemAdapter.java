package ir.rastanco.mobilemarket.presenter.homePresenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.DownloadImage;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 12/27/2015.
 * A Customize Adapter For Home List view
 */
public class PictureProductHomeItemAdapter extends ArrayAdapter<Product> {

    private Activity myContext;
    private ArrayList<Product> allProduct;
    private Bitmap imageProduct;

    public PictureProductHomeItemAdapter(Context context, int resource, ArrayList<Product> products) {
        super(context, resource,products);
        myContext=(Activity)context;
        allProduct=products;

    }

    public View getView(int position, View convertView, ViewGroup parent){

        Bitmap image=null;

        try {
            image=new DownloadImage()
                    .execute(allProduct.get(position).getImagesMainPath()+
                            allProduct.get(position).getImagesPath().get(0)+
                            "&size="+
                            Configuration.homeDisplaySize+"x"+Configuration.homeDisplaySize+
                            "&q=30").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        LayoutInflater inflater = myContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.picture_product_item_home, null);
        ImageView PicProductImage = (ImageView) rowView.findViewById(R.id.img_picProduct);
        PicProductImage.setImageBitmap(image);

        ImageButton shareImgB=(ImageButton)rowView.findViewById(R.id.imbt_share);
        shareImgB.setBackgroundColor(Color.TRANSPARENT);
        return rowView;
    }
}
