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
import java.util.HashMap;
import java.util.Map;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/11/27.
 * DialogFragment For Displaying SubCategories Title and Child Of A SubCategory Title
 */
public class FilterSubCategory extends DialogFragment{

    private ServerConnectionHandler sch;
    private int categoryId;
    private Map<String,Integer> mapCategoryTitleToIdACategory;
    public static FilterSubCategory newInstance(String name) {
        FilterSubCategory f = new FilterSubCategory();
        // Supply num input as an argument.
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch=new ServerConnectionHandler(Configuration.ShopFragmentContext);
        categoryId=getArguments().getInt("categorySelectedId");
        mapCategoryTitleToIdACategory =new HashMap<String,Integer>();
        mapCategoryTitleToIdACategory =sch.MapTitleToIDForChildOfACategory(categoryId);
        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageButton btnCancelAlertDialog = (ImageButton) dialogView.findViewById(R.id.cancel);
        ImageButton btnResetAlertDialog = (ImageButton)dialogView.findViewById(R.id.reset_action);
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

        final ArrayList<String> subCategoryChildTitle=sch.getTitleOfChildOfACategory(categoryId);
        final ListView listSubCategory = (ListView) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryChildTitle);
        listSubCategory.setAdapter(adapter);

        listSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s=parent.getItemAtPosition(position).toString();
                int catId=mapCategoryTitleToIdACategory.get(s);

                if (sch.getCategoryHasChildWithId(catId)){

                    /*Bundle args = new Bundle();
                    args.putString("name", parent.getItemAtPosition(position).toString());
                    FilterSubCategory filterSubCategory = new FilterSubCategory();
                    filterSubCategory.setArguments(args);
                    filterSubCategory.setTargetFragment(getFragmentManager().findFragmentByTag("Category"), 0);
                    filterSubCategory.show(getFragmentManager(), "SubCategory");
                    dismiss();*/
                   //subCategoryIdSelected[0] =sch.getCategoryIdWithTitle(s);
                    ArrayList<String> subCategoryChildTitle=sch.getTitleOfChildOfACategory(mapCategoryTitleToIdACategory.get(s));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryChildTitle);
                    listSubCategory.setAdapter(adapter);
                    mapCategoryTitleToIdACategory =sch.MapTitleToIDForChildOfACategory(catId);

                }
                else {
                    Intent args = new Intent();
                    args.putExtra("subCategorySelected", mapCategoryTitleToIdACategory.get(s));
                    getTargetFragment().onActivityResult(getTargetRequestCode(),0,args);
                    dismiss();
                }

            }
        });
        return dialogView;
    }

    public void show(FragmentManager supportFragmentManager, String tag) {
    }
}
