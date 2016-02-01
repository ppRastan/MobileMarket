package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/10/30.
 */
public class shoppingBagAdapter extends ArrayAdapter<Integer> {
    private Activity myContext;
    private ArrayList<Integer> selectedProducts;
    private Product aProduct;
    private ServerConnectionHandler sch;
    private Spinner spinnerCounter;
    private String spinnerValueInString;
    private int spinnerValueInInteger;
    EditText spinnerValue;
    private Typeface trafficFont;

    public shoppingBagAdapter(Context context, int resource, ArrayList<Integer> productsId) {
        super(context, resource, productsId);

        selectedProducts = productsId;
        myContext =(Activity) context;
        sch=new ServerConnectionHandler(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.shopping_bag_item, null);
        aProduct=new Product();
        aProduct=sch.getAProduct(selectedProducts.get(position));
        spinnerCounter = (Spinner)rowView.findViewById(R.id.spinner_counter);
        spinnerValueInString = spinnerCounter.getSelectedItem().toString();
        spinnerValueInInteger = Integer.parseInt(spinnerValueInString);
        //spinnerValue = (EditText)rowView.findViewById(R.id.spinner_value);
        //spinnerValue.setText(spinnerValueInString);
        //spinnerValue.setTypeface(trafficFont);
        ImageView imgProduct=(ImageView)rowView.findViewById(R.id.img_productImage);
        TextView txtProductTitle=(TextView) rowView.findViewById(R.id.txt_productTitle);
        TextView txtProductPrice=(TextView) rowView.findViewById(R.id.txt_productPrice);
        TextView txtProductOff=(TextView) rowView.findViewById(R.id.txt_productOff);
        Button btnDelete=(Button)rowView.findViewById(R.id.imb_delete);
        TextView totalPrice = (TextView)rowView.findViewById(R.id.txt_total_price);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(myContext);
                alertDialog.setMessage(R.string.confirm_to_delete);
                alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        sch.addProductToShoppingBag(aProduct.getId(),1);
                        for (int i=0;i<selectedProducts.size();i++){
                            if (aProduct.getId()==selectedProducts.get(i)){
                                selectedProducts.remove(i);
                                sch.deleteAProductShopping(aProduct.getId());
                            }
                        }
                        updateList(selectedProducts);
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

        ImageLoader imgLoader = new ImageLoader(myContext); // important
        String picCounter = aProduct.getImagesPath().get(0);
        try {
            picCounter= URLEncoder.encode(picCounter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_1 = aProduct.getImagesMainPath()+
                picCounter+
                "&size="+
                Configuration.articleDisplaySize+"x"+Configuration.articleDisplaySize+
                "&q=30";
        trafficFont = Typeface.createFromAsset(getContext().getAssets(),"fonts/B Traffic.ttf");
        imgLoader.DisplayImage(image_url_1, imgProduct);


        txtProductTitle.setText(aProduct.getTitle());

        //Price and off Set
        int finalPrice=0;
        int off = 0;
        if(aProduct.getPriceOff()!=0)
            off=((aProduct.getPrice()*aProduct.getPriceOff())/100);
            finalPrice=(aProduct.getPrice()-off);
        ///Price
        String numberProductPrice = String.valueOf(aProduct.getPrice());
        double amount = Double.parseDouble(numberProductPrice);
        ///off
        String numberProducePriceOff = String.valueOf(off);
        double amountOfPriceOff = Double.parseDouble(numberProducePriceOff);
        ///FinalPrice
        String numberOfFinalPrice = String.valueOf(finalPrice);
        double amountOfFinalPrice = Double.parseDouble(numberOfFinalPrice);

        DecimalFormat formatter = new DecimalFormat("#,###,000");

        txtProductPrice.setText(formatter.format(amount));
        txtProductOff.setText(formatter.format(amountOfPriceOff));
        totalPrice.setText(formatter.format(amountOfFinalPrice));
        txtProductPrice.setTypeface(trafficFont);
        txtProductOff.setTypeface(trafficFont);
        totalPrice.setTypeface(trafficFont);
        return rowView;

    }

    public void updateList(ArrayList<Integer> results) {
        selectedProducts = results;
        //Triggers the list update
        notifyDataSetChanged();
    }

}
