package ir.rastanco.mobilemarket.dataModel.serverConnectionModel;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.dataModel.Categories;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/10/14.
 * This Class Manage All Connection to Server and DataBase
 */
public class ServerConnectionHandler {

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
}
