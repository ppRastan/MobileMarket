package ir.rastanco.mobilemarket.presenter.homePresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 12/27/2015.
 * A Customize Adapter For Home List view
 */
public class PictureProductHomeItemAdapter extends ArrayAdapter<Product>  {

    private Activity myContext;
    private ArrayList<Product> allProduct;

    public PictureProductHomeItemAdapter(Context context, int resource, ArrayList<Product> products) {
        super(context, resource,products);
        myContext=(Activity)context;
        allProduct=products;

    }

    public View getView(final int position, View convertView, ViewGroup parent){

        Bitmap image=null;
        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.picture_product_item_home, null);


        ImageLoader imgLoader = new ImageLoader(Configuration.superACFragment); // important
        ImageView PicProductImage = (ImageView) rowView.findViewById(R.id.img_picProduct);
        String picCounter = allProduct.get(position).getImagesPath().get(0);
        try {
            picCounter= URLEncoder.encode(picCounter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_1 = allProduct.get(position).getImagesMainPath()+picCounter+
                "&size="+
                Configuration.homeDisplaySize+"x"+Configuration.homeDisplaySize+
                "&q=30";
        imgLoader.DisplayImage(image_url_1, PicProductImage);

        ImageButton shareImgB=(ImageButton)rowView.findViewById(R.id.imbt_share);
        shareImgB.setBackgroundColor(Color.TRANSPARENT);

        PicProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("allProducts", allProduct);
                Intent intent = new Intent(rowView.getContext(), ProductInfoActivity.class);
                intent.putExtras(bundle);
                rowView.getContext().startActivity(intent);
            }
        });
        return rowView;
    }
}
