package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)

            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        accountListView = (ListView)findViewById(R.id.account_lv);
        accountListView.setAdapter(new LoginHandlerAdapter(this, listViewTexts,listViewImages));

    }
}
