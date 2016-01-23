package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ShaisteS on 1394/10/30.
 */
public class ParseJsonAuthorize {

    public ArrayList<String> getResponse(String response) {

        JSONObject json = null;
        ArrayList<String> responseArray=new ArrayList<String>();
        try {
            json = new JSONObject(response);
            JSONArray dataJsonArr = json.getJSONArray("user");
            for (int i = 0; i < dataJsonArr.length(); i++) {
                JSONObject c = dataJsonArr.getJSONObject(i);
                responseArray.add(c.getString("error"));
                if (c.has("id"))
                    responseArray.add(c.getString("id"));
                if (c.has("email"))
                    responseArray.add(c.getString("email"));
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return responseArray;
    }
}