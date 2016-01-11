package ir.rastanco.mobilemarket.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
    Bitmap image;
    ProgressBar loadingImage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        thisProduct= (Product) bundle.getSerializable("thisProduct");
        ImageView imgProduct=(ImageView) findViewById(R.id.img_productInfo);
        ImageLoader imgLoader = new ImageLoader(Configuration.superACFragment); // important
        String image_url_1 = thisProduct.getImagesMainPath()+
               thisProduct.getImagesPath().get(0)+
                "&size="+
                Configuration.homeDisplaySize+"x"+ Configuration.homeDisplaySize+
                "&q=30";
        imgLoader.DisplayImage(image_url_1, imgProduct);

    }
}
