package ir.rastanco.mobilemarket.presenter.Filter;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterCategory;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.DataFilter;

import java.util.ArrayList;


/**
 * Created by shaiste on 1394/11/27.
 * DialogFragment For Displaying Categories Title
 */
public class FilterCategory extends DialogFragment {

    private ServerConnectionHandler sch;
    private String pageName;

    public static FilterCategory newInstance() {
        FilterCategory f = new FilterCategory();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch = new ServerConnectionHandler(Configuration.ShopFragmentContext);
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
        //add filter=All
        subCategoryTitle.add(dialogView.getResources().getString(R.string.all));
        ListView listCategory = (ListView) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
        listCategory.setAdapter(adapter);

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemSelectedContent = parent.getItemAtPosition(position).toString();

                if (itemSelectedContent.equals(dialogView.getResources().getString(R.string.all))) {
                    Intent args = new Intent();
                    args.putExtra("all", dialogView.getResources().getString(R.string.all));
                    setTargetFragment(getFragmentManager().findFragmentByTag("category"), 1);
                    onActivityResult(getTargetRequestCode(), 1, args);
                    dismiss();
                } else if (sch.getHasChildACategoryWithTitle(itemSelectedContent) > 0) {
                    Bundle args = new Bundle();
                    args.putString("name", itemSelectedContent);
                    FilterSubCategory filterSubCategory = new FilterSubCategory();
                    filterSubCategory.setArguments(args);
                    filterSubCategory.setTargetFragment(getFragmentManager().findFragmentByTag("Category"), 0);
                    filterSubCategory.show(getFragmentManager(), "SubCategory");
                    dismiss();
                } else if (sch.getHasChildACategoryWithTitle(itemSelectedContent) == 0) {
                    Intent args = new Intent();
                    args.putExtra("noChild", itemSelectedContent);
                    setTargetFragment(getFragmentManager().findFragmentByTag("category"), 2);
                    onActivityResult(getTargetRequestCode(), 2, args);
                    dismiss();
                }


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
                //send subCategory selected to SuperAwesomeCardFragment for show
                DataFilter.FilterCategory = subCategorySelected;
                ObserverFilterCategory.setAddFilter(true);
                break;
            case 1:
                Bundle bundleAll = data.getExtras();
                String selectedAll = bundleAll.getString("all");
                DataFilter.FilterCategory = selectedAll;
                ObserverFilterCategory.setAddFilter(true);
                break;
            case 2:
                Bundle bundleNoChild = data.getExtras();
                String selectACategoryNoChild = bundleNoChild.getString("noChild");
                DataFilter.FilterCategory = selectACategoryNoChild;
                ObserverFilterCategory.setAddFilter(true);
                break;


        }
    }

    public void show(FragmentManager fragmentManager, String tag) {

    }
}
