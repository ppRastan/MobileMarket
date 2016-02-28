package ir.rastanco.mobilemarket.presenter.specialProductPresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;

/**
 * Created by shaisteS on 1394/12/09.
 */
public class SpecialProductFragmentManagement extends Fragment {

    private ServerConnectionHandler sch;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View specialProductView = inflater.inflate(R.layout.fragment_shop_mangement, container, false);
        sch = new ServerConnectionHandler(getContext());
        if (sch.emptyDBProduct()) {
            SpecialLoadingFragment loading = new SpecialLoadingFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.status, loading);
            transaction.commit();
        } else {
            SpecialProductFragment specialProductFragment = new SpecialProductFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.status, specialProductFragment);
            transaction.commit();
        }
        return specialProductView;
    }



}
