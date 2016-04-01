package ir.rastanco.mobilemarket.utility;

/*
created by parisa this class set whole application font
 */
public final class DefaultFontSetter extends android.app.Application {
    public String defaultFont = "fonts/yekan.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalFontSetter.setDefaultFont(this, "MONOSPACE", defaultFont);
    }
}