package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.Security;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

public class LoginHandler extends AppCompatActivity {


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Configuration.UserLoginContext =this;
        sch=new ServerConnectionHandler(Configuration.UserLoginContext);
        btnLogin=(Button)findViewById(R.id.btn_login);
        btnSignUp=(Button)findViewById(R.id.btn_enter);
        btnForgotPass=(Button)findViewById(R.id.btn_forgot);
        username=(EditText)findViewById(R.id.et_username);
        password=(EditText)findViewById(R.id.et_password);
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
                Log.d("Response:", response.get(0));
                if(response.get(0).equals("")){
                    aUser.setUserId(Integer.parseInt(response.get(1)));
                    aUser.setUserLoginStatus(1);
                    sch.addUserInfoToTable(aUser);
                    Configuration.userLoginStatus=true;
                    Intent userAccount=new Intent(Configuration.UserLoginContext,AccountManager.class);
                    startActivity(userAccount);
                    finish();
                }
                if (response.get(0).equals("key_expired"))
                    Toast.makeText(Configuration.UserLoginContext, "دوباره تلاش کنید، سپاسگزار",
                            Toast.LENGTH_LONG).show();
                if (response.get(0).equals("user_pass_invalid"))
                    Toast.makeText(Configuration.UserLoginContext, "نام کاربری یا رمز ورود صحیح نمی باشد",
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
                startActivity(intent);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://decoriss.com/forgetpassword,فراموشی-رمز-عبور_"));
                startActivity(intent);
            }
        });
    }
}
