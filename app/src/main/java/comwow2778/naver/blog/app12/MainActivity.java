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
    LinearLayout linearLayout;
    Animation anim1;
    ListView listview;
    ArrayAdapter<String> adapter;
    ArrayList<data> data;
    data datatype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = (LinearLayout)findViewById(R.id.LinearLayout);
        listview = (ListView)findViewById(R.id.listview);
        anim1 = AnimationUtils.loadAnimation(this,R.anim.anim);
        webView = (WebView)findViewById(R.id.webview);
        webView.addJavascriptInterface(new JavaScriptMethods(),"myapp");
        data = new ArrayList<data>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        et = (EditText)findViewById(R.id.et);
        dialog = new ProgressDialog(this);
        listview.setAdapter(adapter);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("삭제확인");
                dialog.setNegativeButton("취소",null);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                linearLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.VISIBLE);
                listview.setVisibility(View.INVISIBLE);
                String url = data.get(position).getUrl();
                webView.loadUrl("http://"+url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress >=100) dialog.dismiss();
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog.setMessage("Loading!");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                et.setText(url);
                super.onPageFinished(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
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
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.GONE);
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
            linearLayout.setAnimation(anim1);
            anim1.start();
            linearLayout.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
            listview.setVisibility(View.INVISIBLE);
            webView.loadUrl("file:///android_asset/www/addurl.html");
        }
        else{
            linearLayout.setVisibility(View.GONE);
            webView.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    Handler myhandler = new Handler();

    class JavaScriptMethods {

        @JavascriptInterface/*
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
        }*/
        public void visible(){
            myhandler.post(new Runnable() {
                @Override
                public void run() {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            });
        }
        @JavascriptInterface
        public void saveurl(final String sitename, final String url){
            myhandler.post(new Runnable() {
                @Override
                public void run() {
                    int same = 0;
                    for(int i=0; i < data.size(); i++){
                        if(data.get(i).getUrl().equals(url)) {
                            same++;
                        }
                    }
                    if(same == 0) {
                        datatype = new data(sitename, url);
                        data.add(datatype);
                        adapter.add("<" +sitename + ">" + url);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "즐겨찾기에 추가되었습니다",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        webView.loadUrl("javascript:displayMsg()");
                    }
                }
            });
        }
    }

    public void onClick(View v){
            webView.loadUrl("http://" + et.getText().toString());
    }
}
