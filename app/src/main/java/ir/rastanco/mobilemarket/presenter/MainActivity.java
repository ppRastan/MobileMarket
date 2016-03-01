package ir.rastanco.mobilemarket.presenter;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.ronash.pushe.Pushe;
import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ArticlePresenter.ArticleFragment;
import ir.rastanco.mobilemarket.presenter.FirstTabPresenter.FirstTabFragmentManager;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShoppingBagClickListener;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.presenter.SecoundTabPresenter.SecondTabFragmentManager;
import ir.rastanco.mobilemarket.presenter.ThirdtabPresenter.ThirdTabFragmentManager;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.AccountManager;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.LoginHandler;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.presenter.specialProductPresenter.SpecialProductFragmentManagement;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.CounterIconUtils;

//parisa rashidi nezhad connection
//parisa recommit
public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @InjectView(R.id.pager)
    ViewPager pager;
    private MyPagerAdapter adapter;
    private int exitSafeCounter = 0;
    private Drawable oldBackground = null;
    private int currentColor;
    private SystemBarTintManager mTintManager;
    private AutoCompleteTextView textToSearch;
    private ImageButton backButton;
    private ServerConnectionHandler sch;
    private ArrayList<Product> products;
    private ArrayList<Article> articles;
    private ArrayList<Category> categories;
    private ArrayList<String> mainCategoryTitle;
    private String second_page;
    private String third_page;
    private String fourth_page;
    private LinearLayout toolbarSearch;
    private PhoneCallListener phoneListener;
    private TelephonyManager telephonyManager;
    private FloatingActionButton fab;
    private Display display;
    private Point size;
    private int shopCounter;
    private ShoppingBagActivity shoppingBagActivity;
    private Menu menu;
    private String version;

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addActionBar();
        //Pushe Alert For Install Google Play
        Pushe.initialize(this, true);
        this.setFAb();
        this.phoneManager();
        Configuration.IstheFirtTimeGoingToThisPage = true;
        this.addServerConnection();
        shoppingBagActivity = new ShoppingBagActivity();
        mainCategoryTitle= new ArrayList<String>();
        mainCategoryTitle=sch.getMainCategoryTitle();
        if(mainCategoryTitle.size()==0){
            second_page=getString(R.string.second_page);
            third_page=getString(R.string.third_page);
            fourth_page=getString(R.string.fourth_page);
        }
        else {
            second_page=mainCategoryTitle.get(0);
            third_page=mainCategoryTitle.get(1);
            fourth_page=mainCategoryTitle.get(2);
        }
        this.CreatePageRightToLeft();
        this.displayWindow();

        shopCounter=sch.getCountProductShop();
        ObserverShopping.addMyBooleanListener(new ObserverShoppingBagClickListener() {
            @Override
            public void OnMyBooleanChanged() {
                MenuItem item = menu.findItem(R.id.action_notifications);
                LayerDrawable icon = (LayerDrawable) item.getIcon();
                CounterIconUtils.setBadgeCount(Configuration.MainActivityContext,
                        icon, sch.getCountProductShop());
            }
        });
    }

    private void displayWindow() {
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            Configuration.homeDisplaySizeForShow=size.x;
            Configuration.homeDisplaySizeForURL = String.valueOf(size.x);

            Configuration.productInfoHeightSize = String.valueOf(size.x - 100);

            Double s= ((size.x) * 0.5)-12;
            Configuration.shopDisplaySizeForShow=s.intValue();
            Configuration.shopDisplaySizeForURL = String.valueOf(((size.x) * 0.5) - 12);

            Double a= (size.x) * 0.3;
            Configuration.articleDisplaySizeForShow=a.intValue();
            Configuration.articleDisplaySizeForURL =String.valueOf((size.x) * 0.3);

            Double p=(size.x)* 0.125;
            Configuration.progressBarSize=p.intValue();

        }
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        Configuration.MainPager=pager;
        tabs.setViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 5, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(0);
        this.setDecorissThemColour();
        this.addFontAndColors();

    }

    private void setDecorissThemColour() {
        changeColor(getResources().getColor(R.color.decoriss));
    }

    private void CreatePageRightToLeft() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            Configuration.RTL=true;
        }
        else
            Configuration.RTL=false;
    }

    private void setFAb(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_color)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:02166558994"));
                if (ActivityCompat.checkSelfPermission(Configuration.MainActivityContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (ActivityCompat.checkSelfPermission(Configuration.MainActivityContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    private void addServerConnection() {
        Configuration.MainActivityContext = this;
        Configuration.AplicationContext =getBaseContext();
        sch=new ServerConnectionHandler(Configuration.MainActivityContext);
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
        this.menu=menu;
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
        CounterIconUtils.setBadgeCount(this, icon, filBasketColor());

        MenuItem upgradeItem=menu.findItem(R.id.update);
        Configuration.UpgradeButtonMenu=upgradeItem;
        if(sch.checkNewVersion("http://decoriss.com/app/Version.txt"))
            upgradeItem.setVisible(true);
        else
            upgradeItem.setVisible(false);

        return true;
    }

    private int filBasketColor()
    {
        return  shopCounter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                if (Configuration.userLoginStatus){

                    Intent userProfileIntent=new Intent(Configuration.MainActivityContext,AccountManager.class);
                    this.startActivity(userProfileIntent);
                }
                else {
                    Intent userProfileIntent=new Intent(Configuration.MainActivityContext,LoginHandler.class);
                    this.startActivity(userProfileIntent);
                }
                break;
            case R.id.action_search:
            {
                backButton = (ImageButton)findViewById(R.id.back_button);
                textToSearch=(AutoCompleteTextView) findViewById(R.id.text_for_search);
                toolbarSearch = (LinearLayout)findViewById(R.id.toolbar_search);
                toolbar.setVisibility(View.GONE);
                toolbarSearch.setVisibility(View.VISIBLE);
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, sch.searchInProductTitle());
                textToSearch.setAdapter(listAdapter);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbarSearch.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);
                    }
                });
                textToSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int productId=sch.getProductIdWithTitle((String)parent.getItemAtPosition(position));
                        Product aProduct=new Product();
                        aProduct=sch.getAProduct(productId);
                        ArrayList<Product> product=new ArrayList<Product>();
                        product.add(aProduct);
                        Intent intent = new Intent(Configuration.MainActivityContext, ProductInfoActivity.class);
                        intent.putParcelableArrayListExtra("allProduct",product);
                        intent.putExtra("position", 0);
                        startActivity(intent);
                    }
                });
                break;
            }

            case R.id.action_notifications :
                Intent shoppingBagIntent = new Intent(this, ShoppingBagActivity.class);
                this.startActivity(shoppingBagIntent);
                break;
            case R.id.update:
                version=sch.getLastVersionInServer("http://decoriss.com/app/Version.txt");
                new DownloadFileFromURL(this).execute("http://decoriss.com/app/Decoriss.apk");
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("در حال دانلود فایل . . . ");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private Context context;
        public DownloadFileFromURL(Context mayContext){
            context=mayContext;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),8192);
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/Download/Decoriss.apk");

                byte data[] = new byte[2048];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "Decoriss.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                sch.updateVersionApp(version);

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {
                getResources().getString(R.string.first_page)
                ,second_page
                ,third_page
                ,fourth_page
                , getResources().getString(R.string.fifth_page)
        };


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
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

            switch (position){
                case 0:
                    SpecialProductFragmentManagement specialProductFragmentManagement=new SpecialProductFragmentManagement();
                    return specialProductFragmentManagement;
                case 1:
                    Bundle firstArgs = new Bundle();
                    firstArgs.putString("name", TITLES[position]);
                    FirstTabFragmentManager firstTab=new FirstTabFragmentManager();
                    firstTab.setArguments(firstArgs);
                    return firstTab;
                case 2:
                    Bundle secondArgs = new Bundle();
                    secondArgs.putString("name", TITLES[position]);
                    SecondTabFragmentManager secondTab=new SecondTabFragmentManager();
                    secondTab.setArguments(secondArgs);
                    return secondTab;
                case 3:
                    Bundle thirdArgs = new Bundle();
                    thirdArgs.putString("name", TITLES[position]);
                    ThirdTabFragmentManager thirdTab=new ThirdTabFragmentManager();
                    thirdTab.setArguments(thirdArgs);
                    return thirdTab;
                case 4:
                    ArticleFragment article=new ArticleFragment();
                    return article;
                default:
                    return null;
            }
        }

    }


    @Override
    public void onBackPressed() {

        exitSafeCounter++;
        if (exitSafeCounter == 1) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.sure_to_exit),
                    Toast.LENGTH_SHORT).show();
        } else if(exitSafeCounter >1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
    }
}