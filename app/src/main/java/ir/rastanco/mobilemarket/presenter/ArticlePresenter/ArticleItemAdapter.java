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
    private Drawable defaultPicture=null;
    private String articleImageURL;

    public ArticleItemAdapter(Context context, ArrayList<Article> allArticles) {
        super(context, R.layout.article_item, allArticles);
        myContext=(Activity)context;
        articles=allArticles;
        if (defaultPicture==null)
            defaultPicture= Utilities.getInstance().ResizeImage(R.drawable.loadingholder, myContext, Configuration.getConfig().articleDisplaySizeForShow);
    }

    static class ViewHolder{
        private ImageView articleImage;
        private TextView articleTitle;
        private ImageLoader imgLoader;

    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if (convertView==null){
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView  = inflater.inflate(R.layout.article_item, parent,false);
            holder=new ViewHolder();
            holder.articleImage = (ImageView) convertView.findViewById(R.id.img_article);
            holder.articleImage.getLayoutParams().width=Configuration.getConfig().articleDisplaySizeForShow;
            holder.articleImage.getLayoutParams().height=Configuration.getConfig().articleDisplaySizeForShow;
            holder.articleTitle=(TextView)convertView.findViewById(R.id.txt_titleArticle);
            holder.imgLoader = new ImageLoader(getContext()); // important

            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        holder.articleImage.setImageDrawable(defaultPicture);
        articleImageURL= Link.getInstance().generateURLForGetArticleImage(articles.get(position).getImageLink());
        holder.imgLoader.DisplayImage(articleImageURL, holder.articleImage);
        holder.articleTitle.setText(articles.get(position).getTitle());
        return convertView;
    }

}

