package ir.rastanco.mobilemarket.dataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ShaisteS on 12/27/2015.
 * This Class is Packaging Product Information
 */
public class Product implements Parcelable {

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
    private String updateTimeStamp;
    private int showAtHomeScreen;   //0=no show or 1=show
    private String watermarkPath;
    private String imagesMainPath;
    private int like;   //0=dislike or 1=like
    private String linkInSite;
    private String brandName;
    private ArrayList<String> imagesPath;
    private ArrayList<ProductOption> productOptions;

    public Product() {

    }

    protected Product(Parcel in) {
        title = in.readString();
        id = in.readInt();
        groupId = in.readInt();
        price = in.readInt();
        priceOff = in.readInt();
        visits = in.readInt();
        minCounts = in.readInt();
        stock = in.readInt();
        qualityRank = in.readString();
        commentsCount = in.readInt();
        codeProduct = in.readString();
        description = in.readString();
        sellsCount = in.readInt();
        timeStamp = in.readString();
        updateTimeStamp = in.readString();
        showAtHomeScreen = in.readInt();
        watermarkPath = in.readString();
        imagesMainPath = in.readString();
        like = in.readInt();
        linkInSite = in.readString();
        brandName = in.readString();
        imagesPath = in.createStringArrayList();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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

    public String getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(String updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public static Creator<Product> getCREATOR() {
        return CREATOR;
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getLinkInSite() {
        return linkInSite;
    }

    public void setLinkInSite(String linkInSite) {
        this.linkInSite = linkInSite;
    }

    public ArrayList<String> getImagesPath() {
        return imagesPath;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setImagesPath(ArrayList<String> imagesPath) {
        this.imagesPath = imagesPath;
    }

    public ArrayList<ProductOption> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(ArrayList<ProductOption> productProductOptions) {
        this.productOptions = productProductOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeInt(groupId);
        dest.writeInt(price);
        dest.writeInt(priceOff);
        dest.writeInt(visits);
        dest.writeInt(minCounts);
        dest.writeInt(stock);
        dest.writeString(qualityRank);
        dest.writeInt(commentsCount);
        dest.writeString(codeProduct);
        dest.writeString(description);
        dest.writeInt(sellsCount);
        dest.writeString(timeStamp);
        dest.writeString(updateTimeStamp);
        dest.writeInt(showAtHomeScreen);
        dest.writeString(watermarkPath);
        dest.writeString(imagesMainPath);
        dest.writeInt(like);
        dest.writeString(linkInSite);
        dest.writeString(brandName);
        dest.writeStringList(imagesPath);
    }
}
