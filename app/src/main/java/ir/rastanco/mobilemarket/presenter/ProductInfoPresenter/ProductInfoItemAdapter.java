package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;

/**
 * Created by shaisteS on 1394/10/27.
 * A adapter for List view, this list view show product option (Option title and Option value )
 */
public class ProductInfoItemAdapter extends ArrayAdapter<ProductOption> {

    private final ArrayList<ProductOption> options;
    private final Activity myActivity;

    public ProductInfoItemAdapter(Context context, ArrayList<ProductOption> allProductOptions) {
        super(context, R.layout.activity_product_info, allProductOptions);
        options = allProductOptions;
        myActivity = (Activity) context;
    }

    static class ViewHolder {
        TextView txtTitle;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = myActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.product_info_item, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.text_title);
            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();
            holder.txtTitle.setText(options.get(position).getTitle() + " : " + options.get(position).getValue());

        //holder.txtTitle.setText("hello parisa");
        return convertView;
    }
}
