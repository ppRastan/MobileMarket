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
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.GetFile;
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
        if(dbh.emptySettingTable()){
            dbh.insertSetting(timeStamp,lastArticlesNum);
        }
        else
            dbh.updateTimeStamp(timeStamp);

    }
    public String getLastTimeStamp(){
        return dbh.selectLastTimeStamp();
    }

    public Boolean emptySetting(){
        return dbh.emptySettingTable();
    }

    public void addSettingApp(String firstTimeStamp,String articleNum,String version){
        dbh.insertSettingApp(firstTimeStamp, articleNum, version);
    }

    public void updateVersionApp(String newVersin){
        dbh.updateLastVersion(newVersin);
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

    public Map<Integer,String> getMainCategory(){
        return dbh.selectMainCategories();
    }
    public ArrayList<Category> getAllCategoryInfoURL(String url){

        GetFile jParserCategory = new GetFile();
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

    public int getMainCategoryId(String catTitle) {
        Map<Integer, String> catTitleMap = dbh.selectMainCategoryTitle();
        int catId = 0;
        for (Map.Entry<Integer, String> entry : catTitleMap.entrySet()) {
            if (entry.getValue().equals(catTitle))
                catId = entry.getKey();
        }
        return catId;
    }

    public ArrayList<Integer> ChildOfACategory(String title){
        int catId=getMainCategoryId(title);
        ArrayList<Integer> subCategoriesId=new ArrayList<Integer>();
        subCategoriesId=dbh.selectChildIdOfACategory(catId);
      return subCategoriesId;
    }
    public ArrayList<Integer> childOfASubCategory(int subCategoryId){
        ArrayList<Integer> childSubCategoriesId=new ArrayList<Integer>();
        childSubCategoriesId=dbh.selectChildIdOfACategory(subCategoryId);
        return childSubCategoriesId;
    }
    public ArrayList<Product> ProductOfASubCategory(int subcategoryId){
        return dbh.selectAllProductOfACategory(subcategoryId);
    }
    public ArrayList<Product> ProductOfMainCategory(String title){
        ArrayList<Product> products=new ArrayList<Product>();
        ArrayList<Integer> childOfMainCategory=new ArrayList<Integer>();
        childOfMainCategory=ChildOfACategory(title);
        for (int i=0;i<childOfMainCategory.size();i++){
            ArrayList<Product> helpProduct=new ArrayList<Product>();
            helpProduct=ProductOFASubCategory(childOfMainCategory.get(i));
            for (int j=0;j<helpProduct.size();j++)
                products.add(helpProduct.get(j));
        }
        return products;
    }

    public ArrayList<Product> ProductOFASubCategory(int subCatId){
        ArrayList<Product> products=new ArrayList<Product>();
        ArrayList<Integer> childofSubCategory=new ArrayList<Integer>();
        childofSubCategory=childOfASubCategory(subCatId);
        if (childofSubCategory.size()==0){

            ArrayList<Product> helpProducts=new ArrayList<Product>();
            helpProducts=ProductOfASubCategory(subCatId);
            for (int k=0;k<helpProducts.size();k++)
                products.add(helpProducts.get(k));

        }
        else{
            for (int j=0;j<childofSubCategory.size();j++){
                ArrayList<Product> helpProducts=new ArrayList<Product>();
                helpProducts=ProductOfASubCategory(childofSubCategory.get(j));
                for (int k=0;k<helpProducts.size();k++)
                    products.add(helpProducts.get(k));
            }
        }
        return products;
    }
    public Map<Integer,String> getFilterSubCategory(String title){
        int mainCatId=getMainCategoryId(title);
        return dbh.selectChildOfACategory(mainCatId);

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

        GetFile optionJson= new GetFile();
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

    public ArrayList<Product> getSpecialProduct(){
        return dbh.selectSpecialProduct();
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

    public void addAProductOptionsToTable(int productId,ProductOption aOptions){
        dbh.insertOptionProduct(productId, aOptions.getTitle(), aOptions.getValue());
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

    public void refreshProductOption(int groupId,int productId){
        ArrayList<ProductOption> options=new ArrayList<ProductOption>();
        options = getOptionsOfAProductFromURL("http://decoriss.com/json/get,com=options&pid=" +
                String.valueOf(productId) + "&pgid=" + String.valueOf(groupId) + "&cache=false");
        for (int i=0;i<options.size();i++){
            if (dbh.ExistAProductIdInOptionTable(productId)){
                if (dbh.ExistAProductOption(productId,options.get(i).getTitle()))
                    dbh.updateAProductOption(productId,options.get(i));
            }
            else
                addAProductOptionsToTable(productId,options.get(i));
        }
        addProductOptionsToTable(productId,options);
    }

    public ArrayList<String> getAllBrands(ArrayList<Product> products){
        ArrayList<String> brandsTitle=new ArrayList<String>();
        String brand = "";
        for (int i=0;i<products.size();i++){
            ArrayList<ProductOption> productOptions=new ArrayList<ProductOption>();
            productOptions=dbh.selectProductBrand(products.get(i).getId());
            for (int k=0;k<productOptions.size();k++){
                if (productOptions.get(k).getTitle().equals("برند")){
                    if (brandsTitle.size()==0)
                        brandsTitle.add(productOptions.get(k).getValue());
                    else{
                        boolean statusExist=false;
                        boolean statusNotExist=true;
                        for (int j=0;j<brandsTitle.size();j++){
                            if (productOptions.get(k).getValue().equals(brandsTitle.get(j))){
                                statusExist=true;
                            }
                            else{
                                statusNotExist=true;

                            }
                        }
                        if (statusExist==false && statusNotExist==true)
                            brandsTitle.add(productOptions.get(k).getValue());
                    }

                }
            }
        }
        return brandsTitle;
    }

    public ArrayList<Product> getAllProductOfABrand(ArrayList<Product> products,String brandTitle) {
        ArrayList<Product> productsOfABrand = new ArrayList<Product>();
        for (int i = 0; i < products.size(); i++) {
            ArrayList<ProductOption> productOptions = new ArrayList<ProductOption>();
            productOptions = dbh.selectProductBrand(products.get(i).getId());
            for (int k = 0; k < productOptions.size(); k++) {
                if (productOptions.get(k).getValue().equals(brandTitle)) {
                    productsOfABrand.add(products.get(i));

                }
            }
        }
        return productsOfABrand;
    }

    public void changeProductLike(int productId,int like){
        dbh.updateAProductLike(productId,like);
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

        GetFile g=new GetFile();
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

        GetFile jSONKey = new GetFile();
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
        GetFile jsonAuth = new GetFile();
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

        GetFile jsonLastShop = new GetFile();
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
    public void addProductToShoppingBag(int productId,int number){
        dbh.insertShoppingBag(productId,number);
    }

    public Map<Integer,Integer> getAllProductShopping(){
        return dbh.selectAllProductShopping();
    }
    public ArrayList<Integer> getProductShoppingID(){
        return dbh.selectAllIdProductShopping();
    }

    public void deleteAProductShopping(int productId){
        dbh.deleteAProductShopping(productId);
    }
    public int getCountProductShop(){
        return dbh.CounterProductShopping();
    }

    public void changeShoppingNunmber(int productId,int count){
        dbh.updateAShoppingNumberPurchased(productId, count);
    }

    public void emptyShoppingBag(){
        dbh.deleteAllShoppingTable();
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

    //Version Of App
    public String getLastVersionInServer(String url){

        String lastVersionInServer=dbh.selectLastVersionApp();

        GetFile getFileVersionFrmURL=new GetFile();
        try {
            lastVersionInServer = getFileVersionFrmURL.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        lastVersionInServer=lastVersionInServer.replace("\n","");
        return lastVersionInServer;

    }
    public boolean checkNewVersion(String url){

        String lastVersionInDB=dbh.selectLastVersionApp();
        String lastVersionInServer=getLastVersionInServer(url);
        String finalVersion="";

        for(int i=0;i<lastVersionInServer.length()-1;i++)
            finalVersion= finalVersion+String.valueOf(lastVersionInServer.charAt(i+1));

        if(finalVersion.equals(lastVersionInDB))
            return false;
        else
            return true;
    }

}
