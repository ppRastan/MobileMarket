package ir.rastanco.mobilemarket.dataModel.serverHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Product;

/**
 * Created by ShaisteS on 1/2/2016.
 * This Class Parse Product Json String
 */
public class ParseJsonProduct {

    private ArrayList<Product> allProduct;
    private JSONArray dataJsonArr;
    private ArrayList<String> imagePath;
    public ArrayList<Product> getAllProduct(String params) {

        dataJsonArr = null;
        allProduct=new ArrayList<Product>();

        try {

            JSONObject json =new JSONObject(params);

            dataJsonArr = json.getJSONArray("product");
            for (int i = 0; i < dataJsonArr.length(); i++)
            {
                JSONObject c = dataJsonArr.getJSONObject(i);
                imagePath=new ArrayList<String>();
                Product aProduct=new Product();
                aProduct.setTitle(c.getString("t"));
                aProduct.setId(Integer.parseInt(c.getString("id")));
                aProduct.setGroupId(Integer.parseInt(c.getString("gid")));
                aProduct.setPrice(Integer.parseInt(c.getString("p")));
                aProduct.setPriceOff(Integer.parseInt(c.getString("po")));
                aProduct.setVisits(Integer.parseInt(c.getString("v")));
                aProduct.setMinCounts(Integer.parseInt(c.getString("mc")));
                aProduct.setStock(Integer.parseInt(c.getString("stock")));
                aProduct.setQualityRank(c.getString("qr"));
                aProduct.setCommentsCount(Integer.parseInt(c.getString("cc")));
                aProduct.setCodeProduct(c.getString("n"));
                aProduct.setDescription(c.getString("d"));
                aProduct.setSellsCount(Integer.parseInt(c.getString("s")));
                aProduct.setTimeStamp(c.getString("ts"));
                aProduct.setShowAtHomeScreen(Integer.parseInt(c.getString("h")));
                aProduct.setWatermarkPath(c.getString("wm"));
                aProduct.setImagesMainPath(c.getString("ipath"));
                for (int j=0;j<10;j++){
                    String counter="i"+j;
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
