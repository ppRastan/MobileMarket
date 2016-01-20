package ir.rastanco.mobilemarket.dataModel.dataBaseConnectionModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;

/**
 * Created by ShaisteS on 1394/10/14.
 * A Singleton Class for Manage CRUD(Create-Read-Update-delete) operation
 */
public class DataBaseHandler  extends SQLiteOpenHelper {

    private static Context dbContext;
    private Product aProduct;
    private Article aArticle;
    private ArrayList<Category> allCategories;
    private ArrayList<Product> allProducts;
    private ArrayList<Article> allArticles;


    public DataBaseHandler(Context context) {
        super(context, "MobileMarket.dbo", null, 1);
        dbContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table tblSetting" +
                "(id Integer primary key AUTOINCREMENT," +
                "lastTimeStamp String)");
        Log.v("create", "Create Setting Table");


        db.execSQL("create table tblCategory" +
                "(id Integer primary key AUTOINCREMENT," +
                "title text," +
                "catId Integer," +
                "parentId Integer," +
                "hasChild Integer)");
        Log.v("create", "Create Table Category");

        db.execSQL("create table tblProduct" +
                "(id Integer primary key AUTOINCREMENT," +
                "title text," +
                "productId Integer," +
                "groupId Integer," +
                "price Integer," +
                "priceOff Integer," +
                "visits Integer," +
                "minCounts Integer," +
                "stock Integer," +
                "qualityRank text," +
                "commentsCount Integer," +
                "codeProduct text," +
                "description text," +
                "sellsCount Integer," +
                "timeStamp text," +
                "showAtHomeScreen Integer," +
                "watermarkPath text," +
                "imagesMainPath text)");
        Log.v("create", "Create Table Product");

        db.execSQL("create table tblImagesPathProduct" +
                "(id Integer primary key AUTOINCREMENT," +
                "fkProductId Integer not null," +
                "imagePath text," +
                "foreign key (fkProductId) references tblProduct(productId))");
        Log.v("create", "Create Table Images Path For Product");

        db.execSQL("create table tblProductOption" +
                "(id Integer primary key AUTOINCREMENT," +
                "fkProductId Integer not null," +
                "titleOption text," +
                "valueOption text," +
                "foreign key (fkProductId) references tblProduct(productId))");
        Log.v("create", "Create Table Options For Product");

        db.execSQL("create table tblArticle" +
                "(id Integer primary key AUTOINCREMENT," +
                "title text," +
                "brief text," +
                "date text," +
                "linkInWebsite text," +
                "imageLink text)");
        Log.v("create", "Create Table Article");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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

    public void insertSetting(String timeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lastTimeStamp", timeStamp);
        db.insert("tblSetting", null, values);
        Log.v("insert", "insert A TimeStamp into Table");
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

    public ArrayList<Category> selectAllCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblCategory", null);
        allCategories = new ArrayList<Category>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    Category aCategory = new Category();
                    aCategory.setTitle(rs.getString(rs.getColumnIndex("title")));
                    aCategory.setId(Integer.parseInt(rs.getString((rs.getColumnIndex("catId")))));
                    aCategory.setParentId(Integer.parseInt(rs.getString((rs.getColumnIndex("parentId")))));
                    aCategory.setHasChild(Integer.parseInt(rs.getString((rs.getColumnIndex("hasChild")))));
                    allCategories.add(aCategory);
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Category");
        return allCategories;
    }

    public ArrayList<Product> selectAllProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProduct order by id desc", null);
        allProducts = new ArrayList<Product>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
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
                    aProduct.setImagesPath(selectAllImagePathAProduct(aProduct.getId()));
                    aProduct.setProductOptions(selectAllOptionProduct(aProduct.getId()));
                    allProducts.add(aProduct);
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product");
        return allProducts;
    }

    public Product selectAProduct(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblProduct where productId="+productId, null);
        aProduct= new Product();
        if (rs != null) {
            if (rs.moveToFirst()) {
                    aProduct.setTitle(rs.getString(rs.getColumnIndex("title")));
                    aProduct.setPrice(rs.getInt(rs.getColumnIndex("price")));
                    aProduct.setPriceOff(rs.getInt(rs.getColumnIndex("priceOff")));
                    aProduct.setImagesMainPath(rs.getString(rs.getColumnIndex("imagesMainPath")));

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

    public void updateTimeStamp(String TimeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lastTimeStamp", TimeStamp);
        db.update("tblSetting", values, null, null);
        Log.v("update", "Update Last time stamp");

    }

    public void updateAProduct(Product aProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("tblProduct", addFieldToProductTable(aProduct),
                "productId=" + aProduct.getId(), null);
        Log.v("update", "Update a Product");
    }

    public void deleteAProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblImagesPathProduct", "fkProductId=" + productId + "", null);
        db.delete("tblProduct", "productId=" + productId + "", null);
        db.close();
        Log.v("delete", "Delete A Product");
    }

}