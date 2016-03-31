package ir.rastanco.mobilemarket.presenter.Filter;

import android.app.DialogFragment;
import android.app.FragmentManager;
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
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Utilities;

/**
 * Created by ShaisteS on 1394/11/28.
 * DialogFragment For Displaying Products Price
 */
public class FilterOptionPrice extends DialogFragment {

    private static FilterOptionPrice filterOptionPrice;

    public static FilterOptionPrice getInstance() {
        if(filterOptionPrice == null){
           filterOptionPrice = new FilterOptionPrice();
        }
        return filterOptionPrice;
        // Supply num input as an argument.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageButton btnCancelAlertDialog = (ImageButton) dialogView.findViewById(R.id.cancel);
        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnCancelAlertDialog.setImageResource(R.mipmap.small_back_arrow);
        TextView titleBrand = (TextView)dialogView.findViewById(R.id.title_alertdialog_group);
        titleBrand.setText(Configuration.ShopFragmentContext.getResources().getString(R.string.choose_price));
        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ArrayList<String> priceFilter = new ArrayList<>();
        priceFilter= Utilities.getInstance().getPriceFilterTitle();
        ListView listCategory = (ListView) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, priceFilter);
        listCategory.setAdapter(adapter);

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent args = new Intent();
                args.putExtra("priceTitle",parent.getItemAtPosition(position).toString());
                getTargetFragment().onActivityResult(getTargetRequestCode(), 0, args);
                dismiss();
            }
        });
        return dialogView;
    }


}
