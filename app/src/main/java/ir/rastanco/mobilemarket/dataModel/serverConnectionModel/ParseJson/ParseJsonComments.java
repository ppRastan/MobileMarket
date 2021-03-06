package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Comment;

/**
 * Created by ShaisteS on 1394/11/25
 * This Class Parse Comment Json String
 */
public class ParseJsonComments {

    public ArrayList<Comment> getAllCommentAProduct(String params) {

        JSONArray dataJsonArr;
        ArrayList<Comment> allComments;
        allComments = new ArrayList<>();

        JSONObject json;
        try {
            json = new JSONObject(params);
            dataJsonArr = json.getJSONArray("comments");
            for (int i = 0; i < dataJsonArr.length(); i++) {
                JSONObject c = dataJsonArr.getJSONObject(i);
                Comment aComment=new Comment();
                aComment.setName(c.getString("n"));
                aComment.setUserEmail(c.getString("e"));
                aComment.setCommentContent(c.getString("c"));
                aComment.setTimeStamp(c.getString("ts"));
                allComments.add(aComment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return allComments;
    }
}


