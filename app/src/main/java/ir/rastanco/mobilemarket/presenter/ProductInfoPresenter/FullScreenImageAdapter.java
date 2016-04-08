package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

/**
 * Created by ShaisteS on 1394/10/23.
 * A pagerAdapter for show product information and picture
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverLike;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.PriceUtility;
import ir.rastanco.mobilemarket.utility.ToolbarHandler;
import ir.rastanco.mobilemarket.utility.Utilities;

public class FullScreenImageAdapter extends PagerAdapter {

    private final Activity activity;
    private final ArrayList<Product> products;
    private final ServerConnectionHandler sch;
    private final int  productsSize;
    private View viewLayout;
    public FullScreenImageAdapter(Activity activity,ArrayList<Product>allProducts,int allProductSize) {
        this.activity = activity;
        this.products=allProducts;
        this.productsSize=allProductSize;
        sch=ServerConnectionHandler.getInstance(Configuration.getConfig().productInfoActivityContext);
    }

    @Override
    public int getCount() {
        return productsSize;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        LayoutInflater  inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewLayout = inflater.inflate(R.layout.activity_product_info, container, false);
        final Product aProduct=products.get(position);
        Button addToBasketBtn = (Button)viewLayout.findViewById(R.id.full_screen_add_to_basket_btn);
        TextView nameOfCurrentProduct = (TextView)viewLayout.findViewById(R.id.name_of_photo);
        nameOfCurrentProduct.setText(aProduct.getTitle());

        setProductQuality(aProduct.getQualityRank());
        if (aProduct.getPrice()==0){
            addToBasketBtn.setText(activity.getString(R.string.coming_soon));
            addToBasketBtn.setCompoundDrawables(null,null,null,null);
            addToBasketBtn.setEnabled(false);

        }

        if (aProduct.getPriceOff()==0 && aProduct.getPrice()!=0){
            int price=aProduct.getPrice();
            String numberOfFinalPrice = String.valueOf(price);
            addToBasketBtn.setText(activity.getString(R.string.FullScreenImageAdapterproductOriginalPrice,PriceUtility.getInstance().formatPriceCommaSeprated(Integer.valueOf(numberOfFinalPrice))));
        }
        if (aProduct.getPriceOff()!=0 && aProduct.getPrice()!=0)
        {
            int price=aProduct.getPrice();
            int priceOff=aProduct.getPriceOff();
            int priceForYou= Utilities.getInstance().calculatePriceOffProduct(price,priceOff);
            String numberOfFinalPrice = String.valueOf(priceForYou);
            addToBasketBtn.setText(activity.getString(R.string.FullScreenImageAdapterproductPrice,PriceUtility.getInstance().formatPriceCommaSeprated(Integer.valueOf(numberOfFinalPrice))));

        }

        addToBasketBtn = PriceUtility.getInstance().ChangeButtonFont(addToBasketBtn,activity);
        addToBasketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sch.addProductToShoppingBag(aProduct.getId());
                Configuration.getConfig().productInfoActivityContext.startActivity(new Intent(Configuration.getConfig().productInfoActivityContext, ShoppingBagActivity.class));
                ObserverShopping.setMyBoolean(true);
            }
        });

        sch.getAllProductOptionOfAProduct(aProduct.getId(),
                aProduct.getGroupId());

         final ImageButton btnLike = (ImageButton)viewLayout.findViewById(R.id.add_to_favorite);

        if (sch.getAProduct(aProduct.getId()).getLike()==0){
            //this Product No Favorite
            btnLike.setImageResource(R.mipmap.ic_like_toolbar);
        }
        else{

            btnLike.setImageResource(R.mipmap.ic_like_filled_toolbar);
        }
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(sch.getAProduct(aProduct.getId()).getLike()==0){

                    if(Configuration.getConfig().userLoginStatus)
                        Toast.makeText(activity,activity.getResources().getString(R.string.thanks),Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(activity,activity.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    btnLike.setImageResource(R.mipmap.ic_like_filled_toolbar);
                    aProduct.setLike(1);
                    sch.changeProductLike(aProduct.getId(), 1);
                    ObserverLike.setLikeStatus(position);


                }
                else if(sch.getAProduct(aProduct.getId()).getLike()==1){

                    if(!Configuration.getConfig().userLoginStatus)
                        Toast.makeText(activity,activity.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    btnLike.setImageResource(R.mipmap.ic_like_toolbar);
                    aProduct.setLike(0);
                    sch.changeProductLike(aProduct.getId(), 0);
                    ObserverLike.setLikeStatus(position);
                }
            }
        });


        ImageButton  btnInfo=(ImageButton)viewLayout.findViewById(R.id.img_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProductInfo = new Intent(viewLayout.getContext(),ProductOptionActivity.class);
                intentProductInfo.putExtra("productId", aProduct.getId());
                intentProductInfo.putExtra("groupId", aProduct.getGroupId());
                viewLayout.getContext().startActivity(intentProductInfo);
                activity.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

            }
        });
        ImageButton btnShare = (ImageButton)viewLayout.findViewById(R.id.img_share_full_screen);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler.getInstance().generalShare(activity, aProduct.getLinkInSite());
             }
        });


        ImageButton  btnShareByTelegram = (ImageButton)viewLayout.findViewById(R.id.telegram_share);
        btnShareByTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler.getInstance().shareByTelegram(activity, aProduct.getLinkInSite());
            }
        });
        final ImageView imgProduct = (ImageView) viewLayout.findViewById(R.id.img_productInfo);
        imgProduct.getLayoutParams().width=Configuration.getConfig().homeDisplaySizeForShow;
        imgProduct.getLayoutParams().height=Configuration.getConfig().productInfoHeightForShow;
        final ImageLoader imgLoader = new ImageLoader(Configuration.getConfig().productInfoActivityContext,Configuration.getConfig().homeDisplaySizeForShow); // important

        String imageNumberPath;
        if(aProduct.getImagesPath().size()==0)
            imageNumberPath="no_image_path";
        else
            imageNumberPath = aProduct.getImagesPath().get(0);

        try {
            imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_Main = Link.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(),imageNumberPath,Configuration.getConfig().homeDisplaySizeForURL,Configuration.getConfig().productInfoHeightForURL);
        imgLoader.DisplayImage(image_url_Main, imgProduct);
        LinearLayout layout = (LinearLayout) viewLayout.findViewById(R.id.linear);
        int counter;
        if(aProduct.getImagesPath().size()>1)
            counter=0;
        else
            counter=1;

        for (int i = counter; i < aProduct.getImagesPath().size(); i++) {
            final ImageView imageView = new ImageView(Configuration.getConfig().productInfoActivityContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Configuration.getConfig().articleDisplaySizeForShow,Configuration.getConfig().articleDisplaySizeForShow);
            imageView.setLayoutParams(layoutParams);
            imageView.setId(i - 1);
            imageView.setPadding(1, 1, 1, 0);
            layout.addView(imageView);
            imageNumberPath = aProduct.getImagesPath().get(i);
            try {
                imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String image_url_otherPic = Link.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(),imageNumberPath,Configuration.getConfig().articleDisplaySizeForURL,Configuration.getConfig().articleDisplaySizeForURL);
            imgLoader.DisplayImage(image_url_otherPic, imageView);

            final int clickImageNum=i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String imageNumberPath = aProduct.getImagesPath().get(clickImageNum);
                    try {
                        imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String image_url_otherPic = Link.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(),imageNumberPath,Configuration.getConfig().homeDisplaySizeForURL,Configuration.getConfig().productInfoHeightForURL);
                    imgLoader.DisplayImage(image_url_otherPic, imgProduct);

                }
            });

        }
        container.addView(viewLayout);
        return viewLayout;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);

    }

    private void setProductQuality(String quality){
        ImageView imgProductQuality=(ImageView)viewLayout.findViewById(R.id.img_ProductQuality);
        switch (quality){
            case "a":
                imgProductQuality.setImageResource(R.drawable.darajeha);
                break;
            case "b":
                imgProductQuality.setImageResource(R.drawable.darajehb);
                break;
            case "c":
                imgProductQuality.setImageResource(R.drawable.darajehc);
                break;
            case "d":
                imgProductQuality.setImageResource(R.drawable.darajehd);
                break;
            case "e":
                imgProductQuality.setImageResource(R.drawable.darajehe);
                break;
            case "f":
                imgProductQuality.setImageResource(R.drawable.darajehf);
                break;
        }


    }
}