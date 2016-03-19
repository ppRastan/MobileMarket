package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShoppingCancel;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.PriceUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ShaisteS on 1394/10/30.
 */
public class shoppingBagAdapter extends ArrayAdapter<Integer> {
    private Activity shoppingBagActivityContext;
    private ArrayList<Integer> selectedProducts;
    private Product aProduct;
    private ServerConnectionHandler serverConnectionHandler;
    private Spinner spinnerCounter;
    private String spinnerValueInString;
    private int spinnerValueInInteger;
    private TextView eachProductPriceTextView;
    private TextView shoppingBagTotalPriceTextView;
    private EditText spinnerValue;
    private TextView spinnerTextView;
    private ArrayList<String> spinnerList ;
    private LayoutInflater inflater;
    private View rowView;
    private View spinnerView;
    private ImageView imgProduct;
    private TextView nameOfEachProductTextView;
    private ImageButton btnDelete;
    private String numberProducePriceOff;
    private AlertDialog.Builder alertDialog;
    private int counterSelected;
    private int finalPrice = 0;
    private int off = 0;
    private ImageLoader imgLoader;
    private String picCounter;
    private String image_url_1;
    private Map<Integer,Integer> selectedItem;
    private  PriceUtility priceUtility;
    private TextView shoppingOffer;
    private Integer eachproductoff;

    public shoppingBagAdapter(Context context, int resource, ArrayList<Integer> productsId) {
        super(context, resource, productsId);
        selectedProducts = productsId;
        shoppingBagActivityContext =(Activity) context;
        priceUtility = new PriceUtility();
        serverConnectionHandler =new ServerConnectionHandler(context);
        spinnerList = new ArrayList<String>();
        this.fillSpinnerItems();
        selectedItem=new HashMap<Integer,Integer>();

    }

    private void fillSpinnerItems() {

        for(int i=1 ; i<= 10 ; i++)
            spinnerList.add(String.valueOf(i));
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = shoppingBagActivityContext.getLayoutInflater();
        rowView = inflater.inflate(R.layout.shopping_bag_item, null);
        aProduct=new Product();
        aProduct= serverConnectionHandler.getAProduct(selectedProducts.get(position));
        this.createEachCartView();
        this.setSpinner();
        int selectedProductCounterInDB= serverConnectionHandler.getNumberProductShop(aProduct.getId());
        if (selectedItem.containsKey(position))
            spinnerCounter.setSelection(selectedItem.get(position));
        else{
            spinnerCounter.setSelection(selectedProductCounterInDB-1);
            selectedItem.put(position,selectedProductCounterInDB-1);
        }

        this.deleteFromBasket();

        spinnerCounter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int selectedIndex, long id) {

                selectedItem.put(position, selectedIndex);
                counterSelected = Integer.parseInt(spinnerCounter.getSelectedItem().toString());
                serverConnectionHandler.changeShoppingNunmber(aProduct.getId(), counterSelected);
                ObserverShoppingCancel.setShoppingCancel(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rowView;
    }

    private void createEachCartView() {

        imgProduct=(ImageView)rowView.findViewById(R.id.shopping__bag_img);
        nameOfEachProductTextView =(TextView) rowView.findViewById(R.id.shopping_bag_txt_productTitle);
        imgLoader = new ImageLoader(shoppingBagActivityContext,rowView, Configuration.articleDisplaySizeForShow);
        eachProductPriceTextView =(TextView) rowView.findViewById(R.id.shopping_bag_price_Off_product);
        shoppingBagTotalPriceTextView = (TextView)rowView.findViewById(R.id.shopping_bag_price_for_you);
        shoppingOffer = (TextView)rowView.findViewById(R.id.shoppingbag_offer);
        nameOfEachProductTextView.setText(aProduct.getTitle());
        shoppingBagTotalPriceTextView = priceUtility.changeFontToYekan(shoppingBagTotalPriceTextView, shoppingBagActivityContext);
        eachProductPriceTextView = priceUtility.changeFontToYekan(eachProductPriceTextView, shoppingBagActivityContext);
        LinearLayout priceOffLinear = (LinearLayout)rowView.findViewById(R.id.priceOffLinear);
        if (aProduct.getPriceOff() != 0)
            off = ((aProduct.getPrice() * aProduct.getPriceOff()) / 100);
        finalPrice = (aProduct.getPrice()  - off );
        numberProducePriceOff = String.valueOf(off * counterSelected);
        eachproductoff = aProduct.getPrice()-finalPrice;
        if(eachproductoff != 0)
        {
        priceOffLinear.setVisibility(View.VISIBLE);
        shoppingOffer.setVisibility(View.VISIBLE);
        shoppingOffer.setText(shoppingBagActivityContext.getResources().getString(R.string.price_off) + "   " + priceUtility.formatPriceCommaSeprated(eachproductoff) + " " + shoppingBagActivityContext.getResources().getString(R.string.toman));}
        eachProductPriceTextView.setText(shoppingBagActivityContext.getResources().getString(R.string.peice) + "     " + priceUtility.formatPriceCommaSeprated(aProduct.getPrice()) + " " + shoppingBagActivityContext.getResources().getString(R.string.toman));
        shoppingBagTotalPriceTextView.setText(shoppingBagActivityContext.getResources().getString(R.string.price_for_you) + "     " + priceUtility.formatPriceCommaSeprated(finalPrice) + " " + shoppingBagActivityContext.getResources().getString(R.string.toman));
        picCounter = aProduct.getImagesPath().get(0);
        try {
            picCounter= URLEncoder.encode(picCounter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        image_url_1 = aProduct.getImagesMainPath()+
                picCounter+
                "&size="+
                Configuration.articleDisplaySizeForURL +"x"+Configuration.articleDisplaySizeForURL +
                "&q=30";
        imgLoader.DisplayImage(image_url_1, imgProduct);
    }

    private void deleteFromBasket() {
        btnDelete=(ImageButton)rowView.findViewById(R.id._shopping_bag_delete_btn);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(shoppingBagActivityContext);
                alertDialog.setMessage(R.string.confirm_to_delete);
                alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        serverConnectionHandler.addProductToShoppingBag(aProduct.getId(), 1);
                        for (int i=0;i<selectedProducts.size();i++){
                            if (aProduct.getId()==selectedProducts.get(i)){
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

    }

    private void setSpinner() {
        spinnerView = inflater.inflate(R.layout.spinner_item, null);
        spinnerCounter = (Spinner)rowView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(shoppingBagActivityContext, R.layout.spinner_item, spinnerList);
        spinnerCounter.setAdapter(adapter);
        spinnerValueInString = spinnerCounter.getSelectedItem().toString();
        spinnerValueInInteger = Integer.parseInt(spinnerValueInString);
        spinnerTextView = (TextView)spinnerView.findViewById(R.id.spinner_text);
        spinnerTextView = priceUtility.changeFontToYekan(spinnerTextView,shoppingBagActivityContext);

    }

    public void updateList(ArrayList<Integer> results)
    {
        selectedProducts = results;
        notifyDataSetChanged();
    }

}
