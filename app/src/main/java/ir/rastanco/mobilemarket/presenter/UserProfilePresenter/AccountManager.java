package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;

public class AccountManager extends AppCompatActivity {

    private ListView accountListView;
    private ArrayList<String>accountItems;
    public static int [] listViewImages ={R.mipmap.previous_invoices,R.mipmap.favorites,R.mipmap.exit};
    public static String [] listViewTexts ={
            "خریدهای گذشته",
            "علاقه مندی ها",
            "خروج"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        accountListView = (ListView)findViewById(R.id.account_lv);
        accountListView.setAdapter(new LoginHandlerAdapter(this, listViewTexts,listViewImages));

    }
}
