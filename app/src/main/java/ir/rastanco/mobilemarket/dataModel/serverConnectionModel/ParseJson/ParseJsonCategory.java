package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import ir.rastanco.mobilemarket.dataModel.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
                aCategory.setId(c.getInt("id"));
                aCategory.setParentId(c.getInt("pid"));
                aCategory.setHasChild(c.getInt("c"));
                aCategory.setHasChild(c.getInt("so"));
                allCategory.add(aCategory);
                if (c.has("sub1")){
                    JSONArray sub1Array=c.getJSONArray("sub1");
                    for (int j=0;j<sub1Array.length();j++){
                        Category sub1Category=new Category();
                        JSONObject sub1object=sub1Array.getJSONObject(j);
                        sub1Category.setTitle(sub1object.getString("t"));
                        sub1Category.setId(sub1object.getInt("id"));
                        sub1Category.setParentId(sub1object.getInt("pid"));
                        sub1Category.setHasChild(sub1object.getInt("c"));
                        sub1Category.setSortOrder(sub1object.getInt("so"));
                        allCategory.add(sub1Category);
                        if (sub1object.has("sub2")){
                            JSONArray sub2Array=sub1object.getJSONArray("sub2");
                            for (int k=0;k<sub2Array.length();k++){
                                Category sub2Category=new Category();
                                JSONObject sub2object=sub2Array.getJSONObject(k);
                                sub2Category.setTitle(sub2object.getString("t"));
                                sub2Category.setId(sub2object.getInt("id"));
                                sub2Category.setParentId(sub2object.getInt("pid"));
                                sub2Category.setHasChild(sub2object.getInt("c"));
                                sub2Category.setSortOrder(sub2object.getInt("so"));
                                allCategory.add(sub2Category);
                                if (sub2object.has("sub3")){
                                    JSONArray sub3Array=sub2object.getJSONArray("sub3");
                                    for (int y=0;y<sub3Array.length();y++){
                                        Category sub3Category=new Category();
                                        JSONObject sub3object=sub3Array.getJSONObject(y);
                                        sub3Category.setTitle(sub3object.getString("t"));
                                        sub3Category.setId(sub3object.getInt("id"));
                                        sub3Category.setParentId(sub3object.getInt("pid"));
                                        sub3Category.setHasChild(sub3object.getInt("c"));
                                        sub3Category.setSortOrder(sub3object.getInt("so"));
                                        allCategory.add(sub3Category);
                                    }
                                }
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
