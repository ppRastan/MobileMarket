package ir.rastanco.mobilemarket.presenter;

/**
 * Created by Samaneh on 12/20/2015.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ArticlePresenter.ArticleItemAdapter;
import ir.rastanco.mobilemarket.presenter.Filter.FilterCategory;
import ir.rastanco.mobilemarket.presenter.Filter.FilterOptionProduct;
import ir.rastanco.mobilemarket.presenter.homePresenter.PictureProductHomeItemAdapter;
import ir.rastanco.mobilemarket.presenter.shopPresenter.PictureProductShopItemAdapter;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.DataFilter;

public class SuperAwesomeCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    private ServerConnectionHandler sch;
    private ArrayList<Product> products;
    private ArrayList<Article> articles;
    private ArrayList<String> mainCategoryTitle;
    private String second_page;
    private String third_page;
    private String fourth_page;
    private Button btnFilterCategory;
    private Button btnFilterOptionProduct;
    private TextView txtFilterOptionProductSelected;
    private TextView txtFilterCategorySelected;

    private FragmentActivity myContext;

    public SuperAwesomeCardFragment() {
    }

    public static SuperAwesomeCardFragment newInstance(int position) {
        SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View mainView=null;
        position = getArguments().getInt(ARG_POSITION);
        Configuration.superACFragment=getContext();
        myContext=(FragmentActivity)Configuration.superACFragment;
        final String tag=this.getTag();

        sch=new ServerConnectionHandler(Configuration.superACFragment);
        mainCategoryTitle= new ArrayList<String>();
        mainCategoryTitle=sch.getMainCategoryTitle();
        products=new ArrayList<Product>();
        articles=new ArrayList<Article>();
        articles=sch.getAllArticlesFromTable();

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


        switch (position) {

            case 0: {

                products=sch.getSpecialProduct();
                mainView = inflater.inflate(R.layout.fragment_home, null);
                final ListView productListView = (ListView) mainView.findViewById(R.id.lstv_picProduct);
                PictureProductHomeItemAdapter adapter = new PictureProductHomeItemAdapter(getActivity(), R.layout.picture_product_item_home,products);
                productListView.setAdapter(adapter);
                final SwipeRefreshLayout mSwipeRefreshLayout= (SwipeRefreshLayout)
                        mainView.findViewById(R.id.swipe_refresh_layout);
                //refresh grid view
                mSwipeRefreshLayout.setEnabled(false);
                productListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        boolean enable = false;
                        if (productListView != null && productListView.getChildCount() > 0) {
                            // check if the first item of the list is visible
                            boolean firstItemVisible = productListView.getFirstVisiblePosition() == 0;
                            // check if the top of the first item is visible
                            boolean topOfFirstItemVisible = productListView.getChildAt(0).getTop() == 0;
                            // enabling or disabling the refresh layout
                            enable = firstItemVisible && topOfFirstItemVisible;
                        }
                        mSwipeRefreshLayout.setEnabled(enable);

                    }
                });
                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sch.refreshProduct();
                                products = sch.getSpecialProduct();
                                PictureProductHomeItemAdapter newAdapter = new PictureProductHomeItemAdapter(getActivity(), R.layout.picture_product_item_home,products);
                                productListView.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }, 5000);
                    }
                });

                break;
            }
            default:{

                final String pageName;
                if(position==1)
                    pageName=second_page;
                else if (position==2)
                    pageName=third_page;
                else
                    pageName=fourth_page;

                //Show All product for Tab Selected
                products=sch.ProductOfMainCategory(pageName);
                mainView=inflater.inflate(R.layout.fragment_shop, null);
                final GridView gridview = (GridView) mainView.findViewById(R.id.gv_infoProduct);
                final PictureProductShopItemAdapter adapter=new  PictureProductShopItemAdapter(getActivity(),products);
                gridview.setAdapter(adapter);
                //refresh grid view
                final SwipeRefreshLayout mSwipeRefreshLayout= (SwipeRefreshLayout)
                        mainView.findViewById(R.id.swipe_refresh_layout);
                mSwipeRefreshLayout.setEnabled(false);
                gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        boolean enable = false;
                        if (gridview != null && gridview.getChildCount() > 0) {
                            boolean firstItemVisible = gridview.getFirstVisiblePosition() == 0;
                            boolean topOfFirstItemVisible = gridview.getChildAt(0).getTop() == 0;
                            enable = firstItemVisible && topOfFirstItemVisible;
                        }
                        mSwipeRefreshLayout.setEnabled(enable);
                    }
                });
               mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                   @Override
                   public void onRefresh() {
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               ArrayList<Product> newProduct=sch.getProductAfterRefresh(pageName,
                                       txtFilterCategorySelected.getText().toString(),
                                       txtFilterOptionProductSelected.getText().toString(),
                                       DataFilter.FilterOption);
                               PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProduct);
                               gridview.setAdapter(newAdapter);
                               newAdapter.notifyDataSetChanged();
                               mSwipeRefreshLayout.setRefreshing(false);
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
                        gridview.setSelection(ObserverLike.getLikeStatus());
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
                        txtFilterOptionProductSelected.setText(getResources().getText(R.string.all));
                        txtFilterOptionProductSelected.setTextColor(getResources().getColor(R.color.black));
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
                                ArrayList<Product> newProducts = sch.getProductsAfterFilterCategory(txtFilterCategorySelected.getText().toString());
                                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                                gridview.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
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
                        txtFilterCategorySelected.setText(getResources().getText(R.string.all));
                        txtFilterCategorySelected.setTextColor(getResources().getColor(R.color.black));
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
                                ArrayList<Product> newProducts = sch.getProductAsPriceFilter(products,txtFilterOptionProductSelected.getText().toString());
                                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                                gridview.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
                            }
                        });
                        ObserverFilterBrand.changeFilterBrandListener(new ObserverFilterBrandListener() {
                            @Override
                            public void changeFilterBrand() {
                                txtFilterOptionProductSelected.setText(DataFilter.FilterBrand);
                                txtFilterOptionProductSelected.setTextColor(getResources().getColor(R.color.red));
                                ArrayList<Product> newProducts = sch.getAllProductOfABrand(products, DataFilter.FilterBrand);
                                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                                gridview.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                });
                break;
            }
            case 4: {

                mainView = inflater.inflate(R.layout.fragment_article, null);
                final ListView articleList = (ListView) mainView.findViewById(R.id.lv_article);

                final int[] startItem = {0};
                final int[] endItem = new int[1];
                if (articles.size()>25)
                    endItem[0] =25;
                else
                    endItem[0] =articles.size();
                ArrayList<Article> customArticles=new ArrayList<Article>();
                for(int i= startItem[0];i< endItem[0];i++){
                    customArticles.add(articles.get(i));

                }
                final ArticleItemAdapter adapter = new ArticleItemAdapter(getActivity(),
                        R.layout.article_item, customArticles);
                articleList.setAdapter(adapter);
                final View finalMainView = mainView;
                articleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(articles.get(position).getLinkInWebsite()));
                        finalMainView.getContext().startActivity(intent);
                    }
                });

                final SwipeRefreshLayout srlArticles=(SwipeRefreshLayout)mainView.findViewById(R.id.swipe_refresh_layout);
                srlArticles.setEnabled(false);
                srlArticles.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sch.refreshArticles();
                                articles = sch.getAllArticlesFromTable();
                                ArrayList<Article> helpArticlesShow=new ArrayList<Article>();
                                for (int i = 0; i < 25; i++) {
                                    helpArticlesShow.add(articles.get(i));

                                }
                                ArticleItemAdapter newAdapter = new ArticleItemAdapter(getActivity(),
                                        R.layout.article_item, helpArticlesShow);
                                articleList.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
                                srlArticles.setRefreshing(false);
                            }
                        }, 5000);
                    }
                });

                articleList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }

                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        boolean enable = false;
                        if (articleList != null && articleList.getChildCount() > 0) {
                            // check if the first item of the list is visible
                            boolean firstItemVisible = articleList.getFirstVisiblePosition() == 0;
                            // check if the top of the first item is visible
                            boolean topOfFirstItemVisible = articleList.getChildAt(0).getTop() == 0;
                            // enabling or disabling the refresh layout
                            enable = firstItemVisible && topOfFirstItemVisible;
                        }
                        srlArticles.setEnabled(enable);

                        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && endItem[0] < articles.size()) {
                            ArrayList<Article> customArticles = new ArrayList<Article>();
                            startItem[0] = endItem[0];
                            if (endItem[0] + 25 < articles.size())
                                endItem[0] = endItem[0] + 25;
                            else
                                endItem[0] = articles.size();
                            for (int i = startItem[0]; i < endItem[0] - 1; i++) {
                                customArticles.add(articles.get(i));
                                adapter.add(articles.get(i));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            }

        }
        return mainView;
    }
}