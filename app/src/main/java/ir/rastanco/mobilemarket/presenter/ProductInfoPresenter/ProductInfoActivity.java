package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/10/16.
 */
public class ProductInfoActivity extends Activity {

    Product thisProduct;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        Configuration.MainActivityFragment=this;

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        thisProduct= (Product) bundle.getSerializable("thisProduct");
        ImageView imgProduct=(ImageView) findViewById(R.id.img_productInfo);
        ImageLoader imgLoader = new ImageLoader(Configuration.ProductInfoActivity); // important

        String picNum = thisProduct.getImagesPath().get(0);
        try {
            picNum= URLEncoder.encode(picNum, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String image_url_Main = thisProduct.getImagesMainPath()+
                picNum+
                "&size="+
                Configuration.homeDisplaySize+"x"+ Configuration.homeDisplaySize+
                "&q=30";
        imgLoader.DisplayImage(image_url_Main, imgProduct);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        for (int i = 0; i <thisProduct.getImagesPath().size() ; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 0, 2, 0);
            layout.addView(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            picNum = thisProduct.getImagesPath().get(i);
            try {
                picNum= URLEncoder.encode(picNum, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String image_url_otherPic = thisProduct.getImagesMainPath()+
                    picNum+
                    "&size="+
                    Configuration.shopDisplaySize+"x"+ Configuration.shopDisplaySize+
                    "&q=30";
            imgLoader.DisplayImage(image_url_otherPic, imageView);
        }

    }
}
