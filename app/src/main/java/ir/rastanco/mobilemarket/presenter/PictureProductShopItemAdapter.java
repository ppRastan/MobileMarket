package ir.rastanco.mobilemarket.presenter;

import android.content.Context;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ir.rastanco.mobilemarket.R;

/**
 * Created by ShaisteS on 12/28/2015.
 */
public class PictureProductShopItemAdapter extends BaseAdapter{

    private String [] infoProducts;
    private Integer[] priceProducts;
    private Integer [] imageProducts;

    private Context context;
    private static LayoutInflater inflater=null;

    public PictureProductShopItemAdapter(FragmentActivity mainActivity, String[] infoProduct, Integer[] priceProduct , Integer [] imageProduct) {

        infoProducts=infoProduct;
        imageProducts=imageProduct;
        priceProducts=priceProduct;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
       return infoProducts.length;
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
        TextView infoP;
        TextView priceP;
        ImageView imgP;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.picture_produc_item_shop, null);
        holder.infoP=(TextView) rowView.findViewById(R.id.txt_infoProduct);
        holder.priceP=(TextView) rowView.findViewById(R.id.txt_priceProduct);
        holder.imgP=(ImageButton) rowView.findViewById(R.id.imbt_picProduct);

        holder.infoP.setText(infoProducts[position]);
        holder.priceP.setText(priceProducts[position].toString());
        holder.imgP.setImageResource(imageProducts[position]);

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               Toast.makeText(context, "You Clicked " + infoProducts[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}