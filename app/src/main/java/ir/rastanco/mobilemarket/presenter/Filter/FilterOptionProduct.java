package ir.rastanco.mobilemarket.presenter.Filter;

import android.app.DialogFragment;
import android.content.Context;
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
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterBrand;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterPrice;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.DataFilter;

/**
 * Created by ShaisteS on 1394/11/27.
 * DialogFragment For Displaying Products Property (Brand-Price)
 */
public class FilterOptionProduct extends DialogFragment {

    private int pageId;
    private static FilterOptionProduct filterOptionProduct;
    private Context context;
    public static FilterOptionProduct getInstance() {
        if(filterOptionProduct == null){
            filterOptionProduct = new FilterOptionProduct();
        }
        return filterOptionProduct;
        // Supply num input as an argument.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context=Configuration.getConfig().ShopFragmentContext;
        pageId = getArguments().getInt("pageId");
        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageButton btnCancelAlertDialog = (ImageButton) dialogView.findViewById(R.id.cancel);
        TextView titleBrand = (TextView)dialogView.findViewById(R.id.title_alertdialog_group);
        titleBrand.setText(Configuration.ShopFragmentContext.getResources().getString(R.string.filter));
        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ArrayList<String> optionProductFilter = new ArrayList<>();
        optionProductFilter.add(dialogView.getResources().getString(R.string.filterPriceTitle));
        optionProductFilter.add(dialogView.getResources().getString(R.string.filterBrandTitle));
        btnCancelAlertDialog.setImageResource(R.mipmap.ic_cancel_dialog);

        ListView listCategory = (ListView) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, optionProductFilter);
        listCategory.setAdapter(adapter);

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    FilterOptionPrice.getInstance().setTargetFragment(getFragmentManager().findFragmentByTag("FilterProductOption"), 0);
                    FilterOptionPrice.getInstance().show(getFragmentManager(), "FilterOptionPrice");
                } else if(position==1){
                    Bundle args = new Bundle();
                    args.putInt("pageId",pageId);
                    FilterOptionBrand.getInstance().setArguments(args);
                    FilterOptionBrand.getInstance().setTargetFragment(getFragmentManager().findFragmentByTag("FilterProductOption"), 1);
                    FilterOptionBrand.getInstance().show(getFragmentManager(),"FilterOptionBrand");
                }

                dismiss();
            }
        });
        return dialogView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                //get price Selected from FilterOptionPrice Dialog
                Bundle bundlePrice = data.getExtras();
                DataFilter.getInstance().FilterPriceTitle=bundlePrice.getString("priceTitle");
                DataFilter.getInstance().FilterOption=context.getResources().getString(R.string.price);
                ObserverFilterPrice.setAddFilterPrice(true);
                break;
            case 1:
                //get brand Selected from FilterOptionBrand Dialog
                Bundle bundleBrand=data.getExtras();
                DataFilter.FilterBrand=bundleBrand.getString("brand");
                DataFilter.FilterOption=context.getResources().getString(R.string.brand);
                ObserverFilterBrand.setAddFilterBrand(true);
                break;
        }
    }

}
