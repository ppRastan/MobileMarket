package ir.rastanco.mobilemarket.dataModel.serverConnectionModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.dataModel.Categories;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.dataBaseConnectionModel.DataBaseHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by ShaisteS on 1394/10/14.
 * This Class Manage All Connection to Server and DataBase
 */
public class ServerConnectionHandler {

    private Context context;
    private DataBaseHandler dbh;

    public ServerConnectionHandler(Context myContext){
        context=myContext;
        dbh=new DataBaseHandler(myContext);
    }

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
    public ArrayList<Product> addImageToProductInfo(ArrayList<Product> productInfo){
        ArrayList<Bitmap> images;
        Bitmap image;
        for (int i=0;i<productInfo.size();i++){
            images=new ArrayList<Bitmap>();
            for (int j=0;j<productInfo.get(i).getImagesPath().size();j++){
                try {
                    image=new DownloadImage()
                            .execute(productInfo.get(i).getImagesMainPath()+
                                    productInfo.get(i).getImagesPath().get(j)+
                                    "&size="+
                                    Configuration.homeDisplaySize+"x"+Configuration.homeDisplaySize+
                                    "&q=30").get();
                    images.add(image);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            productInfo.get(i).setAllNormalImage(images);
        }
        return productInfo;

    }

    public Boolean emptyDBProduct(){
        Boolean empty=dbh.emptyProductTable();
        return empty;
    }
    public void addAllProductToTable(ArrayList<Product> allProduct){
        for (int i=0;i<allProduct.size();i++){
            dbh.insertAProduct(allProduct.get(i));
            for (int j=0;j<allProduct.get(i).getAllNormalImage().size();j++)
                dbh.insertNormalImageBitmap(allProduct.get(i).getId(), allProduct.get(i).getAllNormalImage().get(j));
            for (int k=0;k<allProduct.get(i).getAllWaterMarkImage().size();k++)
                dbh.insertWaterMarkImageBitmap(allProduct.get(i).getId(),allProduct.get(i).getAllWaterMarkImage().get(k));

        }
    }
    public ArrayList<Product> getAllProductFromTable(){
        ArrayList<Product> allProduct=new ArrayList<Product>();
        ArrayList<byte[]> byteAllImage;
        ArrayList<Bitmap> bitmapAllImage=new ArrayList<Bitmap>();
        allProduct=dbh.selectAllProduct();
        for (int i=0; i<allProduct.size();i++){
            byteAllImage=new ArrayList<byte[]>();
            byteAllImage=dbh.selectNormalImageAProduct(allProduct.get(0).getId());
            for (int j=0; j<byteAllImage.size();j++) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteAllImage.get(j), 0, byteAllImage.get(j).length);
                bitmapAllImage.add(bitmap);
            }
            allProduct.get(i).setAllNormalImage(bitmapAllImage);

        }
        ArrayList<Bitmap> bitmapWAllImage=new ArrayList<Bitmap>();

        for (int i=0; i<allProduct.size();i++){
            byteAllImage=new ArrayList<byte[]>();
            byteAllImage=dbh.selectWaterMarkImageAProduct(allProduct.get(0).getId());
            for (int j=0; j<byteAllImage.size();j++) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteAllImage.get(j), 0, byteAllImage.get(j).length);
                bitmapWAllImage.add(bitmap);
            }
            allProduct.get(i).setAllNormalImage(bitmapWAllImage);
        }
        return allProduct;
    }

}
