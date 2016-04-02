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
import ir.rastanco.mobilemarket.utility.Link;

public class LoginActivity extends AppCompatActivity {


    private ServerConnectionHandler sch;
    private Security loginActivitySecurity;
    private String loginActivityUserName;
    private String loginActivityPassWord;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Configuration.getConfig().userLoginActivityContext =this;
        sch=new ServerConnectionHandler(Configuration.getConfig().userLoginActivityContext);
        Button btnLogin=(Button)findViewById(R.id.btn_login);
        Button btnSignUp=(Button)findViewById(R.id.btn_enter);
        Button btnForgotPass=(Button)findViewById(R.id.btn_forgot);
        username=(EditText)findViewById(R.id.et_username);
        password=(EditText)findViewById(R.id.et_password);
        loginActivitySecurity =new Security();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Configuration.getConfig().connectionStatus){
                    UserInfo aUser=new UserInfo();
                    String key=sch.GetKey(Link.getInstance().generateURLForGetKey());
                    loginActivityUserName = String.valueOf(username.getText());
                    aUser.setUserEmail(loginActivityUserName);
                    loginActivityPassWord = String.valueOf(password.getText());
                    String hashInfo= loginActivitySecurity.encode(loginActivityUserName, loginActivityPassWord, key);
                    ArrayList<String>response=sch.GetAuthorizeResponse(hashInfo,key);
                    Log.d("Response:", response.get(0));
                    if(response.get(0).equals("")){
                        aUser.setUserId(Integer.parseInt(response.get(1)));
                        aUser.setUserLoginStatus(1);
                        sch.addUserInfoToTable(aUser);
                        Configuration.getConfig().userLoginStatus=true;
                        Intent userAccount=new Intent(Configuration.getConfig().userLoginActivityContext,AccountManagerActivity.class);
                        startActivity(userAccount);
                        finish();
                    }
                    if (response.get(0).equals("key_expired"))
                        Toast.makeText(Configuration.getConfig().userLoginActivityContext,Configuration.getConfig().userLoginActivityContext.getResources().getString(R.string.try_more),
                                Toast.LENGTH_LONG).show();
                    if (response.get(0).equals("user_pass_invalid"))
                        Toast.makeText(Configuration.getConfig().userLoginActivityContext,Configuration.getConfig().userLoginActivityContext.getResources().getString(R.string.not_correct),
                                Toast.LENGTH_LONG).show();

                }

                else
                    Toast.makeText(Configuration.getConfig().userLoginActivityContext,Configuration.getConfig().userLoginActivityContext.getResources().getString(R.string.checkConnection),
                            Toast.LENGTH_LONG).show();


            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(Link.getInstance().generateURLSignUp()));
                startActivity(intent);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(Link.getInstance().generateURLForForgotPassword()));
                startActivity(intent);
            }
        });
    }
}
