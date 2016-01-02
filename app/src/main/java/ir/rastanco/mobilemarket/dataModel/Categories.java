package ir.rastanco.mobilemarket.dataModel;

import android.graphics.Bitmap;

/**
 * Created by ShaisteS on 12/28/2015.
 */
public class Categories {

    private String title;
    private int id;
    private int parentId;
    private int hasChild;
    private String name;
    private String normalImagePath;
    private String waterMarkedImagePath;
    private Bitmap image;

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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getHasChild() {
        return hasChild;
    }

    public void setHasChild(int hasChild) {
        this.hasChild = hasChild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalImagePath() {
        return normalImagePath;
    }

    public void setNormalImagePath(String normalImagePath) {
        this.normalImagePath = normalImagePath;
    }

    public String getWaterMarkedImagePath() {
        return waterMarkedImagePath;
    }

    public void setWaterMarkedImagePath(String waterMarkedImagePath) {
        this.waterMarkedImagePath = waterMarkedImagePath;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
