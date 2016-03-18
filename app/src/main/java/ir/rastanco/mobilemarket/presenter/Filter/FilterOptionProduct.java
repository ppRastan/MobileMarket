package ir.rastanco.mobilemarket.presenter.Filter;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterAll;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterBrand;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterPrice;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.DataFilter;

/**
 * Created by shaisteS on 1394/11/27.
 * DialogFragment For Displaying Products Property (Brand-Price)
 */
public class FilterOptionProduct extends DialogFragment {

    private ServerConnectionHandler sch;
    private int pageId;
    private static FilterOptionProduct filterOptionProduct;
    public static FilterOptionProduct getInstance(String name) {
        if(filterOptionProduct == null){
            filterOptionProduct = new FilterOptionProduct();
        }
        return filterOptionProduct;
        // Supply num input as an argument.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch = new ServerConnectionHandler(Configuration.ShopFragmentContext);
        pageId = getArguments().getInt("pageId");
        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageButton btnCancelAlertDialog = (ImageButton) dialogView.findViewById(R.id.cancel);
        ImageButton btnResetAlertDialog = (ImageButton) dialogView.findViewById(R.id.reset_action);
        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnResetAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        ArrayList<String> optionProductFilter = new ArrayList<String>();
        optionProductFilter.add(dialogView.getResources().getString(R.string.filterPriceTitle));
        optionProductFilter.add(dialogView.getResources().getString(R.string.filterBrandTitle));
        optionProductFilter.add(dialogView.getResources().getString(R.string.all));

        ListView listCategory = (ListView) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
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
                else if (position==2){
                    Intent args = new Intent();
                    args.putExtra("all", dialogView.getResources().getString(R.string.all));
                    setTargetFragment(getFragmentManager().findFragmentByTag("FilterProductOption"),2);
                    onActivityResult(getTargetRequestCode(),2,args);

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
                DataFilter.FilterPriceTitle=bundlePrice.getString("priceTitle");
                DataFilter.FilterOption="price";
                ObserverFilterPrice.setAddFilterPrice(true);
                break;
            case 1:
                //get brand Selected from FilterOptionBrand Dialog
                Bundle bundleBrand=data.getExtras();
                DataFilter.FilterBrand=bundleBrand.getString("brand");
                DataFilter.FilterOption="brand";
                ObserverFilterBrand.setAddFilterBrand(true);
                break;
            case 2:
                Bundle bundleAll=data.getExtras();
                DataFilter.FilterAll=bundleAll.getString("all");
                DataFilter.FilterOption="all";
                ObserverFilterAll.setAddFilterAll(true);
                break;
        }
    }

    public void show(FragmentManager fragmentManager, String filterProductOption) {

    }
}
