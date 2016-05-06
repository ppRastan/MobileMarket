package ir.rastanco.mobilemarket.presenter.Filter;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterBrand;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterCancel;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterCancelListener;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterPrice;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/11/27.
 * DialogFragment For Displaying Products Property (Brand-Price)
 */
public class FilterOptionProduct extends DialogFragment {

    private int pageId;
    private static FilterOptionProduct filterOptionProduct;
    private LinearLayout customizeDialogue;
    private Context context;
    private ServerConnectionHandler serverConnectionHandler;

    public static FilterOptionProduct getInstance() {
        if (filterOptionProduct == null) {
            filterOptionProduct = new FilterOptionProduct();
        }
        return filterOptionProduct;
        // Supply num input as an argument.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
        context = Configuration.getConfig().shopFragmentContext;
        customizeDialogue = (LinearLayout) dialogView.findViewById(R.id.customized_dialog);
        pageId = getArguments().getInt("pageId");
        serverConnectionHandler = ServerConnectionHandler.getInstance(context);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageButton btnCancelAlertDialog = (ImageButton) dialogView.findViewById(R.id.cancel);
        TextView titleBrand = (TextView) dialogView.findViewById(R.id.title_alert_dialogue_group);
        titleBrand.setText(Configuration.getConfig().shopFragmentContext.getResources().getString(R.string.filter));
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
                R.layout.listview_layout, R.id.list_viewText, optionProductFilter);
        listCategory.setAdapter(adapter);

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    FilterOptionPrice.getInstance().setTargetFragment(getFragmentManager().findFragmentByTag("FilterProductOption"), 0);
                    FilterOptionPrice.getInstance().show(getFragmentManager(), "FilterOptionPrice");
                } else if (position == 1) {
                    ArrayList<Product> products = serverConnectionHandler.getProductsOfAParentCategory(pageId);
                    ArrayList<String> brandTitle = serverConnectionHandler.getAllBrands(products);
                    if (brandTitle.size() == 0) {
                        String categorySelectedTitle = serverConnectionHandler.getACategoryTitleWithCategoryId(pageId);
                        String message = "دسته ی " + categorySelectedTitle + " فاقد برند می باشد";
                        Snackbar.make(Configuration.getConfig().mainActivityView, message, Snackbar.LENGTH_LONG).show();
                        getDialog().dismiss();
                    } else {
                        Bundle args = new Bundle();
                        args.putStringArrayList("brandsTitle", brandTitle);
                        FilterOptionBrand.getInstance().setArguments(args);
                        FilterOptionBrand.getInstance().setTargetFragment(getFragmentManager().findFragmentByTag("FilterProductOption"), 1);
                        FilterOptionBrand.getInstance().show(getFragmentManager(), "FilterOptionBrand");
                    }

                }
                getDialog().hide();
            }
        });

        ObserverFilterCancel.changeFilterCancel(new ObserverFilterCancelListener() {
            @Override
            public void filterCancel() {
                if (ObserverFilterCancel.getFilterCancel() == true)
                    getDialog().show();
                else
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
                Configuration.getConfig().filterPriceTitle = bundlePrice.getString("priceTitle");
                Configuration.getConfig().filterOption = context.getResources().getString(R.string.price);
                ObserverFilterPrice.setAddFilterPrice(true);
                break;
            case 1:
                //get brand Selected from FilterOptionBrand Dialog
                Bundle bundleBrand = data.getExtras();
                Configuration.getConfig().filterBrand = bundleBrand.getString("brand");
                Configuration.getConfig().filterOption = context.getResources().getString(R.string.brand);
                ObserverFilterBrand.setAddFilterBrand(true);
                break;
        }
    }

}
