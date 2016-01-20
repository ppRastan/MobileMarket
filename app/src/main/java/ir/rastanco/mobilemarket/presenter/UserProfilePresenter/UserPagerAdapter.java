package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Emertat on 01/15/2016.
 */
public class UserPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;


    public UserPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                UserInformation tab1 = new UserInformation();
                return tab1;
            case 1:
                UserAccount tab2 = new UserAccount();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
