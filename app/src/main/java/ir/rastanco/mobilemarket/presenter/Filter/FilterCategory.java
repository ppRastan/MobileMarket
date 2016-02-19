package ir.rastanco.mobilemarket.presenter.Filter;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ObserverFilterCategory;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.DataFilter;

/**
 * Created by shaiste on 1394/11/27.
 */
public class FilterCategory extends DialogFragment {

    private ServerConnectionHandler sch;
    private String pageName;

    public static FilterCategory newInstance() {
        FilterCategory f = new FilterCategory();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch = new ServerConnectionHandler(Configuration.superACFragment);
        pageName = getArguments().getString("name");
        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
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
        TextView text = (TextView) dialogView.findViewById(R.id.title_alertdialog_group);

        int categoryIdSelected = sch.getMainCategoryId(pageName);
        ArrayList<String> subCategoryTitle = sch.getTitleOfChildOfACategory(categoryIdSelected);
        ListView listCategory = (ListView) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
        listCategory.setAdapter(adapter);

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle args = new Bundle();
                args.putString("name", parent.getItemAtPosition(position).toString());
                FilterSubCategory filterSubCategory = new FilterSubCategory();
                filterSubCategory.setArguments(args);
                filterSubCategory.setTargetFragment(getFragmentManager().findFragmentByTag("Category"), 0);
                filterSubCategory.show(getFragmentManager(), "SubCategory");
                dismiss();
            }
        });
        return dialogView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                //get subCategory Selected from FilterSubcategory Dialog
                Bundle bundle = data.getExtras();
                String subCategorySelected = bundle.getString("subCategorySelected");
                Log.d("Tag 1", subCategorySelected);

                //send subCategory selected to SuperAwesomeCardFragment for show
                DataFilter.FilterCategory=subCategorySelected;
                ObserverFilterCategory.setAddFilter(true);
                break;
        }
    }


    public void show(FragmentManager supportFragmentManager, String tag) {
    }

}