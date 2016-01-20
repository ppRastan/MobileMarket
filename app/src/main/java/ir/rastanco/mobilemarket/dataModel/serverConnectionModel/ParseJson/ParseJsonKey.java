package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Category;

/**
 * Created by ShaisteS on 1394/10/30.
 */
public class ParseJsonKey {

    public String getKey(String key) {

        JSONObject json = null;
        String keyGenerated="";
        try {
            json = new JSONObject(key);
            JSONArray dataJsonArr = json.getJSONArray("auth");
            for (int i = 0; i < dataJsonArr.length(); i++) {
                JSONObject c = dataJsonArr.getJSONObject(i);
                keyGenerated=c.getString("key");
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return keyGenerated;
    }
}
