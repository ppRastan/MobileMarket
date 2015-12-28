package ir.rastanco.mobilemarket.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Categories;

/**
 * Created by ShaisteS on 12/28/2015.
 */
public class JSONParse {

    public String readJSON() {
        String mResponse=null;
        try {
            File f = new File("/storage/sdcard0/bluetooth/lawBook.json");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            mResponse = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mResponse;
    }

    public ArrayList<Categories> parseJSON(String jsonFile){
        ArrayList<Categories> allCategory=new ArrayList<Categories>();
        try {
            JSONObject decoriss = new JSONObject(jsonFile);
            JSONArray Category=decoriss.getJSONArray("Category");

            for (int i=0;i<Category.length();i++){


            }
            // Getting JSON Array node
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allCategory;
    }
}
