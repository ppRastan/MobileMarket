package ir.rastanco.mobilemarket.presenter.specialProductPresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverSimilarProduct;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.ToolbarHandler;
import ir.rastanco.mobilemarket.utility.Utilities;

/**
 * Created by ShaisteS on 1394/10/6.
 * A Customize Adapter For Home List view
 */
public class PictureSpecialProductItemAdapter extends ArrayAdapter<Product> {

    private final Activity myContext;
    private final ArrayList<Product> allProduct;
    private final ServerConnectionHandler serverConnectionHandler;
    //private boolean isSelectedForShop = false;
    private final Drawable defaultPicture;
    private final ServerConnectionHandler sch;
    private boolean isLikeButtonClicked = true;

    public PictureSpecialProductItemAdapter(Context context, ArrayList<Product> products) {
        super(context, R.layout.picture_product_item_home, products);
        myContext = (Activity) context;
        allProduct = products;
        serverConnectionHandler = ServerConnectionHandler.getInstance(context);
        defaultPicture = Utilities.getInstance().ResizeImage(R.drawable.loadingholder, myContext, Configuration.getConfig().homeDisplaySizeForShow);
        sch = ServerConnectionHandler.getInstance(myContext);
    }

    static class ViewHolder {
        private ImageButton shareBtn;
        private ImageButton basketToolbar;
        private ImageButton btnAddThisProductToFavorites;
        private ImageButton offerRight;
        private ImageLoader imgLoader;
        private ImageView picProductImage;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.picture_product_item_home, parent, false);
            holder = new ViewHolder();

            holder.basketToolbar = (ImageButton) convertView.findViewById(R.id.basket_toolbar);
            holder.btnAddThisProductToFavorites = (ImageButton) convertView.findViewById(R.id.imageButton_like_specialPage);
            holder.shareBtn = (ImageButton) convertView.findViewById(R.id.imageButton_share);
            holder.offerRight = (ImageButton) convertView.findViewById(R.id.ic_offer_right);
            holder.imgLoader = new ImageLoader(myContext); // important
            holder.picProductImage = (ImageView) convertView.findViewById(R.id.img_picProduct);
            holder.picProductImage.getLayoutParams().width = Configuration.getConfig().homeDisplaySizeForShow;
            holder.picProductImage.getLayoutParams().height = Configuration.getConfig().homeDisplaySizeForShow;

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        //Special Icon
        if (Configuration.getConfig().RTL) {
            if (allProduct.get(position).getPriceOff() != 0) {
                holder.offerRight.setVisibility(View.VISIBLE);
            } else {
                holder.offerRight.setVisibility(View.GONE);
            }
        }
        if (serverConnectionHandler.checkSelectProductForShop(allProduct.get(position).getId()))
            holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
        else
            holder.basketToolbar.setImageResource(R.mipmap.bye_toolbar);

        holder.basketToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
                    serverConnectionHandler.addProductToShoppingBag(allProduct.get(position).getId());
                    myContext.startActivity(new Intent(myContext, ShoppingBagActivity.class));
                    myContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    ObserverShopping.setMyBoolean(true);
            }
        });
        final Product eachProduct = allProduct.get(position);
        if (sch.getAProduct(eachProduct.getId()).getLike() == 0) {
            //this Product No Favorite
            holder.btnAddThisProductToFavorites.setImageResource(R.mipmap.ic_like_toolbar);
            isLikeButtonClicked = false;
        } else {

            holder.btnAddThisProductToFavorites.setImageResource(R.mipmap.ic_like_filled_toolbar);
            isLikeButtonClicked = true;
        }
        holder.btnAddThisProductToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler.getInstance().addCurrentProductToFavorite(myContext, holder.btnAddThisProductToFavorites, eachProduct, isLikeButtonClicked, sch);
            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler.getInstance().generalShare(myContext, allProduct.get(position).getLinkInSite());
            }
        });
        holder.picProductImage.setImageDrawable(defaultPicture);
        String imageNumberPath;
        if (allProduct.get(position).getImagesPath().size() == 0)
            imageNumberPath = "no_image_path";
        else
            imageNumberPath = allProduct.get(position).getImagesPath().get(0);
        try {
            imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String imageURL = Link.getInstance().generateURLForGetImageProduct(allProduct.get(position).getImagesMainPath(), imageNumberPath, Configuration.getConfig().homeDisplaySizeForURL, Configuration.getConfig().homeDisplaySizeForURL);
        holder.imgLoader.DisplayImage(imageURL, holder.picProductImage);
        final View finalConvertView = convertView;
        holder.picProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> newAllProduct = serverConnectionHandler.getSpecialProduct();
                Intent intent = new Intent(finalConvertView.getContext(), ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct", newAllProduct);
                intent.putExtra("position", position);
                finalConvertView.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
