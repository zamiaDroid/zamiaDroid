package uni.projecte.Activities.RemoteDBs;

import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.controler.ProjectConfigControler;
import uni.projecte.dataLayer.RemoteDBManager.objects.RemoteProviderPair;
import uni.projecte.dataLayer.RemoteDBManager.objects.RemoteTaxonTabHandler;
import uni.projecte.dataLayer.utils.TaxonUtils;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;


public class TaxonRemoteTab extends Activity{

	private WebView webView;
	private Spinner spServices;
	private CheckBox cbDefaultTab;
	private TextView tvTabTaxon;
	
	
	private String taxon;
	private String filumLetter;
	private String defaultProvider;
	private long projId;
	
	private boolean pressetedProvider=true;
	

	private ProjectConfigControler projCnfCnt;
	
	private HashMap<String, String> providerList;
    private ProgressDialog mProgress;
    private RemoteProviderPair[] providerId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.taxonremotetab);
        
        webView = (WebView) findViewById(R.id.webview);
        spServices = (Spinner) findViewById(R.id.spServices);
        cbDefaultTab = (CheckBox) findViewById(R.id.cbDefaultTab);
        tvTabTaxon = (TextView) findViewById(R.id.tvTabTaxon);
 
        
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.setWebViewClient(new DataBaseWebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        
        
        
        /*  Intent params */
        projId=getIntent().getExtras().getLong("projId");
        taxon=getIntent().getExtras().getString("taxon");
        filumLetter=getIntent().getExtras().getString("filumLetter");
        
        
        if(projId<0){ 
        	
        	cbDefaultTab.setVisibility(View.GONE);
        	pressetedProvider=false;
        	
        }
        
        if(taxon!=null){
        	
        	tvTabTaxon.setText(Html.fromHtml("<b>"+TaxonUtils.removeAuthors(taxon)+"</b>"));
                
	        RemoteTaxonTabHandler remoteTabHnd= new RemoteTaxonTabHandler(this, filumLetter);
	        	        
		    providerId=remoteTabHnd.getAvailableTaxonTabs(taxon.replace(" ","+"), "ca");
		    providerList=remoteTabHnd.getTabProviderList();
		    
		    cbDefaultTab.setOnClickListener(cbDefaultListener);

		    
		    if(providerList.size()>0){
		    	
			  	projCnfCnt=new ProjectConfigControler(this);
			
			  	if(pressetedProvider) defaultProvider=projCnfCnt.getProjectConfig(projId,ProjectConfigControler.DEFAULT_TAB_PROVIDER);
			   	else defaultProvider="bdbc";
			        
			   fillRemoteTab(providerId);
		   
		    }
		    else{
		    	
		    	Utilities.showToast(getString(R.string.noTaxonTab), this);
	        	finish();		    	
		    }
	    
        
        }
        else {
        	
        	Utilities.showToast(getString(R.string.wrongTaxon), this);
        	finish();
        	
        }
        
        
    }
    
    private void fillRemoteTab(RemoteProviderPair[] providerId) {
    
    	int spinnerSelection=findValuePosition(providerId,defaultProvider);
       
    	ArrayAdapter<RemoteProviderPair> adapter = new ArrayAdapter<RemoteProviderPair>(this,android.R.layout.simple_spinner_item, providerId); 	 
    	spServices.setAdapter(adapter);
    	spServices.setSelection(spinnerSelection);
    	spServices.setOnItemSelectedListener(spListener);
    	    	
	}

    private int findValuePosition(RemoteProviderPair[] providerId,String defaultValue) {
    	
    	int pos=0;
    	boolean found=false;
    	
    	for(int i=0; i<providerId.length && !found && !defaultValue.equals(""); i++){
    		
    		if(providerId[i].getValue().equals(defaultValue)) {
    			
    			found=true;
    			pos=i;
    		}    		
    	}
    	
    	return pos;
	}
    
    private android.view.View.OnClickListener cbDefaultListener = new CheckBox.OnClickListener() 
    {
        public void onClick(View v) {
        	
			RemoteProviderPair d = providerId[spServices.getSelectedItemPosition()];
			defaultProvider=d.getValue();
        	projCnfCnt.changeProjectConfig(projId, ProjectConfigControler.DEFAULT_TAB_PROVIDER, defaultProvider);
			cbDefaultTab.setEnabled(false);
         
        	
        }

    };

	private OnItemSelectedListener spListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long arg3) {

			RemoteProviderPair d = providerId[position];
			
			mProgress = ProgressDialog.show(arg0.getContext(),getString(R.string.loadingTab), d.getSpinnerText(),false,true);
			
			String url=providerList.get(d.getValue());
			
			if(!d.getValue().equals(defaultProvider)) {
				
				cbDefaultTab.setEnabled(true); 
				cbDefaultTab.setChecked(false);
				
			}
			else {
				
				cbDefaultTab.setChecked(true);
				cbDefaultTab.setEnabled(false);
				
			}
			
	        webView.loadUrl(url);
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			
			
		}
	   
};
    
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
    	
    	 public void onPageFinished(WebView view, String url) {
    		 if(mProgress.isShowing()) {
    		 	                    
    			 mProgress.dismiss();
    			 webView.clearHistory();

    			 
    		 }
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
