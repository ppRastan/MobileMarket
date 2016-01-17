package ir.rastanco.mobilemarket.dataModel;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by ShaisteS on 12/28/2015.
 * This Class is Packaging Category Information
 */
public class Category {

    private String title;
    private int id;
    private int parentId;
    private int hasChild;

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

}
