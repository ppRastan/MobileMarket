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

    static class ViewHolder{
        private ImageLoader imgLoader;
        private ImageView picInvoice;
        private TextView invoiceNum;

    }

    public View getView(final int position, View convertView, ViewGroup parent){

        ViewHolder holder;
        if (convertView==null){
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.last_shopping_item,parent,false);
            holder=new ViewHolder();
            holder.imgLoader = new ImageLoader(Configuration.getConfig().userLastShoppingActivityContext,Configuration.getConfig().homeDisplaySizeForShow); // important
            holder.picInvoice = (ImageView) convertView.findViewById(R.id.img_invoice);
            holder.invoiceNum=(TextView) convertView.findViewById(R.id.txt_invoiceNum);
            holder.invoiceNum = PriceUtility.getInstance().changeFontToYekan(holder.invoiceNum, myContext);

            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();

        String imageURL = allProductsShop.get(position).getInvoiceImageLink();
        holder.imgLoader.DisplayImage(imageURL, holder.picInvoice);
        holder.invoiceNum.setText(myContext.getResources().getString(R.string.invoice_number ,allProductsShop.get(position).getInvoiceNumber()));

        return convertView;
    }
}

