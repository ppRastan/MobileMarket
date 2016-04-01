package ir.rastanco.mobilemarket.presenter.shopPresenter;

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
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.PriceUtility;

/**
 * Created by ShaisteS on 1394/12/09.
 */
public class ShopFragment extends Fragment {


    private ServerConnectionHandler sch;
    private TextView txtFilterOptionProductSelected;
    private TextView txtFilterCategorySelected;
    private FragmentActivity myContext;
    private TextView noThingToShow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView=inflater.inflate(R.layout.fragment_shop, container , false);
        Configuration.getConfig().ShopFragmentContext=getContext();
        myContext=(FragmentActivity)Configuration.getConfig().ShopFragmentContext;
        final int pageId;
        pageId=getArguments().getInt("pageId");
        sch=new ServerConnectionHandler(getContext());
        ArrayList<Product> products=sch.getProductsOfAParentCategory(pageId);
        noThingToShow = (TextView)mainView.findViewById(R.id.no_thing_to_show1);
        noThingToShow = PriceUtility.getInstance().changeFontToYekan(noThingToShow,myContext);
        final GridView gridview = (GridView) mainView.findViewById(R.id.gv_infoProduct);
        /*final RecyclerView gridview = (RecyclerView) mainView.findViewById(R.id.gv_infoProduct);
        MyCustomLayoutManager mLayoutManager = new MyCustomLayoutManager(Configuration.ShopFragmentContext);
        gridview.setLayoutManager(mLayoutManager);
        gridview.smoothScrollToPosition(0);
        gridview.setLayoutManager(new GridLayoutManager(Configuration.ShopFragmentContext, 2));
        gridview.addItemDecoration(new RecyclerViewItemDecoration(6, 6));
        final GridLayoutManager layoutManager = ((GridLayoutManager)gridview.getLayoutManager());
        final boolean firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()==0;*/

        if(products.size()==0){
            noThingToShow.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.GONE);
        }
        else
        {
            noThingToShow.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
        }

        final PictureProductShopItemAdapter adapter=new  PictureProductShopItemAdapter(getActivity(),products);
        gridview.setAdapter(adapter);

        //refresh grid view
        final SwipeRefreshLayout mSwipeRefreshLayout= (SwipeRefreshLayout)
                mainView.findViewById(R.id.swipe_refresh_layout);
        //mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setEnabled(false);
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
                    Configuration.getConfig().telephoneFloatingActionButton.setVisibility(View.VISIBLE);
                else
                    Configuration.getConfig().telephoneFloatingActionButton.setVisibility(View.GONE);

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sch.refreshCategories(Link.getInstance().generateURLForGetAllCategories());
                        sch.getNewProducts();
                        sch.getEditProducts();
                        ArrayList<Product> newProducts = sch.getProductAfterRefresh(pageId,
                                Configuration.getConfig().FilterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().FilterOption);
                        if (newProducts.size() == 0) {
                            noThingToShow.setVisibility(View.VISIBLE);
                            gridview.setVisibility(View.GONE);
                        } else {
                            noThingToShow.setVisibility(View.GONE);
                            gridview.setVisibility(View.VISIBLE);
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 5000);
            }
        });

        ObserverSimilarProduct.SimilarProductListener(new ObserverSimilarProductListener() {
            @Override
            public void SimilarProductSet() {
                Configuration.getConfig().FilterCategoryId=ObserverSimilarProduct.getSimilarProduct();
                Configuration.getConfig().FilterPriceTitle=sch.getACategoryTitleWithCategoryId(Configuration.getConfig().FilterCategoryId);
                Configuration.getConfig().FilterBrand=Configuration.getConfig().ShopFragmentContext.getResources().getString(R.string.all);
                txtFilterCategorySelected.setText(Configuration.getConfig().FilterPriceTitle);
                txtFilterCategorySelected.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                txtFilterOptionProductSelected.setText(Configuration.getConfig().ShopFragmentContext.getResources().getString(R.string.all));
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
        Button btnFilterCategory =(Button)mainView.findViewById(R.id.group_dialog);
        txtFilterCategorySelected =(TextView) mainView.findViewById(R.id.group_dialog_text);
        btnFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show Dialog Fragment
                Bundle args = new Bundle();
                args.putInt("pageId", pageId);
                FilterCategory.getInstance().setArguments(args);
                FilterCategory.getInstance().show(myContext.getFragmentManager(), "Category");
                //Change grid view data after set filter
                ObserverFilterCategory.changeFilterCategoryListener(new ObserverFilterCategoryListener() {
                    @Override
                    public void changeFilterCategory() {
                        txtFilterCategorySelected.setText(Configuration.getConfig().FilterCategoryTitle);
                        txtFilterCategorySelected.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().FilterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().FilterOption);
                        if (newProducts.size() == 0) {
                            noThingToShow.setVisibility(View.VISIBLE);
                            gridview.setVisibility(View.GONE);
                        }
                        else
                        {
                            noThingToShow.setVisibility(View.GONE);
                            gridview.setVisibility(View.VISIBLE);
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }

                    }
                });
            }
        });
        ///Filter in Product Features
        txtFilterOptionProductSelected = (TextView)mainView.findViewById(R.id.filter_dialogue_text);
        Button btnFilterOptionProduct =(Button)mainView.findViewById(R.id.filter_dialogue);
        btnFilterOptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("pageId", pageId);
                FilterOptionProduct.getInstance().setArguments(args);
                FilterOptionProduct.getInstance().show(myContext.getFragmentManager(), "FilterProductOption");
                ObserverFilterPrice.changeFilterPriceListener(new ObserverFilterPriceListener() {
                    @Override
                    public void changeFilterPrice() {
                        txtFilterOptionProductSelected.setText(Configuration.getConfig().FilterPriceTitle);
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                        ArrayList<Product> newProducts=sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().FilterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().FilterOption);
                        if (newProducts.size() == 0) {
                            noThingToShow.setVisibility(View.VISIBLE);
                            gridview.setVisibility(View.GONE);
                        }
                        else
                        {

                            noThingToShow.setVisibility(View.GONE);
                            gridview.setVisibility(View.VISIBLE);
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();
                        }

                    }
                });
                ObserverFilterBrand.changeFilterBrandListener(new ObserverFilterBrandListener() {
                    @Override
                    public void changeFilterBrand() {
                        txtFilterOptionProductSelected.setText(Configuration.getConfig().FilterBrand);
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(myContext, R.color.red));
                        ArrayList<Product> newProducts=sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().FilterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().FilterOption);
                        if (newProducts.size() == 0) {
                            noThingToShow.setVisibility(View.VISIBLE);
                            gridview.setVisibility(View.GONE);

                        }
                        else
                        {
                            noThingToShow.setVisibility(View.GONE);
                            gridview.setVisibility(View.VISIBLE);
                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                            gridview.setAdapter(newAdapter);
                            newAdapter.notifyDataSetChanged();

                        }

                    }
                });

                ObserverFilterAll.changeFilterAllListener(new ObserverFilterAllListener() {
                    @Override
                    public void changeFilterAll() {
                        txtFilterOptionProductSelected.setText(Configuration.getConfig().FilterAll);
                        txtFilterOptionProductSelected.setTextColor(ContextCompat.getColor(myContext,R.color.red));
                        ArrayList<Product> newProducts=sch.getProductAfterFilter(pageId,
                                Configuration.getConfig().FilterCategoryId,
                                txtFilterOptionProductSelected.getText().toString(),
                                Configuration.getConfig().FilterOption);
                        if (newProducts.size()==0){
                            noThingToShow.setVisibility(View.VISIBLE);
                            gridview.setVisibility(View.GONE);
                        }
                        else
                        {
                            noThingToShow.setVisibility(View.GONE);
                            gridview.setVisibility(View.VISIBLE);
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
}
