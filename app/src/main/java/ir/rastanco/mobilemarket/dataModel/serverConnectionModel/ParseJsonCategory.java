package ir.rastanco.mobilemarket.dataModel.serverConnectionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Categories;

/**
 * Created by ShaisteS on 12/31/2015.
 * This Class Parse Category Json String
 */
public class ParseJsonCategory {

    private ArrayList<Categories> allCategory;
    private String yourJsonStringUrl;
    private JSONArray dataJsonArr;

    public ArrayList<Categories> getAllCategory(String params) {

        dataJsonArr = null;
        allCategory=new ArrayList<Categories>();

        try {

            JSONObject json =new JSONObject(params);

            dataJsonArr = json.getJSONArray("allcats");
            for (int i = 0; i < dataJsonArr.length(); i++)
            {
                JSONObject c = dataJsonArr.getJSONObject(i);
                Categories aCategory=new Categories();
                aCategory.setTitle(c.getString("t"));
                aCategory.setId(Integer.parseInt(c.getString("id")));
                aCategory.setParentId(Integer.parseInt(c.getString("pid")));
                aCategory.setHasChild(Integer.parseInt(c.getString("c")));
                aCategory.setName(c.getString("n"));
                aCategory.setNormalImagePath(c.getString("i"));
                aCategory.setWaterMarkedImagePath("wm");
                if (c.has("sub1")){
                    JSONArray sub1Array=c.getJSONArray("sub1");
                    for (int j=0;j<sub1Array.length();j++){
                        JSONObject sub1object=sub1Array.getJSONObject(j);
                    }
                }

                allCategory.add(aCategory);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allCategory;
    }
}
