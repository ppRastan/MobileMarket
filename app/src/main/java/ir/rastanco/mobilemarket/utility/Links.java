package ir.rastanco.mobilemarket.utility;

/**
 * Created by ShaisteS on 1395/1/8.
 * A Singleton Class For Generate Link For get data from server
 */
public class Links {

    private static Links link = new Links();

    public static Links getInstance() {
        if (link != null) {
            return link;
        }
        else return new Links();
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
        int endArticle=Integer.parseInt(lastArticlesNum)+100;
        String url="http://decoriss.com/json/get,com=news&name=blog&order=desc&limit="
                +lastArticlesNum+"-"+String.valueOf(endArticle)+"&cache=false";
        return url;
    }

    public String generateGetAuthorizeResponse(String hashInfo,String key) {
        String url = "http://decoriss.com/json/get,com=login&u=" + hashInfo +
                "&k=" + key;
        return url;
    }

    public String generateGetAllCommentAProduct(int productId) {
        String url = "http://decoriss.com/json/get,com=comments&pid=" + productId + "&cache=false";
        return url;
    }


    }
