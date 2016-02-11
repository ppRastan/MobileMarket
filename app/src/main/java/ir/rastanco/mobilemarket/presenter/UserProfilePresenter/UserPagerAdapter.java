package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by Emertat on 01/15/2016.
 */
public class UserPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    private ServerConnectionHandler sch;


    public UserPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        sch=new ServerConnectionHandler(Configuration.UserActivity);

        switch (position) {
            case 0:
                if(sch.emptyUserInfo())
                {
                    UserLogin login = new UserLogin();
                    return login;
                }
                else {
                    UserProfile profile=new UserProfile();
                    return profile;
                }

            case 1:
                UserAccount account = new UserAccount();
                return account;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
