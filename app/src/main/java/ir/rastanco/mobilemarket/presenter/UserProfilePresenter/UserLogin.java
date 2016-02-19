package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.Security;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by Emertat on 01/15/2016.
 */
public class UserLogin extends Fragment {

    private ServerConnectionHandler sch;
    private Security sec;
    private String user;
    private String pass;
    private Button btnLogin;
    private Button btnSignUp;
    private Button btnForgotPass;
    private EditText username;
    private EditText password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         final View userLogin= inflater.inflate(R.layout.fragment_user_login, container, false);
         Configuration.UserLoginFragment =getContext();
         sch=new ServerConnectionHandler(Configuration.UserLoginFragment);
         btnLogin=(Button) userLogin.findViewById(R.id.btn_login);
         btnSignUp=(Button) userLogin.findViewById(R.id.btn_enter);
         btnForgotPass=(Button) userLogin.findViewById(R.id.btn_forgot);
         username=(EditText) userLogin.findViewById(R.id.et_username);
         password=(EditText) userLogin.findViewById(R.id.et_password);
         sec=new Security();
         btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo aUser=new UserInfo();
                String key=sch.GetKey("http://decoriss.com/json/get,com=auth");
                //String key="974401741";
                user= String.valueOf(username.getText());
                //user="mahdavikia.m@gmail.com";
                aUser.setUserEmail(user);
                pass= String.valueOf(password.getText());
                //pass="1234";
                String hashInfo=sec.encode(user,pass,key);
                ArrayList<String> response=new ArrayList<String>();
                response=sch.GetAuthorizeResponse(hashInfo,key);
                Log.d("Response:",response.get(0));
                if(response.get(0).equals("")){
                    aUser.setUserId(Integer.parseInt(response.get(1)));
                    aUser.setUserLoginStatus(1);
                    sch.addUserInfoToTable(aUser);
                    UserProfile profile=new UserProfile();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.replaceFragment,profile );
                    transaction.commit();
                    //Configuration.UserProfileViewPager.setCurrentItem(1);
                }
                if (response.get(0).equals("key_expired"))
                    Toast.makeText(getActivity(), "دوباره تلاش کنید، سپاسگزار",
                            Toast.LENGTH_LONG).show();
                if (response.get(0).equals("user_pass_invalid"))
                    Toast.makeText(getActivity(), "نام کاربری یا رمز ورود صحیح نمی باشد",
                            Toast.LENGTH_LONG).show();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://decoriss.com/register,ثبت-نام_"));
                userLogin.getContext().startActivity(intent);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://decoriss.com/forgetpassword,فراموشی-رمز-عبور_"));
                userLogin.getContext().startActivity(intent);
            }
        });

        return userLogin;
    }
}