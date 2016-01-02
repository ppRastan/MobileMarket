package ir.rastanco.mobilemarket.presenter;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Categories;

/**
 * Created by ShaisteS on 12/31/2015.
 */
public class AsyncTaskParseJsonCategory extends AsyncTask<String, String, ArrayList<Categories>> {

    private ArrayList<Categories> allCategory;
    private String yourJsonStringUrl;
    private JSONArray dataJsonArr;

    @Override
    protected ArrayList<Categories> doInBackground(String... params) {

        yourJsonStringUrl =params[0];
        dataJsonArr = null;
        allCategory=new ArrayList<Categories>();

        try {
            GetJsonFile jParser = new GetJsonFile();
            String jsonFile=jParser.getJSONFromUrl(yourJsonStringUrl);
            JSONObject json =new JSONObject(jsonFile);

            dataJsonArr = json.getJSONArray("category");
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
                allCategory.add(aCategory);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allCategory;
    }

    @Override
    protected void onPreExecute() {}

}
