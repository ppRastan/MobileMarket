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

    private final Activity myContext;
    private final ArrayList<Product> products;

    public UserFavouriteProductItemAdapter(Context context, ArrayList<Product> allProduct) {
        super(context, R.layout.user_profile_like_product_item, allProduct);
        myContext = (Activity) context;
        products = allProduct;
    }

    static class ViewHolder {
        private TextView txtProductName;

    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.user_profile_like_product_item, parent, false);
            holder = new ViewHolder();
            holder.txtProductName = (TextView) convertView.findViewById(R.id.txt_likeProductTitle);

            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtProductName.setText(products.get(position).getTitle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerConnectionHandler sch = new ServerConnectionHandler(myContext);
                Product aProduct = sch.getAProduct(products.get(position).getId());
                ArrayList<Product> product = new ArrayList<>();
                product.add(aProduct);
                Intent intent = new Intent(Configuration.getConfig().mainActivityContext, ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct", product);
                intent.putExtra("position", 0);
                myContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
