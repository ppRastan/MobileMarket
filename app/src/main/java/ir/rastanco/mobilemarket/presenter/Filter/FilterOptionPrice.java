package ir.rastanco.mobilemarket.presenter.Filter;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by ShaisteS on 1394/11/28.
 */
public class FilterOptionPrice extends DialogFragment {

    private ServerConnectionHandler sch;
    private String pageName;

    public static FilterOptionPrice newInstance(String name) {
        FilterOptionPrice f = new FilterOptionPrice();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch = new ServerConnectionHandler(Configuration.superACFragment);
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
        ArrayList<String> priceFilter = new ArrayList<String>();
        priceFilter.add("تا سقف 1 میلیون تومان");
        priceFilter.add("تا سقف 5 میلیون تومان");
        priceFilter.add("تا سقف 10 میلیون تومان");
        priceFilter.add("بالاتر از 10 میلیون توملن");
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
