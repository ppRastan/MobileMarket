package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

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
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;

/**
 * Created by ShaisteS on 1394/10/30.
 */
public class shoppingBagAdapter extends ArrayAdapter<Integer> {

    private Activity myContext;
    private ArrayList<Integer> selectedProducts;
    private Product aProduct;
    private ServerConnectionHandler sch;

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

        ImageView imgProduct=(ImageView)rowView.findViewById(R.id.img_productImage);
        TextView txtProductInfo=(TextView) rowView.findViewById(R.id.txt_productTitle);

        txtProductInfo.setText(aProduct.getTitle());


        return rowView;

    }
}
