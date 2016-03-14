package ir.rastanco.mobilemarket.utility;

/*
created by parisan this class set whole application font
 */
public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/yekan.ttf");
    }
}