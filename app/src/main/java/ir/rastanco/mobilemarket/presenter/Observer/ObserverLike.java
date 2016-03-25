package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS 13/2016.
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
