package ir.rastanco.mobilemarket.utility;

public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/bnazanin.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE","fonts/B Roya.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/bnazanin.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/bnazanin.ttf");
    }
}