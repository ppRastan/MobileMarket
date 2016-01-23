package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
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

/**
 * Created by shaisteS on 1/23/2016.
 */
public class LastShoppingItemAdapter extends ArrayAdapter<ProductShop> {

    private Activity myContext;
    private ArrayList<ProductShop> allProductsShop;

    public LastShoppingItemAdapter(Context context, int resource, ArrayList<ProductShop> productsShop) {
        super(context, resource,productsShop);
        myContext=(Activity)context;
        allProductsShop=productsShop;

    }

    public View getView(final int position, View convertView, ViewGroup parent){

       LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.last_shopping_item, null);


        ImageLoader imgLoader = new ImageLoader(Configuration.UserAccountFragment); // important
        ImageView picInvoice = (ImageView) rowView.findViewById(R.id.img_invoice);
        String image_url_1 = allProductsShop.get(position).getInvoiceImageLink();
        imgLoader.DisplayImage(image_url_1, picInvoice);

        TextView invoiceNum=(TextView) rowView.findViewById(R.id.txt_invoiceNum);
        TextView invoiceDate=(TextView)rowView.findViewById(R.id.txt_invoceDate);
        TextView invoiceStatus=(TextView)rowView.findViewById(R.id.txt_invoiceStatus);

        invoiceNum.setText("شماره فاکتور :"+allProductsShop.get(position).getInvoiceNumber());

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Integer.parseInt(allProductsShop.get(position).getTimeStamp()));
        String date = DateFormat.format("yyyy-MM-dd", cal).toString();
        invoiceDate.setText("تاریخ خرید محصول:"+date);

        invoiceStatus.setText("وضعیت فاکتور :"+String.valueOf(allProductsShop.get(position).getInvoiceStatus()));
        return rowView;
    }
}

