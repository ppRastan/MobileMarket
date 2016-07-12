package ir.rastanco.mobilemarket;

/**
 * Created by ShaisteS on 7/12/2016.
 */

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
@ReportsCrashes(formUri = "http://mbaas.ir/api/acra/ac786bf0")
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        super.onCreate();
        ACRA.init(this);
    }
}