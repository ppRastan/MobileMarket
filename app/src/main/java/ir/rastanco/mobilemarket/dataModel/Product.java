package ir.rastanco.mobilemarket.dataModel;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ShaisteS on 12/27/2015.
 * Managing Product Information.
 */
public class Product {

    private String title;
    private int id;
    private int groupId;
    private int price;
    private int priceOff;
    private int visits;
    private int minCounts;
    private int stock;
    private String qualityRank; //a,b,c,d,e,f,g
    private int commentsCount;
    private String codeProduct; //Name(code)
    private String description;
    private int sellsCount;
    private String timeStamp;
    private int showAtHomeScreen;   //0 or 1
    private String watermarkPath;
    private String imagesMainPath;
    private ArrayList<String> imagesPath;
    private ArrayList<Bitmap> allNormalImage;
    private ArrayList<Bitmap> allWaterMarkImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceOff() {
        return priceOff;
    }

    public void setPriceOff(int priceOff) {
        this.priceOff = priceOff;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public int getMinCounts() {
        return minCounts;
    }

    public void setMinCounts(int minCounts) {
        this.minCounts = minCounts;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getQualityRank() {
        return qualityRank;
    }

    public void setQualityRank(String qualityRank) {
        this.qualityRank = qualityRank;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getCodeProduct() {
        return codeProduct;
    }

    public void setCodeProduct(String codeProduct) {
        this.codeProduct = codeProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSellsCount() {
        return sellsCount;
    }

    public void setSellsCount(int sellsCount) {
        this.sellsCount = sellsCount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getShowAtHomeScreen() {
        return showAtHomeScreen;
    }

    public void setShowAtHomeScreen(int showAtHomeScreen) {
        this.showAtHomeScreen = showAtHomeScreen;
    }

    public String getWatermarkPath() {
        return watermarkPath;
    }

    public void setWatermarkPath(String watermarkPath) {
        this.watermarkPath = watermarkPath;
    }

    public String getImagesMainPath() {
        return imagesMainPath;
    }

    public void setImagesMainPath(String imagesMainPath) {
        this.imagesMainPath = imagesMainPath;
    }

    public ArrayList<String> getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(ArrayList<String> imagesPath) {
        this.imagesPath = imagesPath;
    }

    public ArrayList<Bitmap> getAllNormalImage() {
        return allNormalImage;
    }

    public void setAllNormalImage(ArrayList<Bitmap> allNormalImage) {
        this.allNormalImage = allNormalImage;
    }

    public ArrayList<Bitmap> getAllWaterMarkImage() {
        return allWaterMarkImage;
    }

    public void setAllWaterMarkImage(ArrayList<Bitmap> allWaterMarkImage) {
        this.allWaterMarkImage = allWaterMarkImage;
    }
}
