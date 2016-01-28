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
import ir.rastanco.mobilemarket.presenter.photoPresenter.PictureProductPhotoItemAdapter;
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
        products=sch.getAllProductFromTable();
        articles=sch.getAllArticlesFromTable();


        switch (position) {
            case 0: {

                mainView = inflater.inflate(R.layout.fragment_home, null);
                ListView productListView = (ListView) mainView.findViewById(R.id.lstv_picProduct);
                PictureProductHomeItemAdapter adapter = new PictureProductHomeItemAdapter(getActivity(), R.layout.picture_product_item_home,products);
                productListView.setAdapter(adapter);
                break;
            }
            case 1: {
                mainView = inflater.inflate(R.layout.fragment_photo, null);
                final GridView gridview = (GridView) mainView.findViewById(R.id.gv_photoProduct);
                PictureProductPhotoItemAdapter adapter= new PictureProductPhotoItemAdapter(getActivity(),products);
                gridview.setAdapter(adapter);
                final SwipeRefreshLayout mSwipeRefreshLayout= (SwipeRefreshLayout)
                        mainView.findViewById(R.id.swipe_refresh_layout);
                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sch.refreshProduct();
                                products = sch.getAllProductFromTable();
                                PictureProductShopItemAdapter newAdapter = new
                                        PictureProductShopItemAdapter(getActivity(), products);
                                gridview.setAdapter(newAdapter);
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }, 5000);
                    }
                });
                break;
            }

            case 2:{

                mainView=inflater.inflate(R.layout.fragment_shop,null);
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
                               products=sch.getAllProductFromTable();
                               PictureProductShopItemAdapter newAdapter=new  PictureProductShopItemAdapter(getActivity(),products);
                               gridview.setAdapter(newAdapter);
                               newAdapter.notifyDataSetChanged();
                               mSwipeRefreshLayout.setRefreshing(false);
                           }
                       }, 5000);
                   }
               });

                //Filter Category
                final Map<Integer, String> filterCategory = sch.filterCategories();
                final ArrayList<String> categoryTitle = new ArrayList<String>();
                for (Map.Entry<Integer, String> entry : filterCategory.entrySet()) {
                    categoryTitle.add(entry.getValue());
                }
                final String[] categorySelected = new String[1];
                categorySelected[0]=getActivity().getString(R.string.all);

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
                                android.R.layout.simple_list_item_1, android.R.id.text1, categoryTitle);
                        listCategory.setAdapter(adapter);

                        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                categorySelected[0] = (String) parent.getItemAtPosition(position);
                                dialogGroup.dismiss();
                                groupTextView.setText(categorySelected[0]);
                            }
                        });
                        dialogGroup.setCancelable(true);
                        dialogGroup.show();
                    }
                });

                //Filter Subcategory
                final String[] subCategorySelected = new String[1];
                subCategorySelected[0]=getActivity().getString(R.string.all);
                subGroupTextView = (TextView)mainView.findViewById(R.id.acordingto_dialog_text);
                btnSubGroup=(Button)mainView.findViewById(R.id.acording_to_dialog);
                btnSubGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSubGroup = new Dialog(getActivity());
                        dialogSubGroup.setContentView(R.layout.title_alertdialog_for_sub_group);
                        btnResetSubGroup = (ImageButton)dialogSubGroup.findViewById(R.id.reset_action_subgroup);
                        btnCancleSubGroup = (ImageButton)dialogSubGroup.findViewById(R.id.cancel_action_subgroup);
                        btnCancleSubGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogSubGroup.dismiss();
                            }
                        });
                        btnResetSubGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                subCategorySelected[0] = getActivity().getString(R.string.all);
                                dialogSubGroup.dismiss();
                                subGroupTextView.setText(categorySelected[0]);
                            }
                        });
                        TextView text = (TextView) dialogSubGroup.findViewById(R.id.title_alertdialog_group);
                        int subCategoryId=0;

                        for (Map.Entry<Integer, String> entry : filterCategory.entrySet()) {
                            if (entry.getValue().equals(groupTextView.getText()))
                                subCategoryId=entry.getKey();
                        }
                        final Map<Integer,String> subCategory=sch.filterSubCategory(subCategoryId);
                        ArrayList<String> subCategoryTitle=new ArrayList<String>();
                        for (Map.Entry<Integer, String> entry : subCategory.entrySet()) {
                            subCategoryTitle.add(entry.getValue());
                        }
                        final ListView listCategory = (ListView) dialogSubGroup.findViewById(R.id.list);
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                        listCategory.setAdapter(adapter);

                        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                subCategorySelected[0] = (String) parent.getItemAtPosition(position);
                                subGroupTextView.setText(subCategorySelected[0]);
                                int productIDFilter = 0;
                                for (Map.Entry<Integer, String> entry : subCategory.entrySet()) {
                                    if (entry.getValue().equals(subCategorySelected[0]))
                                        productIDFilter = entry.getKey();
                                    Log.d("filter", String.valueOf(productIDFilter));
                                }

                                Map<Integer, String> finalFilter = sch.filterSubCategory(productIDFilter);
                                ArrayList<Product> helpProduct = new ArrayList<Product>();
                                ArrayList<Product> finalFilterProduct = new ArrayList<Product>();

                                if (finalFilter.size() > 0) {
                                    for (Map.Entry<Integer, String> entry : finalFilter.entrySet()) {
                                        helpProduct = sch.getAllProductOfACategory(entry.getKey());
                                        for (int i = 0; i < helpProduct.size(); i++)
                                            finalFilterProduct.add(helpProduct.get(i));
                                    }
                                } else
                                    finalFilterProduct = sch.getAllProductOfACategory(productIDFilter);

                                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), finalFilterProduct);
                                gridview.setAdapter(newAdapter);
                                newAdapter.notifyDataSetChanged();
                                dialogSubGroup.dismiss();

                            }
                        });
                        dialogSubGroup.setCancelable(true);
                        dialogSubGroup.show();
                    }
                });
                break;
            }
            case 3: {

                mainView = inflater.inflate(R.layout.fragment_article_first, null);
                ListView articleList = (ListView) mainView.findViewById(R.id.lv_article);
                ArticleItemAdapter adapter = new ArticleItemAdapter(getActivity(), R.layout.article_item, articles);
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
                break;

            }
        }
        return mainView;
    }
}