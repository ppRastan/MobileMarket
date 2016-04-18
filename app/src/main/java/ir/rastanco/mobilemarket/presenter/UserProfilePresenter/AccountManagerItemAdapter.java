package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;
/*
 created by parisa
 this adapter handled favourite items that user added during application
 log off configuration handled
 previous shopped items handled
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

public class AccountManagerItemAdapter extends BaseAdapter {
    private final ArrayList<String> result;
    private final Context context;
    private final int[] imageId;
    private final ServerConnectionHandler sch;

    public AccountManagerItemAdapter(AccountManagerActivity mainActivity, ArrayList<String> programNameList, int[] programImages) {

        result = programNameList;
        context = mainActivity;
        imageId = programImages;
        sch = ServerConnectionHandler.getInstance(context);
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        private TextView tv;
        private ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_account_activity_items, parent, false);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.text_of_list_view);
            holder.img = (ImageView) convertView.findViewById(R.id.image_of_list_view);
            convertView.setTag(holder);


        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tv.setText(result.get(position));
        holder.img.setImageResource(imageId[position]);
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (position) {
                    case 0:
                        Intent lastShoppingProduct = new Intent(context, UserLastShoppingProduct.class);
                        context.startActivity(lastShoppingProduct);
                        //open activity from down to top
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        ((Activity) context).finish();
                        break;
                    case 1:
                        Intent favoritesProduct = new Intent(context, UserFavouriteProduct.class);
                        context.startActivity(favoritesProduct);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        ((Activity) context).finish();
                        break;
                    case 2:
                        sch.deleteUserInfo();
                        Configuration.getConfig().userLoginStatus=false;
                        Intent login = new Intent(context, LoginActivity.class);
                        context.startActivity(login);
                        ((Activity) context).finish();
                        break;
                }
            }
        });
        return convertView;
    }

} 