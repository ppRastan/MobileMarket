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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterCategory;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.DataFilter;


/**
 * Created by shaiste on 1394/11/27.
 * DialogFragment For Displaying Categories Title
 */
public class FilterCategory extends DialogFragment {

    private ServerConnectionHandler sch;
    private int pageId;
    private Map<String,Integer> mapCategoryTitleToId;
    private static FilterCategory filterCategory;
    private TextView titleOfAlertDialog;
    public static FilterCategory getInstance() {
        if(filterCategory == null){
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

        sch = new ServerConnectionHandler(Configuration.ShopFragmentContext);
        pageId = getArguments().getInt("pageId");
        mapCategoryTitleToId=new HashMap<String,Integer>();
        mapCategoryTitleToId=sch.MapTitleToIDForAllCategory();
        final View dialogView = inflater.inflate(R.layout.title_alertdialog_for_group, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageButton btnCancelAlertDialog = (ImageButton) dialogView.findViewById(R.id.cancel);

        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        titleOfAlertDialog = (TextView) dialogView.findViewById(R.id.title_alertdialog_group);
        titleOfAlertDialog.setText(Configuration.ShopFragmentContext.getResources().getString(R.string.choose_group));
        btnCancelAlertDialog.setImageResource(R.mipmap.ic_cancel_dialog);
        ArrayList<String> subCategoryTitle = sch.getTitleOfChildOfACategory(pageId);
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
                    args.putExtra("all",0);
                    setTargetFragment(getFragmentManager().findFragmentByTag("category"), 1);
                    onActivityResult(getTargetRequestCode(), 1, args);
                    dismiss();
                } else if (sch.getHasChildACategoryWithId(mapCategoryTitleToId.get(itemSelectedContent)) > 0) {
                    Bundle args = new Bundle();

                    args.putInt("categorySelectedId", mapCategoryTitleToId.get(itemSelectedContent));
                    FilterSubCategory.getInstance().setArguments(args);
                    //FilterSubCategory.getInstance().setDialogTitle(itemSelectedContent);
                    FilterSubCategory.getInstance().setTargetFragment(getFragmentManager().findFragmentByTag("Category"), 0);
                    FilterSubCategory.getInstance().show(getFragmentManager(), "SubCategory");
                    dismiss();
                } else if (sch.getHasChildACategoryWithId(mapCategoryTitleToId.get(itemSelectedContent))==0) {
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
                DataFilter.FilterCategoryId = subCategorySelected;
                DataFilter.FilterCategoryTitle=sch.getACategoryTitle(subCategorySelected);
                ObserverFilterCategory.setAddFilter(true);
                break;
            case 1:
                Bundle bundleAll = data.getExtras();
                int selectedAll = bundleAll.getInt("all");
                DataFilter.FilterCategoryTitle =Configuration.ShopFragmentContext.getResources().getString(R.string.all) ;
                DataFilter.FilterCategoryId=selectedAll;
                ObserverFilterCategory.setAddFilter(true);
                break;
            case 2:
                Bundle bundleNoChild = data.getExtras();
                int selectACategoryNoChild = bundleNoChild.getInt("noChild");
                DataFilter.FilterCategoryTitle=sch.getACategoryTitle(selectACategoryNoChild);
                DataFilter.FilterCategoryId = selectACategoryNoChild;
                ObserverFilterCategory.setAddFilter(true);
                break;
        }
    }

    public void show(FragmentManager fragmentManager, String tag) {

    }


}
