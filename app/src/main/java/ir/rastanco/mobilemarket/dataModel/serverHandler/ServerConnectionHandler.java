package ir.rastanco.mobilemarket.dataModel.serverHandler;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.dataModel.Categories;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/10/14.
 */
public class ServerConnectionHandler {

    public ArrayList<Categories> getAllCategoryInfo(String url){

        GetJsonFile jParserCategory = new GetJsonFile();
        String jsonCategory= null;
        try {
            jsonCategory = jParserCategory.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<Categories> allCategoryInfo=new ArrayList<Categories>();
        allCategoryInfo=new ParseJsonCategory().getAllCategory(jsonCategory);

        return allCategoryInfo;

    }

    public ArrayList<Product> getAllProductInfoACategory(String url){

        GetJsonFile jParserProduct = new GetJsonFile();
        String jsonProduct= null;
        try {
            jsonProduct = jParserProduct.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<Product> ProductInfo=new ArrayList<Product>();
        ProductInfo=new ParseJsonProduct().getAllProduct(jsonProduct);
        return  ProductInfo;
    }

    public ArrayList<Product> convertImageUrlToBitmapForProduct(ArrayList<Product> products){

        Bitmap normalImage;
        Bitmap waterMarkImage;
        ArrayList<Bitmap> aNormalImage;
        ArrayList<Bitmap> aWaterMarkImage;
        for (int i=0;i<products.size();i++)
        {
            aNormalImage=new ArrayList<Bitmap>();
            aWaterMarkImage=new ArrayList<Bitmap>();
            for (int j=0;j<products.get(i).getImagesPath().size();j++)
            {
                try {
                    normalImage=new DownloadImage()
                            .execute(products.get(i).getImagesMainPath()+
                                    products.get(i).getImagesPath().get(j)+
                                    "&size="+
                                    Configuration.widthDisplay+"x"+Configuration.widthDisplay+
                                    "&q=30").get();
                    aNormalImage.add(normalImage);
                    waterMarkImage=new DownloadImage()
                            .execute(products.get(i).getWatermarkPath()+
                                    products.get(i).getImagesPath().get(j)+
                                    "&size="+
                                    Configuration.widthDisplay+"x"+Configuration.widthDisplay+
                                    "&q=30").get();
                    aWaterMarkImage.add(waterMarkImage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            products.get(i).setAllNormalImage(aNormalImage);
            products.get(i).setAllWaterMarkImage(aWaterMarkImage);
        }
        return products;
    }


}
