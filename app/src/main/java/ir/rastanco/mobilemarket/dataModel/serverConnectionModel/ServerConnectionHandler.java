package ir.rastanco.mobilemarket.dataModel.serverConnectionModel;

import android.content.Context;

import java.util.ArrayList;
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
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.GetFileNoAsync;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonArticles;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonAuthorize;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonCategory;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonComments;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonKey;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonLastShop;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProductOption;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson.ParseJsonProduct;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;
import ir.rastanco.mobilemarket.utility.Utilities;

/**
 * Created by ShaisteS on 1394/10/14.
 * This Class Manage All Connection to Server and DataBase
 */
public class ServerConnectionHandler {

    private static ServerConnectionHandler serverConnectionHandlerInstance;
    private final Context context;
    private ArrayList<Product> products;
    private ArrayList<Category> categories;
    private String jsonStringProduct;

    public static ServerConnectionHandler getInstance(Context context) {

        if (serverConnectionHandlerInstance == null) {
            serverConnectionHandlerInstance = new ServerConnectionHandler(context.getApplicationContext());
        }
        return serverConnectionHandlerInstance;
    }

    public ServerConnectionHandler(Context myContext){
        context=myContext;
        products= new ArrayList<>();
        jsonStringProduct="";
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    //Setting
    public void setSetting(String firstTimeStamp,String firstArticleNumber,String version,String firstUpdateTimeStamp,
                           int firstIndexGetProduct,int numberAllProducts){
        DataBaseHandler.getInstance(context).insertSettingApp(firstTimeStamp,
                firstArticleNumber,
                version,
                firstUpdateTimeStamp,
                firstIndexGetProduct,
                numberAllProducts);
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

    public int getFirstIndexForGetProductFromJson(){
        return DataBaseHandler.getInstance(context).SelectFistIndexGetProduct();
    }

    public int getNumberAllProduct(){
        return DataBaseHandler.getInstance(context).SelectNumberAllProducts();
    }

    public void updatePropertyOfGetProduct(String timeStamp,String updateTimeStamp,int firstIndexGetProduct,int numberAllProducts){
        DataBaseHandler.getInstance(context).updatePropertyOfGetProduct(timeStamp,updateTimeStamp,firstIndexGetProduct,numberAllProducts);
    }

    //Category

    public ArrayList<Category> getAllCategoryInfoURL(String url){
        GetFileNoAsync getJsonCategory=new GetFileNoAsync();
        String jsonCategory=getJsonCategory.getFile(url);
        /*GetFile jParserCategory = new GetFile();
        String jsonCategory= null;
        try {
            jsonCategory = jParserCategory.execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }*/
        return new ParseJsonCategory().getAllCategory(jsonCategory);
    }

    public Boolean emptyDBCategory(){
        return DataBaseHandler.getInstance(context).emptyCategoryTable();
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
        Category aCategory=DataBaseHandler.getInstance(context).selectACategoryWithId(categoryId);
        if (aCategory==null){
            for (int i=0;i<categories.size();i++){
                if (categories.get(i).getId()==categoryId)
                    aCategory=categories.get(i);
            }
        }
        return aCategory;
    }

    public String getACategoryTitleWithCategoryId(int categoryId){
        Category aCategory=getACategoryWithId(categoryId);
        return aCategory.getTitle();
    }

    public ArrayList<String> getTitleOfChildOfACategory(int catID){
        ArrayList<String> titleOfChildOfACategory=DataBaseHandler.getInstance(context).selectChildOfACategoryTitle(catID);
        if (emptyDBCategory() && categories.size()!=0){
            for (int i=0;i<categories.size();i++){
                if (categories.get(i).getParentId()==catID)
                    titleOfChildOfACategory.add(categories.get(i).getTitle());
            }
        }
        return titleOfChildOfACategory;
    }

    public int getHasChildACategoryWithId(int categoryId){
        Category aCategory=getACategoryWithId(categoryId);
        return aCategory.getHasChild();
    }

    public ArrayList<Integer> getAllChildOfACategoryWithParentCategoryId(int categoryId){
        ArrayList<Integer> allChildOfACategoryWithParentCategoryId=DataBaseHandler.getInstance(context).selectChildIdOfACategory(categoryId);
        if (emptyDBCategory() && categories.size()!=0){
            for (int i=0;i<categories.size();i++){
                if (categories.get(i).getParentId()==categoryId)
                    allChildOfACategoryWithParentCategoryId.add(categories.get(i).getId());

            }
        }
        return allChildOfACategoryWithParentCategoryId;
    }

    public int getParentIdACategoryWithCategoryId(int categoryId){
        int parentIdACategory=DataBaseHandler.getInstance(context).selectACategoryParent(categoryId);
        if (emptyDBCategory() && categories.size()!=0)
        {
            Category aCategory=getACategoryWithId(categoryId);
            parentIdACategory=aCategory.getId();

        }
        return parentIdACategory;
    }

    public Map<String,Integer> MapTitleToIDForMainCategory(){
        Map<String,Integer> mapTitleToIDMainCategory=DataBaseHandler.getInstance(context).selectMainCategories();
        /*if (emptyDBCategory() && categories.size()!=0){
            for (int i=0;i<categories.size();i++){
                if (categories.get(i).getParentId()==0)
                    mapTitleToIDMainCategory.put(categories.get(i).getTitle(), categories.get(i).getId());
            }
        }*/
        return mapTitleToIDMainCategory;
    }

    public Map<String,Integer> MapTitleToIDForAllCategory(){
        Map<String,Integer> mapTitleToIdForAllCategory=DataBaseHandler.getInstance(context).selectAllCategoryTitleAndId();
        if (emptyDBCategory()&& categories.size()!=0){
            for (int i=0;i<categories.size();i++)
                mapTitleToIdForAllCategory.put(categories.get(i).getTitle(),categories.get(i).getId());
        }
        return mapTitleToIdForAllCategory ;

    }
    public Map<String,Integer> MapTitleToIDForChildOfACategory(int catId){
        Map<String,Integer> mapTitleToIDForChildOfACategory=DataBaseHandler.getInstance(context).selectChildOfACategoryTitleAndId(catId);
        if (emptyDBCategory()&& categories.size()!=0){
            for (int i=0;i<categories.size();i++){
                if (categories.get(i).getParentId()==catId){
                    mapTitleToIDForChildOfACategory.put(categories.get(i).getTitle(),categories.get(i).getId());
                }
            }
        }
        return mapTitleToIDForChildOfACategory;
    }

    public ArrayList<String> getMainCategoryTitle(){
        ArrayList<String> mainCategoryTitle=DataBaseHandler.getInstance(context).selectMainCategoriesTitle();
        /*if(emptyDBCategory() /*&& categories.size()!=0){
            for (int i=0;i<categories.size();i++){
                if (categories.get(i).getParentId()==0)
                    mainCategoryTitle.add(categories.get(i).getTitle());
            }
        }*/
        return mainCategoryTitle;
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
        return DataBaseHandler.getInstance(context).emptyProductTable();
    }

    public ArrayList<Product> getAllProduct(){
        ArrayList<Product> allProducts=DataBaseHandler.getInstance(context).selectAllProduct();
        if (allProducts.size()==0 && products.size()!=0)
            allProducts=products;
        return allProducts;
    }

    public ArrayList<Product> getProductsOfACategoryNoChild(int categoryId){
        ArrayList<Product> allProductOfACategoryNoChild = new ArrayList<>();
        if (Configuration.getConfig().emptyProductTable && products.size()!=0 ){
            for (int i=0;i<products.size();i++){
                if (products.get(i).getGroupId()==categoryId)
                    allProductOfACategoryNoChild.add(products.get(i));
            }
        }
        else
            allProductOfACategoryNoChild=DataBaseHandler.getInstance(context).selectAllProductOfACategory(categoryId);
        return allProductOfACategoryNoChild;
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

    public ArrayList<Product> getAllProductFromURL(String url,int firstIndex,int lastIndex,Boolean lastIndexValidStatus ){
       if (jsonStringProduct.equals("")){
           GetFileNoAsync getJsonProductFile=new GetFileNoAsync();
           jsonStringProduct=getJsonProductFile.getFile(url);
       }
        /*if (jsonStringProduct.equals("")){
            GetFile jsonProductsFile = new GetFile();
            try {
                jsonStringProduct = jsonProductsFile.execute(url).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }*/
        return new ParseJsonProduct().ParseJsonProducts(jsonStringProduct,firstIndex,lastIndex,lastIndexValidStatus);
    }

    public void addProductInformationToDataBaseFirstInstall(String url){
        Configuration.getConfig().numberOfProductMustBeTaken=2;
        int firstIndex=Configuration.getConfig().firstIndexGetProduct;
        int lastIndex=Configuration.getConfig().firstIndexGetProduct+Configuration.getConfig().numberOfProductMustBeTaken;

        ArrayList<Product> allProducts = getAllProductFromURL(url,firstIndex,lastIndex,true);
        addAllProductToTable(allProducts);
        String timeStamp= allProducts.get(0).getTimeStamp();
        updatePropertyOfGetProduct(timeStamp,
                timeStamp,
                Configuration.getConfig().firstIndexGetProduct + Configuration.getConfig().numberOfProductMustBeTaken,
                Configuration.getConfig().numberAllProducts);
    }

    public void getNewProducts(){
        String lastTimeStamp=getLastTimeStamp();
        String url= Link.getInstance().generateUrlForGetNewProduct(lastTimeStamp);
        ArrayList<Product> allProducts = getAllProductFromURL(url,0,0,false);
        addAllProductToTable(allProducts);
    }

    public void getEditProducts(){
        String lastUpdateTimeStamp=getLastUpdateTimeStamp();
        String url= Link.getInstance().generateURLForGetEditProduct(lastUpdateTimeStamp);
        ArrayList<Product> allProducts = getAllProductFromURL(url,0,0,false);
        addAllProductToTable(allProducts);
    }

    public ArrayList<Product> getProductsOfAParentCategory(int categoryId){
        ArrayList<Product> allProducts=new ArrayList<>();
        ArrayList<Integer> childOfParentCategoryId=getAllChildOfACategoryWithParentCategoryId(categoryId);
        if(childOfParentCategoryId.size()==0)
            allProducts=getProductsOfACategoryNoChild(categoryId);
        else{
            for (int i=0;i<childOfParentCategoryId.size();i++){
                ArrayList<Product> helpProduct=getProductsOfAParentCategory(childOfParentCategoryId.get(i));
                for (int j=0;j<helpProduct.size();j++)
                    allProducts.add(helpProduct.get(j));
            }
        }
        return allProducts;
    }

    public ArrayList<Product> getAllProductFavourite(){
        ArrayList<Product> allProduct;
        ArrayList<Product> allProductFavorite=new ArrayList<>();
        allProduct= getAllProduct();
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
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ParseJsonProductOption p=new  ParseJsonProductOption();
        return p.getAllProductOptions(productInfoJson);
    }

    public ArrayList<Product> getSpecialProduct(){
        ArrayList<Product> allProducts=DataBaseHandler.getInstance(context).selectSpecialProduct();
        if (allProducts.size()==0 && products.size()!=0){
            for (int i=0;i<products.size();i++){
                if (products.get(i).getPriceOff()!=0 || products.get(i).getShowAtHomeScreen()==1)
                    allProducts.add(products.get(i));
            }
        }
        return allProducts;
    }

    public void addAProductOptionToTable(int productId, ProductOption aOption){
        DataBaseHandler.getInstance(context).insertOptionProduct(productId,aOption);
    }

    public void addProductOptionsToTable(int productId,ArrayList<ProductOption> options){
        for (int i=0;i<options.size();i++)
            addAProductOptionToTable(productId, options.get(i));
    }

    public ArrayList<ProductOption> getAllProductOptionOfAProduct(int productId, int groupId){
        ArrayList<ProductOption> options;
        options=DataBaseHandler.getInstance(context).selectAllOptionProduct(productId);
        if(options.size()==0) {
            String url= Link.getInstance().generateURLForGetProductOptionsOfAProduct(productId,groupId);
            options = getOptionsOfAProductFromURL(url);
            addProductOptionsToTable(productId,options);
        }
        return options;
    }

    public ArrayList<String> getAllBrands(ArrayList<Product> products){
        ArrayList<String> brandsTitle=new ArrayList<>();
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
                if (!statusExist && !products.get(i).getBrandName().equals(""))
                    brandsTitle.add(products.get(i).getBrandName());

            }
        }
        return brandsTitle;
    }
    public ArrayList<Product> getAllProductOfABrand(ArrayList<Product> products,String brandTitle) {
        ArrayList<Product> productsOfABrand = new ArrayList<>();
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
        Map<Integer,String> productTitle;
        productTitle=DataBaseHandler.getInstance(context).selectAllProductTitle();
        ArrayList<String> titles=new ArrayList<>();
        for (Map.Entry<Integer, String> entry : productTitle.entrySet()) {
            titles.add(entry.getValue());
        }
        return titles;
    }
    public int getProductIdWithTitle(String title){
        Map<Integer,String> productTitle;
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
        ArrayList<Product> productPrice = new ArrayList<>();
        for (int i = 0; i < allProduct.size(); i++) {
            if (allProduct.get(i).getPrice() <= price)
                productPrice.add(allProduct.get(i));
        }
        return productPrice;
    }
    public ArrayList<Product> getProductAboveAsAPrice(ArrayList<Product> allProduct,int price){
        ArrayList<Product> productPrice = new ArrayList<>();
        for (int i = 0; i < allProduct.size(); i++) {
            if (allProduct.get(i).getPrice() >= price)
                productPrice.add(allProduct.get(i));
        }
        return productPrice;
    }
    public ArrayList<Product> getProductsAfterFilterCategory(int pageID,int categoryId){
        ArrayList<Product> products;
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
        ArrayList<Product> allProduct;
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
        ArrayList<Product> allProduct;
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
        return DataBaseHandler.getInstance(context).emptyArticleTable();
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
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ParseJsonArticles a=new ParseJsonArticles();
        return a.getAllProductOptions(articlesInfo);
    }
    public void refreshArticles(){
        String lastArticlesNum=DataBaseHandler.getInstance(context).selectLastArticlesNum();
        String url= Link.getInstance().generateURLForRefreshArticles(lastArticlesNum);
        addAllArticlesToTable(getAllArticlesAndNewsURL(url));
    }

    //Security
    public String GetKey(String url){
        GetFile jSONKey = new GetFile();
        String jsonKeyString= null;
        try {
            jsonKeyString = jSONKey.execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        ParseJsonKey pjk= new ParseJsonKey();
        return  pjk.getKey(jsonKeyString);
    }
    public ArrayList<String> GetAuthorizeResponse(String hashInfo,String key){
        String url= Link.getInstance().generateURLGetAuthorizeResponse(hashInfo, key);
        GetFile jsonAuth = new GetFile();
        String jsonKeyString= null;
        try {
            jsonKeyString = jsonAuth.execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
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
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ParseJsonLastShop pjl= new ParseJsonLastShop();
        return  pjl.getLastShop(jsonLastShopString);
    }
    public void addProductToShoppingBag(int productId){
        int number = 1 ;
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
        } catch (InterruptedException | ExecutionException e) {
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
        String url= Link.getInstance().generateURLGetAllCommentAProduct(productId);
        GetFile jParserComment = new GetFile();
        String jsonComment= null;
        try {
            jsonComment = jParserComment.execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<Comment> allComments;
        allComments=new ParseJsonComments().getAllCommentAProduct(jsonComment);

        return allComments;

    }
    public ArrayList<String> getContentCommentsAllProduct(int productId){
        ArrayList<Comment> allComment;
        allComment=getAllCommentAProduct(productId);
        ArrayList<String> commentsContent=new ArrayList<>();
        for (int i=0;i<allComment.size();i++ )
            commentsContent.add(allComment.get(i).getCommentContent());
        return commentsContent;
    }
}
