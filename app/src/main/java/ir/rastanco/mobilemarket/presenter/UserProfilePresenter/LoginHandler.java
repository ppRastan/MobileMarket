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
import ir.rastanco.mobilemarket.utility.Links;

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
        Configuration.getConfig().UserLoginContext =this;
        sch=new ServerConnectionHandler(Configuration.getConfig().UserLoginContext);
        btnLogin=(Button)findViewById(R.id.btn_login);
        btnSignUp=(Button)findViewById(R.id.btn_enter);
        btnForgotPass=(Button)findViewById(R.id.btn_forgot);
        username=(EditText)findViewById(R.id.et_username);
        password=(EditText)findViewById(R.id.et_password);
        sec=new Security();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Configuration.getConfig().connectionStatus){
                    UserInfo aUser=new UserInfo();
                    String key=sch.GetKey(Links.getInstance().generateURLForGetKey());
                    user= String.valueOf(username.getText());
                    aUser.setUserEmail(user);
                    pass= String.valueOf(password.getText());
                    String hashInfo=sec.encode(user,pass,key);
                    ArrayList<String> response=new ArrayList<String>();
                    response=sch.GetAuthorizeResponse(hashInfo,key);
                    Log.d("Response:", response.get(0));
                    if(response.get(0).equals("")){
                        aUser.setUserId(Integer.parseInt(response.get(1)));
                        aUser.setUserLoginStatus(1);
                        sch.addUserInfoToTable(aUser);
                        Configuration.getConfig().userLoginStatus=true;
                        Intent userAccount=new Intent(Configuration.getConfig().UserLoginContext,AccountManager.class);
                        startActivity(userAccount);
                        finish();
                    }
                    if (response.get(0).equals("key_expired"))
                        Toast.makeText(Configuration.getConfig().UserLoginContext,Configuration.UserLoginContext.getResources().getString(R.string.try_more),
                                Toast.LENGTH_LONG).show();
                    if (response.get(0).equals("user_pass_invalid"))
                        Toast.makeText(Configuration.getConfig().UserLoginContext,Configuration.UserLoginContext.getResources().getString(R.string.not_correct),
                                Toast.LENGTH_LONG).show();

                }

                else
                    Toast.makeText(Configuration.getConfig().UserLoginContext,Configuration.UserLoginContext.getResources().getString(R.string.checkConnection),
                            Toast.LENGTH_LONG).show();


            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(Links.getInstance().generateURLSignUp()));
                startActivity(intent);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(Links.getInstance().generateURLForForgotPassword()));
                startActivity(intent);
            }
        });
    }
}
