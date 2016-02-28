package ir.rastanco.mobilemarket.presenter.ArticlePresenter;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;

/**
 * Created by ShaisteS on 1/16/2016.
 */
public class ArticleFragment extends Fragment {

    private TextView articleText;
    private Button seeMoreBtn;

    private ServerConnectionHandler sch;
    private ArrayList<Article> articles;
    private View mainView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch=new ServerConnectionHandler(getContext());
        articles=new ArrayList<Article>();

        if (sch.emptyDBArticle()){
            articles=sch.getAllArticlesAndNewsURL("http://decoriss.com/json/get,com=news&name=blog&order=desc&limit=0-25&cache=false");
            sch.addAllArticlesToTable(articles);
        }

        mainView = inflater.inflate(R.layout.fragment_article, null);
        articles=sch.getAllArticlesFromTable();
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

        return mainView;


    }

}
