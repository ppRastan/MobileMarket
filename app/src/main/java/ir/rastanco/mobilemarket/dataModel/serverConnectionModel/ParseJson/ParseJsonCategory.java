package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Category;

/**
 * Created by ShaisteS on 12/31/2015.
 * This Class Parse Category Json String
 */
public class ParseJsonCategory {

    private ArrayList<Category> allCategory;
    private String yourJsonStringUrl;
    private JSONArray dataJsonArr;

    public ArrayList<Category> getAllCategory(String params) {

        dataJsonArr = null;
        allCategory=new ArrayList<Category>();

        try {

            JSONObject json =new JSONObject(params);

            dataJsonArr = json.getJSONArray("allcats");
            for (int i = 0; i < dataJsonArr.length(); i++)
            {
                JSONObject c = dataJsonArr.getJSONObject(i);
                Category aCategory=new Category();
                aCategory.setTitle(c.getString("t"));
                aCategory.setId(Integer.parseInt(c.getString("id")));
                aCategory.setParentId(Integer.parseInt(c.getString("pid")));
                aCategory.setHasChild(Integer.parseInt(c.getString("c")));
                allCategory.add(aCategory);
                if (c.has("sub1")){
                    JSONArray sub1Array=c.getJSONArray("sub1");
                    for (int j=0;j<sub1Array.length();j++){
                        Category sub1Category=new Category();
                        JSONObject sub1object=sub1Array.getJSONObject(j);
                        sub1Category.setTitle(sub1object.getString("t"));
                        sub1Category.setId(Integer.parseInt(sub1object.getString("id")));
                        sub1Category.setParentId(Integer.parseInt(sub1object.getString("pid")));
                        sub1Category.setHasChild(Integer.parseInt(sub1object.getString("c")));
                        allCategory.add(sub1Category);
                        if (sub1object.has("sub2")){
                            JSONArray sub2Array=sub1object.getJSONArray("sub2");
                            for (int k=0;k<sub2Array.length();k++){
                                Category sub2Category=new Category();
                                JSONObject sub2object=sub2Array.getJSONObject(k);
                                sub2Category.setTitle(sub2object.getString("t"));
                                sub2Category.setId(Integer.parseInt(sub2object.getString("id")));
                                sub2Category.setParentId(Integer.parseInt(sub2object.getString("pid")));
                                sub2Category.setHasChild(Integer.parseInt(sub2object.getString("c")));
                                allCategory.add(sub2Category);
                            }
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allCategory;
    }
}
