package ir.rastanco.mobilemarket.dataModel.serverConnectionModel;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.ProductShop;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.dataBaseConnectionModel.DataBaseHandler;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.GetJsonFile;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonArticles;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonAuthorize;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonCategory;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonKey;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonLastShop;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProduct;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProductOption;

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

    //Setting
    public void setLastTimeStamp(String timeStamp,String lastArticlesNum){
        if(dbh.emptyTimeStamp()){
            dbh.insertSetting(timeStamp,lastArticlesNum);
        }
        else
            dbh.updateTimeStamp(timeStamp);

    }
    public String getLastTimeStamp(){
        return dbh.selectLastTimeStamp();
    }




    //Category
    public Boolean emptyDBCategory(){
        Boolean empty=dbh.emptyCategoryTable();
        return empty;
    }
    public void addAllCategoryToTable(ArrayList<Category> allCategories){
        for (int i=0;i<allCategories.size();i++){
            dbh.insertACategory(allCategories.get(i));
        }
    }
    public ArrayList<Category> getAllCategoryInfoTable(){

        return dbh.selectAllCategory();
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
    public Map<Integer,String> getCategoryTitle(){
        return dbh.selectAllCategoryTitle();
    }

    public Map<Integer,String> filterCategories(){
        return dbh.selectParentCategories();
    }

    public Map<Integer,String> filterSubCategory(int categoryId){
        return dbh.selectChildOfACategory(categoryId);
    }





    //Product
    public Boolean emptyDBProduct(){
        Boolean empty=dbh.emptyProductTable();
        return empty;
    }

    public void addAllProductToTable(ArrayList<Product> allProducts){
        for (int i=0;i<allProducts.size();i++){
            if(dbh.ExistAProduct(allProducts.get(i).getId()))
                dbh.updateAProduct(allProducts.get(i));
            else
                dbh.insertAProduct(allProducts.get(i));
        }
        if (allProducts.size()>0)
            setLastTimeStamp(allProducts.get(0).getTimeStamp(),"25");

    }
    public ArrayList<Product> getAllProductFromTable(){
        return dbh.selectAllProduct();
    }
    public Product getAProduct(int productId){
        Product aProduct= dbh.selectAProduct(productId);
        aProduct.setImagesPath(dbh.selectAllImagePathAProduct(productId));
        return aProduct;
    }
    public ArrayList<ProductOption> getOptionsOfAProductFromURL(String url){

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
    public void refreshProduct(){
        String lastTimeStamp=getLastTimeStamp();
        ParseJsonProduct pjp=new ParseJsonProduct(context);
        String url="http://decoriss.com/json/get,com=product&newfromts="+
                lastTimeStamp+"&cache=false";
        try {
            pjp.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public Map<Integer,String> getProductTitle(){
        return dbh.selectAllProductTitle();
    }

    public void deleteAProduct(int productId){
        dbh.deleteAProduct(productId);
    }
    public ArrayList<Product> getAllProductOfACategory(int groupId){
        return dbh.selectAllProductOfACategory(groupId);
    }
    public void addProductOptionsToTable(int productId,ArrayList<ProductOption> options){
        for (int i=0;i<options.size();i++)
            dbh.insertOptionProduct(productId,options.get(i).getTitle(),options.get(i).getValue());
    }
    public ArrayList<ProductOption> getProductOption(int productId,int groupId){
        ArrayList<ProductOption> options=new ArrayList<ProductOption>();
        options=dbh.selectAllOptionProduct(productId);
        if(options.size()==0) {
            options = getOptionsOfAProductFromURL("http://decoriss.com/json/get,com=options&pid=" +
                    String.valueOf(productId) + "&pgid=" + String.valueOf(groupId) + "&cache=false");
            addProductOptionsToTable(productId,options);
        }
        return options;
    }





    //article
    public Boolean emptyDBArticle(){
        Boolean empty=dbh.emptyArticleTable();
        return empty;
    }
    public void addAllArticlesToTable(ArrayList<Article> allArticles){
        for (int i=0;i<allArticles.size();i++)
            dbh.insertArticle(allArticles.get(i));
    }
    public ArrayList<Article> getAllArticlesFromTable(){

        return dbh.selectAllArticle();
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
    public void refreshArticles(){
        String lastArticlesNum=dbh.selectLastArticlesNum();
        int endArticle=Integer.parseInt(lastArticlesNum)+100;
        ParseJsonArticles pja=new ParseJsonArticles();
        String url="http://decoriss.com/json/get,com=news&name=blog&order=desc&limit="
                +lastArticlesNum+"-"+String.valueOf(endArticle)+"&cache=false";
        addAllArticlesToTable(getAllArticlesAndNewsURL(url));

    }
    public void setLastArticlesNum(String lastNum){
        dbh.updateLastArticlesNum(lastNum);
    }

    //Security
    public String GetKey(String url){

        GetJsonFile jSONKey = new GetJsonFile();
        String jsonKeyString= null;
        try {
            jsonKeyString = jSONKey.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ParseJsonKey pjk= new ParseJsonKey();
        return  pjk.getKey(jsonKeyString);
    }

    public ArrayList<String> GetAuthorizeResponse(String hashInfo,String key){

        String url="http://decoriss.com/json/get,com=login&u="+hashInfo+
                "&k="+key;
        GetJsonFile jsonAuth = new GetJsonFile();
        String jsonKeyString= null;
        try {
            jsonKeyString = jsonAuth.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ParseJsonAuthorize pja= new ParseJsonAuthorize();
        return  pja.getResponse(jsonKeyString);

    }

    //Product Shopping

    public boolean checkSelectProductForShop(int productId){
        return dbh.ExistAProductShopping(productId);
    }
    public ArrayList<ProductShop> getLastProductShop(String url){

        GetJsonFile jsonLastShop = new GetJsonFile();
        String jsonLastShopString= null;
        try {
            jsonLastShopString = jsonLastShop.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ParseJsonLastShop pjl= new ParseJsonLastShop();
        return  pjl.getLastShop(jsonLastShopString);

    }
    public void addProductToShoppingBag(int productId){
        dbh.insertShoppingBag(productId);
    }

    public ArrayList<Integer> getProductForShopping(){
        return dbh.selectAllProductShopping();
    }

    public void deleteAProductShopping(int productId){
        dbh.deleteAProductShopping(productId);
    }
    public int getCountProductShop(){
        return dbh.CounterProductShopping();
    }





    //User Profile
    public void addUserInfoToTable(UserInfo aUser){
        if(dbh.emptyUserInfoTable()){
            dbh.insertUserInfo(aUser);
        }
        else{
            dbh.deleteUserInfo();
            dbh.insertUserInfo(aUser);
        }
    }

    public UserInfo getUserInfo(){
        if (dbh.emptyUserInfoTable())
            return null;
        else
            return dbh.selectUserInformation();
    }
}
