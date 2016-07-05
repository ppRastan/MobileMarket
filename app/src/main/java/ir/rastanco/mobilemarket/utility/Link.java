package ir.rastanco.mobilemarket.utility;

import java.util.Map;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.Security;

/**
 * Created by ShaisteS on 1395/1/8.
 * A Singleton Class For Generate Link For get data from server
 */
public class Link {

    private final static Link link = new Link();

    public static Link getInstance() {
        if (link != null) {
            return link;
        } else return new Link();
    }

    public String generateURLForGetAllCategories() {
        //return "http://decoriss.com/json/get,com=allcats&cache=false";
        return "http://decoriss.com/json/get,com=allcatNoLimits&cache=false";
    }

    public String generateUrlForGetNewProduct(String lastTimeStamp) {
        return "http://decoriss.com/json/get,com=product&newfromts=" +
                lastTimeStamp + "&cache=false";
    }

    public String generateForGetLimitedProductOfAMainCategory(int categoryId,int minLimit,int maxLimit){
        return "http://decoriss.com/json/p.php?catid="+categoryId+"&order_by_field=hits&limit="+
                minLimit+"-"+maxLimit+"&order=desc";
    }

    public String generateUrlForGetSpecialProduct(int minLimit,int maxLimit){
        return "http://decoriss.com/json/special.php?limit="+minLimit+"-"+maxLimit+"&order=desc";
    }

    public String generateURLForGetEditProduct(String lastUpdateTimeStamp) {
        return "http://decoriss.com/json/get,com=product&updatefromts=" +
                lastUpdateTimeStamp + "&cache=false";
    }

    public String generateURLForGetProductOptionsOfAProduct(int productId, int groupId) {

        return "http://decoriss.com/json/get,com=options&pid=" +
                String.valueOf(productId) + "&pgid=" + String.valueOf(groupId) + "&cache=false";
    }

    public String generateURLForRefreshArticles(String lastArticlesNum) {
        int endArticle = Integer.parseInt(lastArticlesNum) + Utilities.getInstance().getArticleNumberWhenRefresh();
        return "http://decoriss.com/json/get,com=news&name=blog&order=desc&limit="
                + lastArticlesNum + "-" + String.valueOf(endArticle) + "&cache=false";
    }

    public String generateURLGetAuthorizeResponse(String hashInfo, String key) {
        return "http://decoriss.com/json/get,com=login&u=" + hashInfo +
                "&k=" + key;
    }

    public String generateURLGetAllCommentAProduct(int productId) {
        return "http://decoriss.com/json/get,com=comments&pid=" + productId + "&cache=false";
    }

    public String generateURLForGetArticle(int leastArticleNumber, int highestArticleNumber) {
        return "http://decoriss.com/json/get,com=news&name=blog&order=desc&limit=" +
                String.valueOf(leastArticleNumber) + "-" + String.valueOf(highestArticleNumber) + "&cache=false";
    }

    public String generateURLForGetArticleImage(String articleImageLink) {
        return articleImageLink +
                "&size=" +
                Configuration.getConfig().articleDisplaySizeForURL + "x" + Configuration.getConfig().articleDisplaySizeForURL +
                Utilities.getInstance().getImageQuality();
    }

    public String generateURLForGetImageProduct(String mainImageURL, String imageNumberPath, String imageWidth, String imageHeight) {
        /*String url=Utilities.getInstance().deleteSpaceFromUrl( mainImageURL + imageNumberPath);
        return url;
        */
        return mainImageURL + imageNumberPath +
                "&size=" + imageWidth + "x" + imageHeight +
                "&q=" + Utilities.getInstance().getImageQuality();
    }

    public String generateURLForSendShoppingProductsToServer(String userEmail, Map<Integer, Integer> shopInformation) {
        Security security = new Security();
        String url = "http://decoriss.com/app,data=";
        String urlInfo = userEmail + "##";
        for (Map.Entry<Integer, Integer> entry : shopInformation.entrySet())
            urlInfo = urlInfo + entry.getKey() + "_" + entry.getValue() + "#";
        urlInfo = security.Base64(urlInfo);
        url = url + urlInfo;
        return url;
    }

    public String generateURLForGetUserLasShopping(int userId) {
        return "http://decoriss.com/json/get,com=orders&uid=" +
                userId + "&cache=false";
    }

    public String generateURLForGetKey() {
        return "http://decoriss.com/json/get,com=auth";
    }

    public String generateURLSignUp() {
        return "http://decoriss.com/register,ثبت-نام_";
    }

    public String generateURLForForgotPassword() {
        return "http://decoriss.com/forgetpassword,فراموشی-رمز-عبور_";

    }

    public String generateURLForGetLastVersionAppInServer() {
        return "http://decoriss.com/app/Version.txt";
    }

    public String generateYRLForGetApplicationInServer() {
        return "http://decoriss.com/app/Decoriss.apk";
    }

    public String telephoneNumber() {
        return "tel:02166558994";
    }

    public String generatePathAPKApplicationInMobile() {
        return "/Download/Decoriss.apk";

    }
}
