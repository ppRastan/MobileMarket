package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.content.Intent;
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
import ir.rastanco.mobilemarket.presenter.Services.DownloadResultReceiver;
import ir.rastanco.mobilemarket.presenter.Services.UpdateService;
import ir.rastanco.mobilemarket.utility.ColorUtility;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.PriceUtility;

/**
 * Created by ShaisteS on 1394/12/09.
 */
public class ShopFragment extends Fragment implements DownloadResultReceiver.Receiver  {


    private ServerConnectionHandler sch;
    private FragmentActivity myContext;
    private TextView noThingToShow;
    //private ProgressBar progressBar;
    private DownloadResultReceiver mReceiver;
    private GridView gridview;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PictureProductShopItemAdapter adapter;
    private int existProductNumber;
    private int pageIdForRefresh;
    private String txtFilterOptionForRefresh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_shop, container, false);
        Configuration.getConfig().shopFragmentContext = getContext();
        myContext = (FragmentActivity) Configuration.getConfig().shopFragmentContext;
        sch = ServerConnectionHandler.getInstance(getContext());
        final int pageId = getArguments().getInt("pageId");
        final TextView txtFilterOptionProductSelected = (TextView) mainView.findViewById(R.id.filter_dialogue_text);
        final TextView txtFilterCategorySelected = (TextView) mainView.findViewById(R.id.group_dialog_text);
        noThingToShow = (TextView) mainView.findViewById(R.id.no_thing_to_show1);
        noThingToShow = PriceUtility.getInstance().changeFontToYekan(noThingToShow, myContext);
        //progressBar=(ProgressBar)mainView.findViewById(R.id.progressBar_Loading);
        gridview = (GridView) mainView.findViewById(R.id.gv_infoProduct);

        ArrayList<Product> products = sch.getProductsOfAParentCategory(pageId);
        existProductNumber=sch.getFirstIndexForGetProductFromJson();
        int allProductNumber=sch.getNumberAllProduct();

        Configuration.getConfig().filterCategoryId=0;
        Configuration.getConfig().filterCategoryTitle=getString(R.string.all);
        Configuration.getConfig().filterOption=getString(R.string.all);
        txtFilterCategorySelected.setText(getString(R.string.all));
        txtFilterOptionProductSelected.setText(getString(R.string.all));
        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(myContext, R.color.black));


        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        if(showMessage(products.size())){
            adapter = new PictureProductShopItemAdapter(getActivity(), products);
            gridview.setAdapter(adapter);
        }

        //refresh grid view
        mSwipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipe_refresh_layout);
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
                if (enable)
                    Configuration.getConfig().customerSupportFloatingActionButton.setVisibility(View.VISIBLE);
                else
                    Configuration.getConfig().customerSupportFloatingActionButton.setVisibility(View.GONE);

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    //scroll receive button
                    /*int firstIndexGetProduct = ServerConnectionHandler.getInstance(myContext).getFirstIndexForGetProductFromJson();
                    int allNumberProducts = ServerConnectionHandler.getInstance(myContext).getNumberAllProduct();
                    if (firstIndexGetProduct < allNumberProducts) {
                        ArrayList<Product> newProducts = sch.getProductsOfAParentCategory(pageId);
                        adapter.clear();
                        for (int i = 0; i < newProducts.size(); i++) {
                            adapter.add(newProducts.get(i));
                        }
                        adapter.notifyDataSetChanged();

                    }*/
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
                        txtFilterOptionForRefresh= (String) txtFilterOptionProductSelected.getText();
                        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), UpdateService.class);
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
                txtFilterCategorySelected.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                txtFilterOptionProductSelected.setText(Configuration.getConfig().shopFragmentContext.getResources().getString(R.string.all));
                txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(myContext, R.color.black));
                ArrayList<Product> newProducts = sch.getProductsOfAParentCategory(ObserverSimilarProduct.getSimilarProduct());
                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
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
        final Button btnFilterCategory = (Button) mainView.findViewById(R.id.group_dialog);
        btnFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("pageId", pageId);
                if (FilterCategory.getInstance().isAdded()&& Configuration.getConfig().filterCategoryDialogShowStatus) {
                }
                else if(!FilterCategory.getInstance().isAdded() && !Configuration.getConfig().filterCategoryDialogShowStatus) {
                    FilterCategory.getInstance().setArguments(args);
                    FilterCategory.getInstance().show(myContext.getFragmentManager(), "Category");
                    Configuration.getConfig().filterCategoryDialogShowStatus=true;//when category filter dialog open
                }
                //show Dialog Filter category
                //Change grid view data after set filter
                ObserverFilterCategory.changeFilterCategoryListener(new ObserverFilterCategoryListener() {
                    @Override
                    public void changeFilterCategory() {
                        Configuration.getConfig().filterCategoryDialogShowStatus=false;//when category filter dialog close
                        txtFilterCategorySelected.setText(Configuration.getConfig().filterCategoryTitle);
                        txtFilterCategorySelected.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                        txtFilterOptionProductSelected.setText(getString(R.string.all));
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(myContext,R.color.black));
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().filterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().filterOption);
                        if (showMessage(newProducts.size())){
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }

                    }
                });
            }
        });
        ///Filter in Product Features
        Button btnFilterOptionProduct = (Button) mainView.findViewById(R.id.filter_dialogue);
        btnFilterOptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnFilterOptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                if(Configuration.getConfig().filterCategoryId==0) {
                    args.putInt("pageId", pageId);
                }
                else
                    args.putInt("pageId",Configuration.getConfig().filterCategoryId);
                FilterOptionProduct.getInstance().setArguments(args);
                FilterOptionProduct.getInstance().show(myContext.getFragmentManager(), "FilterProductOption");
                ObserverFilterPrice.changeFilterPriceListener(new ObserverFilterPriceListener() {
                    @Override
                    public void changeFilterPrice() {
                        txtFilterOptionProductSelected.setText(Configuration.getConfig().filterPriceTitle);
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().filterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().filterOption);
                        if (showMessage(newProducts.size())) {
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }
                    }
                });
                ObserverFilterBrand.changeFilterBrandListener(new ObserverFilterBrandListener() {
                    @Override
                    public void changeFilterBrand() {
                        txtFilterOptionProductSelected.setText(Configuration.getConfig().filterBrand);
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().filterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().filterOption);
                        if (showMessage(newProducts.size())){
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }

                    }
                });

                ObserverFilterAll.changeFilterAllListener(new ObserverFilterAllListener() {
                    @Override
                    public void changeFilterAll() {
                        txtFilterOptionProductSelected.setText(Configuration.getConfig().filterAll);
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().filterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().filterOption);
                        if(showMessage(newProducts.size())){
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });
        return mainView;
    }

    private Boolean showMessage(int productSize){
        if (productSize == 0) {
            if (existProductNumber==0){
                //Loading bar and please wait... text and grid view gone
                //progressBar.setVisibility(View.VISIBLE);
                noThingToShow.setText(getString(R.string.please_wait_message));
                noThingToShow.setTextColor(ContextCompat.getColor(myContext, R.color.black));
                noThingToShow.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.GONE);
                return false;
            }
            else {
                noThingToShow.setText(getString(R.string.no_product_to_show));
                noThingToShow.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                noThingToShow.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.GONE);
                return false;

            }


           /* else if(existProductNumber!=0){
                //Loading bar gone and no product text and grid view gone
                //progressBar.setVisibility(View.INVISIBLE);
                noThingToShow.setText(getString(R.string.no_product_to_show));
                noThingToShow.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                noThingToShow.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.GONE);
                return false;
            }
            return false;*/

        }
        else {
            //Loading bar gone text view gone grid view visible
            //progressBar.setVisibility(View.GONE);
            noThingToShow.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
            return true;
        }

    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {

            case UpdateService.STATUS_FINISHED:
                ArrayList<Product> newProducts = sch.getProductAfterRefresh(pageIdForRefresh,
                        Configuration.getConfig().filterCategoryId,
                        txtFilterOptionForRefresh,
                        Configuration.getConfig().filterOption);
                if (newProducts.size() == 0) {
                    noThingToShow.setVisibility(View.VISIBLE);
                    gridview.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    noThingToShow.setVisibility(View.GONE);
                    gridview.setVisibility(View.VISIBLE);
                    PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                    gridview.setAdapter(newAdapter);
                    newAdapter.notifyDataSetChanged();
                }

               break;

        }
    }
}
