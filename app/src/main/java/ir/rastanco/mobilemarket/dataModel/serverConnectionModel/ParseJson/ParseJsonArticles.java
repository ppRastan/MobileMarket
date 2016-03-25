package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Article;

/**
 * Created by ShaisteS on 1394/10/26.
 * This Class Parse Articles Information Json String
 */
public class ParseJsonArticles {

    private ArrayList<Article> articles;


    public ArrayList<Article> getAllProductOptions(String jsonString) {

        articles = new ArrayList<Article>();
        JSONArray dataJsonArr = null;

        try {

            JSONObject json =new JSONObject(jsonString);
            dataJsonArr = json.getJSONArray("news");
            for (int i = 0; i < dataJsonArr.length(); i++)
            {
                JSONObject c = dataJsonArr.getJSONObject(i);
                Article aArticle=new Article();
                aArticle.setTitle(c.getString("t"));
                //aArticle.setBrief(c.getString("b"));
                aArticle.setDate(c.getString("d"));
                aArticle.setLinkInWebsite(c.getString("l"));
                aArticle.setImageLink(c.getString("i"));
                articles.add(aArticle);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  articles;
    }
}
