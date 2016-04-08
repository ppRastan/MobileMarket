package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by Parisa on 1394/11/22.
 * This class Manging User Favourite Product , User Last Shopping Product and exit profile
 */


public class AccountManagerActivity extends AppCompatActivity {
    private final static int[] listViewImages = {
            R.mipmap.previous_invoices,
            R.mipmap.favorites,
            R.mipmap.exit};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Configuration.getConfig().accountManagerContext = this;
        ArrayList<String> accountItems = new ArrayList<>();
        accountItems.add(getResources().getString(R.string.previous_shopped_items));
        accountItems.add(getResources().getString(R.string.liked));
        accountItems.add(getResources().getString(R.string.exit));
        ListView accountListView = (ListView) findViewById(R.id.account_lv);
        accountListView.setAdapter(new AccountManagerItemAdapter(this, accountItems, listViewImages));
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

}
