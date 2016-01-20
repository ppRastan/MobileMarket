package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.Security;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by Emertat on 01/15/2016.
 */
public class UserInformation extends Fragment {

    private ServerConnectionHandler sch;
    private Security sec;
    private String user;
    private String pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View userProfileView= inflater.inflate(R.layout.fragment_user_information, container, false);

        Configuration.UserInformationFragment=getContext();
        sch=new ServerConnectionHandler(Configuration.UserInformationFragment);
        Button btnLogin=(Button) userProfileView.findViewById(R.id.btn_login);
        final EditText username=(EditText) userProfileView.findViewById(R.id.et_username);
        final EditText password=(EditText) userProfileView.findViewById(R.id.et_password);

        sec=new Security();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String key=sch.GetKey("http://decoriss.com/json/get,com=auth");
                String key="974401741";
                //user= String.valueOf(username.getText());
                user="mahdavikia.m@gmail.com";
                //pass= String.valueOf(password.getText());
                pass="1234";
                String hashInfo=sec.encode(user,pass,key);
                ArrayList<String> response=new ArrayList<String>();
                response=sch.GetAuthorizeResponse(hashInfo,key);
                Log.d("Response:",response.get(0));
                if(response.get(0).equals("")){
                    Configuration.UserProfileViewPager.setCurrentItem(1);

                }
            }
        });
        return userProfileView;
    }
}
