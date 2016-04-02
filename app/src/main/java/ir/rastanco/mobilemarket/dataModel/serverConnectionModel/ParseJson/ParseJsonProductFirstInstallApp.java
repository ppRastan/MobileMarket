package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;

/**
 * Created by ShaisteS on 03/28/2016.
 */
public class ParseJsonProductFirstInstallApp {

    private final ServerConnectionHandler serverConnectionHandler;

    public ParseJsonProductFirstInstallApp(Context context){
        serverConnectionHandler=ServerConnectionHandler.getInstance(context);
    }

    public String getProductInfoFromServer(String urlProduct) {

        HttpURLConnection connection = null;
        InputStream is = null;
        String jsonString = "";
        try {
            URL url = new URL(urlProduct);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        Log.v("connect", "Connect to Internet");
        int response = 0;
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            response = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("RequestGet", "Request Method Get");
        Log.v("Response", Integer.toString(response));
        try {
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsonString = sb.toString();

        } catch (Exception e) {
            Log.e("TAG", "Error converting result " + e.toString());
        }
        return jsonString;
    }

    public ArrayList<Product> ParseJsonProducts(String jsonString) {

        ArrayList<Product> allProduct;
        JSONArray dataJsonArr;
        ArrayList<String> imagePath;
        allProduct = new ArrayList<>();

        try {

            JSONObject json = new JSONObject(jsonString);
            dataJsonArr = json.getJSONArray("product");
            for (int i = 0; i < dataJsonArr.length(); i++) {
                JSONObject c = dataJsonArr.getJSONObject(i);
                imagePath = new ArrayList<>();
                Product aProduct = new Product();
                aProduct.setTitle(c.getString("t"));
                aProduct.setId(Integer.parseInt(c.getString("id")));
                aProduct.setGroupId(Integer.parseInt(c.getString("gid")));
                aProduct.setPrice(Integer.parseInt(c.getString("p")));
                aProduct.setPriceOff(Integer.parseInt(c.getString("po")));
                //pp
                aProduct.setVisits(Integer.parseInt(c.getString("v")));
                aProduct.setMinCounts(Integer.parseInt(c.getString("mc")));
                aProduct.setBrandName(c.getString("brndname"));
                aProduct.setStock(Integer.parseInt(c.getString("stock")));
                aProduct.setQualityRank(c.getString("qr"));
                aProduct.setCommentsCount(Integer.parseInt(c.getString("cc")));
                aProduct.setCodeProduct(c.getString("n"));
                aProduct.setDescription(c.getString("d"));
                aProduct.setSellsCount(Integer.parseInt(c.getString("s")));
                aProduct.setTimeStamp(c.getString("ts"));
                aProduct.setUpdateTimeStamp(c.getString("update_ts"));

                aProduct.setShowAtHomeScreen(Integer.parseInt(c.getString("h")));
                aProduct.setWatermarkPath(c.getString("wm"));
                aProduct.setImagesMainPath(c.getString("ipath"));
                aProduct.setLinkInSite(c.getString("l"));
                for (int j = 0; j < 10; j++) {
                    String counter = "i" + j;
                    if (c.has(counter))
                        imagePath.add(c.getString(counter));
                }
                aProduct.setImagesPath(imagePath);
                allProduct.add(aProduct);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allProduct;
    }
}
