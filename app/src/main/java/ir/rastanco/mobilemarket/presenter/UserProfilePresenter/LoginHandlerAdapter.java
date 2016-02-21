package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

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

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;

public class LoginHandlerAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    private ServerConnectionHandler sch;
    public LoginHandlerAdapter(AccountManager mainActivity, String[] prgmNameList, int[] prgmImages) {

        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sch=new ServerConnectionHandler(context);
    }
    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.list_view_account_activity_items, null);
        holder.tv=(TextView) rowView.findViewById(R.id.text_of_list_view);
        holder.img=(ImageView) rowView.findViewById(R.id.image_of_list_view);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (position){
                    case 0:
                        Intent lastShoppingProduct=new Intent(context,UserLastShoppingProduct.class);
                        context.startActivity(lastShoppingProduct);
                        ((Activity)context).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        ((Activity)context).finish();
                        break;
                    case 1:
                        Intent favoritesProduct=new Intent(context,UserFavouriteProduct.class);
                        context.startActivity(favoritesProduct);
                        ((Activity)context).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        ((Activity)context).finish();
                        break;
                    case 2:
                        sch.deleteUserInfo();
                        Intent login=new Intent(context,LoginHandler.class);
                        context.startActivity(login);
                        ((Activity)context).finish();
                        break;
                }
            }
        });
        return rowView;
    }

} 