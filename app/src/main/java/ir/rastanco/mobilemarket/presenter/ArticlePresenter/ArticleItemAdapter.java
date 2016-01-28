package ir.rastanco.mobilemarket.presenter.ArticlePresenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 21/10/94
 */
public class ArticleItemAdapter extends ArrayAdapter<Article>{

    private Activity myContext;
    private ArrayList<Article> articles ;

    public ArticleItemAdapter(Context context,int resource, ArrayList<Article> allArticles) {
        super(context, resource, allArticles);
        myContext=(Activity)context;
        articles=allArticles;
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        Bitmap image=null;
        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.article_item, null);


        ImageLoader imgLoader = new ImageLoader(Configuration.superACFragment); // important
        ImageView articleImage = (ImageView) rowView.findViewById(R.id.img_article);
        String articleImageURL= articles.get(position).getImageLink()+
                "&size="+
                Configuration.articleDisplaySize+"x"+Configuration.articleDisplaySize+
                "&q=30";
        imgLoader.DisplayImage(articleImageURL, articleImage);

        TextView articleTitle=(TextView)rowView.findViewById(R.id.txt_titleArticle);
        articleTitle.setText(articles.get(position).getTitle());
        return rowView;
    }

}

