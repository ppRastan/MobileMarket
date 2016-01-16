package ir.rastanco.mobilemarket.presenter.ArticlePresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache.ImageLoader;
import ir.rastanco.mobilemarket.presenter.ProductInfoPresenter.ProductInfoActivity;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 21/10/94
 */
public class ArticleItemAdapter extends ArrayAdapter<Article>{

    private Activity myContext;
    private ArrayList<Article> articles ;

    public ArticleItemAdapter(Context context,int resource, ArrayList<Article> allArticles) {
        super(context, resource,allArticles);
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
        /*try {
            articleImageURL= URLEncoder.encode(articleImageURL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        imgLoader.DisplayImage(articleImageURL, articleImage);

        TextView articleTitle=(TextView)rowView.findViewById(R.id.txt_titleArticle);
        articleTitle.setText(articles.get(position).getTitle());

        /*articleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("thisProduct", allProduct.get(position));
                Intent intent = new Intent(rowView.getContext(), ProductInfoActivity.class);
                intent.putExtras(bundle);
                rowView.getContext().startActivity(intent);
            }
        });*/
        return rowView;
    }
}
