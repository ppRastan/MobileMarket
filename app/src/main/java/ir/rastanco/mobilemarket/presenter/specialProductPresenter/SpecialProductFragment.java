package ir.rastanco.mobilemarket.presenter.specialProductPresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Services.DownloadProductInformationService;
import ir.rastanco.mobilemarket.presenter.Services.DownloadResultReceiver;
import ir.rastanco.mobilemarket.presenter.Services.UpdateService;
import ir.rastanco.mobilemarket.utility.ColorUtility;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;

/**
 * Created by ShaisteS on 1394/12/09.
 * This Fragment show specialProduct(specialProduct is a product that discount or showAtHomeProduct=1)
 */
public class SpecialProductFragment extends Fragment implements DownloadResultReceiver.Receiver {

    private ServerConnectionHandler sch;
    private ArrayList<Product> products;
    private DownloadResultReceiver mReceiver;
    private ListView productListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView noThingToShow;
    private ProgressBar progressBar;
    private Context context;
    private Activity activity;
    private int existProductNumber;
    private int allProductNumber;
    private boolean lockScroll =false;
    private Boolean lockFirstVisitTab=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        activity=getActivity();
        sch = ServerConnectionHandler.getInstance(context);
        View mainView = inflater.inflate(R.layout.fragment_special_product, container, false);
        productListView = (ListView) mainView.findViewById(R.id.listView_picProduct);
        noThingToShow = (TextView) mainView.findViewById(R.id.no_thing_to_show1);
        //noThingToShow = PriceUtility.getInstance().changeFontToYekan(noThingToShow, context);
        progressBar=(ProgressBar)mainView.findViewById(R.id.progressBar_Loading_special_page);
        existProductNumber = sch.getFirstIndexForGetProductFromJson();
        allProductNumber = sch.getNumberAllProduct();
        products = new ArrayList<>();
        products = sch.getSpecialProduct();
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);



        if (showMessage(products.size())) {
            PictureSpecialProductItemAdapter adapter = new PictureSpecialProductItemAdapter(getActivity(), products);
            productListView.setAdapter(adapter);
        }

        if (products.size()==0){
            getProductInformationFromServerWhenEnterToTab(0);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);
        //refresh grid view
        mSwipeRefreshLayout.setEnabled(false);
        productListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean enable = false;
                if (productListView.getChildCount() > 0) {
                    // check if the first slider_items of the list is visible
                    boolean firstItemVisible = productListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first slider_items is visible
                    boolean topOfFirstItemVisible = productListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);
               // if (enable)
                 //   Configuration.getConfig().customerSupportFloatingActionButton.setVisibility(View.VISIBLE);
                //else
                  //  Configuration.getConfig().customerSupportFloatingActionButton.setVisibility(View.GONE);

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && !lockScroll) {
                    //scroll receive button
                    lockScroll =true;
                    int minLimited=productListView.getAdapter().getCount();
                    int maxLimited=minLimited+Configuration.getConfig().someOfFewProductNumberWhenScrollIsButton;
                    getProductInformationFromServerWhenScroll(minLimited,maxLimited);

                }
            }
        });
        ColorUtility.getConfig().setColorOfSwipeRefresh(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*sch.refreshCategories(Link.getInstance().generateURLForGetAllCategories());
                        sch.getNewProducts();
                        sch.getEditProducts();*/
                        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), UpdateService.class);
                        intent.putExtra("receiver", mReceiver);
                        intent.putExtra("requestId", 101);
                        getActivity().startService(intent);

                    }
                }, 5000);
            }
        });
        return mainView;
    }

    private Boolean showMessage(int productSize) {
        if (productSize == 0) {
            if (existProductNumber < allProductNumber || existProductNumber == 0) {
                //Loading bar and please wait... text and grid view gone
                progressBar.setVisibility(View.VISIBLE);
                progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                //noThingToShow.setText(getString(R.string.please_wait_message));
                //noThingToShow.setTextColor(ContextCompat.getColor(context, R.color.black));
                //noThingToShow.setVisibility(View.VISIBLE);
                noThingToShow.setVisibility(View.GONE);
                productListView.setVisibility(View.GONE);
                return false;
            } else if (existProductNumber != 0) {
                //Loading bar gone and no product text and grid view gone
                progressBar.setVisibility(View.GONE);
                noThingToShow.setText(getString(R.string.no_product_to_show));
                noThingToShow.setTextColor(ContextCompat.getColor(context, R.color.orange));
                noThingToShow.setVisibility(View.VISIBLE);
                productListView.setVisibility(View.GONE);
                return false;
            }
            return false;
        } else {
            //Loading bar gone text view gone grid view visible
            progressBar.setVisibility(View.GONE);
            noThingToShow.setVisibility(View.GONE);
            productListView.setVisibility(View.VISIBLE);
            return true;
        }

    }

    private void getProductInformationFromServerWhenEnterToTab(int minStarLimited){
        String UrlGetProducts= Link.getInstance().generateUrlForGetSpecialProduct(minStarLimited,Configuration.getConfig().someOfFewSpecialProductNumber);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), DownloadProductInformationService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("Link",UrlGetProducts);
        context.startService(intent);
    }

    private void getProductInformationFromServerWhenScroll(int minStarLimited,int maxEndLimited){
        String UrlGetProducts= Link.getInstance().generateUrlForGetSpecialProduct(minStarLimited,maxEndLimited);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), DownloadProductInformationService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("Link",UrlGetProducts);
        intent.putExtra("code",201);
        context.startService(intent);

    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        PictureSpecialProductItemAdapter newAdapter;
        switch (resultCode) {
            case UpdateService.STATUS_FINISHED:
                products = sch.getSpecialProduct();
                newAdapter = new PictureSpecialProductItemAdapter(getActivity(), products);
                productListView.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                //ObserverUpdateCategories.setUpdateCategoriesStatus(true);
                break;
            case DownloadProductInformationService.STATUS_FINISHED_WHEN_SCROLL:
                int lastSpecialProductNumber=products.size();
                ArrayList<Product> newProducts = (ArrayList<Product>) resultData.getSerializable("newProduct");

                if (newProducts.size()>0){
                    for(int i=0;i<newProducts.size();i++)
                        ((PictureSpecialProductItemAdapter) productListView.getAdapter()).add(newProducts.get(i));
                    if(lockScroll && lastSpecialProductNumber<newProducts.size())
                        lockScroll=!lockScroll;
                    if (lockFirstVisitTab)
                        lockFirstVisitTab=!lockFirstVisitTab;
                }
                //products = sch.getSpecialProduct();
                //productListView.setAdapter(newAdapter)
                //newAdapter.notifyDataSetChanged();;
                //productListView.setSelection(lastSpecialProductNumber);

                break;
        }
    }
}
