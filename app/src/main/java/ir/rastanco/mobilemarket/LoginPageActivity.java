package ir.rastanco.mobilemarket;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class LoginPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //Displaying TextInputLayout Error
        TextInputLayout lNameLayout = (TextInputLayout) findViewById(R.id
                .lNameLayout);
        lNameLayout.setErrorEnabled(true);
        lNameLayout.setError("Min 2 chars required");

        //Displaying EditText Error
        EditText age = (EditText) findViewById(R.id.age);
        age.setError("Required");

        //Displaying both TextInputLayout and EditText Errors
        TextInputLayout phoneLayout = (TextInputLayout) findViewById(R.id
                .phoneLayout);
        phoneLayout.setErrorEnabled(true);
        phoneLayout.setError("Please enter a phone number");
        EditText phone = (EditText) findViewById(R.id.phone);
        phone.setError("Required");
    }

}
