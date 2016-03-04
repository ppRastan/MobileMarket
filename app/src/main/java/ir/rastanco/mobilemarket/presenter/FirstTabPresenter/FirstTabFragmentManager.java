package ir.rastanco.mobilemarket.presenter.FirstTabPresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.presenter.LoadingFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverChangeFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverChangeFragmentListener;
import ir.rastanco.mobilemarket.presenter.shopPresenter.ShopFragment;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/12/10.
 * This class managed first Tab from Main Menu for displaying fragments(Loading Fragment or Information Product Fragment)
 * First tab from Main Menu = مبلمان منزل
 */
public class FirstTabFragmentManager extends Fragment {

    private String pageName;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View firstProductView = inflater.inflate(R.layout.fragment_first_tab_manager, container, false);
        pageName=getArguments().getString("name");

        if (Configuration.productTableEmptyStatus==true) {
            LoadingFragment loading = new LoadingFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.firstTabManager, loading);
            transaction.commit();
        }
        else if (Configuration.productTableEmptyStatus==false)
        {
            Bundle args = new Bundle();
            args.putString("name",pageName);
            ShopFragment shop=new ShopFragment();
            shop.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.firstTabManager, shop);
            transaction.commit();
        }

        ObserverChangeFragment.ObserverChangeFragmentListener(new ObserverChangeFragmentListener() {
            @Override
            public void changeFragment() {

                Bundle args = new Bundle();
                args.putString("name", pageName);
                ShopFragment shop = new ShopFragment();
                shop.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.firstTabManager, shop);
                transaction.commit();

            }
        });

        return firstProductView;
    }
}