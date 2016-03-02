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

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.utility.Configuration;

import java.util.ArrayList;


/**
 * Created by ShaisteS on 21/10/94
 * This class is for filling data in listView for displaying Articles information
 */
public class ArticleItemAdapter extends ArrayAdapter<Article>{

    private Activity myContext;
    private ArrayList<Article> articles ;
    private ImageLoader imgLoader;

    public ArticleItemAdapter(Context context,int resource, ArrayList<Article> allArticles) {
        super(context, resource, allArticles);
        myContext=(Activity)context;
        articles=allArticles;
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        Bitmap image=null;
        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.article_item, null);


        imgLoader = new ImageLoader(getContext(),rowView, Configuration.articleDisplaySizeForShow); // important
        ImageView articleImage = (ImageView) rowView.findViewById(R.id.img_article);
        articleImage.getLayoutParams().width=Configuration.articleDisplaySizeForShow;
        articleImage.getLayoutParams().height=Configuration.articleDisplaySizeForShow;
        String articleImageURL= articles.get(position).getImageLink()+
                "&size="+
                Configuration.articleDisplaySizeForURL +"x"+Configuration.articleDisplaySizeForURL +
                "&q=30";
        imgLoader.DisplayImage(articleImageURL, articleImage);

        /*Glide.with(Configuration.superACFragment)
                .load(articleImageURL)
                        // The placeholder image is shown immediately and
                        // replaced by the remote image when Picasso has
                        // finished fetching it.
                .placeholder(R.drawable.loadingholder)
                        //A request will be retried three times before the error placeholder is shown.
                .error(R.drawable.loadingholder)
                        // Transform images to better fit into layouts and to
                        // reduce memory size.
                .into(articleImage);*/

        TextView articleTitle=(TextView)rowView.findViewById(R.id.txt_titleArticle);
        articleTitle.setText(articles.get(position).getTitle());
        return rowView;
    }

}

