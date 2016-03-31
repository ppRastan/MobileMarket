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
 * A Singleton Class for Manage CRUD(Create-Retrieve-Update-Delete) operation
 */
public class DataBaseHandler  extends SQLiteOpenHelper {

    private Product aProduct;
    private ArrayList<Category> allCategories;
    private ArrayList<Product> allProducts;
    private ArrayList<Article> allArticles;

    private static DataBaseHandler sInstance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MobileMarket";
    private Context myContext;

    private final String TABLE_USER_INFO = "tblUserInfo";
    private final String TABLE_SETTINGS = "tblSetting";
    private final String TABLE_SHOPPING = "tblShopping";
    private final String TABLE_CATEGORY = "tblCategory";
    private final String TABLE_PRODUCT = "tblProduct";
    private final String TABLE_IMAGES_PATH_PRODUCT = "tblImagesPathProduct";
    private final String TABLE_PRODUCT_OPTION = "tblProductOption";
    private final String TABLE_ARTICLE = "tblArticle";

    private final String UserInfoTable_Column_Primary_Id ="id";
    private final String UserInfoTable_Column_User_Id = "userId";
    private final String UserInfoTable_Column_User_email="userEmail";
    private final String UserInfoTable_Column_User_Login_Status="userLoginStatus";

    private final String SettingsTable_Column_Primary_Id="id";
    private final String SettingsTable_Column_Last_TimeStamp="lastTimeStamp";
    private final String SettingsTable_Column_Last_Update_TimeStamp ="lastUpdateTimeStamp";
    private final String SettingsTable_Column_Last_Articles_Number="lastArticlesNum";
    private final String SettingsTable_Column_Last_Version_Application="lastVersionOfApp";

    private final String ShoppingTable_Column_Primary_Id="id";
    private final String ShoppingTable_Column_ForeignKey_ProductId="fkProductId";
    private final String ShoppingTable_Column_Number_Purchased="numberPurchased";

    private final String CategoryTable_Column_Primary_Id="id";
    private final String CategoryTable_Column_Title="title";
    private final String CategoryTable_Column_Category_Id="catId";
    private final String CategoryTable_Column_Parent_Id="parentId";
    private final String CategoryTable_Column_SortOrder="sortOrder";
    private final String CategoryTable_Column_Has_Child="hasChild";

    private final String ProductTable_Column_Primary_Key="id";
    private final String ProductTable_Column_Title="title";
    private final String ProductTable_Column_Product_Id="productId";
    private final String ProductTable_Column_Group_Id="groupId";
    private final String ProductTable_Column_Price="price";
    private final String ProductTable_Column_Price_Off="priceOff";
    private final String ProductTable_Column_Visits="visits";
    private final String ProductTable_Column_Brand_Name="brandName";
    private final String ProductTable_Column_Min_Counts="minCounts";
    private final String ProductTable_Column_Stock="stock";
    private final String ProductTable_Column_Quality_Rank="qualityRank";
    private final String ProductTable_Column_Comments_Count="commentsCount";
    private final String ProductTable_Column_Code_Product="codeProduct";
    private final String ProductTable_Column_Description="description";
    private final String ProductTable_Column_Sells_Count="sellsCount";
    private final String ProductTable_Column_TimeStamp="timeStamp";
    private final String ProductTable_Column_Update_TimeStamp="updateTimeStamp";
    private final String ProductTable_Column_Show_AtHome_Screen="showAtHomeScreen";
    private final String ProductTable_Column_Like="like";
    private final String ProductTable_Column_Link_In_Site="linkInSite";
    private final String ProductTable_Column_WaterMark_Path="watermarkPath";
    private final String ProductTable_Column_Images_Main_Path="imagesMainPath";

    private final String ImagePathProductTable_Primary_Key="id";
    private final String ImagePathProductTable_ForeignKey_ProductId="fkProductId";
    private final String ImagePathProductTable_Image_Path="imagePath";

    private final String ProductOptionTable_Primary_Key="id";
    private final String ProductOptionTable_ForeignKey_ProductId="fkProductId";
    private final String ProductOptionTable_Title_Option="titleOption";
    private final String ProductOptionTable_Value_Option="valueOption";

    private final String ArticleTable_Primary_Key="id";
    private final String ArticleTable_Title="title";
    private final String ArticleTable_Brief="brief";
    private final String ArticleTable_Date="date";
    private final String ArticleTable_Link_In_Website="linkInWebsite";
    private final String ArticleTable_Image_Link="imageLink";



    public static synchronized DataBaseHandler getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DataBaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_USER_INFO +
                "(" +
                UserInfoTable_Column_Primary_Id + " Integer primary key AUTOINCREMENT," +
                UserInfoTable_Column_User_Id + " Integer," +
                UserInfoTable_Column_User_email + " String," +
                UserInfoTable_Column_User_Login_Status + " Integer)");
        Log.v("create", "Create User Information Table");

        db.execSQL("create table " + TABLE_SETTINGS +
                "(" +
                SettingsTable_Column_Primary_Id + " Integer primary key AUTOINCREMENT," +
                SettingsTable_Column_Last_TimeStamp + " String," +
                SettingsTable_Column_Last_Update_TimeStamp + " String," +
                SettingsTable_Column_Last_Articles_Number + " String," +
                SettingsTable_Column_Last_Version_Application + " String)");
        Log.v("create", "Create Setting Table");

        db.execSQL("create table " + TABLE_SHOPPING +
                "(" +
                ShoppingTable_Column_Primary_Id + " Integer primary key AUTOINCREMENT," +
                ShoppingTable_Column_ForeignKey_ProductId + " Integer unique," +
                ShoppingTable_Column_Number_Purchased + " Integer," +
                "foreign key (" + ShoppingTable_Column_ForeignKey_ProductId + ") references " +
                TABLE_PRODUCT + "(" + ProductTable_Column_Product_Id + "))");
        Log.v("create", "Create Shopping Table");

        db.execSQL("create table " + TABLE_CATEGORY +
                "(" +
                CategoryTable_Column_Primary_Id + " Integer primary key AUTOINCREMENT," +
                CategoryTable_Column_Title + " text," +
                CategoryTable_Column_Category_Id + " Integer UNIQUE ," +
                CategoryTable_Column_Parent_Id + " Integer," +
                CategoryTable_Column_SortOrder + " Integer," +
                CategoryTable_Column_Has_Child + " Integer)");
        Log.v("create", "Create Table Category");

        db.execSQL("create table " + TABLE_PRODUCT +
                "(" +
                ProductTable_Column_Primary_Key + " Integer primary key AUTOINCREMENT," +
                ProductTable_Column_Title + " text," +
                ProductTable_Column_Product_Id + " Integer UNIQUE," +
                ProductTable_Column_Group_Id + " Integer," +
                ProductTable_Column_Price + " Integer," +
                ProductTable_Column_Price_Off + " Integer," +
                ProductTable_Column_Visits + " Integer," +
                ProductTable_Column_Brand_Name + " text," +
                ProductTable_Column_Min_Counts + " Integer," +
                ProductTable_Column_Stock + " Integer," +
                ProductTable_Column_Quality_Rank + " text," +
                ProductTable_Column_Comments_Count + " Integer," +
                ProductTable_Column_Code_Product + " text," +
                ProductTable_Column_Description + " text," +
                ProductTable_Column_Sells_Count + " Integer," +
                ProductTable_Column_TimeStamp + " text," +
                ProductTable_Column_Update_TimeStamp + " text," +
                ProductTable_Column_Show_AtHome_Screen + " Integer," +
                ProductTable_Column_Like + " Integer," +
                ProductTable_Column_Link_In_Site + " text," +
                ProductTable_Column_WaterMark_Path + " text," +
                ProductTable_Column_Images_Main_Path + " text)");
        Log.v("create", "Create Table Product");

        db.execSQL("create table " + TABLE_IMAGES_PATH_PRODUCT +
                "(" +
                ImagePathProductTable_Primary_Key + " Integer primary key AUTOINCREMENT," +
                ImagePathProductTable_ForeignKey_ProductId + " Integer not null," +
                ImagePathProductTable_Image_Path + " text," +
                "foreign key (" + ImagePathProductTable_ForeignKey_ProductId + ") references " +
                TABLE_PRODUCT + "(" + ProductTable_Column_Product_Id + "))");
        Log.v("create", "Create Table Images Path For Product");

        db.execSQL("create table " + TABLE_PRODUCT_OPTION +
                "(" +
                ProductOptionTable_Primary_Key + " Integer primary key AUTOINCREMENT," +
                ProductOptionTable_ForeignKey_ProductId + " Integer not null," +
                ProductOptionTable_Title_Option + " text," +
                ProductOptionTable_Value_Option + " text," +
                "foreign key (" + ProductOptionTable_ForeignKey_ProductId + ") references " +
                TABLE_PRODUCT + "(" + ProductTable_Column_Product_Id + "))");
        Log.v("create", "Create Table Options For Product");

        db.execSQL("create table "+TABLE_ARTICLE +
                "("+
                ArticleTable_Primary_Key +" Integer primary key AUTOINCREMENT," +
                ArticleTable_Title +" text unique," +
                ArticleTable_Brief +" text," +
                ArticleTable_Date +" text," +
                ArticleTable_Link_In_Website +" text," +
                ArticleTable_Image_Link +" text)");
        Log.v("create", "Create Table Article");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean emptyUserInfoTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from "+TABLE_USER_INFO, null);
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
        Cursor rs = db.rawQuery("select * from "+TABLE_CATEGORY, null);
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
        Cursor rs = db.rawQuery("select * from "+TABLE_PRODUCT, null);
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
        Cursor rs = db.rawQuery("select * from "+TABLE_ARTICLE, null);
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
        String query="select "+ProductTable_Column_Product_Id+
                " from " +TABLE_PRODUCT +
                " where " +ProductTable_Column_Product_Id +"="+ productId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
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

    public Boolean ExistAProductImagePath(int productId,String imagePath) {
        String query="select * from "+TABLE_IMAGES_PATH_PRODUCT+
                " where "+ImagePathProductTable_ForeignKey_ProductId+ " = " + productId +
                " and "+ImagePathProductTable_Image_Path+ "='"+imagePath+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
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
        String query="select "+CategoryTable_Column_Category_Id+
                " from "+TABLE_CATEGORY+"" +
                " where "+CategoryTable_Column_Category_Id+"="+ catId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
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


    public void insertSettingApp(String lastTimeStamp,String articlesNum,String version,String lastUpdateTimeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_SETTINGS, null, addFieldToSettingsTable(lastTimeStamp, articlesNum, version, lastUpdateTimeStamp));
        Log.v("insert", "insert Setting for App");
    }

    private ContentValues addFieldToSettingsTable(String lastTimeStamp,String articlesNum,String version,String lastUpdateTimeStamp){
        ContentValues values=new ContentValues();
        values.put(SettingsTable_Column_Last_TimeStamp,lastTimeStamp);
        values.put(SettingsTable_Column_Last_Articles_Number,articlesNum);
        values.put(SettingsTable_Column_Last_Version_Application, version);
        values.put(SettingsTable_Column_Last_Update_TimeStamp, lastUpdateTimeStamp);
        return values;
    }

    public void insertUserInfo(UserInfo aUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USER_INFO, null, addFieldToUserInfoTable(aUser));
        Log.v("insert", "insert A UserLogin into Table");

    }
    private ContentValues addFieldToUserInfoTable(UserInfo aUser) {
        ContentValues values = new ContentValues();
        values.put(UserInfoTable_Column_User_Id, aUser.getUserId());
        values.put(UserInfoTable_Column_User_email, aUser.getUserEmail());
        values.put(UserInfoTable_Column_User_Login_Status, aUser.getUserLoginStatus());
        return values;
    }

    public void insertShoppingBag(int productID, int numberPurchased) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShoppingTable_Column_ForeignKey_ProductId, productID);
        values.put(ShoppingTable_Column_Number_Purchased,numberPurchased);
        db.insert(TABLE_SHOPPING, null, values);
        Log.v("insert", "insert A ProductId into Shopping Table");
    }
    public void insertACategory(Category aCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CATEGORY, null, addFieldToCategoryTable(aCategory));
        Log.v("insert", "insert A Category into Table");
    }
    private ContentValues addFieldToCategoryTable(Category aCategory) {
        ContentValues values = new ContentValues();
        values.put(CategoryTable_Column_Title, aCategory.getTitle());
        values.put(CategoryTable_Column_Category_Id, aCategory.getId());
        values.put(CategoryTable_Column_Parent_Id, aCategory.getParentId());
        values.put(CategoryTable_Column_Has_Child, aCategory.getHasChild());
        values.put(CategoryTable_Column_SortOrder, aCategory.getSortOrder());
        return values;
    }
    public void insertAProduct(Product aProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCT, null, addFieldToProductTable(aProduct));
        for (int i = 0; i < aProduct.getImagesPath().size(); i++)
            insertImagePathProduct(aProduct.getId(), aProduct.getImagesPath().get(i));
        Log.v("insert", "insert A Product into Table");
    }
    private ContentValues addFieldToProductTable(Product aProduct) {
        ContentValues values = new ContentValues();
        values.put(ProductTable_Column_Title, aProduct.getTitle());
        values.put(ProductTable_Column_Product_Id, aProduct.getId());
        values.put(ProductTable_Column_Group_Id, aProduct.getGroupId());
        values.put(ProductTable_Column_Price, aProduct.getPrice());
        values.put(ProductTable_Column_Price_Off, aProduct.getPriceOff());
        values.put(ProductTable_Column_Visits, aProduct.getVisits());
        values.put(ProductTable_Column_Min_Counts, aProduct.getMinCounts());
        values.put(ProductTable_Column_Stock, aProduct.getStock());
        values.put(ProductTable_Column_Quality_Rank, aProduct.getQualityRank());
        values.put(ProductTable_Column_Comments_Count, aProduct.getCommentsCount());
        values.put(ProductTable_Column_Code_Product, aProduct.getCodeProduct());
        values.put(ProductTable_Column_Description, aProduct.getDescription());
        values.put(ProductTable_Column_Sells_Count, aProduct.getSellsCount());
        values.put(ProductTable_Column_TimeStamp, aProduct.getTimeStamp());
        values.put(ProductTable_Column_Update_TimeStamp,aProduct.getUpdateTimeStamp());
        values.put(ProductTable_Column_Show_AtHome_Screen, aProduct.getShowAtHomeScreen());
        values.put(ProductTable_Column_WaterMark_Path, aProduct.getWatermarkPath());
        values.put(ProductTable_Column_Images_Main_Path, aProduct.getImagesMainPath());
        values.put(ProductTable_Column_Like,aProduct.getLike());
        values.put(ProductTable_Column_Link_In_Site,aProduct.getLinkInSite());
        values.put(ProductTable_Column_Brand_Name,aProduct.getBrandName());
        Log.v("insert", "insert A Field into Product Table");
        return values;
    }
    public void insertImagePathProduct(int productId, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_IMAGES_PATH_PRODUCT, null, addFieldImagePath(productId, path));
        Log.v("insert", "insert A Image Path Product into Table");

    }
    private ContentValues addFieldImagePath(int productId, String path) {
        ContentValues values = new ContentValues();
        values.put(ImagePathProductTable_ForeignKey_ProductId, productId);
        values.put(ImagePathProductTable_Image_Path, path);
        return values;
    }
    public void insertOptionProduct(int productId, ProductOption aOption) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCT_OPTION, null, addFieldOptionProduct(productId,aOption));
        Log.v("insert", "insert A Option of Product into Table");

    }
    private ContentValues addFieldOptionProduct(int productId,ProductOption aOption) {
        ContentValues values = new ContentValues();
        values.put(ProductOptionTable_ForeignKey_ProductId, productId);
        values.put(ProductOptionTable_Title_Option, aOption.getTitle());
        values.put(ProductOptionTable_Value_Option, aOption.getValue());
        return values;
    }
    public void insertArticle(Article aArticle) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_ARTICLE, null, addFieldToArticleTable(aArticle));
        Log.v("insert", "insert A Product into Table");
    }
    private ContentValues addFieldToArticleTable(Article aArticle) {
        ContentValues values = new ContentValues();
        values.put(ArticleTable_Title, aArticle.getTitle());
        values.put(ArticleTable_Brief, aArticle.getBrief());
        values.put(ArticleTable_Date, aArticle.getDate());
        values.put(ArticleTable_Link_In_Website, aArticle.getLinkInWebsite());
        values.put(ArticleTable_Image_Link, aArticle.getImageLink());
        return values;
    }


    public String selectLastVersionApp() {
        String query="select "+ SettingsTable_Column_Last_Version_Application+
                " from "+TABLE_SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        String lastVersion = "";
        if (rs != null) {
            if (rs.moveToFirst()) {
                lastVersion = rs.getString(rs.getColumnIndex(SettingsTable_Column_Last_Version_Application));
            }
            rs.close();
        }
        Log.v("select", "Select Last Version Of App ");
        return lastVersion;
    }


    public String selectLastTimeStamp() {
        String query="select "+SettingsTable_Column_Last_TimeStamp+
                " from "+TABLE_SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        String timeStamp = "";
        if (rs != null) {
            if (rs.moveToFirst()) {
                timeStamp = rs.getString(rs.getColumnIndex(SettingsTable_Column_Last_TimeStamp));
            }
            rs.close();
        }
        Log.v("select", "Select TimeStamp");
        return timeStamp;
    }

    public String selectLastUpdateTimeStamp() {
        String query="select "+SettingsTable_Column_Last_Update_TimeStamp+
                " from "+TABLE_SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        String timeStamp = "";
        if (rs != null) {
            if (rs.moveToFirst()) {
                timeStamp = rs.getString(rs.getColumnIndex(SettingsTable_Column_Last_Update_TimeStamp));
            }
            rs.close();
        }
        Log.v("select", "Select Last Update TimeStamp");
        return timeStamp;
    }

    public String selectLastArticlesNum() {
        String query="select "+SettingsTable_Column_Last_Articles_Number+
                " from "+TABLE_SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        String timeStamp = "";
        if (rs != null) {
            if (rs.moveToFirst()) {
                timeStamp = rs.getString(rs.getColumnIndex(SettingsTable_Column_Last_Articles_Number));
            }
            rs.close();
        }
        Log.v("select", "Select Last Articles Num ");
        return timeStamp;
    }
    public UserInfo selectUserInformation() {
        String query="select * from "+TABLE_USER_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        UserInfo aUser=new UserInfo();
        if (rs != null) {
            if (rs.moveToFirst()) {
                aUser.setUserEmail(rs.getString(rs.getColumnIndex(UserInfoTable_Column_User_email)));
                aUser.setUserId(rs.getInt(rs.getColumnIndex(UserInfoTable_Column_User_Id)));
                aUser.setUserLoginStatus(rs.getInt(rs.getColumnIndex(UserInfoTable_Column_User_Login_Status)));
            }
            rs.close();
        }
        Log.v("select", "Select User Information");
        return aUser;
    }


    public ArrayList<Integer> selectAllIdProductShopping(){
        String query="select * from "+TABLE_SHOPPING+
                " order by "+ShoppingTable_Column_Primary_Id+" DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        ArrayList<Integer> allProductsId=new ArrayList<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    allProductsId.add(rs.getInt(rs.getColumnIndex(ShoppingTable_Column_ForeignKey_ProductId)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product Id From Shopping Table ");
        return  allProductsId;

    }
    public Map<Integer,Integer> selectAllProductShopping(){
        String query="select * from "+TABLE_SHOPPING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        Map<Integer,Integer> allProductsShop=new HashMap<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    allProductsShop.put(rs.getInt(rs.getColumnIndex(ShoppingTable_Column_ForeignKey_ProductId)),
                            rs.getInt(rs.getColumnIndex(ShoppingTable_Column_Number_Purchased)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Products Info From Shopping Table ");
        return  allProductsShop;

    }
    public boolean ExistAProductShopping(int productId){
        String query="select * from "+TABLE_SHOPPING+
                " where "+ShoppingTable_Column_ForeignKey_ProductId+"="+productId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
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
        String query="select * from "+TABLE_SHOPPING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        return  rs.getCount();
    }

    public int numberPurchasedAProduct(int productId){
        String query="select * from "+TABLE_SHOPPING+
                " where "+ShoppingTable_Column_ForeignKey_ProductId+"=" + productId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        int numberPurchased=0;
        if (rs!= null)
            if(rs.moveToFirst())
                numberPurchased=rs.getInt(rs.getColumnIndex(ShoppingTable_Column_Number_Purchased));
        return numberPurchased;

    }

    private Category createACategoryFromCursor(Cursor rs){
        Category aCategory = new Category();
        aCategory.setTitle(rs.getString(rs.getColumnIndex(CategoryTable_Column_Title)));
        aCategory.setId(Integer.parseInt(rs.getString((rs.getColumnIndex(CategoryTable_Column_Category_Id)))));
        aCategory.setParentId(Integer.parseInt(rs.getString((rs.getColumnIndex(CategoryTable_Column_Parent_Id)))));
        aCategory.setHasChild(Integer.parseInt(rs.getString((rs.getColumnIndex(CategoryTable_Column_Has_Child)))));
        aCategory.setSortOrder(Integer.parseInt(rs.getString((rs.getColumnIndex(CategoryTable_Column_SortOrder)))));
        return aCategory;
    }

    public ArrayList<Category> selectAllCategory() {
        String query="select * from "+TABLE_CATEGORY+
                " order by "+CategoryTable_Column_Category_Id+
                " and "+CategoryTable_Column_SortOrder+" ASC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        allCategories = new ArrayList<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    allCategories.add(createACategoryFromCursor(rs));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Category");
        return allCategories;
    }

    public Category selectACategoryWithId(int catId){
        String query="select * from "+TABLE_CATEGORY+
                " where "+CategoryTable_Column_Category_Id+"=" + catId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        Category aCategory = new Category();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    aCategory=createACategoryFromCursor(rs);
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select A Category With Id");
        return aCategory;
    }

    public Map<String,Integer> selectAllCategoryTitleAndId() {
        String query="select * from "+TABLE_CATEGORY+
                " order by "+CategoryTable_Column_Category_Id+
                " and "+CategoryTable_Column_SortOrder+" ASC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        Map<String,Integer> allCategory = new HashMap<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                   allCategory.put(rs.getString(rs.getColumnIndex(CategoryTable_Column_Title)),
                           rs.getInt(rs.getColumnIndex(CategoryTable_Column_Category_Id)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Category Id and title");
        return allCategory;
    }

    public Map<String,Integer> selectChildOfACategoryTitleAndId(int categoryId) {
        String query="select * from "+TABLE_CATEGORY+
                " where "+CategoryTable_Column_Parent_Id+"== "+categoryId+
                " order by "+CategoryTable_Column_Category_Id+
                " and "+CategoryTable_Column_SortOrder+" ASC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        Map<String,Integer> allCategory = new HashMap<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    allCategory.put(rs.getString(rs.getColumnIndex(CategoryTable_Column_Title)),
                            rs.getInt(rs.getColumnIndex(CategoryTable_Column_Category_Id)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Category Id and title");
        return allCategory;
    }



    public ArrayList<String> selectMainCategoriesTitle(){
        String query="select "+CategoryTable_Column_Title+
                " from "+TABLE_CATEGORY+
                " where "+CategoryTable_Column_Parent_Id+"=0"+
                " order by "+CategoryTable_Column_SortOrder+" ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        ArrayList<String> categoryTitles = new ArrayList<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.add(rs.getString(rs.getColumnIndex(CategoryTable_Column_Title)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Parent Categories Title");
        return categoryTitles;
    }

    public Map<String,Integer> selectMainCategories(){
        String query="select "+CategoryTable_Column_Category_Id+","+CategoryTable_Column_Title+
                " from "+TABLE_CATEGORY+
                " where "+CategoryTable_Column_Parent_Id+"=0"+
                " order by "+CategoryTable_Column_Category_Id+
                " and "+CategoryTable_Column_SortOrder+" ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        Map<String,Integer> categoryTitles = new HashMap<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.put(rs.getString(rs.getColumnIndex(CategoryTable_Column_Title)),
                            rs.getInt(rs.getColumnIndex(CategoryTable_Column_Category_Id)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Parent Categories Title and ID");
        return categoryTitles;
    }

    public int selectACategoryParent(int catId){
        String query="select * from "+TABLE_CATEGORY+
                " where "+CategoryTable_Column_Category_Id+"= " + catId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        int parentId = 0;
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    parentId=rs.getInt(rs.getColumnIndex(CategoryTable_Column_Parent_Id));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select Parent of A Category ID");
        return parentId;

    }


    public ArrayList<Integer> selectChildIdOfACategory(int parentID){
        String query="select * from "+TABLE_CATEGORY+
                " where "+CategoryTable_Column_Parent_Id+"= " + parentID+
                " order by "+CategoryTable_Column_Category_Id+
                " and "+CategoryTable_Column_SortOrder+" ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        ArrayList<Integer> categoryId = new ArrayList<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryId.add(rs.getInt(rs.getColumnIndex(CategoryTable_Column_Category_Id)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Child of A Category ID");
        return categoryId;
    }

    public ArrayList<String> selectChildOfACategoryTitle(int parentID){
        String query="select * from "+TABLE_CATEGORY+
                " where "+CategoryTable_Column_Parent_Id+"=" + parentID +
                " order by "+CategoryTable_Column_SortOrder+","+CategoryTable_Column_Category_Id+" ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        ArrayList<String> categoryTitles = new ArrayList<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.add(rs.getString(rs.getColumnIndex(CategoryTable_Column_Title)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Child of A Category Title ");
        return categoryTitles;
    }

    public Map<Integer,String> selectMainCategoryTitle(){
        String query="select "+CategoryTable_Column_Category_Id+","+CategoryTable_Column_Title+
                " from "+TABLE_CATEGORY+
                " where "+CategoryTable_Column_Parent_Id+"=0"+
                " order by "+CategoryTable_Column_Category_Id+","+CategoryTable_Column_SortOrder+" Desc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        Map<Integer,String> categoryTitles = new HashMap<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    categoryTitles.put(rs.getInt(rs.getColumnIndex(CategoryTable_Column_Category_Id)),
                            rs.getString(rs.getColumnIndex(CategoryTable_Column_Title)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select Main Category Title and ID");
        return categoryTitles;
    }


    public Map<Integer,String> selectAllProductTitle(){
        String query="select "+ProductTable_Column_Product_Id+","+ProductTable_Column_Title+
                " from "+TABLE_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        Map<Integer,String> productTitle = new HashMap<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    productTitle.put(rs.getInt(rs.getColumnIndex(ProductTable_Column_Product_Id)),
                            rs.getString(rs.getColumnIndex(ProductTable_Column_Title)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product Title and ID");
        return productTitle;
    }


    public ArrayList<Product> selectAllProduct() {
        String query="select * from "+TABLE_PRODUCT+
                " order by "+ProductTable_Column_Primary_Key+" Desc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        allProducts = new ArrayList<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {

                    allProducts.add(createAProductFromCursor(rs));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product");
        return allProducts;
    }

    public Product createAProductFromCursor(Cursor rs){
        Product aProduct = new Product();
        aProduct.setTitle(rs.getString(rs.getColumnIndex(ProductTable_Column_Title)));
        aProduct.setId(rs.getInt(rs.getColumnIndex(ProductTable_Column_Product_Id)));
        aProduct.setGroupId(rs.getInt(rs.getColumnIndex(ProductTable_Column_Group_Id)));
        aProduct.setPrice(rs.getInt(rs.getColumnIndex(ProductTable_Column_Price)));
        aProduct.setPriceOff(rs.getInt(rs.getColumnIndex(ProductTable_Column_Price_Off)));
        aProduct.setVisits(rs.getInt(rs.getColumnIndex(ProductTable_Column_Visits)));
        aProduct.setMinCounts(rs.getInt(rs.getColumnIndex(ProductTable_Column_Min_Counts)));
        aProduct.setStock(rs.getInt(rs.getColumnIndex(ProductTable_Column_Stock)));
        aProduct.setQualityRank(rs.getString(rs.getColumnIndex(ProductTable_Column_Quality_Rank)));
        aProduct.setCommentsCount(rs.getInt(rs.getColumnIndex(ProductTable_Column_Comments_Count)));
        aProduct.setCodeProduct(rs.getString(rs.getColumnIndex(ProductTable_Column_Code_Product)));
        aProduct.setDescription(rs.getString(rs.getColumnIndex(ProductTable_Column_Description)));
        aProduct.setSellsCount(rs.getInt(rs.getColumnIndex(ProductTable_Column_Sells_Count)));
        aProduct.setTimeStamp(rs.getString(rs.getColumnIndex(ProductTable_Column_TimeStamp)));
        aProduct.setUpdateTimeStamp((rs.getString(rs.getColumnIndex(ProductTable_Column_Update_TimeStamp))));
        aProduct.setShowAtHomeScreen(rs.getInt(rs.getColumnIndex(ProductTable_Column_Show_AtHome_Screen)));
        aProduct.setWatermarkPath(rs.getString(rs.getColumnIndex(ProductTable_Column_WaterMark_Path)));
        aProduct.setImagesMainPath(rs.getString(rs.getColumnIndex(ProductTable_Column_Images_Main_Path)));
        aProduct.setLike(rs.getInt(rs.getColumnIndex(ProductTable_Column_Like)));
        aProduct.setBrandName(rs.getString(rs.getColumnIndex(ProductTable_Column_Brand_Name)));
        aProduct.setLinkInSite(rs.getString(rs.getColumnIndex(ProductTable_Column_Link_In_Site)));
        aProduct.setImagesPath(selectAllImagePathAProduct(aProduct.getId()));
        aProduct.setProductOptions(selectAllOptionProduct(aProduct.getId()));
        return aProduct;
    }
    public ArrayList<Product> selectAllProductOfACategory(int categoryId){
        String query="select * from "+TABLE_PRODUCT+
                " where "+ProductTable_Column_Group_Id+"="+categoryId+
                " order by "+ProductTable_Column_Primary_Key+" ASC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        allProducts = new ArrayList<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    allProducts.add(createAProductFromCursor(rs));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product");
        return allProducts;
    }


    public ArrayList<Product> selectSpecialProduct(){
        String query="select * from "+TABLE_PRODUCT+
                " where "+ProductTable_Column_Price_Off+" !=0"+
                " or "+ProductTable_Column_Show_AtHome_Screen+"=1"+
                " order by "+ProductTable_Column_Primary_Key+" ASC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        allProducts = new ArrayList<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {

                    allProducts.add(createAProductFromCursor(rs));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Special Product");
        return allProducts;
    }

    public Product selectAProduct(int productId) {
        String query="select * from "+TABLE_PRODUCT+
                " where "+ProductTable_Column_Product_Id+"="+productId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        aProduct= new Product();
        if (rs != null) {
            if (rs.moveToFirst()) {
                    aProduct= createAProductFromCursor(rs);
            }
            rs.close();
        }
        Log.v("select", "Select A Product");
        return aProduct;
    }
    public ArrayList<String> selectAllImagePathAProduct(int productId) {
        String query="select * from "+TABLE_IMAGES_PATH_PRODUCT+
                " where "+ImagePathProductTable_ForeignKey_ProductId+"=" + productId;
        ArrayList<String> path = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    path.add(rs.getString(rs.getColumnIndex(ImagePathProductTable_Image_Path)));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        return path;
    }
    private ProductOption createAProductOptionFromCursor(Cursor rs){
        ProductOption aOption = new ProductOption();
        aOption.setTitle(rs.getString(rs.getColumnIndex(ProductOptionTable_Title_Option)));
        aOption.setValue(rs.getString(rs.getColumnIndex(ProductOptionTable_Value_Option)));
        return aOption;
    }

    public ArrayList<ProductOption> selectAllOptionProduct(int productId) {
        ArrayList<ProductOption> options = new ArrayList<>();
        String query="select * from "+TABLE_PRODUCT_OPTION+
                " where "+ProductOptionTable_ForeignKey_ProductId+"=" + productId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    options.add(createAProductOptionFromCursor(rs));
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        return options;
    }

    private Article createAArticleFromCursor(Cursor rs){
        Article aArticle = new Article();
        aArticle.setTitle(rs.getString(rs.getColumnIndex(ArticleTable_Title)));
        aArticle.setBrief(rs.getString(rs.getColumnIndex(ArticleTable_Brief)));
        aArticle.setDate(rs.getString(rs.getColumnIndex(ArticleTable_Date)));
        aArticle.setImageLink(rs.getString(rs.getColumnIndex(ArticleTable_Image_Link)));
        aArticle.setLinkInWebsite(rs.getString(rs.getColumnIndex(ArticleTable_Link_In_Website)));
        return aArticle;
    }
    public ArrayList<Article> selectAllArticle() {
        String query="select * from "+TABLE_ARTICLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery(query, null);
        allArticles = new ArrayList<>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    allArticles.add(createAArticleFromCursor(rs));
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
        values.put(SettingsTable_Column_Last_Version_Application, newVersion);
        db.update(TABLE_SETTINGS, values, null, null);
        Log.v("update", "Update Last Version Off App");

    }


    public void updateTimeStamp(String TimeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SettingsTable_Column_Last_TimeStamp, TimeStamp);
        db.update(TABLE_SETTINGS, values, null, null);
        Log.v("update", "Update Last time stamp");

    }


    public void updateLastUpdateTimeStamp(String updateTimeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SettingsTable_Column_Last_Update_TimeStamp, updateTimeStamp);
        db.update(TABLE_SETTINGS, values, null, null);
        Log.v("update", "Update Last Update Time stamp");

    }

    public void updateAProduct(Product aProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PRODUCT, addFieldToProductTable(aProduct),
                ProductTable_Column_Product_Id+"=" + aProduct.getId(), null);
        Log.v("update", "Update a Product");
    }

    public void updateACategory(Category aCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CATEGORY, addFieldToCategoryTable(aCategory),
                CategoryTable_Column_Category_Id+"=" + aCategory.getId(), null);
        Log.v("update", "Update a Category");
    }


    public void updateAProductLike(int productId,int like) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ProductTable_Column_Like,like);
        db.update(TABLE_PRODUCT, values,
                ProductTable_Column_Product_Id+"="+productId, null);
        Log.v("update", "Update a Product Like");
    }

    public void updateAShoppingNumberPurchased(int productId,int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ShoppingTable_Column_Number_Purchased,count);
        db.update(TABLE_SHOPPING, values,
                ShoppingTable_Column_ForeignKey_ProductId + "=" + productId, null);
        Log.v("update", "Update a shopping Number Purchased");
    }

    public void deleteAProductShopping(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPPING,ShoppingTable_Column_ForeignKey_ProductId + "=" + productId, null);
        Log.v("delete", "Delete A Product from Shopping Table");
    }

    public void deleteUserInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_INFO,null, null);
        Log.v("delete", "Delete A User Information from Table");
    }

    public void deleteAllShoppingTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPPING,null, null);
        Log.v("delete", "Delete All Record From Shopping Table");
    }
}