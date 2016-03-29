package ir.rastanco.mobilemarket.presenter.specialProductPresenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import ir.rastanco.mobilemarket.utility.Links;

/**
 * Created by ShaisteS on 1394/10/6.
 * A Customize Adapter For Home List view
 */
public class PictureSpecialProductItemAdapter extends ArrayAdapter<Product>  {

    private Activity myContext;
    private ArrayList<Product> allProduct;
    private ServerConnectionHandler serverConnectionHandler;
    private String textToSend = null;
    private Dialog shareDialog;
    private Intent sendIntent;
    private boolean isSelectedForShop=false;

    public PictureSpecialProductItemAdapter(Context context, int resource, ArrayList<Product> products) {
        super(context, resource,products);
        myContext=(Activity)context;
        allProduct=products;
        serverConnectionHandler =new ServerConnectionHandler(context);

    }

    public class Holder{
        ImageButton shareBtn;
        ImageButton cancelShareDialog;
        ImageButton basketToolbar;
        Button sendBtn;
        EditText editTextToShare;
        Button btnSimilar;
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.picture_product_item_home, null);
        final Holder holder=new Holder();
        holder.basketToolbar = (ImageButton)rowView.findViewById(R.id.basket_toolbar);
        holder.btnSimilar=(Button) rowView.findViewById(R.id.btn_similar);
        holder.shareBtn = (ImageButton) rowView.findViewById(R.id.imbt_share);


        //Special Icon
        //ImageButton offerLeft = (ImageButton)rowView.findViewById(R.id.ic_offer_left);
        ImageButton offerRight = (ImageButton)rowView.findViewById(R.id.ic_offer_right);
        if(Configuration.RTL)
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

            if (isSelectedForShop==false) {
                holder.basketToolbar.setImageResource(R.mipmap.green_bye_toolbar);
                isSelectedForShop=true;
                serverConnectionHandler.addProductToShoppingBag(allProduct.get(position).getId(), 1);
                myContext.startActivity(new Intent(myContext,ShoppingBagActivity.class));
                myContext.overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
                ObserverShopping.setMyBoolean(true);
                isSelectedForShop = true;

            }

            else if (isSelectedForShop==true){
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
                int switchToPage=Configuration.getConfig().MainPager.getCurrentItem();
                for (int i=0;i<Configuration.getConfig().MainPager.getAdapter().getCount();i++){
                    if (Configuration.getConfig().MainPager.getAdapter().getPageTitle(i).toString().equals(pageTitle))
                        switchToPage=i;
                }
                Configuration.MainPager.setCurrentItem(switchToPage);
                int parentId= serverConnectionHandler.getACategoryWithId(allProduct.get(position).getGroupId()).getParentId();
                ObserverSimilarProduct.setSimilarProduct(parentId);
           }
       });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareDialog = new Dialog(myContext);
                shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                shareDialog.setContentView(R.layout.share_alert_dialog);
                holder.cancelShareDialog = (ImageButton) shareDialog.findViewById(R.id.close_pm_to_friend);
                holder.sendBtn = (Button)shareDialog.findViewById(R.id.send_my_pm);
                holder.editTextToShare = (EditText)shareDialog.findViewById(R.id.text_to_send);
                holder.cancelShareDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                    }
                });
                holder.sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.sendBtn.setTextColor(Color.parseColor("#EB4D2A"));
                        textToSend = holder.editTextToShare.getText().toString();
                        String Share = textToSend + "\n\n" +
                                allProduct.get(position).getLinkInSite() + "\n\n" +
                                myContext.getResources().getString(R.string.text_to_advertise) + "\n\n"
                                + myContext.getResources().getString(R.string.LinkDownloadApp);

                        sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, textToSend);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, Share);
                        sendIntent.setType("text/plain");
                        myContext.startActivity(sendIntent);
                        shareDialog.cancel();
                    }
                });

                shareDialog.setCancelable(true);
                shareDialog.show();
//                shareDialog.cancel();
            }
        });
        ImageLoader imgLoader = new ImageLoader(myContext,rowView,Configuration.getConfig().homeDisplaySizeForShow); // important
        final  ImageView PicProductImage = (ImageView) rowView.findViewById(R.id.img_picProduct);
        PicProductImage.getLayoutParams().width= Configuration.getConfig().homeDisplaySizeForShow;
        PicProductImage.getLayoutParams().height=Configuration.getConfig().homeDisplaySizeForShow;
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
        String imageURL = Links.getInstance().generateURLForGetImageProduct(allProduct.get(position).getImagesMainPath(),imageNumberPath,Configuration.getConfig().homeDisplaySizeForURL,Configuration.getConfig().homeDisplaySizeForURL);
        imgLoader.DisplayImage(imageURL, PicProductImage);
        PicProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> newAllProduct=new ArrayList<Product>();
                newAllProduct= serverConnectionHandler.getSpecialProduct();
                Intent intent = new Intent(rowView.getContext(), ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct",newAllProduct);
                intent.putExtra("position",position);
                rowView.getContext().startActivity(intent);
            }
        });
        return rowView;
    }
}
