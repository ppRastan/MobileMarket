package ir.rastanco.mobilemarket.presenter;

/**
 * Created by Samaneh on 12/20/2015.
 */

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.ArticlePresenter.ArticleItemAdapter;
import ir.rastanco.mobilemarket.presenter.homePresenter.PictureProductHomeItemAdapter;
import ir.rastanco.mobilemarket.presenter.shopPresenter.PictureProductShopItemAdapter;
import ir.rastanco.mobilemarket.utility.Configuration;

public class SuperAwesomeCardFragment extends Fragment{

    private static final String ARG_POSITION = "position";
    private int position;
    private ServerConnectionHandler sch;
    private ArrayList<Product> products;
    private ArrayList<Article> articles;
    private Button btnCategory;
    private Button btnSubGroup;
    private Dialog dialogGroup;
    private Dialog dialogSubGroup;
    private ImageButton btnResetAlertDialog;
    private ImageButton btnCancelAlertDialog;
    private ImageButton btnResetSubGroup;
    private ImageButton btnCancleSubGroup;
    private TextView subGroupTextView;
    private TextView groupTextView;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView=null;

        position = getArguments().getInt(ARG_POSITION);
        Configuration.superACFragment=getContext();

        sch=new ServerConnectionHandler(Configuration.superACFragment);
        products=new ArrayList<Product>();
        articles=new ArrayList<Article>();
        articles=sch.getAllArticlesFromTable();


        switch (position) {

            case 0: {

                products=sch.getAllProductFromTable();
                mainView = inflater.inflate(R.layout.fragment_home, null);
                ListView productListView = (ListView) mainView.findViewById(R.id.lstv_picProduct);
                PictureProductHomeItemAdapter adapter = new PictureProductHomeItemAdapter(getActivity(), R.layout.picture_product_item_home,products);
                productListView.setAdapter(adapter);
                break;
            }
            case 1:{
                products=sch.ProductOfMainCategory("مبلمان منزل");
                mainView=inflater.inflate(R.layout.fragment_shop, null);
                final GridView gridview = (GridView) mainView.findViewById(R.id.gv_infoProduct);
                final PictureProductShopItemAdapter adapter=new  PictureProductShopItemAdapter(getActivity(),products);
                gridview.setAdapter(adapter);
                final SwipeRefreshLayout mSwipeRefreshLayout= (SwipeRefreshLayout)
                        mainView.findViewById(R.id.swipe_refresh_layout);
                //refresh grid view
                mSwipeRefreshLayout.setEnabled(false);
                gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        boolean enable = false;
                        if (gridview != null && gridview.getChildCount() > 0) {
                            // check if the first item of the list is visible
                            boolean firstItemVisible = gridview.getFirstVisiblePosition() == 0;
                            // check if the top of the first item is visible
                            boolean topOfFirstItemVisible = gridview.getChildAt(0).getTop() == 0;
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
                               products=sch.ProductOfMainCategory("مبلمان منزل");
                               PictureProductShopItemAdapter newAdapter=new  PictureProductShopItemAdapter(getActivity(),products);
                               gridview.setAdapter(newAdapter);
                               newAdapter.notifyDataSetChanged();
                               mSwipeRefreshLayout.setRefreshing(false);
                           }
                       }, 5000);
                   }
               });

                //Filter
                ///FilterSubCategory
                final Map<Integer, String> filterSubCategory = sch.getFilterSubCategory("مبلمان منزل");
                final ArrayList<String> subCategoryTitle = new ArrayList<String>();
                for (Map.Entry<Integer, String> entry : filterSubCategory.entrySet()) {
                    subCategoryTitle.add(entry.getValue());
                }
                final String[] categorySelected = new String[1];
                categorySelected[0]=getActivity().getString(R.string.all);
                final int[] categoryIdSelected = new int[1];
                categoryIdSelected[0]=sch.getMainCategoryId("مبلمان منزل");

                groupTextView = (TextView)mainView.findViewById(R.id.group_dialog_text);
                btnCategory=(Button)mainView.findViewById(R.id.group_dialog);
                btnCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGroup = new Dialog(getActivity());
                        dialogGroup.setContentView(R.layout.title_alertdialog_for_group);
                        btnCancelAlertDialog = (ImageButton) dialogGroup.findViewById(R.id.cancel);
                        btnResetAlertDialog = (ImageButton)dialogGroup.findViewById(R.id.reset_action);
                        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGroup.dismiss();
                            }
                        });
                        btnResetAlertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                categorySelected[0]=getActivity().getString(R.string.all);
                                groupTextView.setText(categorySelected[0]);
                                dialogGroup.dismiss();

                            }
                        });
                        TextView text = (TextView) dialogGroup.findViewById(R.id.title_alertdialog_group);

                        final ListView listCategory = (ListView) dialogGroup.findViewById(R.id.list);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                        listCategory.setAdapter(adapter);

                        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                categorySelected[0] = (String) parent.getItemAtPosition(position);
                                dialogGroup.dismiss();
                                groupTextView.setText(categorySelected[0]);
                                int subCategoryId = 0;
                                for (Map.Entry<Integer, String> entry : filterSubCategory.entrySet()) {
                                    if (entry.getValue().equals(categorySelected[0]))
                                        subCategoryId=entry.getKey();
                                }
                                products=sch.ProductOFASubCategory(subCategoryId);
                                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), products);
                                gridview.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();




                            }
                        });
                        dialogGroup.setCancelable(true);
                        dialogGroup.show();
                    }
                });
                break;
            }
            case 2:{
                products=sch.ProductOfMainCategory("کالای خواب");
                mainView=inflater.inflate(R.layout.fragment_shop, null);
                final GridView gridview = (GridView) mainView.findViewById(R.id.gv_infoProduct);
                final PictureProductShopItemAdapter adapter=new  PictureProductShopItemAdapter(getActivity(),products);
                gridview.setAdapter(adapter);
                final SwipeRefreshLayout mSwipeRefreshLayout= (SwipeRefreshLayout)
                        mainView.findViewById(R.id.swipe_refresh_layout);
                //refresh grid view
                mSwipeRefreshLayout.setEnabled(false);
                gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        boolean enable = false;
                        if (gridview != null && gridview.getChildCount() > 0) {
                            // check if the first item of the list is visible
                            boolean firstItemVisible = gridview.getFirstVisiblePosition() == 0;
                            // check if the top of the first item is visible
                            boolean topOfFirstItemVisible = gridview.getChildAt(0).getTop() == 0;
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
                                products=sch.ProductOfMainCategory("کالای خواب");
                                PictureProductShopItemAdapter newAdapter=new  PictureProductShopItemAdapter(getActivity(),products);
                                gridview.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }, 5000);
                    }
                });
                //Filter
                ///FilterSubCategory
                final Map<Integer, String> filterSubCategory = sch.getFilterSubCategory("کالای خواب");
                final ArrayList<String> subCategoryTitle = new ArrayList<String>();
                for (Map.Entry<Integer, String> entry : filterSubCategory.entrySet()) {
                    subCategoryTitle.add(entry.getValue());
                }
                final String[] categorySelected = new String[1];
                categorySelected[0]=getActivity().getString(R.string.all);
                final int[] categoryIdSelected = new int[1];
                categoryIdSelected[0]=sch.getMainCategoryId("کالای خواب");

                groupTextView = (TextView)mainView.findViewById(R.id.group_dialog_text);
                btnCategory=(Button)mainView.findViewById(R.id.group_dialog);
                btnCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGroup = new Dialog(getActivity());
                        dialogGroup.setContentView(R.layout.title_alertdialog_for_group);
                        btnCancelAlertDialog = (ImageButton) dialogGroup.findViewById(R.id.cancel);
                        btnResetAlertDialog = (ImageButton)dialogGroup.findViewById(R.id.reset_action);
                        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGroup.dismiss();
                            }
                        });
                        btnResetAlertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                categorySelected[0]=getActivity().getString(R.string.all);
                                groupTextView.setText(categorySelected[0]);
                                dialogGroup.dismiss();

                            }
                        });
                        TextView text = (TextView) dialogGroup.findViewById(R.id.title_alertdialog_group);

                        final ListView listCategory = (ListView) dialogGroup.findViewById(R.id.list);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                        listCategory.setAdapter(adapter);

                        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                categorySelected[0] = (String) parent.getItemAtPosition(position);
                                dialogGroup.dismiss();
                                groupTextView.setText(categorySelected[0]);
                                int subCategoryId = 0;
                                for (Map.Entry<Integer, String> entry : filterSubCategory.entrySet()) {
                                    if (entry.getValue().equals(categorySelected[0]))
                                        subCategoryId=entry.getKey();
                                }
                                products=sch.ProductOFASubCategory(subCategoryId);
                                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), products);
                                gridview.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
                            }
                        });
                        dialogGroup.setCancelable(true);
                        dialogGroup.show();
                    }
                });
                break;

            }

            case 3:{

                products=sch.ProductOfMainCategory("نوزاد و نوجوان");
                mainView=inflater.inflate(R.layout.fragment_shop, null);
                final GridView gridview = (GridView) mainView.findViewById(R.id.gv_infoProduct);
                final PictureProductShopItemAdapter adapter=new  PictureProductShopItemAdapter(getActivity(),products);
                gridview.setAdapter(adapter);
                final SwipeRefreshLayout mSwipeRefreshLayout= (SwipeRefreshLayout)
                        mainView.findViewById(R.id.swipe_refresh_layout);
                //refresh grid view
                mSwipeRefreshLayout.setEnabled(false);
                gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        boolean enable = false;
                        if (gridview != null && gridview.getChildCount() > 0) {
                            // check if the first item of the list is visible
                            boolean firstItemVisible = gridview.getFirstVisiblePosition() == 0;
                            // check if the top of the first item is visible
                            boolean topOfFirstItemVisible = gridview.getChildAt(0).getTop() == 0;
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
                                products=sch.ProductOfMainCategory("نوزاد و نوجوان");
                                PictureProductShopItemAdapter newAdapter=new  PictureProductShopItemAdapter(getActivity(),products);
                                gridview.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }, 5000);
                    }
                });
                //Filter
                ///FilterSubCategory
                final Map<Integer, String> filterSubCategory = sch.getFilterSubCategory("نوزاد و نوجوان");
                final ArrayList<String> subCategoryTitle = new ArrayList<String>();
                for (Map.Entry<Integer, String> entry : filterSubCategory.entrySet()) {
                    subCategoryTitle.add(entry.getValue());
                }
                final String[] categorySelected = new String[1];
                categorySelected[0]=getActivity().getString(R.string.all);
                final int[] categoryIdSelected = new int[1];
                categoryIdSelected[0]=sch.getMainCategoryId("نوزاد و نوجوان");

                groupTextView = (TextView)mainView.findViewById(R.id.group_dialog_text);
                btnCategory=(Button)mainView.findViewById(R.id.group_dialog);
                btnCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGroup = new Dialog(getActivity());
                        dialogGroup.setContentView(R.layout.title_alertdialog_for_group);
                        btnCancelAlertDialog = (ImageButton) dialogGroup.findViewById(R.id.cancel);
                        btnResetAlertDialog = (ImageButton)dialogGroup.findViewById(R.id.reset_action);
                        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGroup.dismiss();
                            }
                        });
                        btnResetAlertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                categorySelected[0]=getActivity().getString(R.string.all);
                                groupTextView.setText(categorySelected[0]);
                                dialogGroup.dismiss();

                            }
                        });
                        TextView text = (TextView) dialogGroup.findViewById(R.id.title_alertdialog_group);

                        final ListView listCategory = (ListView) dialogGroup.findViewById(R.id.list);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                        listCategory.setAdapter(adapter);

                        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                categorySelected[0] = (String) parent.getItemAtPosition(position);
                                dialogGroup.dismiss();
                                groupTextView.setText(categorySelected[0]);
                                int subCategoryId = 0;
                                for (Map.Entry<Integer, String> entry : filterSubCategory.entrySet()) {
                                    if (entry.getValue().equals(categorySelected[0]))
                                        subCategoryId=entry.getKey();
                                }
                                products=sch.ProductOFASubCategory(subCategoryId);
                                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), products);
                                gridview.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
                            }
                        });
                        dialogGroup.setCancelable(true);
                        dialogGroup.show();
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