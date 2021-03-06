package ir.rastanco.mobilemarket.presenter.ArticlePresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOK;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOKListener;
import ir.rastanco.mobilemarket.presenter.WebView;
import ir.rastanco.mobilemarket.utility.ColorUtility;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.Utilities;

/**
 * Created by ShaisteS on 1394/10/26.
 * This Class is for Displaying Articles Information
 */
public class ArticleFragment extends Fragment {

    private ServerConnectionHandler sch;
    private ArrayList<Article> articles;
    private int leastArticleNumberInFirstTime;
    private int startArticleNumber;
    private SwipeRefreshLayout srlArticles;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            this.activity= (FragmentActivity) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch = ServerConnectionHandler.getInstance(getContext());
        leastArticleNumberInFirstTime = Utilities.getInstance().getAtLeastArticleInFirstTime();
        startArticleNumber = Utilities.getInstance().getStartArticleNumber();

        articles = new ArrayList<>();

        if (sch.emptyDBArticle()) {
            String url = Link.getInstance().generateURLForGetArticle(startArticleNumber, leastArticleNumberInFirstTime);
            articles = sch.getAllArticlesAndNewsURL(url);
            try {
                sch.addAllArticlesToTable(articles);
            }
            catch (Exception e){
                Log.d("add article","error in article add function");

            }
        }

        ObserverConnectionInternetOK.ObserverConnectionInternetOKListener(new ObserverConnectionInternetOKListener() {
            @Override
            public void connectionOK() {

                if (sch.emptyDBArticle()) {
                    String url = Link.getInstance().generateURLForGetArticle(startArticleNumber, leastArticleNumberInFirstTime);
                    articles = sch.getAllArticlesAndNewsURL(url);
                    sch.addAllArticlesToTable(articles);
                }
            }
        });

        View mainView = inflater.inflate(R.layout.fragment_article, container, false);
        articles = sch.getAllArticlesFromTable();
        final ListView articleList = (ListView) mainView.findViewById(R.id.lv_article);
        final int[] startItem = {0};
        final int[] endItem = new int[1];
        if (articles.size() > leastArticleNumberInFirstTime)
            endItem[0] = leastArticleNumberInFirstTime;
        else
            endItem[0] = articles.size();
        ArrayList<Article> customArticles = new ArrayList<>();
        for (int i = startItem[0]; i < endItem[0]; i++) {
            customArticles.add(articles.get(i));

        }
        final ArticleItemAdapter adapter = new ArticleItemAdapter(activity,
                customArticles);
        articleList.setAdapter(adapter);
        final View finalMainView = mainView;
        articleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), WebView.class);
                intent.putExtra("url",articles.get(position).getLinkInWebsite());
                finalMainView.getContext().startActivity(intent);
            }
        });

        srlArticles = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);
        ColorUtility.getConfig().setColorOfSwipeRefresh(srlArticles);
        srlArticles.setEnabled(false);
        srlArticles.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sch.refreshArticles();
                        articles = sch.getAllArticlesFromTable();
                        ArrayList<Article> helpArticlesShow = new ArrayList<>();
                        for (int i = 0; i < leastArticleNumberInFirstTime; i++) {
                            helpArticlesShow.add(articles.get(i));

                        }
                        ArticleItemAdapter newAdapter = new ArticleItemAdapter(activity,
                                helpArticlesShow);
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
                if (articleList.getChildCount() > 0) {
                    // check if the first slider_items of the list is visible
                    boolean firstItemVisible = articleList.getFirstVisiblePosition() == 0;
                    // check if the top of the first slider_items is visible
                    boolean topOfFirstItemVisible = articleList.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                   // if (enable)
                     //   Configuration.getConfig().customerSupportFloatingActionButton.setVisibility(View.VISIBLE);
                    //else
                      //  Configuration.getConfig().customerSupportFloatingActionButton.setVisibility(View.GONE);

                }
                srlArticles.setEnabled(enable);

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && endItem[0] < articles.size()) {
                    ArrayList<Article> customArticles = new ArrayList<>();
                    startItem[0] = endItem[0];
                    if (endItem[0] + leastArticleNumberInFirstTime < articles.size())
                        endItem[0] = endItem[0] + leastArticleNumberInFirstTime;
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
        return mainView;
    }

}
