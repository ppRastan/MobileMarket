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
public class AsyncTaskParseJson extends AsyncTask<String, String, ArrayList<Categories>> {

    ArrayList<Categories> allCategory;

    // set your json string url here
    String yourJsonStringUrl;

    JSONArray dataJsonArr = null;
    @Override
    protected void onPreExecute() {}
    @Override
    protected ArrayList<Categories> doInBackground(String... params) {

        yourJsonStringUrl =params[0];

        try {
            // instantiate our json parser
            GetJsonFile jParser = new GetJsonFile();

            // get json string from url
            String jsonFile=jParser.getJSONFromUrl(yourJsonStringUrl);
            String customizeJsonFile=jsonFile.substring(11);
            JSONObject json =new JSONObject(customizeJsonFile);

            // get the array of all category
            dataJsonArr = json.getJSONArray("category");
            allCategory=new ArrayList<Categories>();

            // loop through all category
            for (int i = 0; i < dataJsonArr.length()-1; i++) {

                JSONObject c = dataJsonArr.getJSONObject(i);
                Categories aCategory=new Categories();
                aCategory.setTitle(c.getString("title"));
                aCategory.setId(Integer.parseInt(c.getString("id")));
                aCategory.setParent_id(Integer.parseInt(c.getString("parent_id")));
                aCategory.setHasChild(Integer.parseInt(c.getString("hasChild")));
                aCategory.setName(c.getString("name"));
                allCategory.add(aCategory);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allCategory;
    }
}
