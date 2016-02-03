package ir.rastanco.mobilemarket.utility;
//Created by parisaRashidiNezhad
public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/B Yekan_p30download.com.ttf");
    }
}