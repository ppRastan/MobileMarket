package ir.rastanco.mobilemarket.dataModel.serverConnectionModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Categories;
import ir.rastanco.mobilemarket.dataModel.ParseJsonArticles;
import ir.rastanco.mobilemarket.dataModel.ParseJsonProductOption;
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

    public ArrayList<Categories> getAllCategoryInfo(String url){

        GetJsonFile jParserCategory = new GetJsonFile();
        String jsonCategory= null;
        try {
            jsonCategory = jParserCategory.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<Categories> allCategoryInfo=new ArrayList<Categories>();
        allCategoryInfo=new ParseJsonCategory().getAllCategory(jsonCategory);

        return allCategoryInfo;

    }
    public ArrayList<Product> getAllProductInfoACategory(String url){

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
    public ArrayList<Article> getAllArticlesAndNews(String url){

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

    public Boolean emptyDBProduct(){
        Boolean empty=dbh.emptyProductTable();
        return empty;
    }
    public void addAllProductToTable(ArrayList<Product> allProduct){
        for (int i=0;i<allProduct.size();i++){
            dbh.insertAProduct(allProduct.get(i));
        }
    }
    public ArrayList<Product> getAllProductFromTable(){
        ArrayList<Product> allProduct=new ArrayList<Product>();
        allProduct=dbh.selectAllProduct();
        return allProduct;
    }

}
