package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Filter.FilterCategory;
import ir.rastanco.mobilemarket.presenter.Filter.FilterOptionProduct;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterAll;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterAllListener;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterBrand;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterBrandListener;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterCategory;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterCategoryListener;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterPrice;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverFilterPriceListener;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverLike;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverLikeListener;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverSimilarProduct;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverSimilarProductListener;
import ir.rastanco.mobilemarket.presenter.Services.DownloadProductInformationService;
import ir.rastanco.mobilemarket.presenter.Services.DownloadResultReceiver;
import ir.rastanco.mobilemarket.presenter.Services.UpdateService;
import ir.rastanco.mobilemarket.presenter.specialProductPresenter.PictureSpecialProductItemAdapter;
import ir.rastanco.mobilemarket.utility.ColorUtility;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.PriceUtility;

/**
 * Created by ShaisteS on 1394/12/09.
 */
public class ShopFragment extends Fragment implements DownloadResultReceiver.Receiver {

    private ServerConnectionHandler sch;
    //private FragmentActivity myContext;
    private TextView noThingToShow;
    private ProgressBar progressBar;
    private DownloadResultReceiver mReceiver;
    private DownloadResultReceiver resultReceiver;
    private GridView gridview;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PictureProductShopItemAdapter adapter;
    private int existProductNumber;
    private int pageIdForRefresh;
    private String txtFilterOptionForRefresh;
    private int categoryId;
    private Boolean lockScroll=false;
    private Boolean lockFirstVisitTab=true;
    private FragmentActivity activity;
    private ProgressBar progress;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            this.activity= (FragmentActivity) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_shop, container, false);
        progress = (ProgressBar)mainView.findViewById(R.id.progressBar_Loading);
        progress.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        Configuration.getConfig().shopFragmentContext = getContext();
        //myContext = (FragmentActivity) Configuration.getConfig().shopFragmentContext;
        sch = ServerConnectionHandler.getInstance(Configuration.getConfig().shopFragmentContext);
        final int pageId = getArguments().getInt("pageId");
        categoryId=pageId;
        final TextView txtFilterOptionProductSelected = (TextView) mainView.findViewById(R.id.filter_dialogue_text);
        final TextView txtFilterCategorySelected = (TextView) mainView.findViewById(R.id.group_dialog_text);
        noThingToShow = (TextView) mainView.findViewById(R.id.no_thing_to_show1);
        noThingToShow = PriceUtility.getInstance().changeTextViewFont(noThingToShow,activity);
        progressBar=(ProgressBar)mainView.findViewById(R.id.progressBar_Loading);
        gridview = (GridView) mainView.findViewById(R.id.gv_infoProduct);
        resultReceiver = new DownloadResultReceiver(new Handler());
        resultReceiver.setReceiver(this);
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);


        ArrayList<Product> products = new ArrayList<Product>();
        products=sch.getProductsOfAParentCategory(pageId);
        adapter = new PictureProductShopItemAdapter(activity, products);
        if (products.size()==0) {
            loadingMessage();
            getProductInformationFromServerWhenFirstEnterTab(categoryId,0);
        }
        else {
            loadProductMessage();
            gridview.setAdapter(adapter);
        }

        Configuration.getConfig().filterCategoryId = 0;
        Configuration.getConfig().filterCategoryTitle = getString(R.string.all);
        Configuration.getConfig().filterOption = getString(R.string.all);
        txtFilterCategorySelected.setText(getString(R.string.all));
        txtFilterOptionProductSelected.setText(getString(R.string.all));
        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(activity, R.color.black));

        //refresh grid view
        mSwipeRefreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);
        //mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setEnabled(false);
        ColorUtility.getConfig().setColorOfSwipeRefresh(mSwipeRefreshLayout);
        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean enable = false;
                if (gridview.getChildCount() > 0) {
                    boolean firstItemVisible = gridview.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = gridview.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);
//                if (enable)
  //                  Configuration.getConfig().customerSupportFloatingActionButton.setVisibility(View.VISIBLE);
    //            else
      //              Configuration.getConfig().customerSupportFloatingActionButton.setVisibility(View.GONE);

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && !lockScroll) {
                    //scroll receive button
                    lockScroll=true;
                    int minLimited=gridview.getAdapter().getCount();
                    int maxLimited=minLimited+Configuration.getConfig().someOfFewProductNumberWhenScrollIsButton;
                    if (txtFilterCategorySelected.getText().equals(getString(R.string.all)) && txtFilterOptionProductSelected.getText().equals(getString(R.string.all)))
                        getProductInformationFromServerWhenScroll(pageId,minLimited,maxLimited);
                    else if(!txtFilterCategorySelected.getText().equals(getString(R.string.all)) && txtFilterOptionProductSelected.getText().equals(getString(R.string.all)))
                        getProductInformationFromServerWhenScroll(Configuration.getConfig().filterCategoryId,minLimited,maxLimited);
                }

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*sch.refreshCategories(Link.getInstance().generateURLForGetAllCategories());
                        sch.getNewProducts();
                        sch.getEditProducts();*/
                        pageIdForRefresh = pageId;
                        txtFilterOptionForRefresh = txtFilterOptionProductSelected.getText().toString();
                        Intent intent = new Intent(Intent.ACTION_SYNC, null,activity, UpdateService.class);
                        intent.putExtra("receiver", mReceiver);
                        intent.putExtra("requestId", 101);
                        getActivity().startService(intent);
                    }
                }, 5000);
            }
        });

        ObserverSimilarProduct.SimilarProductListener(new ObserverSimilarProductListener() {
            @Override
            public void SimilarProductSet() {
                Configuration.getConfig().filterCategoryId = ObserverSimilarProduct.getSimilarProduct();
                Configuration.getConfig().filterPriceTitle = sch.getACategoryTitleWithCategoryId(Configuration.getConfig().filterCategoryId);
                Configuration.getConfig().filterBrand = Configuration.getConfig().shopFragmentContext.getResources().getString(R.string.all);
                txtFilterCategorySelected.setText(Configuration.getConfig().filterPriceTitle);
                txtFilterCategorySelected.setTextColor(ContextCompat.getColor(activity, R.color.colorFilterText));
                txtFilterOptionProductSelected.setText(Configuration.getConfig().shopFragmentContext.getResources().getString(R.string.all));
                txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(activity, R.color.black));
                ArrayList<Product> newProducts = sch.getProductsOfAParentCategory(ObserverSimilarProduct.getSimilarProduct());
                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(activity, newProducts);
                gridview.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();

            }
        });

        ObserverLike.changeLikeStatusListener(new ObserverLikeListener() {
            @Override
            public void changeLikeStatus() {
                //gridview.scrollToPosition(ObserverLike.getLikeStatus());
                gridview.setSelection(ObserverLike.getLikeStatus());
                gridview.setAdapter(adapter);

            }
        });
        //Filter
        ///FilterSubCategory
        Button btnFilterCategory = (Button) mainView.findViewById(R.id.group_dialog);
        btnFilterCategory = PriceUtility.getInstance().ChangeButtonFont(btnFilterCategory,activity);
        btnFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("pageId", pageId);
                if (FilterCategory.getInstance().isAdded() && Configuration.getConfig().filterCategoryDialogShowStatus) {
                } else if (!FilterCategory.getInstance().isAdded() && !Configuration.getConfig().filterCategoryDialogShowStatus) {
                    FilterCategory.getInstance().setArguments(args);
                    FilterCategory.getInstance().show(activity.getFragmentManager(), "Category");
                    Configuration.getConfig().filterCategoryDialogShowStatus = true;//when category filter dialog open
                }
                //show Dialog Filter category
                //Change grid view data after set filter
                ObserverFilterCategory.changeFilterCategoryListener(new ObserverFilterCategoryListener() {
                    @Override
                    public void changeFilterCategory() {
                        Configuration.getConfig().filterCategoryDialogShowStatus = false;//when category filter dialog close
                        txtFilterCategorySelected.setText(Configuration.getConfig().filterCategoryTitle);
                        txtFilterCategorySelected.setTextColor(ContextCompat.getColor(activity, R.color.colorFilterText));
                        txtFilterOptionProductSelected.setText(getString(R.string.all));
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(activity, R.color.black));
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().filterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().filterOption);
                        if (newProducts.size()==0){
                            getProductInformationFromServerWhenFilter(Configuration.getConfig().filterCategoryId,0);

                        }
                        else {
                            loadProductMessage();
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(activity, newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }

                    }
                });
            }
        });
        ///Filter in Product Features
        Button btnFilterOptionProduct = (Button) mainView.findViewById(R.id.filter_dialogue);
        btnFilterOptionProduct = PriceUtility.getInstance().ChangeButtonFont(btnFilterOptionProduct,activity);
        btnFilterOptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnFilterOptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                if (Configuration.getConfig().filterCategoryId == 0) {
                    args.putInt("pageId", pageId);
                } else
                    args.putInt("pageId", Configuration.getConfig().filterCategoryId);
                FilterOptionProduct.getInstance().setArguments(args);
                FilterOptionProduct.getInstance().show(activity.getFragmentManager(), "FilterProductOption");
                ObserverFilterPrice.changeFilterPriceListener(new ObserverFilterPriceListener() {
                    @Override
                    public void changeFilterPrice() {
                        txtFilterOptionProductSelected.setText(Configuration.getConfig().filterPriceTitle);
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(activity, R.color.colorFilterText));
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().filterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().filterOption);
                        if (newProducts.size()==0){
                            noProductMessage();
                        }
                        else  {
                            loadProductMessage();
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(activity, newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }

                    }
                });
                ObserverFilterBrand.changeFilterBrandListener(new ObserverFilterBrandListener() {
                    @Override
                    public void changeFilterBrand() {
                        txtFilterOptionProductSelected.setText(Configuration.getConfig().filterBrand);
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(activity, R.color.colorFilterText));
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().filterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().filterOption);
                        if (newProducts.size()==0){
                            noProductMessage();

                        }
                        else  {

                            loadProductMessage();
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(activity, newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }

                    }
                });

                ObserverFilterAll.changeFilterAllListener(new ObserverFilterAllListener() {
                    @Override
                    public void changeFilterAll() {
                        txtFilterOptionProductSelected.setText(Configuration.getConfig().filterAll);
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(activity, R.color.colorFilterText));
                        Configuration.getConfig().filterCategoryId=pageId;
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().filterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().filterOption);
                        if (newProducts.size()==0){
                            noProductMessage();
                        }
                        else  {
                            loadProductMessage();
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(activity, newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });
        return mainView;
    }

    private void getProductInformationFromServerWhenFirstEnterTab(int categoryId,int minStarLimited){
        String UrlGetProducts= Link.getInstance().generateForGetLimitedProductOfAMainCategory(categoryId,
                minStarLimited,Configuration.getConfig().someOfFewProductNumberForGetEveryTab);
        Intent intent = new Intent(Intent.ACTION_SYNC, null,activity, DownloadProductInformationService.class);
        intent.putExtra("receiver", resultReceiver);
        intent.putExtra("Link",UrlGetProducts);
        intent.putExtra("code",200);
        activity.startService(intent);
    }

    private void getProductInformationFromServerWhenScroll(int categoryId,int minStarLimited,int maxEndLimited){
        String UrlGetProducts= Link.getInstance().generateForGetLimitedProductOfAMainCategory(categoryId,
                minStarLimited,maxEndLimited);
        Intent intent = new Intent(Intent.ACTION_SYNC, null,activity, DownloadProductInformationService.class);
        intent.putExtra("receiver", resultReceiver);
        intent.putExtra("Link",UrlGetProducts);
        intent.putExtra("code",201);
        activity.startService(intent);

    }

    private void getProductInformationFromServerWhenFilter(int categoryId, int minStarLimited){
        String UrlGetProducts= Link.getInstance().generateForGetLimitedProductOfAMainCategory(categoryId,
                minStarLimited,Configuration.getConfig().someOfFewProductNumberForGetEveryTab);
        Intent intent = new Intent(Intent.ACTION_SYNC, null,activity, DownloadProductInformationService.class);
        intent.putExtra("receiver", resultReceiver);
        intent.putExtra("Link",UrlGetProducts);
        intent.putExtra("code",202);
        activity.startService(intent);
    }
    private void loadingMessage(){
        //noThingToShow.setText(getString(R.string.please_wait_message));
        //noThingToShow.setTextColor(ContextCompat.getColor(myContext, R.color.black));
        noThingToShow.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        gridview.setVisibility(View.GONE);
    }

    private void loadProductMessage(){
        noThingToShow.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        gridview.setVisibility(View.VISIBLE);

    }

    private void noProductMessage(){
        gridview.setVisibility(View.GONE);
        noThingToShow.setText(R.string.no_product_to_show);
        noThingToShow.setTextColor(ContextCompat.getColor(activity, R.color.colorFilterText));
        noThingToShow.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        ArrayList<Product> newProducts = new ArrayList<>();
        switch (resultCode) {

            case UpdateService.STATUS_FINISHED:
                newProducts = sch.getProductAfterRefresh(pageIdForRefresh,
                        Configuration.getConfig().filterCategoryId,
                        txtFilterOptionForRefresh,
                        Configuration.getConfig().filterOption);
                mSwipeRefreshLayout.setRefreshing(false);
                if (newProducts.size() == 0) {
                    noThingToShow.setVisibility(View.VISIBLE);
                    gridview.setVisibility(View.GONE);
                } else {
                    noThingToShow.setVisibility(View.GONE);
                    gridview.setVisibility(View.VISIBLE);
                    PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(activity, newProducts);
                    gridview.setAdapter(newAdapter);
                    newAdapter.notifyDataSetChanged();
                }
                //ObserverUpdateCategories.setUpdateCategoriesStatus(true);
                break;

            case DownloadProductInformationService.STATUS_FINISHED_FIRST_ENTER_TAB:
                newProducts= (ArrayList<Product>) resultData.getSerializable("newProduct");
                if (newProducts.size()==0){
                    noProductMessage();
                }
                else {
                    loadProductMessage();
                    gridview.setAdapter(new PictureProductShopItemAdapter(activity,newProducts));
                }
                break;

            case DownloadProductInformationService.STATUS_FINISHED_WHEN_SCROLL:
                newProducts = (ArrayList<Product>) resultData.getSerializable("newProduct");
                int lastProductNumber;
                if (newProducts.size()==0 && gridview.getAdapter()==null){
                   noProductMessage();

                }
                else if(newProducts.size()>0 && gridview.getAdapter()==null){
                    lastProductNumber=0;
                    loadProductMessage();
                    PictureSpecialProductItemAdapter adapter=new PictureSpecialProductItemAdapter(activity,newProducts);
                    gridview.setAdapter(adapter);
                    if(lockScroll && lastProductNumber<newProducts.size())
                        lockScroll=!lockScroll;
                    if (lockFirstVisitTab)
                        lockFirstVisitTab=!lockFirstVisitTab;

                }
                else if(newProducts.size()>0 && gridview.getAdapter()!=null){
                    lastProductNumber=gridview.getAdapter().getCount();
                    loadProductMessage();
                    for(int i=0;i<newProducts.size();i++)
                        ((PictureProductShopItemAdapter) gridview.getAdapter()).add(newProducts.get(i));
                    if(lockScroll && lastProductNumber<newProducts.size())
                        lockScroll=!lockScroll;
                    if (lockFirstVisitTab)
                        lockFirstVisitTab=!lockFirstVisitTab;

                }
                break;
            case DownloadProductInformationService.STATUS_FINISHED_FILTER:
                newProducts = (ArrayList<Product>) resultData.getSerializable("newProduct");
                if (newProducts.size()==0){
                   noProductMessage();

                }
                else{
                    loadProductMessage();
                    gridview.setAdapter(new PictureProductShopItemAdapter(activity,newProducts));
                }
                break;
        }

    }
}
