package ir.rastanco.mobilemarket.presenter.Filter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;

/**
 * Created by ShaisteS on 1395/1/27.
 */
public class FilterCategoryItemAdapter extends ArrayAdapter<Integer> {
    private Activity context;
    private ArrayList<Integer> categoriesId;
    private ServerConnectionHandler serverConnectionHandler;

    public FilterCategoryItemAdapter(Context context, ArrayList<Integer> categoriesId) {
        super(context, R.layout.dialog_filter_item, categoriesId);
        this.context = (Activity) context;
        this.categoriesId = categoriesId;
        serverConnectionHandler = ServerConnectionHandler.getInstance(context);
    }

    static class ViewHolder {
        TextView txtCategoryName;
        ImageButton imgNextIcon;

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.dialog_filter_item, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.txtCategoryName = (TextView) convertView.findViewById(R.id.txt_Name);
            //holder.txtCategoryName= PriceUtility.getInstance().changeFontToYekan(holder.txtCategoryName,context);
            holder.imgNextIcon = (ImageButton) convertView.findViewById(R.id.imb_next);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (categoriesId.get(position) == 0) {
            holder.imgNextIcon.setVisibility(View.INVISIBLE);
            holder.txtCategoryName.setText(context.getString(R.string.all));

        } else {

            Category aCategory = serverConnectionHandler.getACategoryWithId(categoriesId.get(position));

            if (aCategory.getHasChild() == 0) {
                holder.imgNextIcon.setVisibility(View.INVISIBLE);
            } else
                holder.imgNextIcon.setVisibility(View.VISIBLE);

            holder.txtCategoryName.setText(aCategory.getTitle());

        }

        return convertView;
    }
}
