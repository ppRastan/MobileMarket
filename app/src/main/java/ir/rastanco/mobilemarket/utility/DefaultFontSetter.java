package ir.rastanco.mobilemarket.utility;

/*
created by parisan this class set whole application font
 */
public final class DefaultFontSetter extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GlobalFontSetter.setDefaultFont(this, "MONOSPACE", "fonts/yekan.ttf");
    }
}