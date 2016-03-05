package ir.rastanco.mobilemarket.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisetS on 1394/12/15.
 */
public class CheckConnectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View checkConnection=inflater.inflate(R.layout.fragment_check_connectin_to_internet,null);
        ImageButton reload=(ImageButton)checkConnection.findViewById(R.id.imgbReload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configuration.connectionStatus){
                    
                    switch (Configuration.MainPager.getCurrentItem()){
                        case 4:
                            LoadingFragment loading0 = new LoadingFragment();
                            FragmentTransaction transaction0 = getFragmentManager().beginTransaction();
                            transaction0.replace(R.id.specialProductManagement, loading0);
                            transaction0.commit();
                            break;
                        case 3:
                            LoadingFragment loading1 = new LoadingFragment();
                            FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                            transaction1.replace(R.id.firstTabManager, loading1);
                            transaction1.commit();
                            break;
                        case 2:
                            LoadingFragment loading2 = new LoadingFragment();
                            FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                            transaction2.replace(R.id.secondTabManager, loading2);
                            transaction2.commit();
                            break;
                        case 1:
                            LoadingFragment loading3 = new LoadingFragment();
                            FragmentTransaction transaction3 = getFragmentManager().beginTransaction();
                            transaction3.replace(R.id.thirdTabManager, loading3);
                            transaction3.commit();
                            break;
                    }
                }

            }
        });


        return checkConnection ;


    }
}
