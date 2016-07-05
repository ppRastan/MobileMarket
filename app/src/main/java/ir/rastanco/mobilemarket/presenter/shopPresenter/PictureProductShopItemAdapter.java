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

import com.bumptech.glide.Glide;

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

//import com.bumptech.glide.Glide;


public class PictureProductShopItemAdapter extends ArrayAdapter<Product> {

    private final ArrayList<Product> allProduct;
    private boolean isLikeButtonClicked = true;
    private final ServerConnectionHandler sch;
    private final Context myContext;
    private final Activity shopPresenterActivity;
    private Drawable defaultPicture=null;
//    private String textToSend = null;
//    private Dialog shareDialog;
//    private Intent sendIntent;

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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.picture_produc_item_shop, parent, false);
            holder = new ViewHolder();

            holder.infoP = (TextView) convertView.findViewById(R.id.txt_infoProduct);
            //holder.infoP = PriceUtility.getInstance().changeFontToYekan(holder.infoP, myContext);
            holder.originalPrice = (TextView) convertView.findViewById(R.id.txt_priceProduct);
            //holder.originalPrice = PriceUtility.getInstance().changeFontToYekan(holder.originalPrice, myContext);
            holder.priceForYou = (TextView) convertView.findViewById(R.id.txt_price_for_you);
            //holder.priceForYou = PriceUtility.getInstance().changeFontToYekan(holder.priceForYou, myContext);
            holder.imgP = (ImageView) convertView.findViewById(R.id.imageButton_picProduct);
            holder.imgP.getLayoutParams().width = Configuration.getConfig().shopDisplaySizeForShow;
            holder.imgP.getLayoutParams().height = Configuration.getConfig().shopDisplaySizeForShow;
            holder.offerRight = (ImageButton) convertView.findViewById(R.id.ic_offer_right);
            holder.basketToolbar = (ImageButton) convertView.findViewById(R.id.basket_toolbar);
            holder.shareToolBar = (ImageButton) convertView.findViewById(R.id.share_toolbar_in_main_page);
            holder.likeToolBar = (ImageButton) convertView.findViewById(R.id.empty_like_toolbar);
            holder.imgLoader = new ImageLoader(myContext);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        final Product eachProduct = allProduct.get(position);
        if (eachProduct.getPriceOff() == 0 && eachProduct.getPrice() != 0) {
            holder.priceForYou.setVisibility(View.INVISIBLE);
            holder.originalPrice.setTextColor(Color.BLACK);
            holder.originalPrice.setText(myContext.getResources().getString(R.string.eachPrice,PriceUtility.getInstance().formatPriceCommaSeparated(eachProduct.getPrice())));
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.basketToolbar.setVisibility(View.VISIBLE);
        } else if (eachProduct.getPrice() != 0) {
            int price = eachProduct.getPrice();
            int discountPercent = eachProduct.getPriceOff();
            int finalPrice = Utilities.getInstance().calculatePriceOffProduct(price, discountPercent);
            holder.originalPrice.setTextColor(Color.RED);
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.originalPrice.setText(myContext.getResources().getString(R.string.eachPrice ,PriceUtility.getInstance().formatPriceCommaSeparated(price)));
            //holder.priceForYou = PriceUtility.getInstance().changeFontToYekan(holder.priceForYou, shopPresenterActivity);
            holder.priceForYou.setText(myContext.getResources().getString(R.string.eachPrice, PriceUtility.getInstance().formatPriceCommaSeparated(finalPrice)));
            holder.priceForYou.setVisibility(View.VISIBLE);
            holder.basketToolbar.setVisibility(View.VISIBLE);
        } else if (eachProduct.getPriceOff() == 0) {
            holder.basketToolbar.setVisibility(View.GONE);
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.originalPrice.setText(myContext.getString(R.string.coming_soon));
        }

        if (Configuration.getConfig().RTL) {

            if (eachProduct.getPriceOff() != 0) {
                holder.offerRight.setVisibility(View.VISIBLE);
            } else {
                holder.offerRight.setVisibility(View.GONE);
            }
        }

        if (sch.checkSelectProductForShop(eachProduct.getId()))
            holder.basketToolbar.setImageResource(R.drawable.green_bye_toolbar);
        else
            holder.basketToolbar.setImageResource(R.drawable.toolbar_add_product_to_basket);

        holder.basketToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.basketToolbar.setImageResource(R.drawable.green_bye_toolbar);
                    sch.addProductToShoppingBag(eachProduct.getId());
                    myContext.startActivity(new Intent(myContext, ShoppingBagActivity.class));
                    ObserverShopping.setMyBoolean(true);
                }
        });

        holder.shareToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler.getInstance().generalShare(shopPresenterActivity, eachProduct.getLinkInSite());
//                shareDialog = new Dialog(myContext);
//                shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                shareDialog.setContentView(R.layout.share_alert_dialog);
//                ImageButton cancelShareDialog = (ImageButton) shareDialog.findViewById(R.id.close_pm_to_friend);
//                final Button sendBtn = (Button)shareDialog.findViewById(R.id.send_my_pm);
//                final EditText editTextToShare = (EditText)shareDialog.findViewById(R.id.text_to_send);
//                cancelShareDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        shareDialog.dismiss();
//                    }
//                });
//                sendBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sendBtn.setTextColor(Color.parseColor("#EB4D2A"));
//                        textToSend = editTextToShare.getText().toString();
//                        String share=textToSend+"\n\n"+
//                                eachProduct.getLinkInSite()+ "\n\n"+
//                                myContext.getResources().getString(R.string.text_to_advertise)+"\n\n"
//                                + myContext.getResources().getString(R.string.LinkDownloadApp) ;
//                        sendIntent = new Intent();
//                        sendIntent.setAction(Intent.ACTION_SEND);
//                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,textToSend);
//                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,textToSend);
//                        sendIntent.putExtra(Intent.EXTRA_TEXT,share);
//                        sendIntent.setType("text/plain");
//                        myContext.startActivity(sendIntent);
//                        shareDialog.cancel();
//                    }
//                });
//                shareDialog.setCancelable(true);
//                shareDialog.show();

            }
        });

        if (sch.getAProduct(eachProduct.getId()).getLike() == 0) {
            //this Product No Favorite
            holder.likeToolBar.setImageResource(R.drawable.toolbar_add_to_favorite_border);
            isLikeButtonClicked = false;
        } else {

            holder.likeToolBar.setImageResource(R.drawable.toolbar_add_to_favorit_fill_like);
            isLikeButtonClicked = true;
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
        holder.imgP.setImageDrawable(defaultPicture);
        //holder.imgLoader.DisplayImage(imageURL, holder.imgP);
        Glide.with(myContext)
                .load(imageURL)
                .crossFade()
                .into(holder.imgP);
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

