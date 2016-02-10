package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.ProductOption;

/**
 * Created by shaisteS on 1394/10/27.
 */
public class ProductInfoItemAdapter extends ArrayAdapter<ProductOption> {

    private ArrayList<ProductOption> options;
    private Activity myContext;
    private Typeface yekanFont;
    private TextView txtTitle;
    //private TextView txtValue;
    private LayoutInflater inflater;
    private View rowView;
    public ProductInfoItemAdapter(Context context, int resource, ArrayList<ProductOption> allProductOptions) {
        super(context, resource,allProductOptions);
        options = allProductOptions;
        myContext = (Activity) context;
        yekanFont = Typeface.createFromAsset(myContext.getAssets(),"fonts/yekan.ttf");
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = myContext.getLayoutInflater();
        rowView = inflater.inflate(R.layout.product_info_item, null);
        txtTitle=(TextView)rowView.findViewById(R.id.txt_title);
        //txtValue=(TextView)rowView.findViewById(R.id.txt_value);
        txtTitle.setText(options.get(position).getTitle() + " : " + options.get(position).getValue());
        txtTitle.setTypeface(yekanFont);
        //txtValue.setText(options.get(position).getValue());
        //txtValue.setTypeface(yekanFont);
        return rowView;
    }
}
