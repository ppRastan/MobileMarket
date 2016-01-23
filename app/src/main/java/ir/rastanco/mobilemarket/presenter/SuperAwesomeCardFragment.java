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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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
    private ImageButton btnReset;
    private ImageButton btnCancelAlertDialog;
    private TextView subGroupTextView;
    private TextView groupTextView;

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
                               adapter.notifyDataSetChanged();
                               gridview.setAdapter(adapter);
                               mSwipeRefreshLayout.setRefreshing(false);
                           }
                       }, 5000);
                   }
               });
                subGroupTextView = (TextView)mainView.findViewById(R.id.acordingto_dialog_text);
                groupTextView = (TextView)mainView.findViewById(R.id.group_dialog_text);
                btnCategory=(Button)mainView.findViewById(R.id.group_dialog);
                btnCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGroup = new Dialog(getActivity());
                        dialogGroup.setContentView(R.layout.title_alertdialog_for_group);
                        btnCancelAlertDialog = (ImageButton)v.findViewById(R.id.cancel);
//                        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                           //     dialogSubGroup.dismiss();
//                            }
//                        });
//                        btnReset = (ImageButton)v.findViewById(R.id.reset_action);
//                        btnReset.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGroup.dismiss();
//                                groupTextView.setText(R.string.all);
//                                groupTextView.setBackgroundColor(Color.BLACK);
//                            }
//                        });
                        TextView text = (TextView) dialogGroup.findViewById(R.id.title_alertdialog_group);
                        text.setText(getActivity().getString(R.string.group));
                        dialogGroup.setCancelable(true);
                        dialogGroup.show();


                    }
                });

                btnSubGroup=(Button)mainView.findViewById(R.id.acording_to_dialog);
                btnSubGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSubGroup = new Dialog(getActivity());
                        dialogSubGroup.setContentView(R.layout.title_alertdialog_for_sub_group);
                        TextView text = (TextView) dialogSubGroup.findViewById(R.id.title_alertdialog_group);
                        text.setText(getActivity().getString(R.string.acording_to));
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