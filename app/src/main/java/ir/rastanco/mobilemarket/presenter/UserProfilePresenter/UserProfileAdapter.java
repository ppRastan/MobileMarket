package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by Emertat on 02/11/2016.
 */
public class UserProfileAdapter extends ArrayAdapter<Product> {

    private Activity myContext;
    private ArrayList<Product> products;
    private LayoutInflater inflater;
    private View rowView;
    private Typeface yekanFont;
    private TextView txtProductName;
    private ImageButton imbLike;


    public UserProfileAdapter(Context context, int resource, ArrayList<Product> allProduct) {
        super(context, resource, allProduct);
        myContext = (Activity) context;
        products = allProduct;
        yekanFont = Typeface.createFromAsset(myContext.getAssets(), "fonts/yekan.ttf");
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        inflater = myContext.getLayoutInflater();
        rowView = inflater.inflate(R.layout.user_profile_like_product_item, null);
        TextView txtProductName=(TextView)rowView.findViewById(R.id.txt_likeProductTitle);
        ImageButton imbLike=(ImageButton)rowView.findViewById(R.id.imb_like);
        txtProductName.setText(products.get(position).getTitle());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerConnectionHandler sch = new ServerConnectionHandler(myContext);
                int productId = sch.getProductIdWithTitle((String)products.get(position).getTitle());
                Product aProduct = new Product();
                aProduct = sch.getAProduct(productId);
                ArrayList<Product> product = new ArrayList<Product>();
                product.add(aProduct);
                Intent intent = new Intent(Configuration.MainActivityContext, ProductInfoActivity.class);
                intent.putParcelableArrayListExtra("allProduct", product);
                intent.putExtra("position", 0);
                myContext.startActivity(intent);
            }
        });

        return rowView;
    }
}
