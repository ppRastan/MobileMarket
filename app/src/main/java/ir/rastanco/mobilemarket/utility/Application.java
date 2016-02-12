package ir.rastanco.mobilemarket.utility;

public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/yekan.ttf");
    }
}