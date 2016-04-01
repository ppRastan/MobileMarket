package ir.rastanco.mobilemarket.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rastanco.mobilemarket.R;

/**
 * Created by ShaisteS on 1394/11/10.
 * contains loadingBar in mainActivity
 */
public class LoadingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_loading,container,false);
    }
}