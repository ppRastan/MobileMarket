package ir.rastanco.mobilemarket.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;

/**
 * Created by ShaisteS on 12/27/2015.
 */
public class PictureProductItemAdapter extends ArrayAdapter<Product> {

    private Activity myContext;
    private ArrayList<Product> products;

    public PictureProductItemAdapter(Context context, int resource, ArrayList<Product> allProduct) {
        super(context, resource, allProduct);
        myContext=(Activity)context;
        products=allProduct;

    }

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = myContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.picture_product_item, null);
        ImageView PicProductImage = (ImageView) rowView.findViewById(R.id.img_picProduct);
        PicProductImage.setImageResource(products.get(position).getPic());

        ImageButton shareImgB=(ImageButton)rowView.findViewById(R.id.imbt_share);
        shareImgB.setBackgroundColor(Color.TRANSPARENT);

        return rowView;
    }
}
