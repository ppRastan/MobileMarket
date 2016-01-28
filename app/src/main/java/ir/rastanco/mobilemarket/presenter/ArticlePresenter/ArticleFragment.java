package ir.rastanco.mobilemarket.presenter.ArticlePresenter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ir.rastanco.mobilemarket.R;

/**
 * Created by ShaisteS on 1/16/2016.
 */
public class ArticleFragment extends Fragment {

    private TextView articleText;
    private Button seeMoreBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setFont();
        return inflater.inflate(R.layout.fragment_article_first, container, false);
    }

    private void setFont() {
        articleText = (TextView) getView().findViewById(R.id.txt_titleArticle);
        seeMoreBtn = (Button)getView().findViewById(R.id.btn_next);
    }
}
