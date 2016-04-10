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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterCategory;
import ir.rastanco.mobilemarket.utility.Configuration;


/**
 * Created by ShaisteS on 1394/11/27.
 * DialogFragment For Displaying Categories Title
 */
public class FilterCategory extends DialogFragment {

    private ServerConnectionHandler sch;
    private Map<String, Integer> mapCategoryTitleToId;
    private static FilterCategory filterCategory;

    public static FilterCategory getInstance() {
        if (filterCategory == null) {
            filterCategory = new FilterCategory();
        }
        return filterCategory;
        // Supply num input as an argument.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch = ServerConnectionHandler.getInstance(Configuration.getConfig().shopFragmentContext);
        Integer pageId = getArguments().getInt("pageId");
        mapCategoryTitleToId = new HashMap<>();
        mapCategoryTitleToId = sch.MapTitleToIDForAllCategory();
        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageButton btnCancelAlertDialog = (ImageButton) dialogView.findViewById(R.id.cancel);
        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView titleOfAlertDialog = (TextView) dialogView.findViewById(R.id.title_alert_dialogue_group);
        titleOfAlertDialog.setText(Configuration.getConfig().shopFragmentContext.getResources().getString(R.string.choose_group));
        btnCancelAlertDialog.setImageResource(R.mipmap.ic_cancel_dialog);
        ArrayList<String> subCategoryTitle = sch.getTitleOfChildOfACategory(pageId);

        //add filter=All
        subCategoryTitle.add(0, dialogView.getResources().getString(R.string.all));
        final ListView listCategory = (ListView) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
        listCategory.setAdapter(adapter);

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemSelectedContent = parent.getItemAtPosition(position).toString();

                if (itemSelectedContent.equals(dialogView.getResources().getString(R.string.all))) {
                    Intent args = new Intent();
                    args.putExtra("all", 0);
                    setTargetFragment(getFragmentManager().findFragmentByTag("category"), 1);
                    onActivityResult(getTargetRequestCode(), 1, args);
                    dismiss();
                } else if (sch.getHasChildACategoryWithId(mapCategoryTitleToId.get(itemSelectedContent)) > 0) {

                    int catId = mapCategoryTitleToId.get(itemSelectedContent);
                    ArrayList<String> subCategoryChildTitle = sch.getTitleOfChildOfACategory(catId);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryChildTitle);
                    subCategoryChildTitle.add(0,dialogView.getResources().getString(R.string.all));
                    listCategory.setAdapter(adapter);
                    mapCategoryTitleToId = sch.MapTitleToIDForChildOfACategory(catId);

                } else if (sch.getHasChildACategoryWithId(mapCategoryTitleToId.get(itemSelectedContent)) == 0) {
                    Intent args = new Intent();
                    args.putExtra("noChild", mapCategoryTitleToId.get(itemSelectedContent));
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
                int subCategorySelected = bundle.getInt("subCategorySelected");
                //send subCategory selected to SuperAwesomeCardFragment for show
                Configuration.getConfig().filterCategoryId = subCategorySelected;
                Configuration.getConfig().filterCategoryTitle = sch.getACategoryTitleWithCategoryId(subCategorySelected);
                ObserverFilterCategory.setAddFilter(true);
                break;
            case 1:
                Bundle bundleAll = data.getExtras();
                int selectedAll = bundleAll.getInt("all");
                Configuration.getConfig().filterCategoryTitle = Configuration.getConfig().shopFragmentContext.getResources().getString(R.string.all);
                Configuration.getConfig().filterCategoryId = selectedAll;
                ObserverFilterCategory.setAddFilter(true);
                break;
            case 2:
                Bundle bundleNoChild = data.getExtras();
                int selectACategoryNoChild = bundleNoChild.getInt("noChild");
                Configuration.getConfig().filterCategoryTitle = sch.getACategoryTitleWithCategoryId(selectACategoryNoChild);
                Configuration.getConfig().filterCategoryId = selectACategoryNoChild;
                ObserverFilterCategory.setAddFilter(true);
                break;
        }
    }

    public void show(FragmentManager fragmentManager, String tag) {


    }


}
