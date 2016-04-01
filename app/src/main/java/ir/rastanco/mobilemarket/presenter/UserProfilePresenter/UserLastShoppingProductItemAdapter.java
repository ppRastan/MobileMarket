package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.ProductShop;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.PriceUtility;


/**
 * Created by ShaisteS on 1394/11/3.
 * This class is a ArrayAdapter for list view in UserLastShoppingProduct Class
 */
public class UserLastShoppingProductItemAdapter extends ArrayAdapter<ProductShop> {

    private final Activity myContext;
    private final ArrayList<ProductShop> allProductsShop;
    public UserLastShoppingProductItemAdapter(Context context, ArrayList<ProductShop> productsShop) {
        super(context,R.layout.last_shopping_item,productsShop);
        myContext=(Activity)context;
        allProductsShop=productsShop;
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = myContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.last_shopping_item,parent,false);


        ImageLoader imgLoader = new ImageLoader(Configuration.getConfig().UserLastShoppingContext,Configuration.getConfig().homeDisplaySizeForShow); // important
        ImageView picInvoice = (ImageView) rowView.findViewById(R.id.img_invoice);
        String imageURL = allProductsShop.get(position).getInvoiceImageLink();
        imgLoader.DisplayImage(imageURL, picInvoice);

        TextView invoiceNum=(TextView) rowView.findViewById(R.id.txt_invoiceNum);
        TextView invoiceDate=(TextView)rowView.findViewById(R.id.txt_invoiceDate);
        TextView invoiceStatus=(TextView)rowView.findViewById(R.id.txt_invoiceStatus);
        invoiceNum = PriceUtility.getInstance().changeFontToYekan(invoiceNum, myContext);
        invoiceDate = PriceUtility.getInstance().changeFontToYekan(invoiceDate, myContext);
        invoiceStatus = PriceUtility.getInstance().changeFontToYekan(invoiceStatus, myContext);

        invoiceNum.setText(myContext.getResources().getString(R.string.invoice_number)+allProductsShop.get(position).getInvoiceNumber());

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Integer.parseInt(allProductsShop.get(position).getTimeStamp()));
        //String date = DateFormat.format("yyyy-MM-dd", cal).toString();
        return rowView;
    }
}

