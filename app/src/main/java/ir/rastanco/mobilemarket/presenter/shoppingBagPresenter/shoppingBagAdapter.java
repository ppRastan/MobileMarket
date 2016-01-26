package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Typeface font;
    private Activity myContext;
    private ArrayList<Integer> selectedProducts;
    private Product aProduct;
    private ServerConnectionHandler sch;

    public shoppingBagAdapter(Context context, int resource, ArrayList<Integer> productsId) {
        super(context, resource, productsId);

        selectedProducts = productsId;
        myContext =(Activity) context;
        sch=new ServerConnectionHandler(context);
        font = Typeface.createFromAsset(myContext.getAssets(),"fonts/B Zar.ttf");
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
        ImageButton btnDelete=(ImageButton)rowView.findViewById(R.id.imb_delete);
        TextView totalPrice = (TextView)rowView.findViewById(R.id.txt_total_price);
        txtProductOff.setTypeface(font);
        txtProductPrice.setTypeface(font);
        totalPrice.setTypeface(font);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(myContext);
                alertDialog.setMessage(R.string.confirm_to_delete);
                alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {


                                        sch.addProductToShoppingBag(aProduct.getId());
                                        for (int i=0;i<selectedProducts.size();i++){
                                        if (aProduct.getId()==selectedProducts.get(i)){
                                            selectedProducts.remove(i);
                                             sch.deleteAProductShopping(aProduct.getId());
                                              }
                                               }
                                                updateList(selectedProducts);
                    }
                });

                alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();

            }
        });

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
        totalPrice.setText("قابل پرداخت :" + String.valueOf(aProduct.getPrice()-aProduct.getPriceOff()));
        return rowView;

    }

    public void updateList(ArrayList<Integer> results) {
        selectedProducts = results;
        //Triggers the list update
        notifyDataSetChanged();
    }

}
