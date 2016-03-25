package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1394/12/02.
 */
public class ObserverFilterAll  {

    private static Boolean addFilterAll;
    private static List<ObserverFilterAllListener> ChangeSetFilterAllListener=new ArrayList<ObserverFilterAllListener>();

    public static Boolean getAddFilterAll() {
        return addFilterAll;
    }
    public static void setAddFilterAll(Boolean addFilterAll) {
        ObserverFilterAll.addFilterAll = addFilterAll;
        for (ObserverFilterAllListener setFilter : ChangeSetFilterAllListener) {
            setFilter.changeFilterAll();
        }
    }

    public static void changeFilterAllListener(ObserverFilterAllListener change){
        ChangeSetFilterAllListener.add(change);
    }
}

