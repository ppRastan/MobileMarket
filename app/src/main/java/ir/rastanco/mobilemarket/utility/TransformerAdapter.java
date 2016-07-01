package ir.rastanco.mobilemarket.utility;

/**
 * Created by parisan on 30/06/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;

import ir.rastanco.mobilemarket.R;

/**
 * Created by daimajia on 14-5-29.
 */
public class TransformerAdapter extends BaseAdapter{
    private Context mContext;
    public TransformerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return SliderLayout.Transformer.values().length;
    }

    @Override
    public Object getItem(int position) {
        return SliderLayout.Transformer.values()[position].toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t = (TextView)LayoutInflater.from(mContext).inflate(R.layout.slider_items,null);
        t.setText(getItem(position).toString());
        return t;
    }
}