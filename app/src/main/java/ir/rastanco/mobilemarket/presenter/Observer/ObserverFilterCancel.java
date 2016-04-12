package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1395/10/24.
 */
public class ObserverFilterCancel {

    private static Boolean filterCancel;
    private final static List<ObserverFilterCancelListener> ChangeSetFilterCancle =new ArrayList<>();

    public static Boolean getFilterCancel() {
        return filterCancel;
    }
    public static void setFilterCancel(Boolean filterCancel) {
        ObserverFilterCancel.filterCancel = filterCancel;
        for (ObserverFilterCancelListener setFilterCancel : ChangeSetFilterCancle) {
            setFilterCancel.filterCancel();
        }
    }

    public static void changeFilterCancel(ObserverFilterCancelListener change){
        ChangeSetFilterCancle.add(change);
    }
}
