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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.ronash.pushe.Pushe;
import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ArticlePresenter.ArticleFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverChangeFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOK;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOKListener;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShoppingBagClickListener;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.AccountManager;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.LoginHandler;
import ir.rastanco.mobilemarket.presenter.shopPresenter.ShopFragment;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.presenter.specialProductPresenter.SpecialProductFragmentManagement;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.CounterIconDisplayer;

public class MainActivity extends AppCompatActivity {
//parisa
    private TabLayout tabLayout;
    private ViewPager viewPager;
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
    private Map<String,Integer> mapTitleToIdMainCategory;
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
    private Typeface yekanFont;

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
        this.addActionBar();
        //Pushe Alert For Install Google Play
        Pushe.initialize(this, true);
        this.setFAb();
        this.phoneManager();
        Configuration.IstheFirtTimeGoingToThisPage = true;
        this.addServerConnection();
        shoppingBagActivity = new ShoppingBagActivity();
        mainCategoryTitle= new ArrayList<String>();
        mapTitleToIdMainCategory=new HashMap<String,Integer>();
        mainCategoryTitle=sch.getMainCategoryTitle();
        mapTitleToIdMainCategory=sch.MapTitleToIDForMainCategory();
        Configuration.MainTabCount=mainCategoryTitle.size();
        /*if(Configuration.MainTabCount==0){
            second_page=getString(R.string.second_page);
            third_page=getString(R.string.third_page);
            fourth_page=getString(R.string.fourth_page);
        }
        else {
            second_page=mainCategoryTitle.get(0);
            third_page=mainCategoryTitle.get(1);
            fourth_page=mainCategoryTitle.get(2);
        }*/
        this.CreatePageRightToLeft();
        this.displayWindow();
        shopCounter=sch.getCountProductShop();
        ObserverShopping.addMyBooleanListener(new ObserverShoppingBagClickListener() {
            @Override
            public void OnMyBooleanChanged() {
                MenuItem item = menu.findItem(R.id.action_notifications);
                LayerDrawable icon = (LayerDrawable) item.getIcon();
                CounterIconDisplayer.setBadgeCount(Configuration.MainActivityContext,
                        icon, sch.getCountProductShop());
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Configuration.MainPager=viewPager;
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.BLACK, Color.RED);
        yekanFont= Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf");
        this.changeTabsFont();

        //DataBase empty
        if (Configuration.productTableEmptyStatus)
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        if (Configuration.productTableEmptyStatus && Configuration.connectionStatus) {
            final String[] jsonString = {""};
            Thread getProductInfoFromServerThread = new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            // Wait given period of time or exit on touch
                            checkDbState();
                            jsonString[0] = getProductInfoFromServer("http://decoriss.com/json/get,com=product&newfromts=1352689345&cache=false");
                            wait(10);
                        }
                    } catch (InterruptedException ex) {
                    }
                    String timeStamp = addProductToTable(jsonString[0]);
                    String lastVersionInServer=sch.getLastVersionInServer("http://decoriss.com/app/Version.txt");
                    sch.setSetting(timeStamp,
                            Configuration.MainActivityContext.getResources().getString(R.string.firstArticleNumber),
                            lastVersionInServer,
                            timeStamp);
                    Configuration.productTableEmptyStatus = false;
                    ObserverChangeFragment.setChangeFragmentParameter(true);
                }
            };
            getProductInfoFromServerThread.start();
        }

        ObserverConnectionInternetOK.ObserverConnectionInternetOKListener(new ObserverConnectionInternetOKListener() {
            @Override
            public void connectionOK() {
                if (Configuration.productTableEmptyStatus && Configuration.connectionStatus) {
                    final String[] jsonString = {""};
                    Thread getProductInfoFromServerThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                synchronized (this) {
                                    // Wait given period of time or exit on touch
                                    checkDbState();
                                    jsonString[0] = getProductInfoFromServer("http://decoriss.com/json/get,com=product&newfromts=1352689345&cache=false");
                                    wait(10);
                                }
                            } catch (InterruptedException ex) {
                            }
                            String timeStamp = addProductToTable(jsonString[0]);
                            String lastVersionInServer=sch.getLastVersionInServer("http://decoriss.com/app/Version.txt");
                            sch.setSetting(timeStamp,
                                    Configuration.MainActivityContext.getResources().getString(R.string.firstArticleNumber),
                                    lastVersionInServer,
                                    timeStamp);
                            Configuration.productTableEmptyStatus = false;
                            ObserverChangeFragment.setChangeFragmentParameter(true);
                        }
                    };
                    getProductInfoFromServerThread.start();

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

    private void displayWindow() {
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            Configuration.homeDisplaySizeForShow=size.x;
            Configuration.homeDisplaySizeForURL = String.valueOf(size.x);

            Configuration.productInfoHeightForShow=size.x - 100;
            Configuration.productInfoHeightForURL = String.valueOf(size.x - 100);

            Double s= ((size.x) * 0.5)-12;
            Configuration.shopDisplaySizeForShow=s.intValue();
            Configuration.shopDisplaySizeForURL = String.valueOf(((size.x) * 0.5) - 12);

            Double a= (size.x) * 0.3;
            Configuration.articleDisplaySizeForShow=a.intValue();
            Configuration.articleDisplaySizeForURL =String.valueOf((size.x) * 0.3);

            Double p=(size.x)* 0.125;
            Configuration.progressBarSize=p.intValue();

        }
        /*ButterKnife.inject(this);
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
        this.addFontAndColors();*/
    }

    private void CreatePageRightToLeft() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            Configuration.RTL=true;
        }
        else
            Configuration.RTL=false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
        CounterIconDisplayer.setBadgeCount(this, icon, filBasketColor());

        MenuItem upgradeItem=menu.findItem(R.id.update);
        Configuration.UpgradeButtonMenu=upgradeItem;
        if(!sch.checkNewVersion("http://decoriss.com/app/Version.txt")||
                Configuration.productTableEmptyStatus ||
                !Configuration.connectionStatus)
            upgradeItem.setVisible(false);
        else
            upgradeItem.setVisible(true);

        return true;

        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;*/
    }
    private int filBasketColor()
    {
        return  shopCounter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/

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


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int tabNumber) {
            return mFragmentList.get(tabNumber);
            /*if (tabNumber==0){
                ArticleFragment article=new ArticleFragment();
                return article;
            }
            else if(tabNumber==Configuration.MainTabCount+1){
                SpecialProductFragmentManagement specialProductFragmentManagement=new SpecialProductFragmentManagement();
                return specialProductFragmentManagement;
            }
            else {
                return  ShopFragmentManager.newInstance(tabNumber,getPageTitle(tabNumber).toString());
                /*Bundle shopArgs = new Bundle();
                shopArgs.putString("name", getPageTitle(tabNumber).toString());
                ShopFragmentManager shopTab=new ShopFragmentManager();
                shopTab.setArguments(shopArgs);
                return shopTab;
            }*/
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

    public String getProductInfoFromServer(String urlProduct) {

        HttpURLConnection connection = null;
        InputStream is = null;
        String jsonString = "";
        try {
            URL url = new URL(urlProduct);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        Log.v("connect", "Connecte to Internet");
        int response = 0;
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            response = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("RequestGet", "Request Method Get");
        Log.v("Response", Integer.toString(response));
        try {
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsonString = sb.toString();

        } catch (Exception e) {
            Log.e("TAG", "Error converting result " + e.toString());
        }
        return jsonString;
    }

    public String addProductToTable(String jsonString) {

        ArrayList<Product> allProduct;
        JSONArray dataJsonArr;
        ArrayList<String> imagePath;

        dataJsonArr = null;
        allProduct = new ArrayList<Product>();

        try {

            JSONObject json = new JSONObject(jsonString);
            dataJsonArr = json.getJSONArray("product");
            for (int i = 0; i < dataJsonArr.length(); i++) {
                JSONObject c = dataJsonArr.getJSONObject(i);
                imagePath = new ArrayList<String>();
                Product aProduct = new Product();
                aProduct.setTitle(c.getString("t"));
                aProduct.setId(Integer.parseInt(c.getString("id")));
                aProduct.setGroupId(Integer.parseInt(c.getString("gid")));
                aProduct.setPrice(Integer.parseInt(c.getString("p")));
                aProduct.setPriceOff(Integer.parseInt(c.getString("po")));
                //pp
                aProduct.setVisits(Integer.parseInt(c.getString("v")));
                aProduct.setMinCounts(Integer.parseInt(c.getString("mc")));
                aProduct.setBrandName(c.getString("brndname"));
                aProduct.setStock(Integer.parseInt(c.getString("stock")));
                aProduct.setQualityRank(c.getString("qr"));
                aProduct.setCommentsCount(Integer.parseInt(c.getString("cc")));
                aProduct.setCodeProduct(c.getString("n"));
                aProduct.setDescription(c.getString("d"));
                aProduct.setSellsCount(Integer.parseInt(c.getString("s")));
                aProduct.setTimeStamp(c.getString("ts"));
                aProduct.setUpdateTimeStamp(c.getString("update_ts"));

                aProduct.setShowAtHomeScreen(Integer.parseInt(c.getString("h")));
                aProduct.setWatermarkPath(c.getString("wm"));
                aProduct.setImagesMainPath(c.getString("ipath"));
                aProduct.setLinkInSite(c.getString("l"));
                for (int j = 0; j < 10; j++) {
                    String counter = "i" + j;
                    if (c.has(counter))
                        imagePath.add(c.getString(counter));
                }
                aProduct.setImagesPath(imagePath);
                allProduct.add(aProduct);
            }
            sch.addAllProductToTable(allProduct);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allProduct.get(0).getTimeStamp();
    }

    private void checkDbState() {

        ArrayList<Category> categories = new ArrayList<Category>();
        if (sch.emptyDBCategory()) {
            categories = sch.getAllCategoryInfoURL("http://decoriss.com/json/get,com=allcats&cache=false");
            sch.addAllCategoryToTable(categories);
        } else
            sch.refreshCategories("http://decoriss.com/json/get,com=allcats&cache=false");

    }

}
