package ir.rastanco.mobilemarket.utility;

/*
created by parisa this class set whole application font
 */
public final class DefaultFontSetter extends android.app.Application {
    public final String defaultFont = "fonts/decoriss_iransans.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalFontSetter.setDefaultFont(this, defaultFont);
    }
}