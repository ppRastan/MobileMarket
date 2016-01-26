package ir.rastanco.mobilemarket.presenter.ProductInfoPresenter;

import android.app.Fragment;
import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/11/05
 */
public class ProductOptionFragment extends Fragment {

    private ArrayList<ProductOption> options;
    private ServerConnectionHandler sch;
    private Product aProduct;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View productOption = inflater.inflate(R.layout.product_info_fragment, container, false);

        Configuration.ProductOptionFragment=getContext();
        sch=new ServerConnectionHandler(Configuration.ProductOptionFragment);
        options=new ArrayList<ProductOption>();
        options=sch.getOptionsOfAProduct("http://decoriss.com/json/get,com=options&pid="+
                aProduct.getId()+"&pgid="+aProduct.getGroupId()+"&cache=false");
        ListView lvProductOption=(ListView)productOption.findViewById(R.id.lv_productOption);
        ProductInfoItemAdapter adapter = new ProductInfoItemAdapter(Configuration.ProductInfoActivity,
                R.layout.product_info_item,options);
        lvProductOption.setAdapter(adapter);
        return productOption;
    }
}
