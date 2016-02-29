package ir.rastanco.mobilemarket.presenter.ThirdtabPresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.presenter.shopPresenter.ShopFragment;
import ir.rastanco.mobilemarket.presenter.shopPresenter.ShopLoadingFragment;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/11/10.
 */
public class ThirdTabFragmentManager extends Fragment{

    private String pageName;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View thirdTabView = inflater.inflate(R.layout.fragment_third_tab_manager, container, false);
        pageName=getArguments().getString("name");

        if (Configuration.productTableEmptyStatus==true) {
            ShopLoadingFragment loading = new ShopLoadingFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.thirdTabManager, loading);
            transaction.commit();
        }
        else if (Configuration.productTableEmptyStatus==false)
        {
            Bundle args = new Bundle();
            args.putString("name",pageName);
            ShopFragment shop=new ShopFragment();
            shop.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.thirdTabManager, shop);
            transaction.commit();
        }

        /*ObserverChangeFragment.ObserverChangeFragmentListener(new ObserverChangeFragmentListener() {
            @Override
            public void changeFragment() {

                Bundle args = new Bundle();
                args.putString("name",pageName);
                ShopFragment shop=new ShopFragment();
                shop.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.thirdTabManager, shop);
                transaction.commit();

            }
        });*/
        return thirdTabView;
    }
}

