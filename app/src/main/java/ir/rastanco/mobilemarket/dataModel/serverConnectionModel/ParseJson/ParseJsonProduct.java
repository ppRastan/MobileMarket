package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 03/28/2016.
 */
public class ParseJsonProduct {


    public ArrayList<Product> ParseJsonProducts(String jsonString,int firstIndex,int lastIndex,Boolean lastIndexValidStatus) {

        ArrayList<Product> allProduct;
        JSONArray dataJsonArr;
        ArrayList<String> imagePath;
        allProduct = new ArrayList<>();
        int lastIndexUse;
        try {

            JSONObject json = new JSONObject(jsonString);
            dataJsonArr = json.getJSONArray("product");
            if (lastIndexValidStatus)
                lastIndexUse=lastIndex;
            else
                lastIndexUse=dataJsonArr.length();
            Configuration.getConfig().numberAllProducts=dataJsonArr.length();
            for (int i = firstIndex;i <lastIndexUse ; i++) {
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
