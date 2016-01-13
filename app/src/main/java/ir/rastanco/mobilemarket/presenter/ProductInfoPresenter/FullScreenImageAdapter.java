package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

/**
 * Created by ShaisteS on 1394/10/23.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.utility.Configuration;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<Product> products;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,ArrayList<Product>allProducts) {
        this.activity = activity;
        this.products=allProducts;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ScrollView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.activity_product_info, container,
                false);

        final ImageView imgProduct = (ImageView) viewLayout.findViewById(R.id.img_productInfo);
        final ImageLoader imgLoader = new ImageLoader(Configuration.ProductInfoActivity); // important
        String picNum = products.get(position).getImagesPath().get(0);
        try {
            picNum = URLEncoder.encode(picNum, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String image_url_Main = products.get(position).getImagesMainPath() +
                picNum +
                "&size=" +
                Configuration.homeDisplaySize + "x" + Configuration.productInfoHeightSize +
                "&q=30";
        imgLoader.DisplayImage(image_url_Main, imgProduct);
        LinearLayout layout = (LinearLayout) viewLayout.findViewById(R.id.linear);
        for (int i = 0; i < products.get(position).getImagesPath().size(); i++) {
            final ImageView imageView = new ImageView(Configuration.ProductInfoActivity);
            imageView.setId(i);
            imageView.setPadding(2, 0, 2, 0);
            layout.addView(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            picNum = products.get(position).getImagesPath().get(i);
            try {
                picNum = URLEncoder.encode(picNum, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String image_url_otherPic = products.get(position).getImagesMainPath() +
                    picNum +
                    "&size=" +
                    Configuration.shopDisplaySize + "x" + Configuration.shopDisplaySize +
                    "&q=30";
            imgLoader.DisplayImage(image_url_otherPic, imageView);

            final int parentClickImage=position;
            final int clickImageNum=i;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String picNum = products.get(parentClickImage).getImagesPath().get(clickImageNum);
                    try {
                        picNum = URLEncoder.encode(picNum, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String image_url_otherPic = products.get(parentClickImage).getImagesMainPath() +
                            picNum +
                            "&size=" +
                            Configuration.homeDisplaySize + "x" + Configuration.productInfoHeightSize +
                            "&q=30";
                    imgLoader.DisplayImage(image_url_otherPic,imgProduct);

                }
            });
        }
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ScrollView) object);

    }
}