package app.vercel.miteve.twa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TvActivity extends Activity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public boolean isAndroidApp() {
                return true;
            }
        }, "AndroidApp");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (failingUrl != null) {
                    String html = "<html><body style='background:#000;color:#fff;padding:40px;font-family:sans-serif;text-align:center;'>" +
                        "<h2>Error</h2>" +
                        "<p style='color:#ff6b6b;word-break:break-all;'>URL: " + failingUrl + "</p>" +
                        "<p>Codigo: " + errorCode + " | " + description + "</p>" +
                        "<br><button onclick='location.reload()' style='padding:12px 40px;font-size:18px;background:#fff;color:#000;border:none;border-radius:8px;'>Reintentar</button>" +
                        "</body></html>";
                    view.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                }
            }
        });
        webView.loadUrl("https://miteve.vercel.app/");
        webView.requestFocus();
    }
}
