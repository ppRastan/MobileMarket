package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/12/10.
 */
public class ShopFragmentManager extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View specialProductView = inflater.inflate(R.layout.fragment_shop_manager, container, false);

        if (Configuration.productTableEmptyStatus==true) {
            ShopLoadingFragment loading = new ShopLoadingFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.status, loading);
            transaction.commit();
        }
        else if (Configuration.productTableEmptyStatus==false)
        {
            String pageName = getArguments().getString("name");
            Bundle args = new Bundle();
            args.putString("name",pageName);
            ShopFragment shop=new ShopFragment();
            shop.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.status, shop);
            transaction.commit();
        }
        return specialProductView;
    }


}
