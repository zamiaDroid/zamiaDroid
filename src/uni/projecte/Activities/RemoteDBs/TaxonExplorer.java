package uni.projecte.Activities.RemoteDBs;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TaxonExplorer extends Activity {
	
	private WebView webView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.remote_taxon);
        
        /* URL: @from resource  */
        String url=getIntent().getExtras().getString("url");
        
        Log.i("DB","Connecting: "+url);        
        
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new DataBaseWebViewClient());

    }
    
    public class DataBaseWebViewClient extends WebViewClient {

    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    	
    		  if(url.contains(".pdf")){
                  Uri uri = Uri.parse(url.toString());
                  Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                  startActivity(browserIntent);
                  return true;
             }
             else  view.loadUrl(url);
    	
    	return true;
   
    	}
    	
    	
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
