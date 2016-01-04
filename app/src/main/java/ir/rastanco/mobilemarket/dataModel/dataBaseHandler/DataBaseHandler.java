package ir.rastanco.mobilemarket.dataModel.dataBaseHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ShaisteS on 1394/10/14.
 * A Singleton Class for Manage CRUD(Create-Read-Update-delete) operation
 */
public class DataBaseHandler  extends SQLiteOpenHelper {

    private static Context dbContext;
    private static DataBaseHandler dbh=new DataBaseHandler(dbContext);

    public static DataBaseHandler getConfig(){
        if (dbh!= null){
            return dbh;
        }
        else return new DataBaseHandler(dbContext);

    }

    public DataBaseHandler(Context context) {
        super(context, "MobileMarket.dbo", null, 1);
        dbContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table tblCategory"+
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
