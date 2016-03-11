package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import ir.rastanco.mobilemarket.utility.DataFilter;
import ir.rastanco.mobilemarket.utility.RecyclerViewItemDecoration;

/**
 * Created by ShaisteS on 1394/12/09.
 */
public class ShopFragment extends Fragment {


    private ServerConnectionHandler sch;
    private ArrayList<Product> products;
    private Button btnFilterCategory;
    private Button btnFilterOptionProduct;
    private TextView txtFilterOptionProductSelected;
    private TextView txtFilterCategorySelected;
    private FragmentActivity myContext;
    private TextView noThingToShow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView=inflater.inflate(R.layout.fragment_shop, null);
        Configuration.ShopFragmentContext=getContext();
        myContext=(FragmentActivity)Configuration.ShopFragmentContext;
        final String pageName;
        pageName=getArguments().getString("pageName");
        sch=new ServerConnectionHandler(getContext());
        products=sch.getProductOfMainCategory(pageName);
        noThingToShow = (TextView)mainView.findViewById(R.id.no_thing_to_show1);
        noThingToShow.setTypeface(Typeface.createFromAsset(myContext.getAssets(), "fonts/yekan.ttf"));
        final RecyclerView gridview = (RecyclerView) mainView.findViewById(R.id.gv_infoProduct);
        gridview.setLayoutManager(new GridLayoutManager(Configuration.ShopFragmentContext,2));
        gridview.addItemDecoration(new RecyclerViewItemDecoration(6, 6));
        final GridLayoutManager layoutManager = ((GridLayoutManager)gridview.getLayoutManager());
        final boolean firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()==0;

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
        mSwipeRefreshLayout.setEnabled(true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sch.refreshCategories("http://decoriss.com/json/get,com=allcats&cache=false");
                        sch.getNewProducts();
                        sch.getEditProducts();
                        ArrayList<Product> newProducts=sch.getProductAfterRefresh(pageName,
                                txtFilterCategorySelected.getText().toString(),
                                txtFilterOptionProductSelected.getText().toString(),
                                DataFilter.FilterOption);
                        if (newProducts.size() == 0) {
                            noThingToShow.setVisibility(View.VISIBLE);
                            gridview.setVisibility(View.GONE);
                        }
                        else {
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
                txtFilterCategorySelected.setText(sch.getACategoryTitle(ObserverSimilarProduct.getSimilarProduct()));
                txtFilterCategorySelected.setTextColor(getResources().getColor(R.color.red));
                ArrayList<Product> newProducts = sch.ProductOFASubCategory(ObserverSimilarProduct.getSimilarProduct());
                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                gridview.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();

            }
        });

        ObserverLike.changeLikeStatusListener(new ObserverLikeListener() {
            @Override
            public void changeLikeStatus() {
                gridview.scrollToPosition(ObserverLike.getLikeStatus());
                gridview.setAdapter(adapter);

            }
        });
        //Filter
        ///FilterSubCategory
        btnFilterCategory =(Button)mainView.findViewById(R.id.group_dialog);
        txtFilterCategorySelected =(TextView) mainView.findViewById(R.id.group_dialog_text);
        btnFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*txtFilterOptionProductSelected.setText(getResources().getText(R.string.all));
                txtFilterOptionProductSelected.setTextColor(getResources().getColor(R.color.black));*/
                //show Dialog Fragment
                Bundle args = new Bundle();
                args.putString("name", pageName);
                FilterCategory filterCategory = new FilterCategory();
                filterCategory.setArguments(args);
                filterCategory.show(myContext.getFragmentManager(), "Category");
                //Change grid view data after set filter
                ObserverFilterCategory.changeFilterCategoryListener(new ObserverFilterCategoryListener() {
                    @Override
                    public void changeFilterCategory() {
                        txtFilterCategorySelected.setText(DataFilter.FilterCategory);
                        txtFilterCategorySelected.setTextColor(getResources().getColor(R.color.red));
                        //ArrayList<Product> newProducts = sch.getProductsAfterFilterCategory(products, txtFilterCategorySelected.getText().toString());
                        ArrayList<Product> newProducts = sch.getProductAfterFilter(pageName,
                                txtFilterCategorySelected.getText().toString(),
                                txtFilterOptionProductSelected.getText().toString(),
                                DataFilter.FilterOption);
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
        txtFilterOptionProductSelected = (TextView)mainView.findViewById(R.id.acordingto_dialog_text);
        btnFilterOptionProduct =(Button)mainView.findViewById(R.id.acording_to_dialog);
        btnFilterOptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txtFilterCategorySelected.setText(getResources().getText(R.string.all));
                //txtFilterCategorySelected.setTextColor(getResources().getColor(R.color.black));
                Bundle args = new Bundle();
                args.putString("name", pageName);
                FilterOptionProduct filterOptionProduct = new FilterOptionProduct();
                filterOptionProduct.setArguments(args);
                filterOptionProduct.show(myContext.getFragmentManager(), "FilterProductOption");
                ObserverFilterPrice.changeFilterPriceListener(new ObserverFilterPriceListener() {
                    @Override
                    public void changeFilterPrice() {
                        txtFilterOptionProductSelected.setText(DataFilter.FilterPriceTitle);
                        txtFilterOptionProductSelected.setTextColor(getResources().getColor(R.color.red));
                        //ArrayList<Product> newProducts = sch.getProductAsPriceFilter(products, txtFilterOptionProductSelected.getText().toString());
                        ArrayList<Product> newProducts=sch.getProductAfterFilter(pageName,
                                txtFilterCategorySelected.getText().toString(),
                                txtFilterOptionProductSelected.getText().toString(),
                                DataFilter.FilterOption);
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
                        txtFilterOptionProductSelected.setText(DataFilter.FilterBrand);
                        txtFilterOptionProductSelected.setTextColor(getResources().getColor(R.color.red));
                        //ArrayList<Product> newProducts = sch.getAllProductOfABrand(products, DataFilter.FilterBrand);
                        ArrayList<Product> newProducts=sch.getProductAfterFilter(pageName,
                                txtFilterCategorySelected.getText().toString(),
                                txtFilterOptionProductSelected.getText().toString(),
                                DataFilter.FilterOption);
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
                        txtFilterOptionProductSelected.setText(DataFilter.FilterAll);
                        txtFilterOptionProductSelected.setTextColor(getResources().getColor(R.color.red));
                        ArrayList<Product> newProducts=sch.getProductAfterFilter(pageName,
                                txtFilterCategorySelected.getText().toString(),
                                txtFilterOptionProductSelected.getText().toString(),
                                DataFilter.FilterOption);
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
