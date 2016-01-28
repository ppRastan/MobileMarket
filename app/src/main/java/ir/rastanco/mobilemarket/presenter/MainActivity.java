package ir.rastanco.mobilemarket.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductShop;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProduct;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.UserProfileActivity;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.CounterIconUtils;
import ir.rastanco.mobilemarket.utility.CounterIconUtils;
//parisa rashidi nezhad connection
public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @InjectView(R.id.pager)
    ViewPager pager;
    private MyPagerAdapter adapter;
    private Drawable oldBackground = null;
    private int currentColor;
    private SystemBarTintManager mTintManager;
    private AutoCompleteTextView textToSearch;
    private ImageButton backButton;
    private ServerConnectionHandler sch;
    private ArrayList<Product> products;
    private ArrayList<Article> articles;
    private ArrayList<Category> categories;
    private LinearLayout toolbarSearch;
    private PhoneCallListener phoneListener;
    private TelephonyManager telephonyManager;
    private FloatingActionButton fab;
    private Display display;
    private Point size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.CreatePageRightToLeft();
        this.addActionBar();
        this.addFontAndColors();
        this.addServerConnection();
        this.checkDbState();
        this.phoneManager();
        this.setFAb();
        this.displayWindow();
    }

    private void displayWindow() {
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            Configuration.homeDisplaySize = String.valueOf(size.x);
            Configuration.productInfoHeightSize = String.valueOf(size.x - 100);
            Configuration.shopDisplaySize = String.valueOf(((size.x) * 0.5)-12);
            Configuration.articleDisplaySize=String.valueOf((size.x) * 0.3);
        }
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 5, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(1);
        this.setDecorissThemColour();


    }

    private void setDecorissThemColour() {
        changeColor(getResources().getColor(R.color.decoriss));
    }

    private void CreatePageRightToLeft() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    private void setFAb()
    {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_color)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:00000000000"));
                if (ActivityCompat.checkSelfPermission(Configuration.MainActivityFragment, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (ActivityCompat.checkSelfPermission(Configuration.MainActivityFragment, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);

            }
        });

    }

    private void phoneManager() {
        phoneListener = new PhoneCallListener();
        telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private void checkDbState() {
        if (sch.emptyDBCategory()){
            categories=sch.getAllCategoryInfoURL("http://decoriss.com/json/get,com=allcats&cache=false");
            sch.addAllCategoryToTable(categories);
        }

        if (sch.emptyDBProduct()){

            ParseJsonProduct pjp=new ParseJsonProduct(Configuration.MainActivityFragment);
            pjp.execute("http://decoriss.com/json/get,com=product&newfromts=1352689345&cache=false");
        }

        if (sch.emptyDBArticle()){
            articles=sch.getAllArticlesAndNewsURL("http://decoriss.com/json/get,com=news&name=blog&order=desc&limit=0-25&cache=false");
            sch.addAllArticlesToTable(articles);
        }
    }

    private void addServerConnection() {
        Configuration.MainActivityFragment = this;
        Configuration.AplicationCOntext=getBaseContext();
        sch=new ServerConnectionHandler(Configuration.MainActivityFragment);
        categories=new ArrayList<Category>();
        products=new ArrayList<Product>();
        articles=new ArrayList<Article>();
    }

    private void addFontAndColors() {

        tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setTextColor(getResources().getColorStateList(R.color.tab_text_color));
    }

    private void addActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
        CounterIconUtils.setBadgeCount(this, icon, filBasketColor());
        return true;
    }

    private int filBasketColor() {
        //TODO fill this by basket items
        return 5;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                Intent userProfileIntent=new Intent(this,UserProfileActivity.class);
                this.startActivity(userProfileIntent);
               break;
            case R.id.action_search:
            {
                backButton = (ImageButton)findViewById(R.id.back_button);
                toolbarSearch = (LinearLayout)findViewById(R.id.toolbar_search);
                toolbar.setVisibility(View.GONE);
                toolbarSearch.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbarSearch.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);

                    }
                });

                break;
            }

            case R.id.action_notifications :
                Intent shoppingBagIntent=new Intent(this, ShoppingBagActivity.class);
                this.startActivity(shoppingBagIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeColor(int newColor) {
        tabs.setBackgroundColor(newColor);
        mTintManager.setTintColor(newColor);
        // change ActionBar color just if an ActionBar is available
        Drawable colorDrawable = new ColorDrawable(newColor);
        Drawable bottomDrawable = new ColorDrawable(getResources().getColor(android.R.color.transparent));
        LayerDrawable ld = new LayerDrawable(new Drawable[]{colorDrawable, bottomDrawable});
        if (oldBackground == null) {
            getSupportActionBar().setBackgroundDrawable(ld);
        } else {
            TransitionDrawable td = new TransitionDrawable(new Drawable[]{oldBackground, ld});
            getSupportActionBar().setBackgroundDrawable(td);
            td.startTransition(200);
        }
        oldBackground = ld;
        currentColor = newColor;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");
        changeColor(currentColor);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {


        private final String[] TITLES = {
                getResources().getString(R.string.home)
                , getResources().getString(R.string.photo)
                , getResources().getString(R.string.sell)
                , getResources().getString(R.string.papers)
                , getResources().getString(R.string.comment)


        };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return SuperAwesomeCardFragment.newInstance(position);
        }
    }
}