package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

/**
 * Created by ShaisteS on 1394/10/23.
 * A pagerAdapter for show product information and picture
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
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
    private final int productsSize;
    private View viewLayout;
    private Context myContext;
    private Drawable largeDefaultPicture = null;
    private Drawable smallDefaultPicture = null;
//    private String textToSend = null;
//    private Dialog shareDialog;
//    private ImageButton cancelShareDialog;
//    private Button sendBtn;
//    private EditText editTextToShare;
//    private Intent sendIntent;

    public FullScreenImageAdapter(Activity activity, ArrayList<Product> allProducts, int allProductSize) {
        this.activity = activity;
        this.products = allProducts;
        this.productsSize = allProductSize;
        myContext = Configuration.getConfig().productInfoActivityContext;
        sch = ServerConnectionHandler.getInstance(myContext);
        if (largeDefaultPicture == null)
            largeDefaultPicture = Utilities.getInstance().ResizeImage(R.drawable.loadingholder, myContext, Configuration.getConfig().homeDisplaySizeForShow);
        if (smallDefaultPicture == null)
            smallDefaultPicture = Utilities.getInstance().ResizeImage(R.drawable.loadingholder, myContext, Configuration.getConfig().articleDisplaySizeForShow);
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

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewLayout = inflater.inflate(R.layout.activity_product_info, container, false);
        ImageButton offerImageButton = (ImageButton) viewLayout.findViewById(R.id.ic_offer_full_screen_right);
        final Product aProduct = products.get(position);
        Button addToBasketBtn = (Button) viewLayout.findViewById(R.id.full_screen_add_to_basket_btn);
        TextView nameOfCurrentProduct = (TextView) viewLayout.findViewById(R.id.name_of_photo);
        nameOfCurrentProduct.setText(aProduct.getTitle());
        setProductQuality(aProduct.getQualityRank());
        if (aProduct.getPrice() == 0) {
            addToBasketBtn.setText(activity.getString(R.string.coming_soon));
            addToBasketBtn.setCompoundDrawables(null, null, null, null);
            addToBasketBtn.setEnabled(false);

        }
        if (aProduct.getPriceOff() != 0) {

            offerImageButton.setVisibility(View.VISIBLE);
        } else {
            offerImageButton.setVisibility(View.GONE);
        }
        if (aProduct.getPriceOff() == 0 && aProduct.getPrice() != 0) {
            int price = aProduct.getPrice();
            String numberOfFinalPrice = String.valueOf(price);
            addToBasketBtn.setText(activity.getString(R.string.FullScreenImageAdapterproductOriginalPrice, PriceUtility.getInstance().formatPriceCommaSeparated(Integer.valueOf(numberOfFinalPrice))));
        }
        if (aProduct.getPriceOff() != 0 && aProduct.getPrice() != 0) {
            int price = aProduct.getPrice();
            int priceOff = aProduct.getPriceOff();
            int priceForYou = Utilities.getInstance().calculatePriceOffProduct(price, priceOff);
            String numberOfFinalPrice = String.valueOf(priceForYou);
            addToBasketBtn.setText(activity.getString(R.string.FullScreenImageAdapterproductPrice, PriceUtility.getInstance().formatPriceCommaSeparated(Integer.valueOf(numberOfFinalPrice))));

        }

        addToBasketBtn = PriceUtility.getInstance().ChangeButtonFont(addToBasketBtn, activity);
        addToBasketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sch.addProductToShoppingBag(aProduct.getId());
                Configuration.getConfig().productInfoActivityContext.startActivity(new Intent(Configuration.getConfig().productInfoActivityContext, ShoppingBagActivity.class));
                ObserverShopping.setMyBoolean(true);
            }
        });

        final ImageButton btnLike = (ImageButton) viewLayout.findViewById(R.id.add_to_favorite);

        if (sch.getAProduct(aProduct.getId()).getLike() == 0) {
            //this Product No Favorite
            btnLike.setImageResource(R.drawable.toolbar_add_to_favorite_border);
        } else {

            btnLike.setImageResource(R.drawable.toolbar_add_to_favorit_fill_like);
        }
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToolbarHandler.getInstance().addToFavoriteInProductPage(activity,sch, aProduct, btnLike, activity, position);
            }
        });
        Button displayCurrentProductInSite = (Button) viewLayout.findViewById(R.id.displayCurrentImageInSite);
        displayCurrentProductInSite = PriceUtility.getInstance().ChangeButtonFont(displayCurrentProductInSite, myContext);
        displayCurrentProductInSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler.getInstance().productIndicative(aProduct.getLinkInSite(), activity);
            }
        });

        ImageButton btnInfo = (ImageButton) viewLayout.findViewById(R.id.img_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler.getInstance().displayInformationOfCurrentProduct(aProduct, activity, viewLayout.getContext());

            }
        });
        ImageButton btnShare = (ImageButton) viewLayout.findViewById(R.id.img_share_full_screen);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler.getInstance().generalShare(activity, aProduct.getLinkInSite());
//                shareDialog = new Dialog(activity);
//                shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                shareDialog.setContentView(R.layout.share_alert_dialog);
//                cancelShareDialog = (ImageButton) shareDialog.findViewById(R.id.close_pm_to_friend);
//                sendBtn = (Button)shareDialog.findViewById(R.id.send_my_pm);
//                editTextToShare = (EditText)shareDialog.findViewById(R.id.text_to_send);
//                cancelShareDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        shareDialog.dismiss();
//                    }
//                });
//
//                sendBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        sendBtn.setTextColor(Color.parseColor("#EB4D2A"));
//                        textToSend = editTextToShare.getText().toString();
//                        String Share=textToSend+"\n\n"+
//                                products.get(position).getLinkInSite()+ "\n\n"+
//                                Configuration.getConfig().productInfoActivityContext.getResources().getString(R.string.text_to_advertise)+"\n\n"
//                                +Configuration.getConfig().productInfoActivityContext.getResources().getString(R.string.LinkDownloadApp);
//
//                        sendIntent = new Intent();
//                        sendIntent.setAction(Intent.ACTION_SEND);
//                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,textToSend);
//                        sendIntent.putExtra(Intent.EXTRA_TEXT, Share);
//                        sendIntent.setType("text/plain");
//                        activity.startActivity(sendIntent);
//                        shareDialog.cancel();
//
//                    }
//                });
//                shareDialog.setCancelable(true);
//                shareDialog.show();
            }
        });


        ImageButton btnShareByTelegram = (ImageButton) viewLayout.findViewById(R.id.telegram_share);
        btnShareByTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolbarHandler.getInstance().shareByTelegram(activity, aProduct.getLinkInSite());
            }
        });
        final ImageView imgProduct = (ImageView) viewLayout.findViewById(R.id.img_productInfo);
        imgProduct.getLayoutParams().width = Configuration.getConfig().homeDisplaySizeForShow;
        imgProduct.getLayoutParams().height = Configuration.getConfig().productInfoHeightForShow;
        imgProduct.setImageDrawable(largeDefaultPicture);
        final ImageLoader imgLoader = new ImageLoader(Configuration.getConfig().productInfoActivityContext); // important

        String imageNumberPath;
        if (aProduct.getImagesPath().size() == 0)
            imageNumberPath = "no_image_path";
        else
            imageNumberPath = aProduct.getImagesPath().get(0);

        try {
            imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_Main = Link.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(), imageNumberPath, Configuration.getConfig().homeDisplaySizeForURL, Configuration.getConfig().productInfoHeightForURL);
        imgLoader.DisplayImage(image_url_Main, imgProduct);
        LinearLayout layout = (LinearLayout) viewLayout.findViewById(R.id.linear);
        int counter;
        if (aProduct.getImagesPath().size() > 1)
            counter = 0;
        else
            counter = 1;

        for (int i = counter; i < aProduct.getImagesPath().size(); i++) {
            final ImageView imageView = new ImageView(Configuration.getConfig().productInfoActivityContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Configuration.getConfig().articleDisplaySizeForShow, Configuration.getConfig().articleDisplaySizeForShow);
            imageView.setLayoutParams(layoutParams);
            imageView.setId(i - 1);
            imageView.setPadding(1, 1, 1, 0);
            imageView.setImageDrawable(smallDefaultPicture);
            layout.addView(imageView);
            imageNumberPath = aProduct.getImagesPath().get(i);
            try {
                imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String image_url_otherPic = Link.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(), imageNumberPath, Configuration.getConfig().articleDisplaySizeForURL, Configuration.getConfig().articleDisplaySizeForURL);
            imgLoader.DisplayImage(image_url_otherPic, imageView);

            final int clickImageNum = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String imageNumberPath = aProduct.getImagesPath().get(clickImageNum);
                    try {
                        imageNumberPath = URLEncoder.encode(imageNumberPath, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String image_url_otherPic = Link.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(), imageNumberPath, Configuration.getConfig().homeDisplaySizeForURL, Configuration.getConfig().productInfoHeightForURL);
                    imgLoader.DisplayImage(image_url_otherPic, imgProduct);

                }
            });

            //getProductOption(aProduct.getId(), aProduct.getGroupId());

        }
        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);

    }

            /*public void getProductOption(int productId, int groupId) {

                if (!sch.existOptionsForAProduct(productId)) {
                    Intent productOptionService = new Intent(Intent.ACTION_SYNC, null, myContext, DownloadProductOption.class);
                    productOptionService.putExtra("productId", productId);
                    productOptionService.putExtra("groupId", groupId);
                    myContext.startService(productOptionService);
                }
            }*/

    private void setProductQuality(String quality) {
        //image product icon declared by numbers
        //test
        ImageView imgProductQuality = (ImageView) viewLayout.findViewById(R.id.img_ProductQuality);
        TextView rankOfProduct = (TextView) viewLayout.findViewById(R.id.product_guality);
        switch (quality) {
            case "a":
                imgProductQuality.setImageResource(R.drawable.darajeha);
                rankOfProduct.setText(myContext.getResources().getString(R.string.rank_of_product)+" "+"(5 از 5)");
                break;
            case "b":
                imgProductQuality.setImageResource(R.drawable.darajehb);
                rankOfProduct.setText(myContext.getResources().getString(R.string.rank_of_product) +" "+"(4 از 5)");
                break;
            case "c":
                imgProductQuality.setImageResource(R.drawable.darajehc);
                rankOfProduct.setText(myContext.getResources().getString(R.string.rank_of_product) +" "+ "(3 از 5)");
                break;
            case "d":
                imgProductQuality.setImageResource(R.drawable.darajehd);
                rankOfProduct.setText(myContext.getResources().getString(R.string.rank_of_product) +" "+ "(2 از 5)");
                break;
            case "e":
                imgProductQuality.setImageResource(R.drawable.darajehe);
                rankOfProduct.setText(myContext.getResources().getString(R.string.rank_of_product) +" "+ "(1 از 5)");
                break;
            case "f":
                imgProductQuality.setImageResource(R.drawable.darajehf);
                rankOfProduct.setText(myContext.getResources().getString(R.string.rank_of_product) +" "+ "(0 از 5)");
                break;
        }


    }
}