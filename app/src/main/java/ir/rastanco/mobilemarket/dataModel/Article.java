package ir.rastanco.mobilemarket.dataModel;

/**
 * Created by ShaisteS on 21/10/94.
 */
public class Article {
    private String title;
    private String brief;
    private String date;
    private String LinkInWebsite;
    private String ImageLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLinkInWebsite() {
        return LinkInWebsite;
    }

    public void setLinkInWebsite(String linkInWebsite) {
        LinkInWebsite = linkInWebsite;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }
}
