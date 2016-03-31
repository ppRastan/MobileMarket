package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1394/12/15.
 */
public class ObserverConnectionInternetOK {

    private static Boolean changeConnection;
    private static List<ObserverConnectionInternetOKListener> ChangeConnectionListener=new ArrayList<>();

    public static Boolean getChangeFragmentParameter() {
        return changeConnection;
    }
    public static void setChangeFragmentParameter( Boolean changeConnection) {
        ObserverConnectionInternetOK.changeConnection = changeConnection;
        for (ObserverConnectionInternetOKListener setConnection : ChangeConnectionListener) {
            setConnection.connectionOK();
        }
    }

    public static void ObserverConnectionInternetOKListener(ObserverConnectionInternetOKListener change){
        ChangeConnectionListener.add(change);
    }
}
