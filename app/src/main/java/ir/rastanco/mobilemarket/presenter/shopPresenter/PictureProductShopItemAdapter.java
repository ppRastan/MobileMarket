package ir.rastanco.mobilemarket.presenter.shopPresenter;

/**
 * Created by ShaisteS on 1394/10/7.
 * A Customize Adapter For Shop Grid view
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.PriceUtility;
import ir.rastanco.mobilemarket.utility.ToolbarHandler;
import ir.rastanco.mobilemarket.utility.Utilities;


public class PictureProductShopItemAdapter extends ArrayAdapter<Product>{

    private final ArrayList<Product> allProduct;
    private boolean isLikeButtonClicked = true;
    private boolean isSelectedForShop=false;
    private final ServerConnectionHandler sch;
    private final Context myContext;
    private final Activity shopPresenterActivity;
    private final Drawable defaultPicture;

    public PictureProductShopItemAdapter(FragmentActivity mainActivity,ArrayList<Product> products) {
        super(mainActivity, R.layout.article_item, products);

        myContext =mainActivity;
        allProduct =products;
        sch=ServerConnectionHandler.getInstance(myContext);
        shopPresenterActivity =(Activity) myContext;
        defaultPicture=Utilities.getInstance().ResizeImage(R.drawable.loadingholder, myContext, Configuration.getConfig().shopDisplaySizeForShow);
    }

    @Override
    public int getCount() {
        return allProduct.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView infoP;
        private TextView originalPrice;
        private TextView priceForYou;
        private ImageView imgP;
        private ImageButton shareToolBar;
        private ImageButton basketToolbar;
        private ImageButton likeToolBar;
        private ImageButton offerRight;
        private ImageLoader imgLoader;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView==null){
            LayoutInflater inflater = ( LayoutInflater ) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.picture_produc_item_shop, parent , false);
            holder=new ViewHolder();

            holder.infoP=(TextView) convertView.findViewById(R.id.txt_infoProduct);
            holder.infoP= PriceUtility.getInstance().changeFontToYekan(holder.infoP, myContext);
            holder.originalPrice =(TextView) convertView.findViewById(R.id.txt_priceProduct);
            holder.originalPrice =PriceUtility.getInstance().changeFontToYekan(holder.originalPrice,myContext);
            holder.priceForYou = (TextView)convertView.findViewById(R.id.txt_price_for_you);
            holder.priceForYou=PriceUtility.getInstance().changeFontToYekan(holder.priceForYou,myContext);
            holder.imgP=(ImageView) convertView.findViewById(R.id.imageButton_picProduct);
            holder.imgP.getLayoutParams().width= Configuration.getConfig().shopDisplaySizeForShow;
            holder.imgP.getLayoutParams().height=Configuration.getConfig().shopDisplaySizeForShow;
            holder.offerRight = (ImageButton)convertView.findViewById(R.id.ic_offer_right);
            holder. basketToolbar = (ImageButton)convertView.findViewById(R.id.basket_toolbar);
            holder.shareToolBar = (ImageButton)convertView.findViewById(R.id.share_toolbar_in_main_page);
            holder.likeToolBar = (ImageButton)convertView.findViewById(R.id.empty_like_toolbar);
            holder.imgLoader= new ImageLoader(myContext,Configuration.getConfig().shopDisplaySizeForShow);convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();


        final Product eachProduct =allProduct.get(position);
        if (eachProduct.getPriceOff()==0 && eachProduct.getPrice()!=0){
            holder.priceForYou.setVisibility(View.INVISIBLE);
            holder.originalPrice.setTextColor(Color.BLACK);
            holder.originalPrice.setText(PriceUtility.getInstance().formatPriceCommaSeprated(eachProduct.getPrice()));
            holder.basketToolbar.setVisibility(View.VISIBLE);
        }
        else if(eachProduct.getPrice()!=0) {
            int price= eachProduct.getPrice();
            int discountPercent=eachProduct.getPriceOff();
            int finalPrice= Utilities.getInstance().calculatePriceOffProduct(price,discountPercent);
            holder.originalPrice.setTextColor(Color.RED);
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.originalPrice.setText(PriceUtility.getInstance().formatPriceCommaSeprated(price));
            holder.priceForYou = PriceUtility.getInstance().changeFontToYekan(holder.priceForYou, shopPresenterActivity);
            holder.priceForYou.setText(PriceUtility.getInstance().formatPriceCommaSeprated(finalPrice));
            holder.priceForYou.setVisibility(View.VISIBLE);
            holder.basketToolbar.setVisibility(View.VISIBLE);
        }
        else if(eachProduct.getPriceOff()==0){
            holder.basketToolbar.setVisibility(View.GONE);
            holder.originalPrice.setText(myContext.getString(R.string.coming_soon));
        }

        if(Configuration.getConfig().RTL)
        {

            if(eachProduct.getPriceOff() != 0)
            {
                holder.offerRight.setVisibility(View.VISIBLE);
            }
            else {
                holder.offerRight.setVisibility(View.GONE);
            }
        }

        if (sch.checkSelectProductForShop(eachProduct.getId()))
            holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
        else
            holder.basketToolbar.setImageResource(R.mipmap.bye_toolbar);

        holder.basketToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSelectedForShop) {
                    holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
                    isSelectedForShop=true;
                    sch.addProductToShoppingBag(eachProduct.getId());
                    myContext.startActivity(new Intent(myContext, ShoppingBagActivity.class));
                    ObserverShopping.setMyBoolean(true);
                    isSelectedForShop = true;

                }

                else if (isSelectedForShop)
                {
                    holder.basketToolbar.setImageResource(R.mipmap.bye_toolbar);
                    isSelectedForShop=false;
                    sch.deleteAProductShopping(eachProduct.getId());
                    ObserverShopping.setMyBoolean(false);
                    isSelectedForShop = false;
                }
            }
        });

        holder.shareToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler toolbarHandler = new ToolbarHandler();
                toolbarHandler.generalShare(shopPresenterActivity, eachProduct.getLinkInSite());
            }
        });

        if (sch.getAProduct(eachProduct.getId()).getLike()==0){
            //this Product No Favorite
            holder.likeToolBar.setImageResource(R.mipmap.ic_like_toolbar);
            isLikeButtonClicked=false;
        }
        else{

            holder.likeToolBar.setImageResource(R.mipmap.ic_like_filled_toolbar);
            isLikeButtonClicked=true;
        }

        holder.likeToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sch.getAProduct(eachProduct.getId()).getLike() == 0) {

                    if(Configuration.getConfig().userLoginStatus)
                        Toast.makeText(myContext, myContext.getResources().getString(R.string.thanks), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(myContext,myContext.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_filled_toolbar);
                    isLikeButtonClicked = true;
                    sch.changeProductLike(eachProduct.getId(), 1);
                } else if (sch.getAProduct(eachProduct.getId()).getLike() == 1) {

                    if(!Configuration.getConfig().userLoginStatus)
                        Toast.makeText(myContext,myContext.getResources().getString(R.string.pleaseLogin),Toast.LENGTH_LONG).show();

                    holder.likeToolBar.setImageResource(R.mipmap.ic_like_toolbar);
                    isLikeButtonClicked = false;
                    sch.changeProductLike(eachProduct.getId(), 0);
                }
            }
        });

        //get main picture from server or cache
        String imageNumberPath;
        if(eachProduct.getImagesPath().size()==0)
            imageNumberPath="no_image_path";
        else
            imageNumberPath = eachProduct.getImagesPath().get(0);

        try {
            imageNumberPath= URLEncoder.encode(imageNumberPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String imageURL = Link.getInstance().generateURLForGetImageProduct(eachProduct.getImagesMainPath(),imageNumberPath,Configuration.getConfig().shopDisplaySizeForURL,Configuration.getConfig().shopDisplaySizeForURL);
        holder.imgP.setImageDrawable(defaultPicture);
        holder.imgLoader.DisplayImage(imageURL, holder.imgP);
        holder.infoP.setText(eachProduct.getTitle());

        final View finalConvertView = convertView;
        final View finalConvertView1 = convertView;
        holder.imgP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finalConvertView.getContext(), ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct", allProduct);
                intent.putExtra("position", position);
                finalConvertView1.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}

