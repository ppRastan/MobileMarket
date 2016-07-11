package ir.rastanco.mobilemarket.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import ir.rastanco.mobilemarket.R;

/**
 * Created by ShaisteS on 5/29/2016.
 */
public class WebView extends Activity{

    private android.webkit.WebView browser;
    private ImageButton backbuttonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        String url= getIntent().getStringExtra("url");
        browser = (android.webkit.WebView) findViewById(R.id.web_view);
        backbuttonPressed = (ImageButton)findViewById(R.id.actionbar_back_icon);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl(url);
        backbuttonPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(browser.canGoBack()) {
                    browser.goBack();
                } else {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();
        }
    }
}