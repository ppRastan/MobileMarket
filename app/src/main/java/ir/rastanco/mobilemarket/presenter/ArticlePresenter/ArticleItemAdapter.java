package ir.rastanco.mobilemarket.presenter.ArticlePresenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.Utilities;


/**
 * Created by ShaisteS on 1394/10/21
 * This class is for filling data in listView for displaying Articles information
 */

public class ArticleItemAdapter extends ArrayAdapter<Article>{

    private final Activity myContext;
    private final ArrayList<Article> articles ;
    private final Drawable defaultPicture;

    public ArticleItemAdapter(Context context, ArrayList<Article> allArticles) {
        super(context, R.layout.article_item, allArticles);
        myContext=(Activity)context;
        articles=allArticles;
        defaultPicture= Utilities.getInstance().ResizeImage(R.drawable.loadingholder, myContext, Configuration.getConfig().articleDisplaySizeForShow);
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.article_item, parent,false);
        ImageLoader imgLoader = new ImageLoader(getContext(), Configuration.getConfig().articleDisplaySizeForShow); // important
        ImageView articleImage = (ImageView) rowView.findViewById(R.id.img_article);
        articleImage.getLayoutParams().width=Configuration.getConfig().articleDisplaySizeForShow;
        articleImage.getLayoutParams().height=Configuration.getConfig().articleDisplaySizeForShow;
        articleImage.setImageDrawable(defaultPicture);
        String articleImageURL= Link.getInstance().generateURLForGetArticleImage(articles.get(position).getImageLink());
        imgLoader.DisplayImage(articleImageURL, articleImage);
        TextView articleTitle=(TextView)rowView.findViewById(R.id.txt_titleArticle);
        articleTitle.setText(articles.get(position).getTitle());
        return rowView;
    }

}

