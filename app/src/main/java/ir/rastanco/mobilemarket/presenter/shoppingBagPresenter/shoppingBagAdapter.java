package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/10/30.
 */
public class shoppingBagAdapter extends ArrayAdapter<Integer> {

    private Activity myContext;
    private ArrayList<Integer> selectedProducts;
    private Product aProduct;
    private ServerConnectionHandler sch;

    public shoppingBagAdapter(Context context, int resource, ArrayList<Integer> productsId) {
        super(context, resource, productsId);

        selectedProducts = productsId;
        myContext =(Activity) context;
        sch=new ServerConnectionHandler(context);

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.shopping_bag_item, null);

        aProduct=new Product();
        aProduct=sch.getAProduct(selectedProducts.get(position));

        ImageView imgProduct=(ImageView)rowView.findViewById(R.id.img_productImage);
        TextView txtProductTitle=(TextView) rowView.findViewById(R.id.txt_productTitle);
        TextView txtProductPrice=(TextView) rowView.findViewById(R.id.txt_productPrice);
        TextView txtProductOff=(TextView) rowView.findViewById(R.id.txt_productOff);

        ImageLoader imgLoader = new ImageLoader(myContext); // important
        String picCounter = aProduct.getImagesPath().get(0);
        try {
            picCounter= URLEncoder.encode(picCounter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String image_url_1 = aProduct.getImagesMainPath()+
                picCounter+
                "&size="+
                Configuration.articleDisplaySize+"x"+Configuration.articleDisplaySize+
                "&q=30";
        imgLoader.DisplayImage(image_url_1, imgProduct);

        txtProductTitle.setText(aProduct.getTitle());
        txtProductPrice.setText("قیمت :"+String.valueOf(aProduct.getPrice()));
        txtProductOff.setText("تخفیف : "+String.valueOf(aProduct.getPriceOff()));

        return rowView;

    }
}
