package ir.rastanco.mobilemarket.presenter.specialProductPresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.presenter.CheckConnectionFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverChangeFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverChangeFragmentListener;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/12/09.
 */
public class SpecialProductFragmentManagement extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View specialProductView = inflater.inflate(R.layout.fragment_special_product_manager, container, false);
        if (Configuration.productTableEmptyStatus==true && !Configuration.connectionStatus) {

            CheckConnectionFragment check=new CheckConnectionFragment();
            FragmentTransaction setCheck=getFragmentManager().beginTransaction();
            setCheck.replace(R.id.specialProductManagement,check);
            setCheck.commit();

        }
        if (Configuration.productTableEmptyStatus==true && Configuration.connectionStatus){

            SpecialLoadingFragment loading = new SpecialLoadingFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.specialProductManagement, loading);
            transaction.commit();

        }

        else if (Configuration.productTableEmptyStatus==false)
        {
            SpecialProductFragment specialProductFragment = new SpecialProductFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.specialProductManagement, specialProductFragment);
            transaction.commit();
        }

        ObserverChangeFragment.ObserverChangeFragmentListener(new ObserverChangeFragmentListener() {
            @Override
            public void changeFragment() {
                if (Configuration.MainPager.getCurrentItem()==4){
                    SpecialProductFragment specialProductFragment = new SpecialProductFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.specialProductManagement, specialProductFragment);
                    transaction.commit();
                }
            }
        });

        return specialProductView;
    }



}
