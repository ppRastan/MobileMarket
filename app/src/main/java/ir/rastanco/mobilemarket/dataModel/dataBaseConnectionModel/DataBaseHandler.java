package ir.rastanco.mobilemarket.dataModel.dataBaseConnectionModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.UserInfo;

/**
 * Created by ShaisteS on 1394/10/14.
 * A Singleton Class for Manage CRUD(Create-Retrieve-Update-delete) operation
 */
public class DataBaseHandler  extends SQLiteOpenHelper {

    private static Context dbContext;
    private Product aProduct;
    private Article aArticle;
    private ArrayList<Category> allCategories;
    private ArrayList<Product> allProducts;
    private ArrayList<Article> allArticles;

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "MobileMarket";
    private static final String TABLE_USER_INFO = "tblUserInfo";
    private static final String TABLE_SETTINGS = "tblSetting";
    private static final String TABLE_SHOPPING = "tblShopping";
    private static final String TABLE_CATEGORY = "tblCategory";
    private static final String TABLE_PRODUCT = "tblProduct";
    private static final String TABLE_IMAGES_PATH_PRODUCT = "tblImagesPathProduct";
    private static final String TABLE_PRODUCT_OPTION = "tblProductOption";
    private static final String TABLE_ARTICLE = "tblArticle";




    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_USER_INFO +
                "(id Integer primary key AUTOINCREMENT," +
                "userId Integer," +
                "userEmail String," +
                "userLoginStatus Integer)");
        Log.v("create", "Create User Information Table");

        db.execSQL("create table " + TABLE_SETTINGS +
                "(id Integer primary key AUTOINCREMENT," +
                "lastTimeStamp String," +
                "lastArticlesNum String," +
                "lastVersionOfApp String)");
        Log.v("create", "Create Setting Table");

        db.execSQL("create table " + TABLE_SHOPPING +
                "(id Integer primary key AUTOINCREMENT," +
                "fkProductId Integer unique," +
                "numberPurchased Integer," +
                "foreign key (fkProductId) references tblProduct(productId))");
        Log.v("create", "Create Shopping Table");

        db.execSQL("create table " + TABLE_CATEGORY +
                "(id Integer primary key AUTOINCREMENT," +
                "title text," +
                "catId Integer," +
                "parentId Integer," +
                "sortOrder Integer," +
                "hasChild Integer)");
        Log.v("create", "Create Table Category");

        db.execSQL("create table "+ TABLE_PRODUCT +
                "(id Integer primary key AUTOINCREMENT," +
                "title text," +
                "productId Integer," +
                "groupId Integer," +
                "price Integer," +
                "priceOff Integer," +
                "visits Integer," +
                "brandName text," +
                "minCounts Integer," +
                "stock Integer," +
                "qualityRank text," +
                "commentsCount Integer," +
                "codeProduct text," +
                "description text," +
                "sellsCount Integer," +
                "timeStamp text," +
                "showAtHomeScreen Integer," +
                "like Integer," +
                "linkInSite text," +
                "watermarkPath text," +
                "imagesMainPath text)");
        Log.v("create", "Create Table Product");

        db.execSQL("create table "+TABLE_IMAGES_PATH_PRODUCT +
                "(id Integer primary key AUTOINCREMENT," +
                "fkProductId Integer not null," +
                "imagePath text," +
                "foreign key (fkProductId) references tblProduct(productId))");
        Log.v("create", "Create Table Images Path For Product");

        db.execSQL("create table "+TABLE_PRODUCT_OPTION +
                "(id Integer primary key AUTOINCREMENT," +
                "fkProductId Integer not null," +
                "titleOption text," +
                "valueOption text," +
                "foreign key (fkProductId) references tblProduct(productId))");
        Log.v("create", "Create Table Options For Product");

        db.execSQL("create table "+TABLE_ARTICLE +
                "(id Integer primary key AUTOINCREMENT," +
                "title text unique," +
                "brief text," +
                "date text," +
                "linkInWebsite text," +
                "imageLink text)");
        Log.v("create", "Create Table Article");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion<4){
            db.execSQL("ALTER TABLE "+ TABLE_PRODUCT + " ADD COLUMN brandName text;");
        }

        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFO);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES_PATH_PRODUCT);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_OPTION);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        // Create tables again
//        onCreate(db);
    }



    public Boolean emptyUserInfoTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblUserInfo", null);
        if (rs.moveToFirst()) {
            //Not empty
            rs.close();
            return false;
        } else {
            //Is Empty
            rs.close();
            return true;
        }
    }
    public Boolean emptySettingTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblSetting", null);
        if (rs.moveToFirst()) {
            //Not empty
            rs.close();
            return false;
        } else {
            //Is Empty
            rs.close();
            return true;
        }
    }
    public Boolean emptyShoppingTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblShopping", null);
        if (rs.moveToFirst()) {
            //Not empty
            rs.close();
            return false;
        } else {
            //Is Empty
            rs.close();
            return true;
        }
    }
    public Boolean emptyCategoryTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblCategory", null);
        if (rs.moveToFirst()) {
            //Not empty
            rs.close();
            return false;
        } else {
            //Is Empty
            rs.close();
            return true;
        }
    }
    public Boolean emptyProductTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProduct", null);
        if (rs.moveToFirst()) {
            //Not empty
            rs.close();
            return false;
        } else {
            //Is Empty
            rs.close();
            return true;
        }
    }
    public Boolean emptyArticleTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblArticle", null);
        if (rs.moveToFirst()) {
            //Not empty
            rs.close();
            return false;
        } else {
            //Is Empty
            rs.close();
            return true;
        }
    }
    public Boolean ExistAProduct(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select productId from tblProduct where productId=" + productId, null);
        if (rs.moveToFirst()) {
            //Exist Product
            rs.close();
            return true;
        } else {
            //Not Exist
            rs.close();
            return false;
        }
    }

    public Boolean ExistACategory(int catId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select catId from tblCategory where catId=" + catId, null);
        if (rs.moveToFirst()) {
            //Exist Product
            rs.close();
            return true;
        } else {
            //Not Exist
            rs.close();
            return false;
        }
    }


    public Boolean ExistAProductIdInOptionTable(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProductOption where " +
                "fkProductId=" + productId, null);
        if (rs.moveToFirst()) {
            //Exist Product
            rs.close();
            return true;
        } else {
            //Not Exist
            rs.close();
            return false;
        }
    }


    public Boolean ExistAProductOption(int productId,String title) {
        SQLiteDatabase db = this.getReadableDatabase();
         Cursor rs = db.rawQuery("select * from tblProductOption where " +
                "fkProductId=" + productId+" and titleOption"+ "='" +title+"'", null);
        if (rs.moveToFirst()) {
            //Exist Product
            rs.close();
            return true;
        } else {
            //Not Exist
            rs.close();
            return false;
        }
    }


    public void insertSettingApp(String lastTimeStamp,String articlesNum,String version) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("lastTimeStamp",lastTimeStamp);
        values.put("lastArticlesNum",articlesNum);
        values.put("lastVersionOfApp",version);
        db.insert("tblSetting", null,values);
        Log.v("insert", "insert Setting for App");
        db.close();

    }

    public void insertUserInfo(UserInfo aUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblUserInfo", null, addFieldToUserInfoTable(aUser));
        Log.v("insert", "insert A UserLogin into Table");
        db.close();

    }
    private ContentValues addFieldToUserInfoTable(UserInfo aUser) {
        ContentValues values = new ContentValues();
        values.put("userId", aUser.getUserId());
        values.put("userEmail", aUser.getUserEmail());
        values.put("userLoginStatus", aUser.getUserLoginStatus());
        return values;
    }
    public void insertSetting(String timeStamp,String lastArticlesNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lastTimeStamp", timeStamp);
        values.put("lastArticlesNum",lastArticlesNum);
        db.insert("tblSetting", null, values);
        Log.v("insert", "insert A TimeStamp into Table");
        db.close();

    }
    public void insertShoppingBag(int productID, int numberPurchased) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fkProductId", productID);
        values.put("numberPurchased",numberPurchased);
        db.insert("tblShopping", null, values);
        Log.v("insert", "insert A ProductId into Shopping Table");
        db.close();
    }
    public void insertACategory(Category aCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblCategory", null, addFieldToCategoryTable(aCategory));
        Log.v("insert", "insert A Category into Table");
        db.close();
    }
    private ContentValues addFieldToCategoryTable(Category aCategory) {
        ContentValues values = new ContentValues();
        values.put("title", aCategory.getTitle());
        values.put("catId", aCategory.getId());
        values.put("parentId", aCategory.getParentId());
        values.put("hasChild", aCategory.getHasChild());
        values.put("sortOrder", aCategory.getSortOrder());
        return values;
    }
    public void insertAProduct(Product aProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblProduct", null, addFieldToProductTable(aProduct));
        for (int i = 0; i < aProduct.getImagesPath().size(); i++)
            insertImagePathProduct(aProduct.getId(), aProduct.getImagesPath().get(i));
        Log.v("insert", "insert A Product into Table");
        db.close();
    }
    private ContentValues addFieldToProductTable(Product aProduct) {
        ContentValues values = new ContentValues();
        values.put("title", aProduct.getTitle());
        values.put("productId", aProduct.getId());
        values.put("groupId", aProduct.getGroupId());
        values.put("price", aProduct.getPrice());
        values.put("priceOff", aProduct.getPriceOff());
        values.put("visits", aProduct.getVisits());
        values.put("minCounts", aProduct.getMinCounts());
        values.put("stock", aProduct.getStock());
        values.put("qualityRank", aProduct.getQualityRank());
        values.put("commentsCount", aProduct.getCommentsCount());
        values.put("codeProduct", aProduct.getCodeProduct());
        values.put("description", aProduct.getDescription());
        values.put("sellsCount", aProduct.getSellsCount());
        values.put("timeStamp", aProduct.getTimeStamp());
        values.put("showAtHomeScreen", aProduct.getShowAtHomeScreen());
        values.put("watermarkPath", aProduct.getWatermarkPath());
        values.put("imagesMainPath", aProduct.getImagesMainPath());
        values.put("like",aProduct.getLike());
        values.put("linkInSite",aProduct.getLinkInSite());
        values.put("brandName",aProduct.getBrandName());
        Log.v("insert", "insert A Field into Product Table");
        return values;
    }
    public void insertImagePathProduct(int productId, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblImagesPathProduct", null, addFieldImagePath(productId, path));
        Log.v("insert", "insert A Image Path Product into Table");
        db.close();

    }
    private ContentValues addFieldImagePath(int productId, String path) {
        ContentValues values = new ContentValues();
        values.put("fkProductId", productId);
        values.put("imagePath", path);
        return values;
    }
    public void insertOptionProduct(int productId, String title, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblProductOption", null, addFieldOptionProduct(productId, title, value));
        Log.v("insert", "insert A Option of Product into Table");
        db.close();

    }
    private ContentValues addFieldOptionProduct(int productId, String title, String value) {
        ContentValues values = new ContentValues();
        values.put("fkProductId", productId);
        values.put("titleOption", title);
        values.put("valueOption", value);
        return values;
    }
    public void insertArticle(Article aArticle) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblArticle", null, addFieldToArticleTable(aArticle));
        Log.v("insert", "insert A Product into Table");
        db.close();
    }
    private ContentValues addFieldToArticleTable(Article aArticle) {
        ContentValues values = new ContentValues();
        values.put("title", aArticle.getTitle());
        values.put("brief", aArticle.getBrief());
        values.put("date", aArticle.getDate());
        values.put("linkInWebsite", aArticle.getLinkInWebsite());
        values.put("imageLink", aArticle.getImageLink());
        return values;
    }


    public String selectLastVersionApp() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select lastVersionOfApp from tblSetting", null);
        String lastVersion = "";
        if (rs != null) {
            if (rs.moveToFirst()) {
                lastVersion = rs.getString(rs.getColumnIndex("lastVersionOfApp"));
            }
            rs.close();
        }
        Log.v("select", "Select Last Version Of App ");
        return lastVersion;
    }


    public String selectLastTimeStamp() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select lastTimeStamp from tblSetting", null);
        String timeStamp = "";
        if (rs != null) {
            if (rs.moveToFirst()) {
                timeStamp = rs.getString(rs.getColumnIndex("lastTimeStamp"));
            }
            rs.close();
        }
        Log.v("select", "Select TimeStamp");
        return timeStamp;
    }
    public String selectLastArticlesNum() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select lastArticlesNum from tblSetting", null);
        String timeStamp = "";
        if (rs != null) {
            if (rs.moveToFirst()) {
                timeStamp = rs.getString(rs.getColumnIndex("lastArticlesNum"));
            }
            rs.close();
        }
        Log.v("select", "Select Last Articles Num ");
        return timeStamp;
    }
    public UserInfo selectUserInformation() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblUserInfo", null);
        UserInfo aUser=new UserInfo();
        if (rs != null) {
            if (rs.moveToFirst()) {
                aUser.setUserEmail(rs.getString(rs.getColumnIndex("userEmail")));
                aUser.setUserId(rs.getInt(rs.getColumnIndex("userId")));
                aUser.setUserLoginStatus(rs.getInt(rs.getColumnIndex("userLoginStatus")));
            }
            rs.close();
        }
        Log.v("select", "Select User Information");
        return aUser;
    }


    public ArrayList<Integer> selectAllIdProductShopping(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblShopping", null);
        ArrayList<Integer> allProductsId=new ArrayList<Integer>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    allProductsId.add(rs.getInt(rs.getColumnIndex("fkProductId")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product Id From Shopping Table ");
        return  allProductsId;

    }
    public Map<Integer,Integer> selectAllProductShopping(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblShopping", null);
        Map<Integer,Integer> allProductsShop=new HashMap<Integer,Integer>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    allProductsShop.put(rs.getInt(rs.getColumnIndex("fkProductId")),
                            rs.getInt(rs.getColumnIndex("numberPurchased")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Products Info From Shopping Table ");
        return  allProductsShop;

    }
    public boolean ExistAProductShopping(int productId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblShopping where fkProductId="+productId, null);
        if (rs.moveToFirst()) {
            rs.close();
           return true;
        }
        else {
            rs.close();
            return false;
        }

    }
    public int CounterProductShopping(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblShopping", null);
        return  rs.getCount();
    }

    public int numberPurchasedAProduct(int productId){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblShopping where fkProductId=" + productId, null);
        int numberPurchased=0;
        if (rs!= null)
            if(rs.moveToFirst())
                numberPurchased=rs.getInt(rs.getColumnIndex("numberPurchased"));
        return numberPurchased;

    }

    public ArrayList<Category> selectAllCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblCategory order by catId and sortOrder ASC ", null);
        allCategories = new ArrayList<Category>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    Category aCategory = new Category();
                    aCategory.setTitle(rs.getString(rs.getColumnIndex("title")));
                    aCategory.setId(Integer.parseInt(rs.getString((rs.getColumnIndex("catId")))));
                    aCategory.setParentId(Integer.parseInt(rs.getString((rs.getColumnIndex("parentId")))));
                    aCategory.setHasChild(Integer.parseInt(rs.getString((rs.getColumnIndex("hasChild")))));
                    aCategory.setSortOrder(Integer.parseInt(rs.getString((rs.getColumnIndex("sortOrder")))));
                    allCategories.add(aCategory);
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Category");
        return allCategories;
    }

    public Category selectACategory(int catId){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblCategory where catId=" + catId, null);
        Category aCategory = new Category();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    aCategory.setTitle(rs.getString(rs.getColumnIndex("title")));
                    aCategory.setId(Integer.parseInt(rs.getString((rs.getColumnIndex("catId")))));
                    aCategory.setParentId(Integer.parseInt(rs.getString((rs.getColumnIndex("parentId")))));
                    aCategory.setHasChild(Integer.parseInt(rs.getString((rs.getColumnIndex("hasChild")))));
                    aCategory.setSortOrder(Integer.parseInt(rs.getString((rs.getColumnIndex("sortOrder")))));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select A Category With Id");
        return aCategory;

    }


    public ArrayList<String> selectMainCategoriesTitle(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select title from tblCategory where parentId=0 order by catId and sortOrder ASC", null);
        ArrayList<String> categoryTitles = new ArrayList<String>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.add(rs.getString(rs.getColumnIndex("title")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Parent Categories Title");
        return categoryTitles;
    }

    public Map<Integer,String> selectMainCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select catId,title from tblCategory where parentId=0 order by catId and sortOrder ASC", null);
        Map<Integer,String> categoryTitles = new HashMap<Integer,String>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.put(rs.getInt(rs.getColumnIndex("catId")),
                            rs.getString(rs.getColumnIndex("title")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Parent Categories Title and ID");
        return categoryTitles;
    }

    public int selectACategoryParent(int catId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblCategory where catID= " + catId, null);
        int parentId = 0;
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    parentId=rs.getInt(rs.getColumnIndex("parentId"));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select Parent of A Category ID");
        return parentId;

    }


    public ArrayList<Integer> selectChildIdOfACategory(int parentID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblCategory where parentId= " + parentID+" order by catId and sortOrder ASC", null);
        ArrayList<Integer> categoryId = new ArrayList<Integer>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryId.add(rs.getInt(rs.getColumnIndex("catId")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Child of A Category ID");
        return categoryId;
    }

    public ArrayList<String> selectChildOfACategoryTitle(int parentID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblCategory where parentId=" + parentID + " order by sortOrder and catId ASC", null);
        ArrayList<String> categoryTitles = new ArrayList<String>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.add(rs.getString(rs.getColumnIndex("title")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Child of A Category Title ");
        return categoryTitles;
    }

    public Map<Integer,String> selectChildOfACategory(int parentID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblCategory where parentId="+parentID+" order by sortOrder ASC", null);
        Map<Integer,String> categoryTitles = new HashMap<Integer,String>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.put(rs.getInt(rs.getColumnIndex("catId")),
                            rs.getString(rs.getColumnIndex("title")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Child of A Category Title and ID");
        return categoryTitles;
    }

    public Map<Integer,String> selectMainCategoryTitle(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select catId,title from tblCategory where parentId=0 order by catId,sortOrder Desc", null);
        Map<Integer,String> categoryTitles = new HashMap<Integer,String>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.put(rs.getInt(rs.getColumnIndex("catId")),
                            rs.getString(rs.getColumnIndex("title")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select Main Category Title and ID");
        return categoryTitles;
    }



    public Map<Integer,String> selectAllCategoryTitle(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select catId,title from tblCategory order by catId,sortOrder ASC", null);
        Map<Integer,String> categoryTitles = new HashMap<Integer,String>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.put(rs.getInt(rs.getColumnIndex("catId")),
                            rs.getString(rs.getColumnIndex("title")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Category Title and ID");
        return categoryTitles;
    }
    public Map<Integer,String> selectAllProductTitle(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select productId,title from tblProduct", null);
        Map<Integer,String> productTitle = new HashMap<Integer,String>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    productTitle.put(rs.getInt(rs.getColumnIndex("productId")),
                            rs.getString(rs.getColumnIndex("title")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product Title and ID");
        return productTitle;
    }


    public ArrayList<Product> selectAllProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProduct order by id Desc", null);
        allProducts = new ArrayList<Product>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {

                    allProducts.add(getAProduct(rs));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product");
        return allProducts;
    }

    public Product getAProduct(Cursor rs){
        Product aProduct = new Product();
        aProduct.setTitle(rs.getString(rs.getColumnIndex("title")));
        aProduct.setId(rs.getInt(rs.getColumnIndex("productId")));
        aProduct.setGroupId(rs.getInt(rs.getColumnIndex("groupId")));
        aProduct.setPrice(rs.getInt(rs.getColumnIndex("price")));
        aProduct.setPriceOff(rs.getInt(rs.getColumnIndex("priceOff")));
        aProduct.setVisits(rs.getInt(rs.getColumnIndex("visits")));
        aProduct.setMinCounts(rs.getInt(rs.getColumnIndex("minCounts")));
        aProduct.setStock(rs.getInt(rs.getColumnIndex("stock")));
        aProduct.setQualityRank(rs.getString(rs.getColumnIndex("qualityRank")));
        aProduct.setCommentsCount(rs.getInt(rs.getColumnIndex("commentsCount")));
        aProduct.setCodeProduct(rs.getString(rs.getColumnIndex("codeProduct")));
        aProduct.setDescription(rs.getString(rs.getColumnIndex("description")));
        aProduct.setSellsCount(rs.getInt(rs.getColumnIndex("sellsCount")));
        aProduct.setTimeStamp(rs.getString(rs.getColumnIndex("timeStamp")));
        aProduct.setShowAtHomeScreen(rs.getInt(rs.getColumnIndex("showAtHomeScreen")));
        aProduct.setWatermarkPath(rs.getString(rs.getColumnIndex("watermarkPath")));
        aProduct.setImagesMainPath(rs.getString(rs.getColumnIndex("imagesMainPath")));
        aProduct.setLike(rs.getInt(rs.getColumnIndex("like")));
        aProduct.setBrandName(rs.getString(rs.getColumnIndex("brandName")));
        aProduct.setLinkInSite(rs.getString(rs.getColumnIndex("linkInSite")));
        aProduct.setImagesPath(selectAllImagePathAProduct(aProduct.getId()));
        aProduct.setProductOptions(selectAllOptionProduct(aProduct.getId()));
        return aProduct;
    }
    public ArrayList<Product> selectAllProductOfACategory(int categoryId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProduct where groupId="+categoryId+" "+ "order by id ASC ", null);
        allProducts = new ArrayList<Product>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    allProducts.add(getAProduct(rs));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product");
        return allProducts;
    }

    public ArrayList<Product> selectProductSmallerPrice(String price){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProduct where price <="+price+" "+ "order by id desc ", null);
        allProducts = new ArrayList<Product>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {

                    allProducts.add(getAProduct(rs));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product that Price Smaller from A Range");
        return allProducts;
    }

    public ArrayList<Product> selectSpecialProduct(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProduct where priceOff !=0 or showAtHomeScreen=1 order by id ASC ", null);
        allProducts = new ArrayList<Product>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {

                    allProducts.add(getAProduct(rs));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Special Product");
        return allProducts;
    }

    public Product selectAProduct(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProduct where productId="+productId, null);
        aProduct= new Product();
        if (rs != null) {
            if (rs.moveToFirst()) {
                    aProduct=getAProduct(rs);
            }
            rs.close();
        }
        Log.v("select", "Select A Product");
        return aProduct;
    }
    public ArrayList<String> selectAllImagePathAProduct(int productId) {
        ArrayList<String> path = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblImagesPathProduct where fkProductId=" + productId + "", null);
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    path.add(rs.getString(rs.getColumnIndex("imagePath")));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        return path;
    }
    public ArrayList<ProductOption> selectAllOptionProduct(int productId) {
        ArrayList<ProductOption> options = new ArrayList<ProductOption>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProductOption where fkProductId=" + productId + "", null);
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    ProductOption aOption = new ProductOption();
                    aOption.setTitle(rs.getString(rs.getColumnIndex("titleOption")));
                    aOption.setValue(rs.getString(rs.getColumnIndex("valueOption")));
                    options.add(aOption);
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        return options;
    }

    public ArrayList<ProductOption> selectProductBrand(int productId) {
        ArrayList<ProductOption> productOptions = new ArrayList<ProductOption>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProductOption where fkProductId="+productId, null);
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    ProductOption aOption=new ProductOption();
                    aOption.setTitle(rs.getString(rs.getColumnIndex("titleOption")));
                    aOption.setValue(rs.getString(rs.getColumnIndex("valueOption")));
                    productOptions.add(aOption);
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        return productOptions;
    }

    public ArrayList<Article> selectAllArticle() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblArticle", null);
        allArticles = new ArrayList<Article>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    Article aArticle = new Article();
                    aArticle.setTitle(rs.getString(rs.getColumnIndex("title")));
                    aArticle.setBrief(rs.getString(rs.getColumnIndex("brief")));
                    aArticle.setDate(rs.getString(rs.getColumnIndex("date")));
                    aArticle.setImageLink(rs.getString(rs.getColumnIndex("imageLink")));
                    aArticle.setLinkInWebsite(rs.getString(rs.getColumnIndex("linkInWebsite")));
                    allArticles.add(aArticle);
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Article");
        return allArticles;
    }



    public void updateLastVersion(String newVersion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lastVersionOfApp", newVersion);
        db.update("tblSetting", values, null, null);
        Log.v("update", "Update Last Version Off App");

    }


    public void updateTimeStamp(String TimeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lastTimeStamp", TimeStamp);
        db.update("tblSetting", values, null, null);
        Log.v("update", "Update Last time stamp");

    }
    public void updateLastArticlesNum(String lastNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lastArticlesNum", lastNum);
        db.update("tblSetting", values, null, null);
        Log.v("update", "Update Last Articles Num");

    }
    public void updateAProduct(Product aProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("tblProduct", addFieldToProductTable(aProduct),
                "productId=" + aProduct.getId(), null);
        Log.v("update", "Update a Product");
    }

    public void updateACategory(Category aCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("tblCategory", addFieldToCategoryTable(aCategory),
                "catId=" + aCategory.getId(), null);
        Log.v("update", "Update a Category");
    }


    public void updateAProductOption(int productId,ProductOption aOption) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("tblProductOption",
                addFieldOptionProduct(productId, aOption.getTitle(), aOption.getValue()),
                "fkProductId=" +productId+" and titleOption= ' "+aOption.getTitle()+"'", null);
        Log.v("update", "Update a Product option");
    }

    public void updateAProductLike(int productId,int like) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("like",like);
        db.update("tblProduct",values,
                "productId=" +productId, null);
        Log.v("update", "Update a Product Like");
    }

    public void updateAShoppingNumberPurchased(int productId,int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("numberPurchased",count);
        db.update("tblShopping",values,
                "fkProductId=" +productId, null);
        Log.v("update", "Update a shopping Number Purchased");
    }

    public void deleteAProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblImagesPathProduct", "fkProductId=" + productId + "", null);
        db.delete("tblProduct", "productId=" + productId + "", null);
        db.close();
        Log.v("delete", "Delete A Product");
    }
    public void deleteAProductShopping(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblShopping", "fkProductId=" + productId + "", null);
        db.close();
        Log.v("delete", "Delete A Product from Shopping Table");
    }
    public void deleteUserInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblUserInfo",null, null);
        db.close();
        Log.v("delete", "Delete A User Information from Table");
    }

    public void deleteAllShoppingTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblShopping",null, null);
        db.close();
        Log.v("delete", "Delete All Record From Shopping Table");
    }
}