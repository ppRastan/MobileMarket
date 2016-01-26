package ir.rastanco.mobilemarket.utility;

import ir.rastanco.mobilemarket.utility.FontsOverride;

public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/droidkufi-bold.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE","fonts/B Traffic.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/droidkufi-bold.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/droidkufi-bold.ttf");
    }
}