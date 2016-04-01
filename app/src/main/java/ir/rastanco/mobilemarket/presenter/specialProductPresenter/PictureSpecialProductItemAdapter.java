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
public class PictureSpecialProductItemAdapter extends ArrayAdapter<Product>  {

    private final Activity myContext;
    private final ArrayList<Product> allProduct;
    private final ServerConnectionHandler serverConnectionHandler;
    private boolean isSelectedForShop=false;
    private final Drawable defaultPicture;


    public PictureSpecialProductItemAdapter(Context context, int resource, ArrayList<Product> products) {
        super(context, resource,products);
        myContext=(Activity)context;
        allProduct=products;
        serverConnectionHandler =new ServerConnectionHandler(context);
        defaultPicture= Utilities.getInstance().ResizeImage(R.drawable.loadingholder, myContext, Configuration.getConfig().homeDisplaySizeForShow);

    }

    public class Holder{
        ImageButton shareBtn;
        ImageButton basketToolbar;
        Button btnSimilar;
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.picture_product_item_home,parent,false);
        final Holder holder=new Holder();
        holder.basketToolbar = (ImageButton)rowView.findViewById(R.id.basket_toolbar);
        holder.btnSimilar=(Button) rowView.findViewById(R.id.btn_similar);
        holder.shareBtn = (ImageButton) rowView.findViewById(R.id.imageButton_share);


        //Special Icon
        //ImageButton offerLeft = (ImageButton)rowView.findViewById(R.id.ic_offer_left);
        ImageButton offerRight = (ImageButton)rowView.findViewById(R.id.ic_offer_right);
        if(Configuration.getConfig().RTL)
        {
            //offerLeft.setVisibility(View.GONE);
            if(allProduct.get(position).getPriceOff() != 0)
            {
                offerRight.setVisibility(View.VISIBLE);
            }
            else {
                offerRight.setVisibility(View.GONE);
            }
        }
        /*if (! Configuration.RTL)
        {
            offerRight.setVisibility(View.GONE);
            if(allProduct.get(position).getPriceOff() != 0) {

                offerLeft.setVisibility(View.VISIBLE);
            }
            else
            {
                offerLeft.setVisibility(View.GONE);
            }
        }*/

        if (serverConnectionHandler.checkSelectProductForShop(allProduct.get(position).getId()))
            holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
        else
            holder.basketToolbar.setImageResource(R.mipmap.bye_toolbar);

        holder.basketToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (!isSelectedForShop) {
                holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
                isSelectedForShop=true;
                serverConnectionHandler.addProductToShoppingBag(allProduct.get(position).getId(), 1);
                myContext.startActivity(new Intent(myContext,ShoppingBagActivity.class));
                myContext.overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
                ObserverShopping.setMyBoolean(true);
                isSelectedForShop = true;

            }

            else if (isSelectedForShop){
                holder.basketToolbar.setImageResource(R.mipmap.bye_toolbar);
                isSelectedForShop=false;
                serverConnectionHandler.deleteAProductShopping(allProduct.get(position).getId());
                ObserverShopping.setMyBoolean(false);
                isSelectedForShop = false;

            }
            }
        });
        holder.btnSimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pageTitle= serverConnectionHandler.getTabTitleForSimilarProduct(allProduct.get(position).getGroupId());
                int switchToPage=Configuration.getConfig().mainPager.getCurrentItem();
                for (int i=0;i<Configuration.getConfig().mainPager.getAdapter().getCount();i++){
                    if (Configuration.getConfig().mainPager.getAdapter().getPageTitle(i).toString().equals(pageTitle))
                        switchToPage=i;
                }
                Configuration.getConfig().mainPager.setCurrentItem(switchToPage);
                int parentId= serverConnectionHandler.getACategoryWithId(allProduct.get(position).getGroupId()).getParentId();
                ObserverSimilarProduct.setSimilarProduct(parentId);
           }
       });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler toolbarHandler = new ToolbarHandler();
                toolbarHandler.generalShare(myContext, allProduct.get(position).getLinkInSite());
            }
        });
        ImageLoader imgLoader = new ImageLoader(myContext,Configuration.getConfig().homeDisplaySizeForShow); // important
        final  ImageView PicProductImage = (ImageView) rowView.findViewById(R.id.img_picProduct);
        PicProductImage.getLayoutParams().width= Configuration.getConfig().homeDisplaySizeForShow;
        PicProductImage.getLayoutParams().height=Configuration.getConfig().homeDisplaySizeForShow;
        PicProductImage.setImageDrawable(defaultPicture);
        String imageNumberPath;
        if (allProduct.get(position).getImagesPath().size()==0)
            imageNumberPath="no_image_path";
        else
            imageNumberPath=allProduct.get(position).getImagesPath().get(0);
        try {
            imageNumberPath= URLEncoder.encode(imageNumberPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String imageURL = Link.getInstance().generateURLForGetImageProduct(allProduct.get(position).getImagesMainPath(),imageNumberPath,Configuration.getConfig().homeDisplaySizeForURL,Configuration.getConfig().homeDisplaySizeForURL);
        imgLoader.DisplayImage(imageURL, PicProductImage);
        PicProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> newAllProduct= serverConnectionHandler.getSpecialProduct();
                Intent intent = new Intent(rowView.getContext(), ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct",newAllProduct);
                intent.putExtra("position",position);
                rowView.getContext().startActivity(intent);
            }
        });
        return rowView;
    }
}
