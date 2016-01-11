package ir.rastanco.mobilemarket.dataModel.dataBaseConnectionModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Product;

/**
 * Created by ShaisteS on 1394/10/14.
 * A Singleton Class for Manage CRUD(Create-Read-Update-delete) operation
 */
public class DataBaseHandler  extends SQLiteOpenHelper {

    private static Context dbContext;
    private Product aProduct;
    private ArrayList<Product> allProduct;

    public DataBaseHandler(Context context) {
        super(context, "MobileMarket.dbo", null, 1);
        dbContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table tblCategory" +
                "(id Integer primary key AUTOINCREMENT," +
                "title text," +
                "catId Integer," +
                "parentId Integer," +
                "hasChild Integer," +
                "name text," +
                "normalImagePath text," +
                "waterMarkedImagePath text)");
        Log.v("create", "Create Table Category");

        db.execSQL("create table tblProduct"+
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

        db.execSQL("create table tblImagesPathProduct"+
                "(id Integer primary key AUTOINCREMENT," +
                "fkProductId Integer not null," +
                "imagePath text," +
                "foreign key (fkProductId) references tblProduct(productId))");
        Log.v("create", "Create Table Images Path For Product");

        db.execSQL("create table tblNormalImageProduct"+
                "(id Integer primary key AUTOINCREMENT," +
                "fkProductId Integer not null," +
                "normalImage BLOB," +
                "foreign key (fkProductId) references tblProduct(productId))");
        Log.v("create", "Create Table Normal Image For Product");

        db.execSQL("create table tblWaterMarkImageProduct" +
                "(id Integer primary key AUTOINCREMENT," +
                "fkProductId Integer not null," +
                "waterMarkImage BLOB," +
                "foreign key (fkProductId) references tblProduct(productId))");
        Log.v("create", "Create Table WaterMark Image For Product");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean emptyProductTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery( "select * from tblProduct", null );
        if (rs.moveToFirst() ) {
            //Not empty
            return false;
        }
        else
        {
            //Is Empty
            return true;
        }
    }

    public void insertAProduct(Product aProduct){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblProduct", null,addFieldToProductTable(aProduct));
        Log.v("insert", "insert A Product into Table");
        db.close();
    }
    private ContentValues addFieldToProductTable(Product aProduct){
        ContentValues values = new ContentValues();
        values.put("title", aProduct.getTitle());
        values.put("productId",aProduct.getId());
        values.put("groupId",aProduct.getGroupId());
        values.put("price",aProduct.getPrice());
        values.put("priceOff",aProduct.getPriceOff());
        values.put("visits",aProduct.getVisits());
        values.put("minCounts",aProduct.getMinCounts());
        values.put("stock",aProduct.getStock());
        values.put("qualityRank",aProduct.getQualityRank());
        values.put("commentsCount",aProduct.getCommentsCount());
        values.put("codeProduct",aProduct.getCodeProduct());
        values.put("description",aProduct.getDescription());
        values.put("sellsCount",aProduct.getSellsCount());
        values.put("timeStamp",aProduct.getTimeStamp());
        values.put("showAtHomeScreen",aProduct.getShowAtHomeScreen());
        values.put("watermarkPath",aProduct.getWatermarkPath());
        values.put("imagesMainPath",aProduct.getImagesMainPath());
        Log.v("insert", "insert A Field into Product Table");
        return values;
    }

    public void insertImagePathProduct(int productId,String path){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblImagesPathProduct", null,addFieldImagePath(productId, path));
        Log.v("insert", "insert A Image Path Product into Table");
        db.close();

    }
    private ContentValues addFieldImagePath(int productId,String path) {
        ContentValues values = new ContentValues();
        values.put("fkProductId", productId);
        values.put("imagePath", path);
        return values;
    }

    public void insertNormalImageBitmap(int productId,Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblNormalImageProduct", null,addFieldImageBitmap(productId, image));
        Log.v("insert", "insert A Image Bitmap Product into Table");
        db.close();

    }
    private ContentValues addFieldImageBitmap(int productId,Bitmap image) {
        ContentValues values = new ContentValues();
        values.put("fkProductId", productId);
        values.put("normalImage", String.valueOf(image));
        return values;
    }

    public void insertWaterMarkImageBitmap(int productId,Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblNormalImageProduct", null,addFieldWImageBitmap(productId, image));
        Log.v("insert", "insert A waterMark Image Bitmap Product into Table");
        db.close();

    }
    private ContentValues addFieldWImageBitmap(int productId,Bitmap image) {
        ContentValues values = new ContentValues();
        values.put("fkProductId", productId);
        values.put("waterMarkImage", String.valueOf(image));
        return values;
    }

    public ArrayList<Product> selectAllProduct(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs =  db.rawQuery( "select * from tblProduct", null );
        allProduct=new ArrayList<Product>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    Product aProduct=new Product();
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
                    allProduct.add(aProduct);
                }
                while (rs.moveToNext());
            }
            rs.close();
        }
        Log.v("select", "Select All Product");
        return allProduct;
    }
    public ArrayList<String> selectAllImagePathAProduct(int productId) {
        ArrayList<String> path = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblImagesPathProduct where fkProductId=" + productId + "", null);
        allProduct = new ArrayList<Product>();
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    path.add(rs.getString(rs.getColumnIndex("imagePath")));
                }
                while (rs.moveToNext());
            }
        }
        return path;
    }
    public ArrayList<byte[]> selectNormalImageAProduct(int productId){
        ArrayList<byte[]> nImage=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblNormalImageProduct where fkProductId=" + productId + "", null);
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    nImage.add(rs.getBlob(rs.getColumnIndex("normalImage")));
                }
                while (rs.moveToNext());
            }
        }
        return nImage;
    }
    public ArrayList<byte[]> selectWaterMarkImageAProduct(int productId){
        ArrayList<byte[]> nImage=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from tblWaterMarkImageProduct where fkProductId=" + productId + "", null);
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    nImage.add(rs.getBlob(rs.getColumnIndex("waterMarkImage")));
                }
                while (rs.moveToNext());
            }
        }
        return nImage;
    }
}
