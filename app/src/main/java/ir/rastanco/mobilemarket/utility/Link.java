package ir.rastanco.mobilemarket.utility;

import java.util.Map;

import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.Security;

/**
 * Created by ShaisteS on 1395/1/8.
 * A Singleton Class For Generate Link For get data from server
 */
public class Link {

    private static Link link = new Link();

    public static Link getInstance() {
        if (link != null) {
            return link;
        }
        else return new Link();
    }

    public String generateURLForGetAllCategories(){
        String url="http://decoriss.com/json/get,com=allcats&cache=false";
        return url;
    }

    public String generateUrlForGetNewProduct(String lastTimeStamp){
        String url="http://decoriss.com/json/get,com=product&newfromts="+
                lastTimeStamp+"&cache=false";
        return url;
    }

    public String generateURLForGetEditProduct(String lastUpdateTimeStamp){
        String url="http://decoriss.com/json/get,com=product&updatefromts=" +
                lastUpdateTimeStamp+"&cache=false";
        return url;
    }

    public String generateURLForGetProductOptionsOfAProduct(int productId,int groupId){
        String url="http://decoriss.com/json/get,com=options&pid=" +
                String.valueOf(productId) + "&pgid=" + String.valueOf(groupId) + "&cache=false";
        return url;
    }

    public String generateURLForRefreshArticles(String lastArticlesNum){
        int endArticle=Integer.parseInt(lastArticlesNum)+Utilities.getInstance().getArticleNumberWhenRefresh();
        String url="http://decoriss.com/json/get,com=news&name=blog&order=desc&limit="
                +lastArticlesNum+"-"+String.valueOf(endArticle)+"&cache=false";
        return url;
    }

    public String generateURLGetAuthorizeResponse(String hashInfo, String key) {
        String url = "http://decoriss.com/json/get,com=login&u=" + hashInfo +
                "&k=" + key;
        return url;
    }

    public String generateURLGetAllCommentAProduct(int productId) {
        String url = "http://decoriss.com/json/get,com=comments&pid=" + productId + "&cache=false";
        return url;
    }

    public String generateURLForGetArticle(int leastArticleNumber,int highestArticleNumber){
        String url="http://decoriss.com/json/get,com=news&name=blog&order=desc&limit="+
                String.valueOf(leastArticleNumber)+"-"+String.valueOf(highestArticleNumber)+"&cache=false";
        return url;
    }

    public String generateURLForGetArticleImage(String articleImageLink){
        String url= articleImageLink+
                "&size="+
                Configuration.getConfig().articleDisplaySizeForURL +"x"+Configuration.getConfig().articleDisplaySizeForURL +
                Utilities.getInstance().getImageQuality();
        return url;
    }

    public String generateURLForGetImageProduct(String mainImageURL,String imageNumberPath,String imageWidth,String imageHeight){
        String url = mainImageURL +imageNumberPath +
                "&size=" + imageWidth + "x" + imageHeight +
                "&q="+Utilities.getInstance().getImageQuality();
        return url;
    }

    public String generateURLForSendShoppingProductsToServer(String userEmail,Map<Integer,Integer> shopInformation){
        Security security=new Security();
        String url = "http://decoriss.com/app,data=";
        String urlInfo = userEmail + "##";
        for (Map.Entry<Integer, Integer> entry : shopInformation.entrySet())
            urlInfo = urlInfo + entry.getKey() + "_" + entry.getValue() + "#";
        urlInfo = security.Base64(urlInfo);
        url = url + urlInfo;
        return url;
    }

    public String generateURLForGetUserLasShopping(int userId){
        String url="http://decoriss.com/json/get,com=orders&uid="+
                userId+"&cache=false";
        return url;
    }

    public String generateURLForGetKey(){
        String url="http://decoriss.com/json/get,com=auth";
        return url;
    }

    public String generateURLSignUp(){
        String url="http://decoriss.com/register,ثبت-نام_";
        return url;
    }

    public String generateURLForForgotPassword(){
        String url="http://decoriss.com/forgetpassword,فراموشی-رمز-عبور_";
        return url;

    }

    public String generateURLForGetLastVersionAppInServer(){
        String url="http://decoriss.com/app/Version.txt";
        return url;
    }

    public String generateYRLForGetApplicationInServer(){
        String url="http://decoriss.com/app/Decoriss.apk";
        return url;
    }

    public String telephoneNumber(){
        String telephone="tel:02166558994";
        return telephone;
    }

    public String generatePathAPKApplicationInMobile(){
        String path="/Download/Decoriss.apk";
        return path;

    }
}
