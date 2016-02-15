package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import ir.rastanco.mobilemarket.presenter.Connect;
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
    private TextView txtProductPrice;
    private  TextView totalPrice;
    private EditText spinnerValue;
    private TextView textOfSpinner;
    private ArrayList<String> spinnerList ;
    private Typeface yekanFont;
    private LayoutInflater inflater;
    private View rowView;
    private View spinnerView;
    private ImageView imgProduct;
    private TextView txtProductTitle;
    private Button btnDelete;
    private String numberProductPrice;
    private double amount;
    private String numberProducePriceOff;
    private double amountOfPriceOff;
    private DecimalFormat formatter;
    private AlertDialog.Builder alertDialog;
    private int counterSelected;
    private double amountOfFinalPrice;
    private String numberOfFinalPrice;
    private int finalPrice = 0;
    private int off = 0;
    private ImageLoader imgLoader;
    private String picCounter;
    private String image_url_1;

    public shoppingBagAdapter(Context context, int resource, ArrayList<Integer> productsId) {
        super(context, resource, productsId);
        selectedProducts = productsId;
        myContext =(Activity) context;
        yekanFont= Typeface.createFromAsset(myContext.getAssets(), "fonts/yekan.ttf");
        sch=new ServerConnectionHandler(context);
        spinnerList = new ArrayList<String>();

        this.fillSpinnerItems();

    }

    private void fillSpinnerItems() {

        for(int i=1 ; i<= 10 ; i++)
            spinnerList.add(String.valueOf(i));
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = myContext.getLayoutInflater();
        rowView = inflater.inflate(R.layout.shopping_bag_item, null);
        this.setSpinner();
        this.deleteFromBasket();
        aProduct=new Product();
        aProduct=sch.getAProduct(selectedProducts.get(position));
        this.createEachCartView();
        this.updateBySpinner();
        spinnerCounter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formatter = new DecimalFormat("#,###,000");
                counterSelected = Integer.parseInt(spinnerCounter.getSelectedItem().toString());
                if (aProduct.getPriceOff() != 0)
                    off = ((aProduct.getPrice() * aProduct.getPriceOff()) / 100);
                finalPrice = (aProduct.getPrice()  - off );
                ///Price
                numberProductPrice = String.valueOf(aProduct.getPrice());
                amount = Double.parseDouble(numberProductPrice);
                ///off
                numberProducePriceOff = String.valueOf(off * counterSelected);
                amountOfPriceOff = Double.parseDouble(numberProducePriceOff);
                ///FinalPrice
                numberOfFinalPrice = String.valueOf(finalPrice);
                amountOfFinalPrice = Double.parseDouble(numberOfFinalPrice);
                txtProductPrice.setText(myContext.getResources().getString(R.string.peice) + "     " + formatter.format(amount) + " " + myContext.getResources().getString(R.string.toman));
                totalPrice.setText(myContext.getResources().getString(R.string.price) + "     " + formatter.format(amountOfFinalPrice) + " " + myContext.getResources().getString(R.string.toman));
                sch.changeShoppingNunmber(aProduct.getId(), counterSelected);
                Observer.setShoppingCancel(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rowView;
    }

    private void updateBySpinner() {
    }

    private void createEachCartView() {
        imgProduct=(ImageView)rowView.findViewById(R.id.shopping__bag_img);
        txtProductTitle=(TextView) rowView.findViewById(R.id.shopping_bag_txt_productTitle);
        imgLoader = new ImageLoader(myContext); // important
        txtProductPrice=(TextView) rowView.findViewById(R.id.shopping_bag_price_Of_product);
        totalPrice = (TextView)rowView.findViewById(R.id.shopping_bag_price_for_you);
        txtProductTitle.setText(aProduct.getTitle());
        totalPrice.setTypeface(yekanFont);
        txtProductPrice.setTypeface(yekanFont);
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
        btnDelete=(Button)rowView.findViewById(R.id._shopping_bag_delete_btn);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(myContext);
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
                        Connect.setMyBoolean(false);
                        Observer.setShoppingCancel(true);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(myContext, R.layout.spinner_item, spinnerList);
        spinnerCounter.setAdapter(adapter);
        spinnerValueInString = spinnerCounter.getSelectedItem().toString();
        spinnerValueInInteger = Integer.parseInt(spinnerValueInString);
        textOfSpinner = (TextView)spinnerView.findViewById(R.id.spinner_text);

    }

    public void updateList(ArrayList<Integer> results)
    {
        selectedProducts = results;
        notifyDataSetChanged();
    }

}
