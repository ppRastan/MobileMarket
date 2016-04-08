package ir.rastanco.mobilemarket.presenter.specialProductPresenter;

import android.content.Intent;
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
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOK;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOKListener;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/12/09.
 * This Fragment Manage that show Loading Fragment or Special Product Fragment
 */
public class SpecialProductFragmentManagement extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View specialProductView = inflater.inflate(R.layout.fragment_special_product_manager, container, false);
        Configuration.getConfig().specialProductManagementContext =getContext();
        if (Configuration.getConfig().emptyProductTable && !Configuration.getConfig().connectionStatus && Configuration.getConfig().emptyProductTable) {

            CheckConnectionFragment check=new CheckConnectionFragment();
            FragmentTransaction setCheck=getFragmentManager().beginTransaction();
            setCheck.replace(R.id.specialProductManagement,check);
            setCheck.commit();

        }
        /*if (!Configuration.getConfig().existProductInformation && Configuration.getConfig().connectionStatus){

            LoadingFragment loading = new LoadingFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.specialProductManagement, loading);
            transaction.commit();

        }*/

        else if (!Configuration.getConfig().emptyProductTable)
        {
            SpecialProductFragment specialProductFragment = new SpecialProductFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.specialProductManagement, specialProductFragment);
            transaction.commit();
        }

        ObserverChangeFragment.ObserverChangeFragmentListener(new ObserverChangeFragmentListener() {
            @Override
            public void changeFragment() {

                /*SpecialProductFragment specialProductFragment = new SpecialProductFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.specialProductManagement, specialProductFragment);
                transaction.commit();*/
                Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        ObserverConnectionInternetOK.ObserverConnectionInternetOKListener(new ObserverConnectionInternetOKListener() {
            @Override
            public void connectionOK() {

                LoadingFragment loading0 = new LoadingFragment();
                FragmentTransaction transaction0 = getFragmentManager().beginTransaction();
                transaction0.replace(R.id.specialProductManagement, loading0);
                transaction0.commit();

            }
        });
        return specialProductView;
    }



}
