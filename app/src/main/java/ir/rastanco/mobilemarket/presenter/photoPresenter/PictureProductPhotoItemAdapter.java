package ir.rastanco.mobilemarket.presenter.photoPresenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;

/**
 * Created by ShaisteS on 12/28/2015.
 * A Customize Adapter For Home Grid view
 *
 */
public class PictureProductPhotoItemAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<Product> allProduct;

    // Constructor
    public PictureProductPhotoItemAdapter(Context c) {
        mContext = c;
    }

    public void setData(ArrayList<Product> product) {
        allProduct = product;
    }

    public int getCount() {
        return allProduct.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1,1,1,1);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(allProduct.get(position).getAllNormalImage().get(0));
        return imageView;
    }
}