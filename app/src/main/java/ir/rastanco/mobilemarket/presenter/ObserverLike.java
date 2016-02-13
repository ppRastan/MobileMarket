package ir.rastanco.mobilemarket.presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sha on shaisteS 13/2016.
 */
public class ObserverLike {

    private static int likeStatus;
    private static List<ChangeLikeListener> ChangeLikeListener=new ArrayList<ChangeLikeListener>();


    public static int getLikeStatus() {
        return likeStatus;
    }
    public static void setLikeStatus(int likeStatus) {
        ObserverLike.likeStatus = likeStatus;
        for (ChangeLikeListener change : ChangeLikeListener) {
            change.changeLikeStatus();
        }
    }

    public static void changeLikeStatusListener(ChangeLikeListener change){
        ChangeLikeListener.add(change);
    }
}
