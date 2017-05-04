package comwow2778.naver.blog.app12;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    EditText et;
    ProgressDialog dialog;
    LinearLayout L1;
    Animation anim1;
    LinearLayout L2;
    ListView L3;
    ArrayAdapter<String> adapter;
    ArrayList<data> data;
    data datatype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L1 = (LinearLayout)findViewById(R.id.LinearLayout);
        L2 = (LinearLayout)findViewById(R.id.layout1);
        L3 = (ListView)findViewById(R.id.layout2);
        anim1 = AnimationUtils.loadAnimation(this,R.anim.anim);
        webView = (WebView)findViewById(R.id.webview);
        webView.addJavascriptInterface(new JavaScriptMethods(),"myapp");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        data = new ArrayList<data>();
        L3.setAdapter(adapter);
        et = (EditText)findViewById(R.id.et);
        dialog = new ProgressDialog(this);

        L3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //클릭햇을때
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.setMessage("Loading!");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress >=100) dialog.dismiss();
            }
        });
        webView.loadUrl("https://www.naver.com");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                L1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                L1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"즐겨찾기 추가");
        menu.add(0,2,0,"즐겨찾기 목록");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==1){
            webView.loadUrl("file:///android_asset/www/addurl.html");
            L1.setAnimation(anim1);
            anim1.start();
        }
        else{
            //리스트뷰내용
        }
        return super.onOptionsItemSelected(item);
    }
    Handler myhandler = new Handler();

    class JavaScriptMethods {

        @JavascriptInterface
        public void displayToast(){
           // Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
           myhandler.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("그림 바꾸기").setMessage("그림 바꿀까요?")
                            .setNegativeButton("Cancel", null).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            webView.loadUrl("javascript:changeImage()");
                        }
                    });

                }
            });
        }
    }

    public void onClick(View v){
        if(v.getId() == R.id.btn2){
            webView.loadUrl("javascript:changeImage()");
        }

    }
    public void ListViewadd(String name,String url){
        datatype.setName(name);
        datatype.setUrl(url);
        data.add(datatype);
        adapter.add("<"+name+">"+url);
        adapter.notifyDataSetChanged();
    }

}
