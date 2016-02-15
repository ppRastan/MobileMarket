package ir.rastanco.mobilemarket.presenter;

/**
 * Created by Samaneh on 12/20/2015.
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private ArrayList<String> mainCategoryTitle;
    private String second_page;
    private String third_page;
    private String fourth_page;
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

                products=sch.ProductOfMainCategory(pageName);
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
                               /*for (int i=0;i<products.size();i++){
                                   sch.refreshProductOption(products.get(i).getGroupId(),products.get(i).getId());
                               }*/
                               products = sch.ProductOfMainCategory(pageName);
                               PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), products);
                               gridview.setAdapter(newAdapter);
                               newAdapter.notifyDataSetChanged();
                               mSwipeRefreshLayout.setRefreshing(false);
                           }
                       }, 5000);
                   }
               });
                groupTextView = (TextView)mainView.findViewById(R.id.group_dialog_text);
                ObserverHome.SimilarProductListener(new SimilarProductListener() {
                    @Override
                    public void SimilarProductSet() {
                        groupTextView.setText(sch.getACategoryTitle(ObserverHome.getSimilarProduct()));
                        ArrayList<Product> newProducts = sch.ProductOFASubCategory(ObserverHome.getSimilarProduct());
                        PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                        gridview.setAdapter(newAdapter);
                        newAdapter.notifyDataSetChanged();

                    }
                });

                ObserverLike.changeLikeStatusListener(new ChangeLikeListener() {
                    @Override
                    public void changeLikeStatus() {
                        gridview.setSelection(ObserverLike.getLikeStatus());
                        gridview.setAdapter(adapter);

                    }
                });

                //Filter
                ///FilterSubCategory
                final Map<Integer, String> filterSubCategory = sch.getFilterSubCategory(pageName);
                final String[] categorySelected = new String[1];
                categorySelected[0]=getActivity().getString(R.string.all);
                final int[] categoryIdSelected = new int[1];
                categoryIdSelected[0]=sch.getMainCategoryId(pageName);
                ArrayList<String> subCategoryTitle = new ArrayList<String>();
                subCategoryTitle=sch.getTitleOfChildOfACategory(categoryIdSelected[0]);
                //SubCategory
                btnCategory=(Button)mainView.findViewById(R.id.group_dialog);
                final ArrayList<String> finalSubCategoryTitle = subCategoryTitle;
                btnCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGroup = new Dialog(getActivity());
                        dialogGroup.setContentView(R.layout.title_alertdialog_for_group);
                        btnCancelAlertDialog = (ImageButton) dialogGroup.findViewById(R.id.cancel);
                        btnResetAlertDialog = (ImageButton)dialogGroup.findViewById(R.id.reset_action);
                        groupTextView.setTextColor(Color.parseColor("#EB4D2A"));
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
                                android.R.layout.simple_list_item_1, android.R.id.text1, finalSubCategoryTitle);
                        listCategory.setAdapter(adapter);
                        //childOfASubCategory
                        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                categorySelected[0] = (String) parent.getItemAtPosition(position);
                                final int[] subCategoryId = {0};
                                for (Map.Entry<Integer, String> entry : filterSubCategory.entrySet()) {
                                    if (entry.getValue().equals(categorySelected[0]))
                                        subCategoryId[0] = entry.getKey();
                                }

                                ArrayList<String> childOfASubcategory = new ArrayList<String>();
                                childOfASubcategory = sch.getTitleOfChildOfACategory(subCategoryId[0]);
                                //dialogBox for subCategory
                                final Dialog dialogSGroup = new Dialog(getActivity());
                                dialogSGroup.setContentView(R.layout.title_alertdialog_for_group);
                                btnCancelAlertDialog = (ImageButton) dialogSGroup.findViewById(R.id.cancel);
                                btnResetAlertDialog = (ImageButton) dialogSGroup.findViewById(R.id.reset_action);
                                groupTextView.setTextColor(Color.parseColor("#EB4D2A"));
                                btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogSGroup.dismiss();
                                    }
                                });
                                btnResetAlertDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        categorySelected[0] = getActivity().getString(R.string.all);
                                        groupTextView.setText(categorySelected[0]);
                                        dialogSGroup.dismiss();

                                    }
                                });
                                TextView text = (TextView) dialogSGroup.findViewById(R.id.title_alertdialog_group);
                                final ListView listSubCategory = (ListView) dialogSGroup.findViewById(R.id.list);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1, android.R.id.text1, childOfASubcategory);
                                listSubCategory.setAdapter(adapter);
                                final int[] subCategoryIdSelected = new int[1];
                                listSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String subCategorySelected = (String) parent.getItemAtPosition(position);
                                        groupTextView.setText(subCategorySelected);
                                        subCategoryIdSelected[0] = sch.getCategoryIdWithTitle(subCategorySelected);
                                        dialogGroup.dismiss();
                                        dialogSGroup.dismiss();
                                        ArrayList<Product> newProducts = sch.getproductOfACategory(subCategoryIdSelected[0]);
                                        PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                                        gridview.setAdapter(newAdapter);
                                        newAdapter.notifyDataSetChanged();
                                    }
                                });
                                dialogSGroup.show();
                            }
                        });
                        dialogGroup.show();
                        dialogGroup.setCancelable(true);
                    }
                });
                ///Filter in Product Features
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
                        subGroupTextView.setTextColor(Color.parseColor("#EB4D2A"));
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
                        ArrayList<String> subCategoryTitle=new ArrayList<String>();
                        subCategoryTitle.add("قیمت");
                        subCategoryTitle.add("برند");

                        final ListView listCategory = (ListView) dialogSubGroup.findViewById(R.id.list);
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                        listCategory.setAdapter(adapter);

                        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Filter Price
                                if(position==0)
                                {
                                    dialogSubGroup.dismiss();
                                    final Dialog dialogPrice = new Dialog(getActivity());
                                    dialogPrice.setContentView(R.layout.title_alertdialog_for_sub_group);
                                    ImageButton btnResetPrice = (ImageButton)dialogPrice.findViewById(R.id.reset_action_subgroup);
                                    ImageButton btnCanclePrice = (ImageButton)dialogPrice.findViewById(R.id.cancel_action_subgroup);
                                    btnCanclePrice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogPrice.dismiss();
                                        }
                                    });
                                    btnResetPrice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            subCategorySelected[0] = getActivity().getString(R.string.all);
                                            dialogPrice.dismiss();
                                            subGroupTextView.setText(categorySelected[0]);
                                        }
                                    });
                                    TextView text = (TextView) dialogPrice.findViewById(R.id.title_alertdialog_group);
                                    ArrayList<String> subCategoryTitle=new ArrayList<String>();
                                    subCategoryTitle.add(" تا سقف 1 میلیون تومان");
                                    subCategoryTitle.add("تا سقف 5 میلیون تومان");
                                    subCategoryTitle.add("تا سقف 10 میلیون تومان");
                                    subCategoryTitle.add("بالاتر از 10 میلیون تومان");

                                    final ListView listPrice = (ListView) dialogPrice.findViewById(R.id.list);
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                            android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                                    listPrice.setAdapter(adapter);
                                    listPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            final ArrayList<Product> productPrice = new ArrayList<Product>();
                                            if (position == 0) {
                                                for (int i = 0; i < products.size(); i++) {
                                                    if (products.get(i).getPrice() <= 1000000)
                                                        productPrice.add(products.get(i));
                                                }
                                                subGroupTextView.setText(listPrice.getItemAtPosition(position).toString());

                                            }
                                            if (position == 1) {
                                                for (int i = 0; i < products.size(); i++) {
                                                    if (products.get(i).getPrice() <= 5000000 && products.get(i).getPrice()>1000000)
                                                        productPrice.add(products.get(i));
                                                }
                                                subGroupTextView.setText(listPrice.getItemAtPosition(position).toString());

                                            }

                                            if (position == 2) {
                                                for (int i = 0; i < products.size(); i++) {
                                                    if (products.get(i).getPrice() <= 10000000 && products.get(i).getPrice()>5000000)
                                                        productPrice.add(products.get(i));
                                                }
                                                subGroupTextView.setText(listPrice.getItemAtPosition(position).toString());

                                            }
                                            if (position == 3) {
                                                for (int i = 0; i < products.size(); i++) {
                                                    if (products.get(i).getPrice() > 10000000)
                                                        productPrice.add(products.get(i));
                                                }
                                                subGroupTextView.setText(listPrice.getItemAtPosition(position).toString());

                                            }
                                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), productPrice);
                                            gridview.setAdapter(newAdapter);
                                            newAdapter.notifyDataSetChanged();
                                            dialogPrice.dismiss();
                                        }
                                    });
                                    dialogPrice.show();
                                }
                                //Filter Brand
                                if (position==1){
                                    dialogSubGroup.dismiss();
                                    final Dialog dialogBrand = new Dialog(getActivity());
                                    dialogBrand.setContentView(R.layout.title_alertdialog_for_sub_group);
                                    ImageButton btnResetPrice = (ImageButton)dialogBrand.findViewById(R.id.reset_action_subgroup);
                                    ImageButton btnCanclePrice = (ImageButton)dialogBrand.findViewById(R.id.cancel_action_subgroup);
                                    btnCanclePrice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBrand.dismiss();
                                        }
                                    });
                                    btnResetPrice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            subCategorySelected[0] = getActivity().getString(R.string.all);
                                            dialogBrand.dismiss();
                                            subGroupTextView.setText(categorySelected[0]);
                                        }
                                    });
                                    TextView text = (TextView) dialogBrand.findViewById(R.id.title_alertdialog_group);
                                    ArrayList<String> subCategoryTitle=new ArrayList<String>();
                                    subCategoryTitle=sch.getAllBrands(products);
                                    final ListView listBrand = (ListView) dialogBrand.findViewById(R.id.list);
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                            android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                                    listBrand.setAdapter(adapter);
                                    listBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String brandTile=(String)parent.getItemAtPosition(position);
                                            subGroupTextView.setText(brandTile);
                                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(),
                                                    sch.getAllProductOfABrand(products,brandTile));
                                            gridview.setAdapter(newAdapter);
                                            newAdapter.notifyDataSetChanged();
                                            dialogBrand.dismiss();
                                        }
                                    });
                                    dialogBrand.show();
                                }
                            }
                        });
                        dialogSubGroup.setCancelable(true);
                        dialogSubGroup.show();
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