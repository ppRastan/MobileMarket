package ir.rastanco.mobilemarket.presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sha on shaisteS 13/2016.
 */
public class ObserverLike {

    private static int likeStatus;
    private static List<ObserverLikeListener> ChangeLikeListener=new ArrayList<ObserverLikeListener>();


    public static int getLikeStatus() {
        return likeStatus;
    }
    public static void setLikeStatus(int likeStatus) {
        ObserverLike.likeStatus = likeStatus;
        for (ObserverLikeListener change : ChangeLikeListener) {
            change.changeLikeStatus();
        }
    }

    public static void changeLikeStatusListener(ObserverLikeListener change){
        ChangeLikeListener.add(change);
    }
}
