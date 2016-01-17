package ir.rastanco.mobilemarket.dataModel.serverConnectionModel;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.GetJsonFile;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonArticles;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonCategory;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProduct;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProductOption;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.dataBaseConnectionModel.DataBaseHandler;

/**
 * Created by ShaisteS on 1394/10/14.
 * This Class Manage All Connection to Server and DataBase
 */
public class ServerConnectionHandler {

    private Context context;
    private DataBaseHandler dbh;

    public ServerConnectionHandler(Context myContext){
        context=myContext;
        dbh=new DataBaseHandler(myContext);
    }

    public Boolean emptyDBProduct(){
        Boolean empty=dbh.emptyProductTable();
        return empty;
    }
    public Boolean emptyDBCategory(){
        Boolean empty=dbh.emptyCategoryTable();
        return empty;
    }

    public Boolean emptyDBArticle(){
        Boolean empty=dbh.emptyArticleTable();
        return empty;
    }

    public void addAllCategoryToTable(ArrayList<Category> allCategories){
        for (int i=0;i<allCategories.size();i++){
            dbh.insertACategory(allCategories.get(i));
        }
    }
    public void addAllProductToTable(ArrayList<Product> allProducts){
        for (int i=0;i<allProducts.size();i++){
            dbh.insertAProduct(allProducts.get(i));
        }
    }
    public void addAllArticlesToTable(ArrayList<Article> allArticles){
        for (int i=0;i<allArticles.size();i++)
            dbh.insertArticle(allArticles.get(i));
    }

    public ArrayList<Category> getAllCategoryInfoTable(){

        return dbh.selectAllCategory();
    }
    public ArrayList<Product> getAllProductFromTable(){
        return dbh.selectAllProduct();
    }
    public ArrayList<Article> getAllArticlesFromTable(){
        return dbh.selectAllArticle();
    }


    public ArrayList<Category> getAllCategoryInfoURL(String url){

        GetJsonFile jParserCategory = new GetJsonFile();
        String jsonCategory= null;
        try {
            jsonCategory = jParserCategory.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<Category> allCategoryInfo=new ArrayList<Category>();
        allCategoryInfo=new ParseJsonCategory().getAllCategory(jsonCategory);

        return allCategoryInfo;

    }
    public ArrayList<Product> getAllProductInfoACategoryURL(String url){

        GetJsonFile jParserProduct = new GetJsonFile();
        String jsonProduct= null;
        try {
            jsonProduct = jParserProduct.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<Product> ProductInfo=new ArrayList<Product>();
        ProductInfo=new ParseJsonProduct().getAllProduct(jsonProduct);
        return  ProductInfo;
    }
    public ArrayList<Article> getAllArticlesAndNewsURL(String url){

        GetJsonFile g=new GetJsonFile();
        String articlesInfo=null;
        try {
            articlesInfo=g.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ParseJsonArticles a=new ParseJsonArticles();
        return a.getAllProductOptions(articlesInfo);
    }

    public ArrayList<ProductOption> getOptionsOfAProduct(String url){

        GetJsonFile optionJson= new GetJsonFile();
        String productInfoJson=null;
        try {
            productInfoJson=optionJson.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ParseJsonProductOption p=new  ParseJsonProductOption();
        return p.getAllProductOptions(productInfoJson);
    }


}
