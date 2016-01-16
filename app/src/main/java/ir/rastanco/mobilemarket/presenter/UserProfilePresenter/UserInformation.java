package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rastanco.mobilemarket.R;

/**
 * Created by Emertat on 01/15/2016.
 */
public class UserInformation extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_information, container, false);
    }
}
