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

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/11/27.
 */
public class FilterSubCategory extends DialogFragment{

    private ServerConnectionHandler sch;
    private String categoryName;

    public static FilterCategory newInstance(String name) {
        FilterCategory f = new FilterCategory();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("name", name);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch=new ServerConnectionHandler(Configuration.superACFragment);
        categoryName=getArguments().getString("name");
        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
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

        int subCategoryIdSelected=sch.getCategoryIdWithTitle(categoryName);
        final ArrayList<String> subCategoryChildTitle=sch.getTitleOfChildOfACategory(subCategoryIdSelected);
        ListView listSubCategory = (ListView) dialogView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryChildTitle);
        listSubCategory.setAdapter(adapter);

        listSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if (sch.getCategoryHasChildWithTitle(parent.getItemAtPosition(position).toString())){
                    Bundle args = new Bundle();
                    args.putString("name", parent.getItemAtPosition(position).toString());
                    FilterSubCategory filterSubCategory = new FilterSubCategory();
                    filterSubCategory.setArguments(args);
                    filterSubCategory.setTargetFragment(getFragmentManager().findFragmentByTag("Category"), 0);
                    filterSubCategory.show(getFragmentManager(), "SubCategory");
                    dismiss();
                }
                else {*/
                    Intent args = new Intent();
                    args.putExtra("subCategorySelected", parent.getItemAtPosition(position).toString());
                    getTargetFragment().onActivityResult(getTargetRequestCode(),0,args);
                    dismiss();
                //}

            }
        });
        return dialogView;
    }

    public void show(FragmentManager supportFragmentManager, String tag) {
    }
}
