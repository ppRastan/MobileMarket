package ir.rastanco.mobilemarket.presenter.SecoundTabPresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.presenter.CheckConnectionFragment;
import ir.rastanco.mobilemarket.presenter.LoadingFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverChangeFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverChangeFragmentListener;
import ir.rastanco.mobilemarket.presenter.shopPresenter.ShopFragment;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/12/10.
 * This class managed Second Tab from Main Menu for displaying fragments(Loading Fragment or Information Product Fragment)
 * Second tab from Main Menu = کالای خواب
 */
public class SecondTabFragmentManager extends Fragment {

    private String pageName;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View secondProductView = inflater.inflate(R.layout.fragment_second_tab_manager, container, false);
        pageName=getArguments().getString("name");

        if (Configuration.productTableEmptyStatus==true && !Configuration.connectionStatus) {

            CheckConnectionFragment check=new CheckConnectionFragment();
            FragmentTransaction setCheck=getFragmentManager().beginTransaction();
            setCheck.replace(R.id.secondTabManager,check);
            setCheck.commit();

        }
        if (Configuration.productTableEmptyStatus==true && Configuration.connectionStatus){

            LoadingFragment loading = new LoadingFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.secondTabManager, loading);
            transaction.commit();

        }
        else if (Configuration.productTableEmptyStatus==false)
        {
            Bundle args = new Bundle();
            args.putString("name",pageName);
            ShopFragment shop=new ShopFragment();
            shop.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.secondTabManager, shop);
            transaction.commit();
        }

        ObserverChangeFragment.ObserverChangeFragmentListener(new ObserverChangeFragmentListener() {
            @Override
            public void changeFragment() {
                if (Configuration.MainPager.getCurrentItem()==2){
                    Bundle args = new Bundle();
                    args.putString("name", pageName);
                    ShopFragment shop = new ShopFragment();
                    shop.setArguments(args);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.secondTabManager, shop);
                    transaction.commit();
                }

            }
        });
        return secondProductView;
    }
}
