	package uni.projecte.Activities.RemoteDBs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import uni.projecte.R;
import uni.projecte.R.drawable;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.CitationControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.CitationManager.FileExporter;
import uni.projecte.dataLayer.RemoteDBManager.AbstractDBConnection;
import uni.projecte.dataLayer.RemoteDBManager.BiocatDBManager;
import uni.projecte.dataLayer.RemoteDBManager.DBManager;
import uni.projecte.dataLayer.RemoteDBManager.RemoteTaxon;
import uni.projecte.dataLayer.RemoteDBManager.objects.LocalTaxonListAdapter;
import uni.projecte.dataLayer.RemoteDBManager.objects.LocalTaxonNoThListAdapter;
import uni.projecte.dataLayer.RemoteDBManager.objects.RemoteTaxonListAdapter;
import uni.projecte.dataLayer.RemoteDBManager.objects.RemoteTaxonsExport;
import uni.projecte.dataLayer.RemoteDBManager.objects.TotalTaxonListAdapter;
import uni.projecte.dataTypes.LocalTaxon;
import uni.projecte.dataTypes.LocalTaxonSet;
import uni.projecte.dataTypes.RemoteTaxonSet;
import uni.projecte.dataTypes.TotalTaxonSet;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.maps.UTMDisplay;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

public class TaxonListExplorer extends TabActivity {

	
	public static final int CHANGE_METHOD=Menu.FIRST;
	public static final int DB_CONFIG=Menu.FIRST+1;
	public static final int EXPORT_PRESENCE=Menu.FIRST+2;
	
	public final static int UPDATE_TAXON_LIST = 1;
	 
	private ListView lvRemotetaxonList;
	private ListView lvLocalTaxonList;
	private ListView lvTotalTaxonList;
	private TextView tvUTM;
	private TextView tvListType;
	private TextView dataBaseType;
	private TextView tvLocality;
	private ImageButton imgSearch;
	private LinearLayout llUTM10x10buttons;	

	private boolean searchActive=false;
	
	private boolean userLocation;
	
	private EditText filterList;
	private RemoteTaxonListAdapter rl;
	private LocalTaxonListAdapter lt;
	private TotalTaxonListAdapter tl;
	private RemoteTaxonSet remoteList;
	private LocalTaxonSet localTaxonList;
	private TotalTaxonSet total;
	
	private Button bt10x10;
	private Button bt1x1;
	
	private ArrayList<RemoteTaxon> totalTaxonList;
	
	private static long projId;
	private String projName;	
	private String utm;
	private int level;
	private String filumType;
	private String dbName;
	private String thName;
	private String thType;
	
	private String remoteData;
	private String localData;
	private String totalData;
	
	private boolean showTaxonInfo=true;
	private boolean exportMail=false;
	private String lang;
	
	private TabHost mTabHost;
	
	private ProgressDialog pd;
	private PreferencesControler prefC;
	private ProgressDialog pdCitationExport;

	
	private AbstractDBConnection dbConn;
	
	private double latitude;
	private double longitude;
	
	private int workingDB=-1;
	
	boolean utm1x1=false;
	
	private ProjectControler projCnt;
	private DBManager dbM;
	private Dialog dialogPresenceExport;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.taxonlistexplorer);
        
     
        /* Preparing tab interface */
        
        mTabHost = getTabHost();
        
        remoteData=getString(R.string.tabRemoteTaxons);
        localData=getString(R.string.tabLocalTaxons);
        totalData=getString(R.string.tabTotalTaxons);
        
        TabHost.TabSpec spec=mTabHost.newTabSpec("tab_test1").setIndicator(remoteData).setContent(R.id.remoteDBTab);
        TabHost.TabSpec spec2=mTabHost.newTabSpec("tab_test2").setIndicator(localData).setContent(R.id.localDBTab);
        TabHost.TabSpec spec3=mTabHost.newTabSpec("tab_test3").setIndicator(totalData).setContent(R.id.totalDBTab);
        
        mTabHost.addTab(spec);
        mTabHost.addTab(spec2);
        mTabHost.addTab(spec3);

        mTabHost.setCurrentTab(0);
        
        projCnt= new ProjectControler(this);
        prefC=new PreferencesControler(this);
        
        /* Getting UI elements */
        
        lvRemotetaxonList= (ListView)findViewById(R.id.taxonListView);
        lvLocalTaxonList= (ListView)findViewById(R.id.localTaxonListView);
        lvTotalTaxonList=(ListView)findViewById(R.id.totalListView);
        tvUTM= (TextView)findViewById(R.id.tvDBUTM);
        filterList=(EditText)findViewById(R.id.etFilterTaxonList);
        dataBaseType=(TextView)findViewById(R.id.tvDBServer);
        tvLocality=(TextView)findViewById(R.id.tvLocality);
        filterList.addTextChangedListener(filterTextWatcher);
        filterList.setVisibility(View.INVISIBLE);
        
        bt10x10=(Button)findViewById(R.id.bt10x10km);
        bt10x10.setOnClickListener(changeUTMListener);
        bt10x10.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));

        bt1x1=(Button)findViewById(R.id.bt1x1km);
        bt1x1.setOnClickListener(changeUTMListener);
        
        imgSearch=(ImageButton)findViewById(R.id.btTaxonSearch);
        imgSearch.setBackgroundResource(android.R.drawable.ic_menu_search);
        imgSearch.setOnClickListener(activateSearchListener);
        
        llUTM10x10buttons=(LinearLayout)findViewById(R.id.llUTM10x10buttons);

        
        /* Getting arguments */
        
        projId=getIntent().getExtras().getLong("projId");
        filumType=getIntent().getExtras().getString("type");
        userLocation=getIntent().getExtras().getBoolean("userLocation");
        
        latitude=getIntent().getExtras().getDouble("latitude");
        longitude=getIntent().getExtras().getDouble("longitude");


        projCnt.loadProjectInfoById(projId);
        thName=projCnt.getThName();
        projName=projCnt.getName();
        
        lang=prefC.getLang();

        dbM=new DBManager(this);

		determineDBThread();

       
    }
	


	@Override
	public void onResume(){
		
		super.onResume();
		
		//loadDataThread();
		
		
	}
	
	
	  private void loadLocalTaxons() {

		 CitationControler sC= new CitationControler(this);
		  
		 CoordinateUTM utmConverter = CoordConverter.getInstance().toUTM(new CoordinateLatLon(latitude,longitude));
		 utm=UTMDisplay.getBdbcUTM10x10(utmConverter.getShortForm());
		  
		 localTaxonList=sC.getLocalTaxon(projId,utm);
	
		  
		  
	}
	  
	  private void loadRemoteTaxons(){

		  if(dbConn.serviceGetTaxonList()<0){
			  
			  Utilities.showToast("0 Element connecting", this);

		  }
			  
		   remoteList=dbConn.getList();
		   rl= new RemoteTaxonListAdapter(this,remoteList.getTaxonList(),localTaxonList,showTaxonCitations,showTaxonInfoClick);
	       
	  }
	  
	  
		private void determineDBThread() {

			String loaderDescrition=getString(R.string.determiningDatabase);
			
	        pd = ProgressDialog.show(this, getString(R.string.connectingDatabase), loaderDescrition, true,
	                true);
		          	
		        new Thread(new Runnable() {

					public void run() {
						
					    Log.i("DB","Determinant BD");

					    	workingDB=dbM.getDefaultDBConnection(latitude, longitude,filumType);
					        
					    Log.i("DB","DB determinada = "+workingDB);
					
				        		        
				        handlerDBDetermined.sendEmptyMessage(0);
					}
		            	
		    	}).start();

		    	
			}
	  
	    private Handler handlerDBDetermined = new Handler() {

	        @Override
	        public void handleMessage(Message msg) {
	 
	        	pd.dismiss();
	        	loadTaxonsList();
	        	
	        	
	        }
	    };
		private FileExporter fExp;
	    
		
		
	  public void loadTaxonsList(){
		  
		  /*
	         * 	Get available DB for latitude, longitude and filum 
	         * 
	         * 	if DB Unavailable -> error and back
	         *	
		         *	Set Locality
		         *
		         *	get DB instance
		         *
		         *	Set DB name
		         *
		         *	Set utm Loading Message
		         *
		     *
	         */
	        
	        if(workingDB>=DBManager.AVAILABLE_DB){
	  		
	        	dbConn=dbM.getDBInstance(workingDB,filumType,lang);
	        	dbConn.setLocation(latitude, longitude,utm1x1);
	  		  	tvUTM.setText(dbConn.getPrettyLocation());
	  		  	
	  		  	if(!dbConn.hasUTM1x1()) llUTM10x10buttons.setVisibility(View.GONE);
	  		  	else llUTM10x10buttons.setVisibility(View.VISIBLE);

	        	
	        	if(dbConn==null){
	        		
	              	Utilities.showToast("Can't get factory connection", getBaseContext());
	    			finish();
	        		
	        	}
	        	else{
	        		
	                tvLocality.setTextColor(Color.WHITE);
	                tvLocality.setText(dbM.getLocality());

	                tvUTM.setTextColor(Color.GREEN);

	        		dbName=dbConn.getDbName();
	        		dataBaseType.setText(dbName);
	                  
	                lvRemotetaxonList.setTextFilterEnabled(true);
	                lvRemotetaxonList.setItemsCanFocus(true);

	                lvRemotetaxonList.setFastScrollEnabled(true);
	                  
	                loadDataThread();
	        		
	        	}
	  
	        	
	        }
	        else if(workingDB==DBManager.NO_DB_FILUM){
	        	
	        	Utilities.showToast(getString(R.string.noFilumDataBase,Utilities.translateThTypeToCurrentLanguage(this, filumType)), getBaseContext());
				finish();
	        	
	        }
	        else if(workingDB==DBManager.NO_DB_TERRITORY){
	        	
				Utilities.showToast(getString(R.string.outOfDataBaseRange), getBaseContext());
				finish();

	        }
	        else if(workingDB==DBManager.NO_SERVER_NOT_FOUND){
	        	
				Utilities.showToast("Server Not found. Check Connection", getBaseContext());
				finish();

	        }
		  
		  
	  }
	
	   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
    	menu.add(0,DB_CONFIG, 0,R.string.mConfigDB).setIcon(getResources().getDrawable(R.drawable.ic_menu_db));
    	menu.add(0,EXPORT_PRESENCE, 0,R.string.btExportPresences).setIcon(getResources().getDrawable(android.R.drawable.ic_menu_save));
			
    	return super.onCreateOptionsMenu(menu);
    }


    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
    	
		switch (item.getItemId()) {
			case CHANGE_METHOD:
	
				if(showTaxonInfo) {
					
					showTaxonInfo=false;
			
					
				}
				else {
					showTaxonInfo=true;
					
				}
				
				
				 			 
			break;
			
			case EXPORT_PRESENCE:
				
				exportPresence();
				 			 
			break;
			
			
			case DB_CONFIG:
				
				String filum=projCnt.getFilum();
				
				if(filum.equals("")){
					
					Utilities.showToast(getString(R.string.projWithoutTh), this);
					
				}
				else{
					
					Intent dbConfigActivity = new Intent(this,RemoteDBConfig.class);
					dbConfigActivity.putExtra("filum", filum);
					startActivityForResult(dbConfigActivity,UPDATE_TAXON_LIST);		        	
				}
				
	  
	        	
			 			 
	        	break;
	
		}
		
	
		return super.onOptionsItemSelected(item);
	}
	
	private void exportPresence() {

		   if(Utilities.isSdPresent()){
			   
				final int currentTab=getTabHost().getCurrentTab();
			   
				final String fileName=utm.replace("_", "")+"_"+projName.replace(" ", "_")+"_"+dbM.getPresenceTag(currentTab,workingDB);
			   
				dialogPresenceExport = new Dialog(this);
	        	dialogPresenceExport.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        	dialogPresenceExport.setContentView(R.layout.exportdialog);
	    	   	
	  	   	  	final Spinner fieldsList=(Spinner)dialogPresenceExport.findViewById(R.id.spFieldsListReport);
	    	   	final EditText name=(EditText)dialogPresenceExport.findViewById(R.id.fileName);
	    	   	final Button bExportFagus = (Button)dialogPresenceExport.findViewById(R.id.bExportFagus);
	    	   	final CheckBox cbSendByMail = (CheckBox)dialogPresenceExport.findViewById(R.id.cbExportSendByMail);

        	   	TextView tvReportLabel=(TextView)dialogPresenceExport.findViewById(R.id.tvCreateReportInfo);
        	   	tvReportLabel.setVisibility(View.VISIBLE);
        	   	
        	   	String destinationPath=String.format(getString(R.string.tvPresenceInfo), prefC.getReportPath());
        	   	tvReportLabel.setText(Html.fromHtml(destinationPath));
	    	   	
        	   	fieldsList.setVisibility(View.GONE);
        	   	cbSendByMail.setVisibility(View.VISIBLE);
        	   	
        	   	
        	   	name.setText(fileName);

        	   	bExportFagus.setText(getString(R.string.btExportPresences));
	    	   		
	    	   	
	    	    bExportFagus.setOnClickListener(new Button.OnClickListener(){
	    	    	             
	    	    	
	    	    	public void onClick(View v){
	    	    		
	    	    		exportMail=cbSendByMail.isChecked();
	    	    		exportPresenceFileCheck(name.getText().toString());
	    	    	                 
	    	    	 }
	    	    	             
	    	    });
	    	    
	    	    dialogPresenceExport.show();
			   
			   
				
			   
		   } 

		   else {
	        	
	        	Toast.makeText(getBaseContext(), 
	                    R.string.noSdAlert, 
	                    Toast.LENGTH_SHORT).show();
	        }
		
	}



	private void exportPresenceFileCheck(final String fileName) {
		
		fExp=new FileExporter(this);
		boolean exists=fExp.createFile("presenceReport", fileName);
		
		
		if(exists){
		
	     	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	
		    builder.setMessage(getString(R.string.presenceFileExists))
		           .setCancelable(false)
		           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		   
		           			dialogPresenceExport.dismiss();
			   				exportPresenceThread(fileName);

		   	  		  	
		               }
		           })
		           .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		            	   	
			
		               }
		           });
		   
		    
			 AlertDialog alert = builder.create();
			 alert.show();
			
			
		}
		else{
			
			dialogPresenceExport.dismiss();
			exportPresenceThread(fileName);
			
		}


		
	}



	private void exportPresenceThread(final String fileName) {

		final int currentTab=getTabHost().getCurrentTab();

		
			//pd and thread
	        pdCitationExport = new ProgressDialog(this);
	        pdCitationExport.setMessage(getString(R.string.exportRemoteToTab));
	        pdCitationExport.show();
			
	
			                 Thread thread = new Thread(){
			  	        	   
				                 @Override
								public void run() {
				               	  
				             		RemoteTaxonsExport rtExp=new RemoteTaxonsExport(remoteList, localTaxonList, totalTaxonList);
				                	 
				         			if(currentTab==0) rtExp.exportRemoteList();
				        			else if (currentTab==1) rtExp.exportLocalList();
				        			else rtExp.exportTotalList();
				         			
				         			rtExp.stringToFile(fileName, getBaseContext());
				         			
				         			exportPresenceHandler.sendEmptyMessage(0);
				         			
				                 }
				           };
				           
				           
			   thread.start();

				
	}
	
	 private Handler exportPresenceHandler = new Handler() {

	    	@Override
			public void handleMessage(Message msg) {
       
	    		sendByMail();
     			pdCitationExport.dismiss();

      }
	    	
	    	
	 };
	
	private void sendByMail(){
		
		
		   if(exportMail){
        	   
        	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	   String curentDateandTime = sdf.format(new Date());
        	   
        	   String exportSubject = String.format(getString(R.string.citationExportEmailSubject),projName);  
        	   String exportSubjectText = String.format(getString(R.string.citationExportEmailText), projName,curentDateandTime,fExp.getFormat());  
        	   

        	   Intent sendIntent = new Intent(Intent.ACTION_SEND);
               sendIntent.setType(fExp.getExportMimeType());
               sendIntent.putExtra(Intent.EXTRA_SUBJECT,exportSubject);
               sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fExp.getFile().getAbsolutePath()+""));
               sendIntent.putExtra(Intent.EXTRA_TEXT, exportSubjectText);
               startActivity(Intent.createChooser(sendIntent, "Email:"));
        	   
           }
		
		
	}



	private void loadDataThread() {

		String loaderDescrition=dbName+" "+getString(R.string.searchingTaxonsUTM)+" "+dbConn.getPrettyLocation();
		
		if(userLocation){
			
			loaderDescrition=dbName+" "+getString(R.string.searchingTaxonsUTM)+" "+dbConn.getPrettyLocation();

		}
		
		
        pd = ProgressDialog.show(this, getString(R.string.connectingDatabase), loaderDescrition, true,
                true);
	          	
	        new Thread(new Runnable() {

				public void run() {
					
			        /* Fetching local and Remote Taxons */ 
			        loadLocalTaxons();
			        
			        Log.i("DB","Carregant remotes");

			        loadRemoteTaxons();
			        		        
			        handlerEnd.sendEmptyMessage(0);
				}

	            	
	    	}).start();

	    	
		}
	
	    
		   private OnClickListener activateSearchListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		       	
		        	if(filterList.getVisibility()==View.INVISIBLE) {
		        	
		        		filterList.setVisibility(View.VISIBLE);
		        		searchActive=true;
		        		
		        	}
		        	else {
		        		
		        		filterList.setVisibility(View.INVISIBLE);
		        		filterList.setText("");
		        		searchActive=false;	
		        	}
		        	
		        }
		    };
		    
		    
		    private OnClickListener changeUTMListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		       	
		        	if(utm1x1){
		        		
		        		utm1x1=false;
		        		
		        		bt1x1.getBackground().invalidateSelf();
		        		bt1x1.getBackground().setColorFilter(null);	
		        		bt10x10.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));
		        		
		        		bt10x10.setClickable(false);
		        		bt1x1.setClickable(true);

		        		loadTaxonsList();

		        		
		        	}
		        	else{
		        		
		        		utm1x1=true;
		        		
		        		bt10x10.getBackground().invalidateSelf();
		        		bt10x10.getBackground().setColorFilter(null);	
		        		bt1x1.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));

		        		bt10x10.setClickable(true);
		        		bt1x1.setClickable(false);
		        		
		        		loadTaxonsList();
		        		
		        	}
	    
		        	
		        }
		    };
	
    private Handler handlerEnd = new Handler() {

        @Override
        public void handleMessage(Message msg) {
 
        	pd.dismiss();
        	  
        	 mixLocalList();
        	
	        setRemoteAdapter();
	        setLocalAdapter();

	        /* Handling options */
	     //   manageOptions();
	        
	        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
	            
	        	public void onTabChanged(String tabId) {
	             
	            	
	            	if(tabId.equals("tab_test3")){
	            		
	            		handlerSearchForm(true);
	            		filterList.setText("");
	                    filterList.addTextChangedListener(filterTotalTextWatcher);

	            		
	            	}
	            	else if(tabId.equals("tab_test1")){
	            		
	            		handlerSearchForm(true);
	            		filterList.setText("");
	                    filterList.addTextChangedListener(filterTextWatcher);
	            	}
	            	else if(tabId.equals("tab_test2")){
	            		
	            		handlerSearchForm(true);
	                    filterList.addTextChangedListener(filterLocalTextWatcher);
	            	}
	            	else{
	            		
	            		handlerSearchForm(false);
	            		
	            	}
	            	
	        }
	        });

        	
        	refreshTabs();	
        	
	        Log.i("DB","Dades Carregades");

        	
        }
    };
    
    
    public void handlerSearchForm(boolean visible){

    	filterList.setText("");
    	if(visible)  {
    		
    		imgSearch.setVisibility(View.VISIBLE);
    		if(searchActive) filterList.setVisibility(View.VISIBLE);       
    		
    	}
    	else{
    		
    		imgSearch.setVisibility(View.GONE);
    		filterList.setVisibility(View.GONE);
    		if(!searchActive) filterList.setVisibility(View.GONE);
       	
    	}
    	
    }
	
	
	private void refreshTabs() {
		
	   ((TextView)mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText(remoteData+" ("+remoteList.getTaxonList().size()+")");
	   ((TextView)mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title)).setText(localData+" ("+localTaxonList.getTaxonList().size()+")");
	   ((TextView)mTabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title)).setText(totalData+" ("+totalTaxonList.size()+")");

	}


	private void setLocalAdapter() {
	      
		lt= new LocalTaxonListAdapter(this,localTaxonList.getTaxonList(),remoteList,projId,showTaxonInfoClick);
	    lvLocalTaxonList.setAdapter(lt);
	    lvLocalTaxonList.setTextFilterEnabled(true);
	    lvLocalTaxonList.setFastScrollEnabled(true);					
	}
	
	
	private void setRemoteAdapter() {
		
		lvRemotetaxonList.setAdapter(rl);
        lvRemotetaxonList.setItemsCanFocus(true);
	}
	
	private void mixLocalList(){
		
		total= new TotalTaxonSet(utm);
		
		ArrayList<LocalTaxon> localListA=localTaxonList.getTaxonList();
		ArrayList<RemoteTaxon> remoteListA=remoteList.getTaxonList();
		
		Iterator<LocalTaxon> itr = localListA.iterator();
		 
		while(itr.hasNext()){
			
			LocalTaxon tmp=itr.next();
			String taxonName=tmp.getTaxon();
			String taxonId="local";
			
			total.addTaxon(taxonName, taxonId);
			
		}
		
		Iterator<RemoteTaxon> itrRemote = remoteListA.iterator();

		while(itrRemote.hasNext()){
			
			RemoteTaxon tmp=itrRemote.next();
			String taxonName=tmp.getTaxon();
			String taxonId=tmp.getTaxonId();
			total.addTaxon(taxonName, taxonId);
			
		}
		
	
			
		HashMap<String, String> mixedList=total.getUniqueNameList();
		SortedSet<String> sortedset= new TreeSet<String>(mixedList.keySet());

	    Iterator<String> it = sortedset.iterator();
	    totalTaxonList=new ArrayList<RemoteTaxon>();
	    

	    while (it.hasNext()) {
	    	
	    	String name=it.next();
	    	totalTaxonList.add(new RemoteTaxon(name, total.getTaxonId(name)));
	    	
	      
	    }
	    
	    tl= new TotalTaxonListAdapter(this,totalTaxonList,localTaxonList,showTaxonInfoClick);
	    
		lvTotalTaxonList.setAdapter(tl);
		lvTotalTaxonList.setFastScrollEnabled(true);
		
		
	}



	private OnClickListener showTaxonCitations = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	    	
         	 String taxonName=v.getTag().toString();
		   	 String taxonCode=remoteList.getTaxonId(taxonName);
		   	 
		   	 if(dbConn.useThId() || taxonCode==null || taxonCode.equals("")){
		   		  	

			   		 ThesaurusControler thC=new ThesaurusControler(v.getContext());
					 thC.initThReader(thName);
					 
					 Cursor c=thC.fetchThesaurusItembyName(taxonName);
					 if(c!=null && c.getCount()>0 && c.getString(5)!=null){ 
						 
						 taxonCode=c.getString(5);
						 c.close();

					 }
					 
			   		 thC.closeThReader();
		   	
		   		 
		   	 }
		   	 
		   	 
		   	 if(taxonCode!=null && !taxonCode.equals("")){
 	       
			      	Intent intent = new Intent(getBaseContext(), TaxonRemoteCitationList.class);
			      	
			      	intent.putExtra("filum",filumType);
			      	intent.putExtra("utm1x1", utm1x1);
			        
			        intent.putExtra("latitude", latitude);
			        intent.putExtra("longitude", longitude);
        
			        intent.putExtra("codiOrc", taxonCode);
			        intent.putExtra("workingDB", workingDB);
			        
			        intent.putExtra("taxon", taxonName);
		
			        startActivityForResult(intent,0);
	
		   	 }
		   	 else{
		   		 
		   		 Utilities.showToast(getString(R.string.wrongTaxon), v.getContext());
		   		 
		   	 }
    	        	
        }
    };
	

	private OnClickListener showTaxonInfoClick = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	
		        
			   	 TextView tv=(TextView) v;
			   	 
			   	 if(tv==null){
			   		
			   		 tv=(TextView) v .findViewById(R.id.tvTaxonTotalList);
			   		 
			   	 }
			   	 
			   	 String taxonName=tv.getText().toString();
			    	
			   	 ThesaurusControler thC=new ThesaurusControler(v.getContext());
			   	 boolean thStatus=thC.initThReader(thName);
			   	 
			   	Cursor thInfo=thC.getThInfo(thName);
			  	thInfo.moveToFirst();
			  	
			  	if(thInfo.getCount()>0){
			  		  																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																												
			  	   thType=Utilities.translateThTypeToCurrentLanguage(v.getContext(), thInfo.getString(4));	        
			  		
			  	}
			  	
			  	
			   	 
			   	if(thStatus){
					 
					 
					 if(thC.checkTaxonBelongs(taxonName)){
						 
						 loadBiologicalRemoteCard(taxonName);
					 
					 }
					 
					 else{
						 
						 Toast.makeText(getBaseContext(), 
			       	               R.string.toastTaxonNotExists, 
			       	                 Toast.LENGTH_SHORT).show();
						 
						 
					 }
					 
					 
				 }
				 else{
					 
					 Toast.makeText(getBaseContext(), 
		       	               R.string.toastNotThesarus, 
		       	                 Toast.LENGTH_SHORT).show();
					 
				 }
			   	 
		    	
		    }
	    };
	

	    private void loadBiologicalRemoteCard(String taxonName){
	  	  
	 	   
	 	   String filum=Utilities.translateThTypeToFilumLetter(this, thType);
	 	   
	 	   if(filum!=null){
	 	   		 
	 			Intent intent = new Intent(getBaseContext(), TaxonRemoteTab.class);
	 			intent.putExtra("filumLetter", filum);
	 			intent.putExtra("taxon",taxonName);
	 			intent.putExtra("projId",projId);

	 			startActivityForResult(intent,6);
	 			
	 		}

	 	   
	    }

	    
	    private TextWatcher filterTotalTextWatcher = new TextWatcher() {

		     
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) {
	        }

	        public void onTextChanged(CharSequence s, int start, int before,int count) {

	        	ArrayList<RemoteTaxon> tempArrayList = new ArrayList<RemoteTaxon>();
	        	
	        	Iterator<RemoteTaxon> iterator = totalTaxonList.iterator();
	        	
	        	String subString=s.toString().toLowerCase();
	    		
	    		while(iterator.hasNext()){
	    			
	    			RemoteTaxon tmpTaxon=iterator.next();
	    			
	    			if(tmpTaxon.getTaxon().toLowerCase().startsWith(subString)){
	    				
	    				tempArrayList.add(tmpTaxon);
	    				
	    			} 
	    		
	    		}
	    		
	    	       tl= new TotalTaxonListAdapter(getBaseContext(),tempArrayList,localTaxonList,showTaxonInfoClick);
	    	       lvTotalTaxonList.setAdapter(tl);

	        
	        }

			public void afterTextChanged(Editable arg0) {
				
				
				
			}

	    };
	    
	    private TextWatcher filterTextWatcher = new TextWatcher() {

	     
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) {
	        }

	        public void onTextChanged(CharSequence s, int start, int before,int count) {

	        	if(s.length()>0){
	        	
		        	ArrayList<RemoteTaxon> tempArrayList = new ArrayList<RemoteTaxon>();
		        	
		        	Iterator<RemoteTaxon> iterator = remoteList.getTaxonList().iterator();
		        	
		        	String subString=s.toString().toLowerCase();
		    		
		    		while(iterator.hasNext()){
		    			
		    			RemoteTaxon tmpTaxon=iterator.next();
		    			
		    			if(tmpTaxon.getTaxon().toLowerCase().startsWith(subString)){
		    				
		    				tempArrayList.add(tmpTaxon);
		    				
		    			} 
		    		
		    		}
		    		
		    	       rl= new RemoteTaxonListAdapter(getBaseContext(),tempArrayList,localTaxonList,showTaxonCitations,showTaxonInfoClick);
		    	       
		    	  

	        	}
	        	else{
	        		
	        		
		    	       rl= new RemoteTaxonListAdapter(getBaseContext(),remoteList.getTaxonList(),localTaxonList,showTaxonCitations,showTaxonInfoClick);

	        		
	        	}
	        	
	            	lvRemotetaxonList.setAdapter(rl);
	                lvRemotetaxonList.setItemsCanFocus(true);
	        	
	        
	        }

			public void afterTextChanged(Editable arg0) {
				
				
				
			}

	    };
	    
	    private TextWatcher filterLocalTextWatcher = new TextWatcher() {

		     
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) {
	        }

	        public void onTextChanged(CharSequence s, int start, int before,int count) {

	        	ArrayList<LocalTaxon> tempArrayList = new ArrayList<LocalTaxon>();
	        	
	        	Iterator<LocalTaxon> iterator = localTaxonList.getTaxonList().iterator();
	        	
	        	String subString=s.toString().toLowerCase();
	    		
	    		while(iterator.hasNext()){
	    			
	    			LocalTaxon tmpTaxon=iterator.next();
	    			
	    			if(tmpTaxon.getTaxon().toLowerCase().startsWith(subString)){
	    				
	    				tempArrayList.add(tmpTaxon);
	    				
	    			} 
	    		
	    		}
	    		
	    	       lt= new LocalTaxonListAdapter(getBaseContext(),tempArrayList,remoteList,projId,showTaxonInfoClick);
	    	       
	    	       lvLocalTaxonList.setAdapter(lt);

	        	
	        	
	        
	        }

			public void afterTextChanged(Editable arg0) {
				
				
				
			}

	    };
   
	    
   

    

    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // super.onActivityResult(requestCode, resultCode, intent);
    	
        	
         switch(requestCode) {
             
                 
         case UPDATE_TAXON_LIST :
         	
        	 loadTaxonsList();
            
             break;

             
             
         default:
             	
   
         }
         

     }
	
	
}
