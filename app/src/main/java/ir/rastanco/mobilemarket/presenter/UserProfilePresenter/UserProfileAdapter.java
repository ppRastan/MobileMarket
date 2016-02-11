package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.app.Activity;
import android.content.Context;
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

    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = myContext.getLayoutInflater();
        rowView = inflater.inflate(R.layout.user_profile_like_product_item, null);
        TextView txtProductName=(TextView)rowView.findViewById(R.id.txt_likeProductTitle);
        ImageButton imbLike=(ImageButton)rowView.findViewById(R.id.imb_like);
        txtProductName.setText(products.get(position).getTitle());
        return rowView;
    }
}
