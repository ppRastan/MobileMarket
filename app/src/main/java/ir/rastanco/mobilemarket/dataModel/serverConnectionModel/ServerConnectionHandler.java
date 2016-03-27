package ir.rastanco.mobilemarket.dataModel.serverConnectionModel;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.Comment;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.ProductShop;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.dataBaseConnectionModel.DataBaseHandler;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.GetFile;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonArticles;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonAuthorize;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonCategory;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonComments;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonKey;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonLastShop;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProduct;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProductOption;
import ir.rastanco.mobilemarket.utility.Links;
import ir.rastanco.mobilemarket.utility.Utilities;

/**
 * Created by ShaisteS on 1394/10/14.
 * This Class Manage All Connection to Server and DataBase
 */
public class ServerConnectionHandler {

    private Context context;

    public ServerConnectionHandler(Context myContext){
        context=myContext;
    }


    //Setting
    public void setSetting(String firstTimeStamp,String firstArticleNumber,String version,String firstUpdateTimeStamp){
        DataBaseHandler.getInstance(context).insertSettingApp(firstTimeStamp,
                firstArticleNumber,
                version,
                firstUpdateTimeStamp);
    }
    public String getLastTimeStamp(){
        return DataBaseHandler.getInstance(context).selectLastTimeStamp();
    }

    public String getLastUpdateTimeStamp(){
        return DataBaseHandler.getInstance(context).selectLastUpdateTimeStamp();
    }

    public void updateVersionApp(String newVersion){
        DataBaseHandler.getInstance(context).updateLastVersion(newVersion);
    }

    public void updateLastUpdateTimeStamp(String updateTimeStamp){
        DataBaseHandler.getInstance(context).updateLastUpdateTimeStamp(updateTimeStamp);
    }

    public void updateTimeStamp(String TimeStamp){
        DataBaseHandler.getInstance(context).updateTimeStamp(TimeStamp);
    }

    //Category

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
        ArrayList<Category> allCategoryInfo=new ParseJsonCategory().getAllCategory(jsonCategory);
        return allCategoryInfo;
    }

    public Boolean emptyDBCategory(){
        Boolean empty=DataBaseHandler.getInstance(context).emptyCategoryTable();
        return empty;
    }
    public void addAllCategoryToTable(ArrayList<Category> allCategories){
        for (int i=0;i<allCategories.size();i++){
            DataBaseHandler.getInstance(context).insertACategory(allCategories.get(i));
        }
    }

    public boolean existACategoryInDataBase(int categoryId){
        return DataBaseHandler.getInstance(context).ExistACategory(categoryId);
    }

    public void updateACategory(Category aCategory){
        DataBaseHandler.getInstance(context).updateACategory(aCategory);
    }

    public void addACategoryToDataBase(Category aCategory){
        DataBaseHandler.getInstance(context).insertACategory(aCategory);
    }

    public Category getACategoryWithId(int categoryId){
        return DataBaseHandler.getInstance(context).selectACategoryWithId(categoryId);
    }

    public String getACategoryTitleWithCategoryId(int categoryId){
        Category aCategory=getACategoryWithId(categoryId);
        return aCategory.getTitle();
    }

    public ArrayList<String> getTitleOfChildOfACategory(int catID){
        return DataBaseHandler.getInstance(context).selectChildOfACategoryTitle(catID);
    }

    public int getHasChildACategoryWithId(int categoryId){
        Category aCategory=getACategoryWithId(categoryId);
        return aCategory.getHasChild();
    }

    public ArrayList<Integer> getAllChildOfACategoryWithParentCategoryId(int categoryId){
        ArrayList<Integer> subCategoriesId=DataBaseHandler.getInstance(context).selectChildIdOfACategory(categoryId);
        return subCategoriesId;
    }

    public int getParentIdACategoryWithCategoryId(int categoryId){
        return DataBaseHandler.getInstance(context).selectACategoryParent(categoryId);
    }

    public Map<String,Integer> MapTitleToIDForMainCategory(){
        return DataBaseHandler.getInstance(context).selectMainCategories();
    }

    public Map<String,Integer> MapTitleToIDForAllCategory(){
        return DataBaseHandler.getInstance(context).selectAllCategoryTitleAndId();

    }
    public Map<String,Integer> MapTitleToIDForChildOfACategory(int catId){
        return DataBaseHandler.getInstance(context).selectChildOfACategoryTitleAndId(catId);
    }

    public ArrayList<String> getMainCategoryTitle(){
        return DataBaseHandler.getInstance(context).selectMainCategoriesTitle();
    }

    public String getTabTitleForSimilarProduct(int categoryId){
        int catId=categoryId;
        int parentId=getParentIdACategoryWithCategoryId(catId);
        while (parentId !=0){
            catId=parentId;
            parentId=getParentIdACategoryWithCategoryId(catId);
        }
        return getACategoryTitleWithCategoryId(catId);
    }

    public void refreshCategories(String url){
        ArrayList<Category> allCategories=getAllCategoryInfoURL(url);
        for (int i=0;i<allCategories.size();i++){
            if (existACategoryInDataBase(allCategories.get(i).getId()))
                updateACategory(allCategories.get(i));
            else
                addACategoryToDataBase(allCategories.get(i));
        }
    }

    //product

    public Boolean emptyDBProduct(){
        Boolean empty=DataBaseHandler.getInstance(context).emptyProductTable();
        return empty;
    }

    public ArrayList<Product> getAllProductFromTable(){
        return DataBaseHandler.getInstance(context).selectAllProduct();
    }

    public ArrayList<Product> getProductsOfACategoryNoChild(int categoryId){
        return DataBaseHandler.getInstance(context).selectAllProductOfACategory(categoryId);
    }

    public Boolean existAProductInDataBase(int productId){
        return DataBaseHandler.getInstance(context).ExistAProduct(productId);
    }

    public void addAProductInDataBase(Product aProduct){
        DataBaseHandler.getInstance(context).insertAProduct(aProduct);
    }

    public Boolean existAProductImagePath(int productId,String imagePath){
        return DataBaseHandler.getInstance(context).ExistAProductImagePath(productId,imagePath);
    }

    public void addAImagePathForAProduct(int productId,String imagePath){
        DataBaseHandler.getInstance(context).insertImagePathProduct(productId, imagePath);
    }

    public void updateAProductInfo(Product aProduct){
        DataBaseHandler.getInstance(context).updateAProduct(aProduct);
        for (int i=0;i<aProduct.getImagesPath().size();i++){
            if ( ! existAProductImagePath(aProduct.getId(), aProduct.getImagesPath().get(i)))
                addAImagePathForAProduct(aProduct.getId(), aProduct.getImagesPath().get(i));
        }
    }

    public void addAllProductToTable(ArrayList<Product> allProducts){
        Boolean isUpdate=false;
        for (int i=0;i<allProducts.size();i++){
            if(existAProductInDataBase(allProducts.get(i).getId())) {
                updateAProductInfo(allProducts.get(i));
                isUpdate=true;
            }
            else
                addAProductInDataBase(allProducts.get(i));
        }
        if (isUpdate && allProducts.size()>0)
            updateLastUpdateTimeStamp(allProducts.get(0).getUpdateTimeStamp());
        else if( !isUpdate && allProducts.size()>0)
            updateTimeStamp(allProducts.get(0).getTimeStamp());
    }

    public void getNewProducts(){
        String lastTimeStamp=getLastTimeStamp();
        ParseJsonProduct pjp=new ParseJsonProduct(context);
        String url= Links.getInstance().generateUrlForGetNewProduct(lastTimeStamp);
        try {
            pjp.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void getEditProducts(){
        String lastUpdateTimeStamp=getLastUpdateTimeStamp();
        ParseJsonProduct pjp=new ParseJsonProduct(context);
        String url=Links.getInstance().generateURLForGetEditProduct(lastUpdateTimeStamp);
        try {
            pjp.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Product> getProductsOfAParentCategory(int categoryId){
        ArrayList<Product> products=new ArrayList<Product>();
        ArrayList<Integer> childOfParentCategoryId=getAllChildOfACategoryWithParentCategoryId(categoryId);
        if(childOfParentCategoryId.size()==0)
            products=getProductsOfACategoryNoChild(categoryId);
        else{
            for (int i=0;i<childOfParentCategoryId.size();i++){
                ArrayList<Product> helpProduct=getProductsOfAParentCategory(childOfParentCategoryId.get(i));
                for (int j=0;j<helpProduct.size();j++)
                    products.add(helpProduct.get(j));
            }
        }
        return products;
    }

    public ArrayList<Product> getAllProductFavourite(){
        ArrayList<Product> allProduct=new ArrayList<Product>();
        ArrayList<Product> allProductFavorite=new ArrayList<Product>();
        allProduct=getAllProductFromTable();
        for (int i=0;i<allProduct.size();i++){
            if (allProduct.get(i).getLike()==1)
                allProductFavorite.add(allProduct.get(i));
        }
        return allProductFavorite;
    }
    public Product getAProduct(int productId){
        Product aProduct= DataBaseHandler.getInstance(context).selectAProduct(productId);
        aProduct.setImagesPath(DataBaseHandler.getInstance(context).selectAllImagePathAProduct(productId));
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

    public ArrayList<Product> getSpecialProduct(){
        return DataBaseHandler.getInstance(context).selectSpecialProduct();
    }

    public void addAProductOptionToTable(int productId, ProductOption aOption){
        DataBaseHandler.getInstance(context).insertOptionProduct(productId,aOption);
    }

    public void addProductOptionsToTable(int productId,ArrayList<ProductOption> options){
        for (int i=0;i<options.size();i++)
            addAProductOptionToTable(productId, options.get(i));
    }

    public ArrayList<ProductOption> getAllProductOptionOfAProduct(int productId, int groupId){
        ArrayList<ProductOption> options=new ArrayList<ProductOption>();
        options=DataBaseHandler.getInstance(context).selectAllOptionProduct(productId);
        if(options.size()==0) {
            String url= Links.getInstance().generateURLForGetProductOptionsOfAProduct(productId,groupId);
            options = getOptionsOfAProductFromURL(url);
            addProductOptionsToTable(productId,options);
        }
        return options;
    }

    public ArrayList<String> getAllBrands(ArrayList<Product> products){
        ArrayList<String> brandsTitle=new ArrayList<String>();
        for (int i=0;i<products.size();i++){
            if(brandsTitle.size()==0 && !products.get(i).getBrandName().equals(""))
                brandsTitle.add(products.get(i).getBrandName());
            else{
                boolean statusExist=false;
                boolean statusNotExist=true;
                for (int j=0;j<brandsTitle.size();j++){
                    if (products.get(i).getBrandName().equals(brandsTitle.get(j))){
                        statusExist=true;
                    }
                    else{
                        statusNotExist=true;
                    }
                }
                if (statusExist==false && statusNotExist==true && !products.get(i).getBrandName().equals(""))
                    brandsTitle.add(products.get(i).getBrandName());

            }
        }
        return brandsTitle;
    }
    public ArrayList<Product> getAllProductOfABrand(ArrayList<Product> products,String brandTitle) {
        ArrayList<Product> productsOfABrand = new ArrayList<Product>();
        for (int i = 0; i < products.size(); i++) {
               if (products.get(i).getBrandName().equals(brandTitle))
                    productsOfABrand.add(products.get(i));
            }
        return productsOfABrand;
    }
    public void changeProductLike(int productId,int like){
        DataBaseHandler.getInstance(context).updateAProductLike(productId, like);
    }
    public ArrayList<String> searchInProductTitle(){
        Map<Integer,String> productTitle= new HashMap<Integer,String>();
        productTitle=DataBaseHandler.getInstance(context).selectAllProductTitle();
        ArrayList<String> titles=new ArrayList<String>();
        for (Map.Entry<Integer, String> entry : productTitle.entrySet()) {
            titles.add(entry.getValue());
        }
        return titles;
    }
    public int getProductIdWithTitle(String title){
        Map<Integer,String> productTitle= new HashMap<Integer,String>();
        productTitle=DataBaseHandler.getInstance(context).selectAllProductTitle();
        int productId=0;
        for (Map.Entry<Integer, String> entry : productTitle.entrySet()) {
            if(entry.getValue().equals(title))
                productId=entry.getKey();

        }
        return productId;
    }
    public ArrayList<Product> getProductAsPriceFilter(ArrayList<Product> allProduct,String priceTitle){
        int price= Utilities.getInstance().convertPriceTitleToInt(priceTitle);
        if(price<=Utilities.getInstance().getAtLeastHighestPrice())
            return getProductSmallerThanAPrice(allProduct,price);
        else
            return getProductAboveAsAPrice(allProduct,price);
    }

    public ArrayList<Product> getProductSmallerThanAPrice(ArrayList<Product> allProduct,int price){
        ArrayList<Product> productPrice = new ArrayList<Product>();
        for (int i = 0; i < allProduct.size(); i++) {
            if (allProduct.get(i).getPrice() <= price)
                productPrice.add(allProduct.get(i));
        }
        return productPrice;
    }
    public ArrayList<Product> getProductAboveAsAPrice(ArrayList<Product> allProduct,int price){
        ArrayList<Product> productPrice = new ArrayList<Product>();
        for (int i = 0; i < allProduct.size(); i++) {
            if (allProduct.get(i).getPrice() >= price)
                productPrice.add(allProduct.get(i));
        }
        return productPrice;
    }
    public ArrayList<Product> getProductsAfterFilterCategory(int pageID,int categoryId){
        ArrayList<Product> products=new ArrayList<Product>();
        if (categoryId==0)
            products= getProductsOfAParentCategory(pageID);
        else {
            products = getProductsOfACategoryNoChild(categoryId);
        }

        return products;
    }
    public ArrayList<Product> getProductAfterRefresh(int pageId,
                                                     int filterCategoryId,
                                                     String filterOptionContent,
                                                     String filterOptionStatus
                                                     ){
        ArrayList<Product> allProduct=new ArrayList<Product>();
        allProduct= getProductsOfAParentCategory(pageId);
        if(filterCategoryId!=0)
             allProduct=getProductsAfterFilterCategory(pageId, filterCategoryId);
        else if (!filterOptionContent.equals(context.getResources().getString(R.string.all))){
            if(filterOptionStatus.equals(context.getResources().getString(R.string.price)))
                allProduct=getProductAsPriceFilter(allProduct, filterOptionContent);
            else if (filterOptionStatus.equals(context.getResources().getString(R.string.brand)))
                allProduct=getAllProductOfABrand(allProduct,filterOptionContent);
        }
        return allProduct;
    }

    public ArrayList<Product> getProductAfterFilter(int pageID,
                                                     int filterCategoryId,
                                                     String filterOptionContent,
                                                     String filterOptionStatus
    ){
        ArrayList<Product> allProduct=new ArrayList<Product>();
        allProduct= getProductsOfAParentCategory(pageID);
        if(filterCategoryId != 0)
            allProduct=getProductsAfterFilterCategory(pageID, filterCategoryId);
        if (!filterOptionContent.equals(context.getResources().getString(R.string.all))){
            if(filterOptionStatus.equals(context.getResources().getString(R.string.price)))
                allProduct=getProductAsPriceFilter(allProduct, filterOptionContent);
            else if (filterOptionStatus.equals(context.getResources().getString(R.string.brand)))
                allProduct=getAllProductOfABrand(allProduct,filterOptionContent);
        }
        return allProduct;
    }

    //article
    public Boolean emptyDBArticle(){
        Boolean empty=DataBaseHandler.getInstance(context).emptyArticleTable();
        return empty;
    }
    public void addAllArticlesToTable(ArrayList<Article> allArticles){
        for (int i=0;i<allArticles.size();i++)
            DataBaseHandler.getInstance(context).insertArticle(allArticles.get(i));
    }
    public ArrayList<Article> getAllArticlesFromTable(){
        return DataBaseHandler.getInstance(context).selectAllArticle();
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
        String lastArticlesNum=DataBaseHandler.getInstance(context).selectLastArticlesNum();
        String url=Links.getInstance().generateURLForRefreshArticles(lastArticlesNum);
        addAllArticlesToTable(getAllArticlesAndNewsURL(url));
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
        String url=Links.getInstance().generateGetAuthorizeResponse(hashInfo, key);
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
        return DataBaseHandler.getInstance(context).ExistAProductShopping(productId);
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
        DataBaseHandler.getInstance(context).insertShoppingBag(productId, number);
    }
    public Map<Integer,Integer> getAllProductShopping(){
        return DataBaseHandler.getInstance(context).selectAllProductShopping();
    }
    public ArrayList<Integer> getProductShoppingID(){
        return DataBaseHandler.getInstance(context).selectAllIdProductShopping();
    }
    public void deleteAProductShopping(int productId){
        DataBaseHandler.getInstance(context).deleteAProductShopping(productId);
    }
    public int getCountProductShop(){
        return DataBaseHandler.getInstance(context).CounterProductShopping();
    }
    public void changeShoppingNumber(int productId, int count){
        DataBaseHandler.getInstance(context).updateAShoppingNumberPurchased(productId, count);
    }
    public void emptyShoppingBag(){
        DataBaseHandler.getInstance(context).deleteAllShoppingTable();
    }
    public int getNumberProductShop(int productId){
        return DataBaseHandler.getInstance(context).numberPurchasedAProduct(productId);
    }

    //User Profile
    public void addUserInfoToTable(UserInfo aUser){
        if(DataBaseHandler.getInstance(context).emptyUserInfoTable()){
            DataBaseHandler.getInstance(context).insertUserInfo(aUser);
        }
        else{
            DataBaseHandler.getInstance(context).deleteUserInfo();
            DataBaseHandler.getInstance(context).insertUserInfo(aUser);
        }
    }
    public boolean emptyUserInfo(){
        if (DataBaseHandler.getInstance(context).emptyUserInfoTable())
            return true;
        else
            return false;
    }
    public UserInfo getUserInfo(){
        if (DataBaseHandler.getInstance(context).emptyUserInfoTable())
            return null;
        else
            return DataBaseHandler.getInstance(context).selectUserInformation();
    }
    public void deleteUserInfo(){
        DataBaseHandler.getInstance(context).deleteUserInfo();
    }

    //Version Of App

    public String getLastVersionInServer(String url){
        String lastVersionInServer=DataBaseHandler.getInstance(context).selectLastVersionApp();
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
        String lastVersionInDB=DataBaseHandler.getInstance(context).selectLastVersionApp();
        String lastVersionInServer=getLastVersionInServer(url);
        String finalVersion="";
        for(int i=0;i<lastVersionInServer.length()-1;i++)
            finalVersion= finalVersion+String.valueOf(lastVersionInServer.charAt(i+1));
        Boolean newVersionExist=false;
        if(finalVersion.equals(lastVersionInDB))
            newVersionExist=false;
        else if (!finalVersion.equals(lastVersionInDB) && finalVersion.equals("") && lastVersionInDB.equals(""))
            newVersionExist=true;
        return newVersionExist;
    }

    //Comments
    public ArrayList<Comment> getAllCommentAProduct(int productId){
        String url=Links.getInstance().generateGetAllCommentAProduct(productId);
        GetFile jParserComment = new GetFile();
        String jsonComment= null;
        try {
            jsonComment = jParserComment.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<Comment> allComments=new ArrayList<Comment>();
        allComments=new ParseJsonComments().getAllCommentAProduct(jsonComment);

        return allComments;

    }
    public ArrayList<String> getContentCommentsAllProduct(int productId){
        ArrayList<Comment> allComment=new ArrayList<Comment>();
        allComment=getAllCommentAProduct(productId);
        ArrayList<String> commentsContent=new ArrayList<String>();
        for (int i=0;i<allComment.size();i++ )
            commentsContent.add(allComment.get(i).getCommentContent());
        return commentsContent;
    }
}
