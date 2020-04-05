package com.example.webviewsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    private int index = 1;

//    String sampleURL = "https://giris.turkiye.gov.tr/Giris/gir?oauthClientId=d6b3b661-173f-4cce-b024-4cac171a3862&continue=https%3A%2F%2Fgiris.turkiye.gov.tr%2FOAuth2AuthorizationServer%2FAuthorizationController%3Fresponse_type%3Dcode%26client_id%3Dd6b3b661-173f-4cce-b024-4cac171a3862%26state%3De51f24c9-818b-4f56-b1e8-7ec6a56517c4%26scope%3DKimlik-Dogrula%26redirect_uri%3Dhttps%253A%252F%252Fservisoag.icisleri.gov.tr%252FServices%252FProducer%252FTurksat%252FSecurityTokenService";
    //String sampleURL = "https://giris.turkiye.gov.tr/OAuth2AuthorizationServer/AuthorizationController?response_type=code&client_id=d6b3b661-173f-4cce-b024-4cac171a3862&state=e51f24c9-818b-4f56-b1e8-7ec6a56517c4&scope=Kimlik-Dogrula&redirect_uri=https://servisoag.icisleri.gov.tr/Services/Producer/Turksat/SecurityTokenService";
//    String sampleURL = "https://www.turkiye.gov.tr/";
    public static int WAITING_TIME = 1000;
    private String javascriptTest = "(function() { return 'this'; })();";
    private String tcNo= "";
    private String pageRefreshJsFunction = "(function() { window.location.reload(); return false; })();";
    private String pageTcNoJsFunction = "(function() { return document.getElementById('tridField').value; })();";
    private final String[] rsltMeta1 = new String[1];
    private String playStoreMetaClearJSFunction =
            "(" +
            "function() " +
                "{ " +
                    "const metas = document.getElementsByTagName('meta');" +
                    "var result= '';" +
                    "for (let i = 0; i < metas.length; i++) " +
                    "{" +
                    "if (metas[i].getAttribute('name') === 'google-play-app') " +
                        "{" +
//                                  "document.getElementsByTagName('google-play-app').setAttribute('content', 'test');" +
//                                  "metas[i].setAttribute('content', 'test');" +
                          "result = metas[i].getAttribute('name');" +
                          "metas[i].remove();" +
                        "}" +
                    "}" +
//                    "window.location.reload();" +
                    "return result; " +
                "}" +
            ")();";

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // Loading started for URL
//            Thread timerThread = new Thread()
//            {
//                public void run(){
//                    try{
//                        sleep(WAITING_TIME);
//                    }catch(InterruptedException e){
//                        e.printStackTrace();
//                    }finally{
//                        Toast.makeText(context, "Sayfa yukleniyor", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };
//            timerThread.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Sayfa yuklenmeye baslandi", Toast.LENGTH_SHORT).show();
                }
            },WAITING_TIME);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Redirecting to URL
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Redirecting to URL", Toast.LENGTH_SHORT).show();
                }
            },WAITING_TIME);
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(final WebView view, String url) {
            // Loading finished for URL
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Yukleme islemi tamamlandi", Toast.LENGTH_SHORT).show();
                }
            },WAITING_TIME);
            /*Dom icinden tc no degerinin cekilmesi*/
            view.evaluateJavascript(pageTcNoJsFunction, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String tcNoStr) {
//                        Log.println(Log.INFO,"Script sonucu :", s); // Prints: "this"
//                    tcNo = s;
                    Log.i("GirilmisTCNo :", tcNoStr); // Prints: "this"
//                    Toast.makeText(context, "Script sonucu : " + tcNoStr, Toast.LENGTH_LONG).show();
                }
            });
//API19 oncesi destegi icin;
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                // In KitKat+ you should use the evaluateJavascript method
//                view.evaluateJavascript(javascript, new ValueCallback<String>() {
//                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//                    @Override
//                    public void onReceiveValue(String s) {
//                        JsonReader reader = new JsonReader(new StringReader(s));
//
//                        // Must set lenient to parse single values
//                        reader.setLenient(true);
//
//                        try {
//                            if(reader.peek() != JsonToken.NULL) {
//                                if(reader.peek() == JsonToken.STRING) {
//                                    String msg = reader.nextString();
//                                    if(msg != null) {
//                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }
//                        } catch (IOException e) {
//                            Log.e("TAG", "MainActivity: IOException", e);
//                        } finally {
//                            try {
//                                reader.close();
//                            } catch (IOException e) {
//                                // NOOP
//                            }
//                        }
//                    }
//                });
//            }

            /*GooglePlayMetaSonuc degerinin header dan kaldirilmasi islemi - test*/
            view.evaluateJavascript(playStoreMetaClearJSFunction, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String rsltMeta) {
                    if(!rsltMeta.isEmpty() && rsltMeta.contains("google-play-app")) {
                        rsltMeta1[0] = rsltMeta;
                    }
                    Log.i("GooglePlayMetaSonuc :", rsltMeta); // Prints: "this"
                }
            });

            view.evaluateJavascript("(function() { return document.getElementsByClassName(\"fieldError\")[0]; })();", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String rsltErr) {
                    Log.i("ErrorFieldResult :", rsltErr);
                }
            });

            /*Sayfa icindeki mobil uygulama redirect banner remove scriptidir.Sayfa yuklendikten 2 saniye sonra remove edilebiliyor.(Contributed by Resul Riza DOLANER)*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.loadUrl("javascript:(function() {document.documentElement.style.marginTop = \"0px\";})()");
                    view.loadUrl("javascript:document.getElementById(\"smartbanner\").setAttribute(\"style\",\"display:none;\");");
                }
            }, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        WebView webView = new WebView(context);
        URI lastUri = null;
        try {
            String baseUrl = "https://giris.turkiye.gov.tr/OAuth2AuthorizationServer/AuthorizationController";
            String qParam = "response_type=code&client_id=d6b3b661-173f-4cce-b024-4cac171a3862&state=e51f24c9-818b-4f56-b1e8-7ec6a56517c4&scope=Kimlik-Dogrula&redirect_uri=https://servisoag.icisleri.gov.tr/Services/Producer/Turksat/SecurityTokenService";
            lastUri = appendUri(baseUrl, qParam);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("HATA : ",e.getMessage());
        }
        System.out.println();

        Log.i("URL info : ",lastUri.toString());
       setContentView(loadUrlWithWebView(webView,lastUri.toString()));
        //setContentView(loadUrlWithWebView(webView,sampleURL));
    }

    public static URI appendUri(String uri, String appendQuery) throws URISyntaxException {
        URI oldUri = new URI(uri);

        String newQuery = oldUri.getQuery();
        if (newQuery == null) {
            newQuery = appendQuery;
        } else {
            newQuery += "&" + appendQuery;
        }

        URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());
        return newUri;
    }


    public WebView loadUrlWithWebView(WebView pWebView, String url) {
        WebSettings settings = pWebView.getSettings();
//        settings.setBuiltInZoomControls(true);
//        settings.setJavaScriptEnabled(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(false);

        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
//        settings.setPluginsEnabled(true);
        settings.setSupportMultipleWindows(false);
        settings.setSupportZoom(false);
        pWebView.setVerticalScrollBarEnabled(false);
        pWebView.setHorizontalScrollBarEnabled(false);
        pWebView.setWebViewClient(webViewClient);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                // Loading started for URL
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "Loading started for URL", Toast.LENGTH_SHORT).show();
//                    }
//                },WAITING_TIME);
//            }
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // Redirecting to URL
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "Redirecting to URL", Toast.LENGTH_SHORT).show();
//                    }
//                },WAITING_TIME);
//                return true;
//            }
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                // Loading finished for URL
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "Loading finished for URL", Toast.LENGTH_SHORT).show();
//                    }
//                },WAITING_TIME);
//            }
//        });
        pWebView.loadUrl(url);

//        pWebView.evaluateJavascript("(function() { return document.getElementById(\"tridField\").value; })();", response ->  {
//            Log.d("T.C. No", response);
////                tcNo = response;
//        });
        return pWebView;
    }
}
