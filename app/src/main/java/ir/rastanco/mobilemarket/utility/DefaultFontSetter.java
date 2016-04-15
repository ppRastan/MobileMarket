package ir.rastanco.mobilemarket.utility;

/*
created by parisa this class set whole application font
 */
public final class DefaultFontSetter extends android.app.Application {
    public final String defaultFont = "fonts/iransans_regular.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalFontSetter.setDefaultFont(this, defaultFont);
    }
}