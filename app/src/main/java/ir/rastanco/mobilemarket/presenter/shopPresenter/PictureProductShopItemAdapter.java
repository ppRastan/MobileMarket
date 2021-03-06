package ir.rastanco.mobilemarket.presenter.shopPresenter;

/**
 * Created by ShaisteS on 1394/10/7.
 * A Customize Adapter For Shop Grid view
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.PriceUtility;
import ir.rastanco.mobilemarket.utility.ToolbarHandler;
import ir.rastanco.mobilemarket.utility.Utilities;
import jp.shts.android.library.TriangleLabelView;

public class PictureProductShopItemAdapter extends ArrayAdapter<Product> {

    private final ArrayList<Product> allProduct;
    private final ServerConnectionHandler sch;
    private final Context myContext;
    private final Activity shopPresenterActivity;
    private Drawable defaultPicture=null;
    public PictureProductShopItemAdapter(FragmentActivity mainActivity, ArrayList<Product> products) {
        super(mainActivity, R.layout.article_item, products);

        myContext = mainActivity;
        allProduct = products;
        sch = ServerConnectionHandler.getInstance(myContext);
        shopPresenterActivity = (Activity) myContext;
        if (defaultPicture==null)
            defaultPicture = Utilities.getInstance().ResizeImage(R.drawable.loadingholder, myContext, Configuration.getConfig().shopDisplaySizeForShow);
    }

    @Override
    public int getCount() {
        return allProduct.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        private TextView priceForYou;
        private ImageView imgP;
        private ImageButton likeToolBar;
        private TriangleLabelView offerRight;
        //private ImageLoader imgLoader;
        private CardView cardView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.picture_produc_item_shop, parent, false);
            holder = new ViewHolder();
            holder.cardView = (CardView)convertView.findViewById(R.id.shopping_page_cart_view);
            holder.priceForYou = (TextView) convertView.findViewById(R.id.txt_price_for_you);
            holder.priceForYou = PriceUtility.getInstance().changeTextViewFont(holder.priceForYou, myContext);
            holder.imgP = (ImageView) convertView.findViewById(R.id.imageProductshoppage);
            holder.imgP.getLayoutParams().width = Configuration.getConfig().shopDisplaySizeForShow;
            holder.imgP.getLayoutParams().height = Configuration.getConfig().shopDisplaySizeForShow;
            holder.offerRight = (TriangleLabelView) convertView.findViewById(R.id.ic_offer_right);

            holder.likeToolBar = (ImageButton) convertView.findViewById(R.id.empty_like_toolbar);
            //holder.imgLoader = new ImageLoader(myContext);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        final Product eachProduct = allProduct.get(position);
        holder.offerRight.setPrimaryText(String.valueOf("%"+eachProduct.getPriceOff()));
        if (eachProduct.getPriceOff() == 0 && eachProduct.getPrice() != 0) {
            holder.offerRight.setVisibility(View.GONE);
            holder.priceForYou.setText(myContext.getResources().getString(R.string.eachPrice, PriceUtility.getInstance().formatPriceCommaSeparated(eachProduct.getPrice())));
        }
        else if (eachProduct.getPriceOff() !=0 && eachProduct.getPrice() != 0) {
            holder.offerRight.setVisibility(View.VISIBLE);
            int price = eachProduct.getPrice();
            int discountPercent = eachProduct.getPriceOff();
            int finalPrice = Utilities.getInstance().calculatePriceOffProduct(price, discountPercent);
            holder.priceForYou = PriceUtility.getInstance().changeTextViewFont(holder.priceForYou, shopPresenterActivity);
            holder.priceForYou.setText(myContext.getResources().getString(R.string.eachPrice, PriceUtility.getInstance().formatPriceCommaSeparated(finalPrice)));
            holder.priceForYou.setVisibility(View.VISIBLE);
        } else if (eachProduct.getPriceOff() == 0) {
            holder.offerRight.setVisibility(View.GONE);
        }


        if (sch.getAProduct(eachProduct.getId()).getLike() == 0) {
            //this Product No Favorite
            holder.likeToolBar.setImageResource(R.drawable.toolbar_add_to_favorite_border);
            //isLikeButtonClicked = false;
        } else {

            holder.likeToolBar.setImageResource(R.drawable.toolbar_add_to_favorit_fill_like);
            //isLikeButtonClicked = true;
        }

        holder.likeToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToolbarHandler.getInstance().addCurrentProductToFavorite(shopPresenterActivity,myContext, holder.likeToolBar, eachProduct, sch);
            }
        });

        //get main picture from server or cache
        String imageNumberPath;
        if (eachProduct.getImagesPath().size() == 0)
            imageNumberPath = "no_image_path";
        else
            imageNumberPath = eachProduct.getImagesPath().get(0);

        try {
            imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String imageURL = Link.getInstance().generateURLForGetImageProduct(eachProduct.getImagesMainPath(), imageNumberPath, Configuration.getConfig().shopDisplaySizeForURL, Configuration.getConfig().shopDisplaySizeForURL);
        //holder.imgP.setImageDrawable(defaultPicture);
        //holder.imgLoader.DisplayImage(imageURL, holder.imgP);
        Glide.with(myContext)
                .load(imageURL)
                .crossFade()
                .into(holder.imgP);
        //holder.infoP.setText(eachProduct.getTitle());
        final View finalConvertView = convertView;
        final View finalConvertView1 = convertView;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
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

