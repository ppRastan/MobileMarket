package ir.rastanco.mobilemarket.presenter;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.ronash.pushe.Pushe;
import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProductFirstInstallApp;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ArticlePresenter.ArticleFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverChangeFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOK;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOKListener;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShoppingBagClickListener;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.AccountManager;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.LoginPage;
import ir.rastanco.mobilemarket.presenter.shopPresenter.ShopFragment;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.presenter.specialProductPresenter.SpecialProductFragmentManagement;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.CounterIconDisplayer;
import ir.rastanco.mobilemarket.utility.LinkHandler;

/*created by parisan*/

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int exitSafeCounter = 0;
    private AutoCompleteTextView textToSearch;
    private ImageButton backButton;
    private ServerConnectionHandler sch;
    private ArrayList<String> mainCategoryTitle;
    private Map<String,Integer> mapTitleToIdMainCategory;
    private LinearLayout toolbarSearch;
    private PhoneCallListener phoneListener;
    private TelephonyManager telephonyManager;
    private FloatingActionButton fab;
    private Display display;
    private Point size;
    private int shopCounter;
    private Menu menu;
    private String version;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.action_bar_title));
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        Pushe.initialize(this, true);//Pushe Alert For Install Google Play
        this.addActionBar();
        this.setFAb();
        this.phoneManager();
        this.InitializationParametersNecessary();
        this.CreatePageRightToLeft();
        this.displayWindow();
        ObserverShopping.addMyBooleanListener(new ObserverShoppingBagClickListener() {
            @Override
            public void OnMyBooleanChanged() {
                MenuItem item = menu.findItem(R.id.action_notifications);
                LayerDrawable icon = (LayerDrawable) item.getIcon();
                CounterIconDisplayer.setBadgeCount(Configuration.MainActivityContext,
                        icon, sch.getCountProductShop());
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Configuration.getConfig().MainPager=viewPager;
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.BLACK, Color.RED);
        this.changeTabsFont();

        //DataBase empty in first install Application
        if (Configuration.getConfig().productTableEmptyStatus)
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        if (Configuration.getConfig().productTableEmptyStatus &&
                Configuration.getConfig().connectionStatus) {
            getInformationFromServerInFirstRun(Configuration.getConfig().MainActivityContext);
        }
        ObserverConnectionInternetOK.ObserverConnectionInternetOKListener(new ObserverConnectionInternetOKListener() {
            @Override
            public void connectionOK() {
                if (Configuration.getConfig().productTableEmptyStatus &&
                        Configuration.getConfig().connectionStatus) {
                    getInformationFromServerInFirstRun(Configuration.getConfig().MainActivityContext);
                }
            }
        });

    }
    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {

            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf"));
                }
            }
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ArticleFragment(),getResources().getString(R.string.fifth_page));
        for (int i=mainCategoryTitle.size()-1;i>=0;i--) {
            Bundle args=new Bundle();
            args.putInt("pageId", mapTitleToIdMainCategory.get(mainCategoryTitle.get(i)));
            ShopFragment shop=new ShopFragment();
            shop.setArguments(args);
            adapter.addFrag(shop, mainCategoryTitle.get(i));
        }
        adapter.addFrag(new SpecialProductFragmentManagement(),getResources().getString(R.string.first_page));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount() - 1);
    }

    private void addActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setFAb(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        Configuration.getConfig().telephoneFloatingActionButton=fab;
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_color)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(LinkHandler.getInstance().telephoneNumber()));
                if (ActivityCompat.checkSelfPermission(Configuration.getConfig().MainActivityContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (ActivityCompat.checkSelfPermission(Configuration.getConfig().MainActivityContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    private void InitializationParametersNecessary() {
        Configuration.getConfig().MainActivityContext = this;
        Configuration.getConfig().AplicationContext =getBaseContext();
        sch=new ServerConnectionHandler(Configuration.getConfig().MainActivityContext);
        Configuration.getConfig().IsTheFirstTimeGoingToThisPage = true;
        mainCategoryTitle= new ArrayList<String>();
        mapTitleToIdMainCategory=new HashMap<String,Integer>();
        mainCategoryTitle=sch.getMainCategoryTitle();
        mapTitleToIdMainCategory=sch.MapTitleToIDForMainCategory();
        Configuration.getConfig().MainTabCount=mainCategoryTitle.size();
        shopCounter=sch.getCountProductShop();
    }

    private void displayWindow() {
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            Configuration.getConfig().homeDisplaySizeForShow=size.x;
            Configuration.getConfig().homeDisplaySizeForURL = String.valueOf(size.x);

            Configuration.getConfig().productInfoHeightForShow=size.x - 100;
            Configuration.getConfig().productInfoHeightForURL = String.valueOf(size.x - 100);

            Double s= ((size.x) * 0.5)-12;
            Configuration.getConfig().shopDisplaySizeForShow=s.intValue();
            Configuration.getConfig().shopDisplaySizeForURL = String.valueOf(((size.x) * 0.5) - 12);

            Double a= (size.x) * 0.3;
            Configuration.getConfig().articleDisplaySizeForShow=a.intValue();
            Configuration.getConfig().articleDisplaySizeForURL =String.valueOf((size.x) * 0.3);

            Double p=(size.x)* 0.125;
            Configuration.getConfig().progressBarSize=p.intValue();

        }
    }

    private void CreatePageRightToLeft() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            Configuration.getConfig().RTL=true;
        }
        else
            Configuration.getConfig().RTL=false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
        CounterIconDisplayer.setBadgeCount(this, icon, filBasketColor());

        MenuItem upgradeItem=menu.findItem(R.id.update);
        Configuration.getConfig().UpgradeButtonMenu=upgradeItem;
        if(!sch.checkNewVersion(LinkHandler.getInstance().generateURLForGetLastVersionAppInServer())||
                Configuration.getConfig().productTableEmptyStatus ||
                !Configuration.getConfig().connectionStatus)
            upgradeItem.setVisible(false);
        else
            upgradeItem.setVisible(true);

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
                if (Configuration.getConfig().userLoginStatus){

                    Intent userProfileIntent=new Intent(Configuration.getConfig().MainActivityContext,AccountManager.class);
                    this.startActivity(userProfileIntent);
                }
                else {
                    Intent userProfileIntent=new Intent(Configuration.getConfig().MainActivityContext,LoginPage.class);
                    this.startActivity(userProfileIntent);
                }
                break;

            case R.id.action_search:
            {
                final Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
                backButton = (ImageButton)findViewById(R.id.back_button);
                textToSearch=(AutoCompleteTextView) findViewById(R.id.text_for_search);
                toolbarSearch = (LinearLayout)findViewById(R.id.toolbar_search);
                toolbar.setVisibility(View.GONE);
                toolbarSearch.setVisibility(View.VISIBLE);
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
                        R.layout.customized_list_for_search, sch.searchInProductTitle());
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
                        Intent intent = new Intent(Configuration.getConfig().MainActivityContext, ProductInfoActivity.class);
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
                version=sch.getLastVersionInServer(LinkHandler.getInstance().generateURLForGetLastVersionAppInServer());
                new DownloadFileFromURL(this).execute(LinkHandler.getInstance().generateYRLForGetApplicationInServer());
                break;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage(Configuration.getConfig().MainActivityContext.getString(R.string.downloadFile));
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
                        + LinkHandler.getInstance().generatePathAPKApplicationInMobile());

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
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + LinkHandler.getInstance().generatePathAPKApplicationInMobile())), "application/vnd.android.package-archive");
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


    private void checkDbState() {
        ArrayList<Category> categories = new ArrayList<Category>();
        if (sch.emptyDBCategory()) {
            categories = sch.getAllCategoryInfoURL(LinkHandler.getInstance().generateURLForGetAllCategories());
            sch.addAllCategoryToTable(categories);
        } else
            sch.refreshCategories(LinkHandler.getInstance().generateURLForGetAllCategories());

    }

    private void getInformationFromServerInFirstRun(Context context){
        final ParseJsonProductFirstInstallApp parseInformationProduct=new ParseJsonProductFirstInstallApp(context);
        final String[] jsonString = {""};
        Thread getProductInfoFromServerThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                        checkDbState();
                        jsonString[0] = parseInformationProduct.getProductInfoFromServer(LinkHandler.getInstance().generateUrlForGetNewProduct(Configuration.getConfig().MainActivityContext.getString(R.string.firstTimeStamp)));
                        wait(10);
                    }
                } catch (InterruptedException ex) {
                }
                String timeStamp = parseInformationProduct.addProductToTable(jsonString[0]);
                String lastVersionInServer=sch.getLastVersionInServer(LinkHandler.getInstance().generateURLForGetLastVersionAppInServer());
                sch.setSetting(timeStamp,
                        Configuration.getConfig().MainActivityContext.getResources().getString(R.string.firstArticleNumber),
                        lastVersionInServer,
                        timeStamp);
                Configuration.getConfig().productTableEmptyStatus = false;
                ObserverChangeFragment.setChangeFragmentParameter(true);
            }
        };
        getProductInfoFromServerThread.start();
    }

}
