package ir.rastanco.mobilemarket.presenter.Filter;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/11/28.
 * DialogFragment For Displaying Products Brand
 */
public class FilterOptionBrand extends DialogFragment{
    private static FilterOptionBrand filterOptionBrand;

    public static FilterOptionBrand getInstance() {
        if(filterOptionBrand == null){
            filterOptionBrand = new FilterOptionBrand();
        }
        return filterOptionBrand;
        // Supply num input as an argument.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ServerConnectionHandler sch = ServerConnectionHandler.getInstance(Configuration.getConfig().ShopFragmentContext);
        Integer pageId=getArguments().getInt("pageId");
        ArrayList<Product> products = sch.getProductsOfAParentCategory(pageId);
        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageButton btnCancelAlertDialog = (ImageButton) dialogView.findViewById(R.id.cancel);
        btnCancelAlertDialog.setImageResource(R.mipmap.small_back_arrow);
        TextView titleBrand = (TextView)dialogView.findViewById(R.id.title_alert_dialogue_group);
        titleBrand.setText(Configuration.getConfig().ShopFragmentContext.getResources().getString(R.string.choose_brand));
        btnCancelAlertDialog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FilterOptionProduct.getInstance();
                    }
                });

        ArrayList<String>brandFilter=sch.getAllBrands(products);

        ListView listCategory = (ListView) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, brandFilter);
        listCategory.setAdapter(adapter);

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent args = new Intent();
                args.putExtra("brand", parent.getItemAtPosition(position).toString());
                getTargetFragment().onActivityResult(getTargetRequestCode(),0,args);
                dismiss();
            }
        });
        return dialogView;
    }
}
