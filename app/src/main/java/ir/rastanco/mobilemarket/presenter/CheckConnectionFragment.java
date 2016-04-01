package ir.rastanco.mobilemarket.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOK;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisetS on 1394/12/15.
 * internet connection handler
 */
public class CheckConnectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View checkConnection=inflater.inflate(R.layout.fragment_check_connectin_to_internet,container,false);
        ImageButton reload=(ImageButton)checkConnection.findViewById(R.id.imgbReload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configuration.getConfig().connectionStatus){
                    if (Configuration.getConfig().productTableEmptyStatus)
                        ObserverConnectionInternetOK.setChangeFragmentParameter(true);
                }
            }
        });
        return checkConnection ;
    }
}
