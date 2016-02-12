package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 02/12/2016.
 */
public class manageUserPage extends Fragment {

    private ServerConnectionHandler sch;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View userManageView = inflater.inflate(R.layout.fragment_user_info, container, false);
        Configuration.ManageUserPage=getContext();
        sch=new ServerConnectionHandler(Configuration.ManageUserPage);
        if(sch.emptyUserInfo()){
            //login page
            UserLogin login=new UserLogin();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.replaceFragment, login);
            transaction.commit();
        }
        else {
            //profile page
            UserProfile profile=new UserProfile();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.replaceFragment, profile);
            transaction.commit();
        }
        return userManageView;
    }
}