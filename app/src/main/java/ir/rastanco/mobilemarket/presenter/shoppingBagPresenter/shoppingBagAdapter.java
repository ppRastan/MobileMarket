package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShoppingCancel;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.PriceUtility;
import ir.rastanco.mobilemarket.utility.Utilities;

/**
 * Created by ShaisteS on 1394/10/30.
 */
public class shoppingBagAdapter extends ArrayAdapter<Integer> {
    private final Activity shoppingBagActivityContext;
    private ArrayList<Integer> selectedProducts;
    private final ServerConnectionHandler serverConnectionHandler;
    private final ArrayList<String> spinnerList;
    private AlertDialog.Builder alertDialog;
    private final Map<Integer, Integer> selectedItem;
    private Drawable defaultPicture = null;

    public shoppingBagAdapter(Context context, ArrayList<Integer> productsId) {
        super(context, R.layout.activity_shopping_bag, productsId);
        selectedProducts = productsId;
        shoppingBagActivityContext = (Activity) context;
        serverConnectionHandler = ServerConnectionHandler.getInstance(context);
        spinnerList = new ArrayList<>();
        this.fillSpinnerItems();
        selectedItem = new HashMap<>();
        if (defaultPicture == null)
            defaultPicture = Utilities.getInstance().ResizeImage(R.drawable.loadingholder, context, Configuration.getConfig().articleDisplaySizeForShow);
    }

    static class ViewHolder {
        private ImageLoader imgLoader;
        private Spinner spinnerCounter;
        private ImageView imgProduct;
        private TextView nameOfEachProductTextView;
        private TextView eachProductPriceTextView;
        private TextView shoppingBagTotalPriceTextView;
        private TextView shoppingOffer;
        private LinearLayout priceOffLinear;
        private ImageButton btnDelete;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        //View ViewHolder Pattern
        if (convertView == null) {
            LayoutInflater inflater = shoppingBagActivityContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.shopping_bag_item, parent, false);
            holder = new ViewHolder();

            holder.imgLoader = new ImageLoader(shoppingBagActivityContext);
            holder.imgProduct = (ImageView) convertView.findViewById(R.id.shopping__bag_img);
            holder.imgProduct.setImageDrawable(defaultPicture);
            holder.nameOfEachProductTextView = (TextView) convertView.findViewById(R.id.shopping_bag_txt_productTitle);
            holder.eachProductPriceTextView = (TextView) convertView.findViewById(R.id.shopping_bag_price_Off_product);
            holder.shoppingBagTotalPriceTextView = (TextView) convertView.findViewById(R.id.shopping_bag_price_for_you);
            holder.shoppingOffer = (TextView) convertView.findViewById(R.id.shoppingBag_offer);
            holder.priceOffLinear = (LinearLayout) convertView.findViewById(R.id.priceOffLinear);
            holder.spinnerCounter = (Spinner) convertView.findViewById(R.id.spinner);
            holder.btnDelete = (ImageButton) convertView.findViewById(R.id._shopping_bag_delete_btn);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final Product aProduct = serverConnectionHandler.getAProduct(selectedProducts.get(position));

        //Create each cartView
        holder.nameOfEachProductTextView.setText(aProduct.getTitle());
        holder.shoppingBagTotalPriceTextView = PriceUtility.getInstance().changeFontToYekan(holder.shoppingBagTotalPriceTextView, shoppingBagActivityContext);
        holder.eachProductPriceTextView = PriceUtility.getInstance().changeFontToYekan(holder.eachProductPriceTextView, shoppingBagActivityContext);
        holder.shoppingOffer = PriceUtility.getInstance().changeFontToYekan(holder.shoppingOffer, shoppingBagActivityContext);

        if (aProduct.getPriceOff() != 0) {
            int finalPrice = Utilities.getInstance().calculatePriceOffProduct(aProduct.getPrice(), aProduct.getPriceOff());
            Integer eachProductOfferPrice = aProduct.getPrice() - finalPrice;
            holder.priceOffLinear.setVisibility(View.VISIBLE);
            holder.shoppingOffer.setText(shoppingBagActivityContext.getResources().getString(R.string.shoppingBagAdapterPriceForYou, PriceUtility.getInstance().formatPriceCommaSeprated(eachProductOfferPrice)));
            holder.eachProductPriceTextView.setText(shoppingBagActivityContext.getResources().getString(R.string.shoppingBagAdapterPriceForYou, PriceUtility.getInstance().formatPriceCommaSeprated(aProduct.getPrice())));
            holder.shoppingBagTotalPriceTextView.setText(shoppingBagActivityContext.getResources().getString(R.string.shoppingBagAdapterPriceForYou, PriceUtility.getInstance().formatPriceCommaSeprated(finalPrice)));

        } else {
            int finalPrice = aProduct.getPrice();
            holder.eachProductPriceTextView.setText(shoppingBagActivityContext.getResources().getString(R.string.shoppingBagAdapterPriceForYou, PriceUtility.getInstance().formatPriceCommaSeprated(aProduct.getPrice())));
            holder.shoppingBagTotalPriceTextView.setText(shoppingBagActivityContext.getResources().getString(R.string.shoppingBagAdapterPriceForYou, PriceUtility.getInstance().formatPriceCommaSeprated(finalPrice)));
        }

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
        String imageURL = Link.getInstance().generateURLForGetImageProduct(aProduct.getImagesMainPath(), imageNumberPath, Configuration.getConfig().articleDisplaySizeForURL, Configuration.getConfig().articleDisplaySizeForURL);
        holder.imgLoader.DisplayImage(imageURL, holder.imgProduct);

        //Set Spinner
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(shoppingBagActivityContext, R.layout.spinner_item, spinnerList);
        holder.spinnerCounter.setAdapter(adapter);

        int selectedProductCounterInDB = serverConnectionHandler.getNumberProductShop(aProduct.getId());
        if (selectedItem.containsKey(position))
            holder.spinnerCounter.setSelection(selectedItem.get(position));
        else {
            holder.spinnerCounter.setSelection(selectedProductCounterInDB - 1);
            selectedItem.put(position, selectedProductCounterInDB - 1);
        }

        //delete from Basket
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(shoppingBagActivityContext);
                alertDialog.setMessage(R.string.confirm_to_delete);
                alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < selectedProducts.size(); i++) {
                            if (aProduct.getId() == selectedProducts.get(i)) {
                                selectedProducts.remove(i);
                                serverConnectionHandler.deleteAProductShopping(aProduct.getId());
                            }
                        }
                        updateList(selectedProducts);
                        ObserverShopping.setMyBoolean(false);
                        ObserverShoppingCancel.setShoppingCancel(true);
                    }
                });

                alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();

            }
        });


        holder.spinnerCounter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int selectedIndex, long id) {

                selectedItem.put(position, selectedIndex);
                serverConnectionHandler.changeShoppingNumber(aProduct.getId(), selectedItem.get(position) + 1);
                ObserverShoppingCancel.setShoppingCancel(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return convertView;
    }

    private void fillSpinnerItems() {

        for (int i = 1; i <= 10; i++)
            spinnerList.add(String.valueOf(i));
    }

    private void updateList(ArrayList<Integer> results) {
        selectedProducts = results;
        notifyDataSetChanged();
    }
}
