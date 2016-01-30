package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

/**
 * Created by ShaisteS on 1394/10/23.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<Product> products;
    private ArrayList<ProductOption> options;
    private LayoutInflater inflater;
    private ServerConnectionHandler sch;
    private int productsSize;
    private ImageButton btnInfo;
    private ImageButton btnShareByTelegram;
    private ImageButton btnShare;
    private ImageButton btnLike;
    private ImageButton btnBuy;
    private RatingBar rateOfProduct;
    private boolean isLikeButtonClicked = true;
    private  boolean isSelectedForShop=false;
    private ArrayList<Product> allProduct;
    // constructor
    public FullScreenImageAdapter(Activity activity,ArrayList<Product>allProducts,int allProductSize) {
        this.activity = activity;
        this.products=allProducts;
        this.productsSize=allProductSize;
        Activity context;
        sch=new ServerConnectionHandler(Configuration.ProductInfoActivity);
    }

    @Override
    public int getCount() {
        return productsSize;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.activity_product_info, container,false);

        /*TextView headerTitle=(TextView)viewLayout.findViewById(R.id.headerTitle);
        headerTitle.setText(products.get(position).getTitle());*/

        /*rateOfProduct = (RatingBar)viewLayout.findViewById(R.id.rank_of_product);
        rateOfProduct.setNumStars(5);*/
        //TODO for shayeste
        //TODO please fill ratingbar items dynamicly
        //rateOfProduct.setRating(2);
        btnInfo=(ImageButton)viewLayout.findViewById(R.id.img_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProductInfo = new Intent(viewLayout.getContext(),ProductOptionActivity.class);
                intentProductInfo.putExtra("productId", products.get(position).getId());
                intentProductInfo.putExtra("groupId", products.get(position).getGroupId());
                viewLayout.getContext().startActivity(intentProductInfo);

            }
        });
        btnBuy = (ImageButton)viewLayout.findViewById(R.id.shopping_full_screen);
//        if (sch.checkSelectProductForShop(allProduct.get(position).getId()))
//            btnBuy.setImageResource(R.mipmap.green_bye_toolbar);
//        else
//            btnBuy.setImageResource(R.mipmap.bye_toolbar);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (isSelectedForShop==false) {
//                    btnBuy.setImageResource(R.mipmap.green_bye_toolbar);
//                    isSelectedForShop=true;
//                    sch.addProductToShoppingBag(allProduct.get(position).getId());
//
//                }
//
//                else if (isSelectedForShop==true){
//                    btnBuy.setImageResource(R.mipmap.bye_toolbar);
//                    isSelectedForShop=false;
//                    sch.deleteAProductShopping(allProduct.get(position).getId());
//                }
            }
        });
        btnLike = (ImageButton)viewLayout.findViewById(R.id.add_to_favorite);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLikeButtonClicked == false){

                    btnLike.setImageResource(R.mipmap.ic_like_filled_toolbar);
                    isLikeButtonClicked = true;
                }
                else if(isLikeButtonClicked) {
                    btnLike.setImageResource(R.mipmap.ic_like_toolbar);
                    isLikeButtonClicked = false;
                }
            }
        });
        btnShare = (ImageButton)viewLayout.findViewById(R.id.img_share_full_screen);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnShareByTelegram = (ImageButton)viewLayout.findViewById(R.id.telegram_share);
        btnShareByTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        for (int i = 1; i < products.get(position).getImagesPath().size(); i++) {
            final ImageView imageView = new ImageView(Configuration.ProductInfoActivity);
            imageView.setId(i-1);
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
                    Configuration.articleDisplaySize + "x" + Configuration.articleDisplaySize +
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
                    imgLoader.DisplayImage(image_url_otherPic, imgProduct);

                }
            });
        }
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }
}