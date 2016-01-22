package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.ProductOption;

/**
 * Created by ShaisteS on 1394/10/26.
 */
public class ParseJsonProductOption {

    private ArrayList<ProductOption> productProductOptions;


    public ArrayList<ProductOption> getAllProductOptions(String jsonString) {

        productProductOptions = new ArrayList<ProductOption>();
        JSONArray dataJsonArr = null;

        try {

            JSONObject json =new JSONObject(jsonString);
            dataJsonArr = json.getJSONArray("options");
            for (int i = 0; i < dataJsonArr.length(); i++)
            {
                JSONObject g = dataJsonArr.getJSONObject(i);
                ProductOption aOption=new ProductOption();
                aOption.setTitle(g.getString("g"));
                aOption.setValue(g.getString("o"));
                productProductOptions.add(aOption);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productProductOptions;
    }
}
