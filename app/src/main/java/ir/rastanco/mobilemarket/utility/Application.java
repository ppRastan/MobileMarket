package ir.rastanco.mobilemarket.utility;

/**
 * Created by ParisaRashidhi on 18/04/2016.
 */
public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/iransans_mobile_font_regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/iransans_mobile_font_regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/iransans_mobile_font_regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/iransans_mobile_font_regular.ttf");
    }
}
