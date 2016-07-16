package ir.rastanco.mobilemarket.presenter;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

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
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ArticlePresenter.ArticleFragment;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOK;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOKListener;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShopping;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverShoppingBagClickListener;
import ir.rastanco.mobilemarket.presenter.Services.DownloadCategoryInformationService;
import ir.rastanco.mobilemarket.presenter.Services.DownloadResultReceiver;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.AccountManagerActivity;
import ir.rastanco.mobilemarket.presenter.UserProfilePresenter.LoginActivity;
import ir.rastanco.mobilemarket.presenter.shopPresenter.ShopFragment;
import ir.rastanco.mobilemarket.presenter.shoppingBagPresenter.ShoppingBagActivity;
import ir.rastanco.mobilemarket.presenter.specialProductPresenter.SpecialProductFragment;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;

/*created by parisa*/
public class TabbedActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, DownloadResultReceiver.Receiver {
    private TabLayout tabLayout;
    private ServerConnectionHandler sch;
    private ArrayList<String> mainCategoryTitle;
    private Map<String, Integer> mapTitleToIdMainCategory;
    private LinearLayout toolbarSearch;
    private int shopCounter;
    private String version;
    private ProgressDialog pDialog;
    private static final int progress_bar_type = 0;
    private DownloadResultReceiver mReceiver;
    private CoordinatorLayout coordinatorLayout;
    private TextView text_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbed_activity);
        //this.setStatusBarColor();
        isStoragePermissionGranted();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        text_count = (TextView) findViewById(R.id.txt_count);
        Pushe.initialize(this, true);//pushe Alert For Install Google Play
        this.InitializationParametersNecessary();
        this.setActionBar();
        //this.setFAb();
        this.phoneManager();
        this.CreatePageRightToLeft();
        this.displayWindow();
        ObserverShopping.addMyBooleanListener(new ObserverShoppingBagClickListener() {
            @Override
            public void OnMyBooleanChanged() {
                int shopCounter = sch.getCountProductShop();
                if (shopCounter == 0)
                    text_count.setVisibility(View.GONE);
                else {
                    text_count.setVisibility(View.VISIBLE);
                    text_count.setText(String.valueOf(shopCounter));

                }
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        Configuration.getConfig().mainPager = viewPager;
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //// TODO: 05/07/2016  set tab colors here
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        //// TODO: 06/07/2016  set yekan font here
        this.changeTabsFont();

        //DataBase empty in first install Application
       if (Configuration.getConfig().emptyCategoryTable) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            mReceiver = new DownloadResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadCategoryInformationService.class);
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("requestId", Configuration.getConfig().requestIdForGetCategoryInformationInFirstInstall);
            startService(intent);
            /*Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getResources().getString(R.string.loading), Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            PriceUtility.getInstance().changeTextViewFont(textView, this);
            snackbar.show();*/
        }

        //check get all product.if don't get all product start service get .
        /*mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        int firstIndexGetProduct = sch.getFirstIndexForGetProductFromJson();
        int allNumberProducts = sch.getNumberAllProduct();
        if (firstIndexGetProduct < allNumberProducts) {
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, CompleteDataAfterInstall.class);
            /* Send optional extras to Download IntentService
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("requestId", 101);
            startService(intent);

        }*/

        /*ObserverConnectionInternetOK.ObserverConnectionInternetOKListener(new ObserverConnectionInternetOKListener() {
            @Override
            public void connectionOK() {
                if (Configuration.getConfig().emptyProductTable &&
                        Configuration.getConfig().connectionStatus) {
                    //getInformationFromServerInFirstRun(Configuration.getConfig().mainActivityContext);
                    ObserverChangeFragment.setChangeFragmentParameter(true);

                }
                int firstIndexGetProduct = sch.getFirstIndexForGetProductFromJson();
                int allNumberProducts = sch.getNumberAllProduct();
                if (firstIndexGetProduct < allNumberProducts) {
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, Configuration.getConfig().mainActivityContext, CompleteDataAfterInstall.class);
                    intent.putExtra("receiver", mReceiver);
                    intent.putExtra("requestId", 101);
                    startService(intent);
                }
            }
        });*/
        ObserverConnectionInternetOK.ObserverConnectionInternetOKListener(new ObserverConnectionInternetOKListener() {
            @Override
            public void connectionOK() {
                if (sch.emptyDBCategory()){
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }

            }
        });

        /*ObserverUpdateCategories.updateCategoriesListener(new ObserverUpdateCategoriesListener() {
            @Override
            public void updateCategories() {
                updateViewPager(Configuration.getConfig().mainPager);
            }
        });*/
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

//    public void setStatusBarColor() {
//        Window window = this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(this.getResources().getColor(R.color.background_window));
//        }
//    }

    private void setActionBar() {

        ImageButton userLogin = (ImageButton) findViewById(R.id.actionbar_userLogin);
        ImageButton shoppingCounter = (ImageButton) findViewById(R.id.actionbar_shoppingBag);
        // ImageButton updateIcon = (ImageButton) findViewById(R.id.actionbarUpdate);
        //ImageButton searchIcon = (ImageButton) findViewById(R.id.actionbar_search);

        if (sch.checkEmptyProductShop()) {
            text_count.setVisibility(View.GONE);
        } else {
            text_count.setText(String.valueOf(sch.getCountProductShop()));
        }
//        Configuration.getConfig().upgradeButtonMenu = updateIcon;
//        if (!sch.checkNewVersion(Link.getInstance().generateURLForGetLastVersionAppInServer()) ||
//                !Configuration.getConfig().connectionStatus)
//            updateIcon.setVisibility(View.INVISIBLE);
//        else
//            updateIcon.setVisibility(View.VISIBLE);

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Configuration.getConfig().userLoginStatus) {

                    Intent userProfileIntent = new Intent(Configuration.getConfig().mainActivityContext, AccountManagerActivity.class);
                    startActivity(userProfileIntent);
                } else {
                    //TODO customize login page is loginPageActivity
                    Intent userProfileIntent = new Intent(Configuration.getConfig().mainActivityContext, LoginActivity.class);
                    startActivity(userProfileIntent);
                }
            }
        });

        shoppingCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shoppingBagIntent = new Intent(TabbedActivity.this, ShoppingBagActivity.class);
                startActivity(shoppingBagIntent);

            }
        });
//        updateIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                version = sch.getLastVersionInServer(Link.getInstance().generateURLForGetLastVersionAppInServer());
//                new DownloadFileFromURL(TabbedActivity.this).execute(Link.getInstance().generateYRLForGetApplicationInServer());
//            }
//        });
//        searchIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//                ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
//                AutoCompleteTextView textToSearch = (AutoCompleteTextView) findViewById(R.id.text_for_search);
//                toolbarSearch = (LinearLayout) findViewById(R.id.toolbar_search);
//                toolbar.setVisibility(View.GONE);
//                toolbarSearch.setVisibility(View.VISIBLE);
//                ArrayAdapter<String> listAdapter = new ArrayAdapter<>(TabbedActivity.this,
//                        R.layout.customized_list_for_search, sch.searchInProductTitle());
//                textToSearch.setAdapter(listAdapter);
//                backButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        toolbarSearch.setVisibility(View.GONE);
//                        toolbar.setVisibility(View.VISIBLE);
//                    }
//                });
//                textToSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        int productId = sch.getProductIdWithTitle((String) parent.getItemAtPosition(position));
//                        Product aProduct = sch.getAProduct(productId);
//                        ArrayList<Product> product = new ArrayList<>();
//                        product.add(aProduct);
//                        Intent intent = new Intent(Configuration.getConfig().mainActivityContext, ProductInfoActivity.class);
//                        intent.putParcelableArrayListExtra("allProduct", product);
//                        intent.putExtra("position", 0);
//                        startActivity(intent);
//                    }
//                });

//            }
//        });
    }


    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {

            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf"));
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        //viewPager.setOffscreenPageLimit(8);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ArticleFragment(), getResources().getString(R.string.fifth_page));
        for (int i = mainCategoryTitle.size() - 1; i >= 0; i--) {
            Bundle args = new Bundle();
            args.putInt("pageId", mapTitleToIdMainCategory.get(mainCategoryTitle.get(i)));
            ShopFragment shop = new ShopFragment();
            shop.setArguments(args);
            adapter.addFragment(shop, mainCategoryTitle.get(i));
        }
        adapter.addFragment(new SpecialProductFragment(), getResources().getString(R.string.first_page));
        //ToDO Parisa For show Home Tab
        //adapter.addFragment(new HomeFragment(), getResources().getString(R.string.home));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount() - 1);
    }

    private void updateViewPager(ViewPager viewPager) {
        try{
            mainCategoryTitle = sch.getMainCategoryTitle();
            mapTitleToIdMainCategory = sch.MapTitleToIDForMainCategory();
            Configuration.getConfig().mainTabCount = mainCategoryTitle.size();
            ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
            deleteAllFragmentPage(adapter);
            adapter.clearAllTab();
            adapter.addFragment(new ArticleFragment(), getResources().getString(R.string.fifth_page));
            for (int i = mainCategoryTitle.size() - 1; i >= 0; i--) {
                Bundle args = new Bundle();
                args.putInt("pageId", mapTitleToIdMainCategory.get(mainCategoryTitle.get(i)));
                ShopFragment shop = new ShopFragment();
                shop.setArguments(args);
                adapter.addFragment(shop, mainCategoryTitle.get(i));
            }
            adapter.addFragment(new SpecialProductFragment(), getResources().getString(R.string.first_page));
            //ToDO Parisa For Show Home Tab
            //adapter.addFragment(new HomeFragment(), getResources().getString(R.string.home));
            viewPager.setAdapter(adapter);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            viewPager.setCurrentItem(adapter.getCount() - 1);
            changeTabsFont();

        }catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
            Log.d("force close",ignored.toString());
        }

    }

    private void deleteAllFragmentPage(ViewPagerAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++)
            getSupportFragmentManager().beginTransaction().remove(adapter.getItem(i)).commit();
    }


//    private void setFAb() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        Configuration.getConfig().customerSupportFloatingActionButton = fab;
//        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.decoriss)));
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                callIntent.setData(Uri.parse(Link.getInstance().telephoneNumber()));
//                startActivity(callIntent);
//
//            }
//        });
//    }

    private void phoneManager() {
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private void InitializationParametersNecessary() {
        Configuration.getConfig().mainActivityContext = this;
        Configuration.getConfig().applicationContext = getBaseContext();
        sch = ServerConnectionHandler.getInstance(Configuration.getConfig().mainActivityContext);
        mainCategoryTitle = new ArrayList<>();
        mapTitleToIdMainCategory = new HashMap<>();
        mainCategoryTitle = sch.getMainCategoryTitle();
        mapTitleToIdMainCategory = sch.MapTitleToIDForMainCategory();
        Configuration.getConfig().mainTabCount = mainCategoryTitle.size();
        shopCounter = sch.getCountProductShop();
        Configuration.getConfig().mainActivityView = findViewById(R.id.main);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            Configuration.getConfig().connectionStatus = true;
        }
        if (sch.emptyUserInfo())
            Configuration.getConfig().userLoginStatus = false; //please login
        else Configuration.getConfig().userLoginStatus = true;//

        if (sch.emptyDBCategory()) {
            Configuration.getConfig().emptyCategoryTable = true;
        } else
            Configuration.getConfig().emptyCategoryTable = false;
        if (sch.emptyDBProduct()) {
            Configuration.getConfig().emptyProductTable = true;
        } else {
            Configuration.getConfig().emptyProductTable = false;
        }

    }

    private void displayWindow() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            Configuration.getConfig().homeDisplaySizeForShow = size.x;
            Configuration.getConfig().homeDisplaySizeForURL = String.valueOf(size.x);

            Configuration.getConfig().productInfoHeightForShow = size.x - 100;
            Configuration.getConfig().productInfoHeightForURL = String.valueOf(size.x - 100);

            Double s = ((size.x) * 0.5) - 12;
            Configuration.getConfig().shopDisplaySizeForShow = s.intValue();
            Configuration.getConfig().shopDisplaySizeForURL = String.valueOf(((size.x) * 0.5) - 12);

            Double a = (size.x) * 0.3;
            Configuration.getConfig().articleDisplaySizeForShow = a.intValue();
            Configuration.getConfig().articleDisplaySizeForURL = String.valueOf((size.x) * 0.3);

            Double p = (size.x) * 0.125;
            Configuration.getConfig().progressBarSize = p.intValue();

        }
    }

    private void CreatePageRightToLeft() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            Configuration.getConfig().RTL = true;
        else
            Configuration.getConfig().RTL = false;
    }


//        LayerDrawable icon = (LayerDrawable) slider_items.getIcon();
//        CounterIconCreator.setBadgeCount(icon, filBasketColor());
//
//        MenuItem upgradeItem = menu.findItem(R.id.update);

//        else
//            upgradeItem.setVisible(true);
//
//        return true;
//    }
//
//    private int filBasketColor() {
//        return shopCounter;
//    }


    @Override
    public void onBackPressed() {
        //Clear Cache
        ImageLoader imageLoader = new ImageLoader(Configuration.getConfig().mainActivityContext);
        imageLoader.clearCache();
//            exitSafeCounter++;
//            if (exitSafeCounter == 1) {
//                //// TODO: 01/07/2016
//                 ToastUtility.getConfig().displayToast(getResources().getString(R.string.sure_to_exit, this),this);
//            } else if (exitSafeCounter > 1) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//                System.exit(0);
        //new AlertDialogWrapper.Builder(this)
//                .setTitle(R.string.title)
        //      .setMessage(R.string.content)
//                .setNegativeButton(R.string.agree, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        }
//                )
//                .setPositiveButton(R.string.disagree, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }
//                )
//
//                .setNeutralButton(R.string.rate_us, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Intent rateIntent = new Intent(Intent.ACTION_VIEW);
//                                rateIntent.setData(Uri.parse("market://details?id="+TabbedActivity.this.getPackageName()));
//                                startActivity(rateIntent);}
//                        }
//                )
//
//                .show();

//        new AlertDialogWrapper.Builder(this)
//                .setMessage(R.string.sure_to_exit)
//                .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        }
//                )
//                .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }
//                )
//
//                .setNeutralButton(R.string.rate_us, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Intent rateIntent = new Intent(Intent.ACTION_VIEW);
//                                rateIntent.setData(Uri.parse("market://details?id=" + TabbedActivity.this.getPackageName()));
//                                startActivity(rateIntent);
//                            }
//                        }
//                )
//
//                .show();
                 finish();
        /*new AlertDialogWrapper.Builder(this)
                .setMessage(R.string.sure_to_exit)
                .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }
                )
                .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                )

                .setNeutralButton(R.string.rate_us, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                                rateIntent.setData(Uri.parse("market://details?id=" + TabbedActivity.this.getPackageName()));
                                startActivity(rateIntent);
                            }
                        }
                )

                .show();*/
        finish();

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {

            case DownloadCategoryInformationService.STATUS_FINISHED:
                updateViewPager(Configuration.getConfig().mainPager);
                break;

        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage(Configuration.getConfig().mainActivityContext.getString(R.string.downloadFile));
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private final Context context;

        public DownloadFileFromURL(Context mayContext) {
            context = mayContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int fileSize = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + Link.getInstance().generatePathAPKApplicationInMobile());

                byte data[] = new byte[2048];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / fileSize));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + Link.getInstance().generatePathAPKApplicationInMobile())), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                //sch.updateVersionApp(version);

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
         //   pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //   dismissDialog(progress_bar_type);
        }

    }
}
