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
import ir.rastanco.mobilemarket.dataModel.serverHandler.DownloadImage;

/**
 * Created by ShaisteS on 12/27/2015.
 */
public class PictureProductHomeItemAdapter extends ArrayAdapter<Product> {

    private Activity myContext;
    private ArrayList<Product> products;
    private Bitmap imageProduct;

    public PictureProductHomeItemAdapter(Context context, int resource, ArrayList<Product> allProduct) {
        super(context, resource, allProduct);
        myContext=(Activity)context;
        products=allProduct;

    }

    public View getView(int position, View convertView, ViewGroup parent){

        try {
            imageProduct= new DownloadImage().execute("http://decoriss.com/roots/wm.php?code=uploads/data/15/1514%20(1).jpg").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        LayoutInflater inflater = myContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.picture_product_item_home, null);
        ImageView PicProductImage = (ImageView) rowView.findViewById(R.id.img_picProduct);
        PicProductImage.setImageBitmap(products.get(position).getAllNormalImage().get(0));

        ImageButton shareImgB=(ImageButton)rowView.findViewById(R.id.imbt_share);
        shareImgB.setBackgroundColor(Color.TRANSPARENT);
        return rowView;
    }
}
