package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.utility.Configuration;


/**
 * Created by ShaisteS on 1394/11/22.
 * This class is a ArrayAdapter for list view in UserFavouriteProduct Class
 */
public class UserFavouriteProductItemAdapter extends ArrayAdapter<Product> {

    private Activity myContext;
    private ArrayList<Product> products;
    private LayoutInflater inflater;
    private View rowView;


    public UserFavouriteProductItemAdapter(Context context, int resource, ArrayList<Product> allProduct) {
        super(context, resource, allProduct);
        myContext = (Activity) context;
        products = allProduct;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        inflater = myContext.getLayoutInflater();
        rowView = inflater.inflate(R.layout.user_profile_like_product_item, null);
        TextView txtProductName=(TextView)rowView.findViewById(R.id.txt_likeProductTitle);
        txtProductName.setText(products.get(position).getTitle());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerConnectionHandler sch = new ServerConnectionHandler(myContext);
                Product aProduct = new Product();
                aProduct = sch.getAProduct(products.get(position).getId());
                ArrayList<Product> product = new ArrayList<Product>();
                product.add(aProduct);
                Intent intent = new Intent(Configuration.getConfig().MainActivityContext, ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct", product);
                intent.putExtra("position", 0);
                myContext.startActivity(intent);
            }
        });

        return rowView;
    }
}
