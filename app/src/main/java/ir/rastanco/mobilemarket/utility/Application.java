package ir.rastanco.mobilemarket.utility;
//Created by parisaRashidiNezhad
public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Droid.Arabic.Kufi_p30download.com.TTF");
    }
}