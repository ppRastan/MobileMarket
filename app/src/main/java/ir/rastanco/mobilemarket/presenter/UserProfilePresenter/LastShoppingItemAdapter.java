package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.ProductShop;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.utility.Configuration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by shaisteS on 1/23/2016.
 */
public class LastShoppingItemAdapter extends ArrayAdapter<ProductShop> {

    private Activity myContext;
    private ArrayList<ProductShop> allProductsShop;
    private LayoutInflater inflater;
    private View rowView;
    private ImageLoader imgLoader;
    private ImageView picInvoice;
    private String image_url_1;
    private TextView invoiceNum;
    private TextView invoiceDate;
    private TextView invoiceStatus;
    private Typeface yekanFont;

    public LastShoppingItemAdapter(Context context, int resource, ArrayList<ProductShop> productsShop) {
        super(context, resource,productsShop);
        myContext=(Activity)context;
        allProductsShop=productsShop;
        yekanFont = Typeface.createFromAsset(myContext.getAssets(),"fonts/yekan.ttf");


    }

    public View getView(final int position, View convertView, ViewGroup parent){

        inflater = myContext.getLayoutInflater();
        rowView = inflater.inflate(R.layout.last_shopping_item, null);


        imgLoader = new ImageLoader(Configuration.UserLastShoppingContext,rowView,Configuration.homeDisplaySizeForShow); // important
        picInvoice = (ImageView) rowView.findViewById(R.id.img_invoice);
        image_url_1 = allProductsShop.get(position).getInvoiceImageLink();
        imgLoader.DisplayImage(image_url_1, picInvoice);

        invoiceNum=(TextView) rowView.findViewById(R.id.txt_invoiceNum);
        invoiceDate=(TextView)rowView.findViewById(R.id.txt_invoceDate);
        invoiceStatus=(TextView)rowView.findViewById(R.id.txt_invoiceStatus);
        invoiceNum.setTypeface(yekanFont);
        invoiceDate.setTypeface(yekanFont);
        invoiceStatus.setTypeface(yekanFont);

        invoiceNum.setText(myContext.getResources().getString(R.string.invoice_number)+allProductsShop.get(position).getInvoiceNumber());

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Integer.parseInt(allProductsShop.get(position).getTimeStamp()));
        String date = DateFormat.format("yyyy-MM-dd", cal).toString();
        return rowView;
    }
}

