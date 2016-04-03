package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ShaisteS on 1394/10/30.
 * This Class Parse Key Json that send server for application when login
 */
public class ParseJsonKey {

    public String getKey(String key) {

        JSONObject json;
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
