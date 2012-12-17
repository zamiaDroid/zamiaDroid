package uni.projecte.Activities.Citations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import uni.projecte.R;
import uni.projecte.Activities.Maps.CitationMap;
import uni.projecte.Activities.Miscelaneous.GalleryGrid;
import uni.projecte.Activities.Projects.ProjectInfo;
import uni.projecte.Activities.Thesaurus.ThesaurusTaxonChecker;
import uni.projecte.controler.CitationControler;
import uni.projecte.controler.DataTypeControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.controler.CitationSecondLevelControler;
import uni.projecte.controler.ReportControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.CitationManager.FileExporter;
import uni.projecte.dataLayer.CitationManager.ListAdapter.CitationListAdapter;
import uni.projecte.dataLayer.CitationManager.objects.Citation;
import uni.projecte.dataLayer.CitationManager.objects.CitationHandler;
import uni.projecte.dataLayer.ThesaurusManager.ListAdapters.ThesaurusAutoCompleteAdapter;
import uni.projecte.dataLayer.ThesaurusManager.ListAdapters.ThesaurusGenusAutoCompleteAdapter;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class CitationManager extends Activity{

	public final static int EDIT_CITATION = 1;
	public static final int UPDATE_LIST = 2;
	   
	private static final int IMPORT_CITATIONS = Menu.FIRST;
	private static final int SHOW_FIELDS = Menu.FIRST+1;
	private static final int SHOW_GALLERY = Menu.FIRST+2;
	private static final int REPORT_CREATOR = Menu.FIRST+3;
	private static final int TAXON_CHECKER = Menu.FIRST+4;

	
	private long projId;
	private ExpandableListView mainCitListView;
	private CitationListAdapter citListAdap;
	private CitationHandler citHand;
	private HashMap<String, String> projFieldsPairs;
	private HashMap<String, String> fieldsLabelNames;
	
	
	private ProjectControler projCnt;
	private ThesaurusControler tC;
	
	private String projectType;
	private String projectName;
	private String thName;
	private String fileName;
	private FileExporter fExp;
	
	private boolean exportMail=false;

	private TextView tvTotalFields;
	private TextView tvFilteredFields;
	private TextView tvSelectedCitations;
	private CheckBox cbSelectAll;
	
	private LinearLayout llFilter;
	private ImageButton btOrderCron;
	private ImageButton btOrderAlpha;
	
	
	private long chosenFieldId;
	
	private String chosenFieldType;
	private String chosenFieldLabel;
	private String comparator="=";
	private String filterValue="";
	private boolean refreshList=false;
	
	
	/* Ui elements from filter dialog  */
	
	private EditText etValue;
	private AutoCompleteTextView etValueAuto;
	private Spinner spListValues;
	private CheckBox cbFilter;
	private DatePicker datePicker;
	private TextView tvFilterMessage;
	private TextView tvFilterMessage1;
	private TextView tvFilterMessage2;

	private ProgressDialog pdRemove;
	private ProgressDialog pdCheckingTh;
	private ProgressDialog pdCitationExport;
	private ProgressDialog pdMain;

	private Button filterByTaxon;

	private Dialog dialog;
	private Dialog taxonFilterdialog;
	private Dialog dateFilterDialog;
	private Dialog locationFilterdialog;
	
	private long thFieldId;
	
	
		 @Override
		public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        
		        Utilities.setLocale(this);

		        setContentView(R.layout.citationmanager);

		        /* Getting references to UI elements  */
		        mainCitListView= (ExpandableListView)findViewById(R.id.citationLV);
		        
		        /* Filter buttons */ 
		        Button applyFilters=(Button) findViewById(R.id.btApplyFilter);
		        Button filterByLocation=(Button) findViewById(R.id.btFilterByLocation);
		        Button filterByDate=(Button) findViewById(R.id.btFilterByDate);
		        filterByTaxon=(Button) findViewById(R.id.btFilterByTaxon);

		        /* Main actions */
		        ImageButton viewMapButton = (ImageButton)findViewById(R.id.ibViewMapCit);
		        ImageButton removeButton = (ImageButton)findViewById(R.id.ibRemoveCit);
		        ImageButton exportButton = (ImageButton)findViewById(R.id.ibExportCit);
		        ImageButton photoButton = (ImageButton)findViewById(R.id.ibPhotoCit);

		        /* Ordering list */
		        btOrderCron=(ImageButton) findViewById(R.id.btOrderCron);
		        btOrderAlpha=(ImageButton) findViewById(R.id.ibOrderAlpha);
		        
		        /* Click listener and appearance configuration */
		        exportButton.setBackgroundResource(android.R.drawable.ic_menu_save);
		        exportButton.setOnClickListener(exportButtonListener);
		        applyFilters.setOnClickListener(applyFilterListener);
	        		        
		        btOrderCron.setOnClickListener(orderCronListener);
		        btOrderCron.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));
		        
		        btOrderAlpha.setOnClickListener(orderAlphaListener);
		        	        
		        filterByLocation.setOnClickListener(filterByLocationListener);
		        filterByDate.setOnClickListener(filterByDateListener);
		        filterByTaxon.setOnClickListener(filterByTaxonListener);
		        
		        viewMapButton.setBackgroundResource(android.R.drawable.ic_menu_mapmode);
		        viewMapButton.setOnClickListener(showMapListener);
		        
		        removeButton.setBackgroundResource(android.R.drawable.ic_menu_delete);
		        removeButton.setOnClickListener(removeCitListener);
		        
		        photoButton.setBackgroundResource(android.R.drawable.ic_menu_gallery);
		        photoButton.setOnClickListener(photoCitListener);
		        
		        cbSelectAll=(CheckBox)findViewById(R.id.cbSelectAllCitations);
		        cbSelectAll.setOnCheckedChangeListener(selectAllListener);
		        
		        tvTotalFields=(TextView) findViewById(R.id.tvTotalCitations);
		        tvFilteredFields=(TextView) findViewById(R.id.tvFilteredCitations);
		        tvSelectedCitations=(TextView) findViewById(R.id.tvSelectedCitations);
		        tvFilterMessage=(TextView)findViewById(R.id.tvFilterMessage);
		        tvFilterMessage1=(TextView)findViewById(R.id.tvFilterMessage2);
		        tvFilterMessage2=(TextView)findViewById(R.id.tvFilterMessage3);

		        llFilter=(LinearLayout)findViewById(R.id.llFilter);
		        
		    	
		        /* Getting intent params */
		        projId=getIntent().getExtras().getLong("id"); 
		        
		        /* Setting main controlers */
		        projCnt= new ProjectControler(this);
	            tC= new ThesaurusControler(this);

	            projFieldsPairs=new HashMap<String, String>();
	            
		        loadUIData();
		        
		        long surenessFieldId=projCnt.hasSurenessField(projId);
		        
		        citHand=new CitationHandler(this,projId,surenessFieldId);
		        
		        citHand.setTvSelected(tvSelectedCitations);
		        
				llFilter.setVisibility(View.GONE);
	            setRemoveFilterButton();
	            

		}
		 
		 
		 private void loadUIData() {

			 projCnt.loadProjectInfoById(getIntent().getExtras().getLong("id"));
		        
		     projectType=projCnt.getProjType();
		     projectName=projCnt.getName();
		     thName=projCnt.getThName();
		        
		     thFieldId=projCnt.getThesaurusFieldId(projId);
		     fieldsLabelNames=projCnt.getProjectFieldsPair(projId);
		        
		     //when it has no thesaurus field or thesaurus is not working
		      TextView projNameTv= (TextView)findViewById(R.id.tvRschName);	
		      TextView projThNameTv= (TextView)findViewById(R.id.tvRschDesc);

		      projNameTv.setText(Html.fromHtml("<b>"+ getString(R.string.cLprojectName) +":</b> "+projectName));
		        
		      
		      //when project has a thesaurus linked that's not on the system  
		      boolean thWorking=tC.checkThWorking(thName);
		      if(!thWorking || thFieldId==-1) filterByTaxon.setVisibility(View.GONE);

		      
		      if(!thWorking) {
		        	
		    	  projThNameTv.setTextColor(Color.RED);
		    	  projThNameTv.setText(getString(R.string.projWithoutTh));
		        	
		      }
		      else projThNameTv.setText(Html.fromHtml("<b>"+getString(R.string.cLthName)+": </b>"+projCnt.getThName())); 
		 

		    /* Setting citation list Indicator to right and adding onClickEvent */
		      
		    Display newDisplay = getWindowManager().getDefaultDisplay(); 
			int width = newDisplay.getWidth();
		    
    	    mainCitListView.setIndicatorBounds(width-50, width-10);
    	    
    	    //if(android.os.Build.VERSION.SDK_INT < 11 || android.os.Build.VERSION.SDK_INT >= 16  ) mainCitListView.setFastScrollEnabled(true);
    	    
    	    mainCitListView.setOnGroupClickListener(new OnGroupClickListener() {
    	       
    	    	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,long id) {

    	    		TextView tvCitation=(TextView) v.findViewById(R.id.citationTag);

    	    		CitationControler sC=new CitationControler(v.getContext());
    	    		String atributes=sC.getCitationHTMLValues((Long) tvCitation.getTag(),fieldsLabelNames);
    	    		citListAdap.setChildrenText(atributes,groupPosition);

    	    		
    	    		return false;
    	    	}
    	        
    	    }
    	    
    	    );
			 
		}


		/*
		  * When activity is not visible the cursors to 
		  * the thesaurus are removed
		  * 
		  */
		 @Override
		protected void onPause(){
			 super.onPause();
			  
			 tC.closeCursors();
			 tC.closeThReader();
				  
		 }
		 
		 
		 /*
		  * onResume method
		  * 
		  */
		 @Override
		protected void onResume(){
			 super.onResume();
		     
			 loadMainCitations(refreshList);
		    	
		 }
		    
		 
		 
		    
		 /*
		  * Global method that maintains the list of citations
		  * 
		  * 	It take cares when a filter is chosen and 
		  * 	if it's necessary to order citation alphabetically
		  * 
		  *  After setting the list adapters it refresh the UI information
		  * 
		  */
		 
		 
		 private void loadMainCitations(final boolean forceReload) {

			 refreshList=false;
			 pdMain = ProgressDialog.show(this, "",getString(R.string.citationListLoading), false);
			 pdMain.show();
			 
			 
			   new Thread(new Runnable() {

					public void run() {
						
						Message msg=new Message();
						Bundle b=new Bundle();
						
						b.putBoolean("forceReload", forceReload);
						b.putBoolean("filtered",isFiltered());

						msg.setData(b);
						
						//mainCitationList
						 if(!isFiltered()){
				 			 
						     citHand.loadAllCitations(projId);
						     setCitationAdapter.sendMessage(msg);
					
						 }
						 //filters
						 else{
							 
							    if(forceReload) {
							    	
							    	 citHand.reloadFilterStructure(projId);
							    	 setCitationAdapter.sendMessage(msg);
							    	 
							    }
							    else {
							    	 
							    	 setCitationAdapter.sendMessage(msg);
							    	 
							    }
						 }
											
					}
		              
	            	
		    	}).start();
		 
			 
		}
		 
		 
		 /*
		  * Updates UI info. Filtered, selected and total number of citations
		  * 
		  */
		 
		 
		    private void updateUICounters(boolean filter){
		    	
		    	
		    	if(filter){
		    	
			    	tvFilteredFields.setText(Html.fromHtml(String.format(getString(R.string.countFiltered), citHand.getFilteredCitationList().size())));

		    	} 
		    	else{

		    		tvFilteredFields.setText(Html.fromHtml(String.format(getString(R.string.countFiltered),0)));

		    	}
		    	
	    		tvTotalFields.setText(Html.fromHtml(String.format(getString(R.string.countTotal),citHand.getMainCitationList().size())));
	    		tvSelectedCitations.setText(Html.fromHtml(String.format(getString(R.string.countSelected),citHand.getSelectionList().size())));
		    	
		    }
		    
		    
		    /*
		     * 
		     * Menu extended options
		     * (non-Javadoc)
		     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
		     */
		 

		    @Override

		    public boolean onCreateOptionsMenu(Menu menu) {
		    	
		    	menu.add(0, SHOW_GALLERY, 0,getBaseContext().getString(R.string.mShowGallery)).setIcon(android.R.drawable.ic_menu_gallery);
		    	menu.add(0, IMPORT_CITATIONS, 0,getBaseContext().getString(R.string.mCitationImport)).setIcon(android.R.drawable.ic_menu_save);
		    	menu.add(0, TAXON_CHECKER, 0,getBaseContext().getString(R.string.mCheckTaxonTh)).setIcon(android.R.drawable.ic_menu_set_as);
		    	menu.add(0, SHOW_FIELDS, 0,getBaseContext().getString(R.string.mShowProjectProperties)).setIcon(android.R.drawable.ic_menu_info_details);
		    	menu.add(0, REPORT_CREATOR, 0,getBaseContext().getString(R.string.mCreateReport)).setIcon(android.R.drawable.ic_menu_agenda);
		        
		        return super.onCreateOptionsMenu(menu);
		    }

		    @Override

		    public boolean onOptionsItemSelected(MenuItem item) {
		       
		    	super.onOptionsItemSelected(item);
		    	
		    	
				switch (item.getItemId()) {
					        
				case IMPORT_CITATIONS:
					
					importCitations();
		
			        break;

				case TAXON_CHECKER:
					
					Intent projActivity = new Intent(getBaseContext(),ThesaurusTaxonChecker.class);
		        	startActivity(projActivity);

			        break;
			        
				case SHOW_FIELDS:
					
					showFields();

		        break;
		        
				case SHOW_GALLERY:
					
					showGallery();

		        break;
		        
				case REPORT_CREATOR:
					
					reportCreator();
					

		        break;
			
				
				}
				
		        return true;
			}
		    
			   
		    
		    private void reportCreator() {
		    	
		    	if(citHand.getSelectionList().size()==0){
	        		
	        		Utilities.showToast(getString(R.string.noCitationsSelected),this);
	        		
	        	}
	        	else{
	        		
	        		   if(Utilities.isSdPresent()){
	        			   
	        		    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        		    	builder.setTitle(getString(R.string.chooseExportFormat));

	        		    	final String[] formats =getResources().getStringArray(R.array.reportCreationFormats);   

	        		    	builder.setSingleChoiceItems(formats,-1, new DialogInterface.OnClickListener() {
	        		    		public void onClick(DialogInterface dialog, int item) {

	        		    			final String[] shortFormats =getResources().getStringArray(R.array.reportCreationFormatsShortName);
	        				    	exportDialogCreate(shortFormats[item]);
	        		    			dialog.cancel();

	        		    		}
	        		    	});
	        		    	
	        		    	AlertDialog alert = builder.create();
	        		    	alert.show();
	        			   
	        		   } 

					   else {
				        	
				        	Toast.makeText(getBaseContext(), 
				                    R.string.noSdAlert, 
				                    Toast.LENGTH_SHORT).show();
				        	
				        }
	        	}
	
			}
		    
		    
		    /*
		     * 
		     * Method that shows the project properties
		     * 
		     */
		    
			private void showFields(){
					  
				   	 
				   	Intent intent = new Intent(this, ProjectInfo.class);
				       
		 			Bundle b = new Bundle();
		 			b.putLong("Id", projId);
		 			intent.putExtras(b);
		 			
		 			b.putString("projName", projectName);
		 			intent.putExtras(b);
		 			intent.putExtras(b);
		 			b = new Bundle();
		 			b.putString("projDescription",thName);
		 			intent.putExtras(b);
		 		
		 			
			 		startActivityForResult(intent, EDIT_CITATION);   
				   

			   }
			
		    
		    
		    
		    private void importCitations(){
		    	

			    AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    builder.setTitle(getString(R.string.chooseExportFormat));
									   
			    final String[] formats = getResources().getStringArray(R.array.importCitFormats);   
					   
			    builder.setSingleChoiceItems(formats,-1, new DialogInterface.OnClickListener() {
					       
			    	public void onClick(DialogInterface dialog, int item) {
					           
					    	dialog.cancel();
					    	
					    	importFileChooser(formats[item]);
					    	   
					    }
			    });
					   
			    AlertDialog alert = builder.create();
			    alert.show();

		    	
		    }
		    
		    
		    
		    /*
		     * 
		     * This method call the UI that allows us to import Fagus citations
		     * 
		     */

		    private void importFileChooser(String format) {
			    	
		    	
			    	Intent myIntent = new Intent(this, CitationImport.class);
		        	myIntent.putExtra("id", projId);
		        	myIntent.putExtra("format", format);

		            startActivityForResult(myIntent, UPDATE_LIST);
					
			}
		    
			  
		    /*
		     * 
		     * This method calls gallery Activity with photos related to citations
		     * 
		     */
		    
		    private void showGallery() {
	 			
	        	if(citHand.getSelectionList().size()==0){
	        		
	        		Utilities.showToast(getString(R.string.noCitationsSelected), this);
	        		
	        		
	        	}
	        	else{
	        		
	        		   if(Utilities.isSdPresent()){
	        			   
		       		    	Intent intent = new Intent(this, GalleryGrid.class);
		 		 	       
		    	 			Bundle b = new Bundle();
		    	 			b = new Bundle();
		    	 			b.putLong("id", projId);
		    	 			intent.putExtras(b);
	        			   
	        				b=new Bundle();
	    			    	b.putString("idSelection",citHand.createIdString());
	    		 			intent.putExtras(b);
	    		 			 
	    		 			startActivity(intent);
	        		   } 

					   else {
				        	
				        	Toast.makeText(getBaseContext(), 
				                    R.string.noSdAlert, 
				                    Toast.LENGTH_SHORT).show();
				        	
				        }
	        	}

			}
		  
		    
		  /*
		   *   Methods related to Filters
		   */

		 private void setRemoveFilterButton() {

		        
	            ImageButton imgButton = (ImageButton)findViewById(R.id.btRemoveFilter);
	            imgButton.setBackgroundResource(android.R.drawable.ic_notification_clear_all);
	            
	            imgButton.setOnClickListener(new OnClickListener() {  
		            	
		            	public void onClick(View v) { 

		            		citHand.unCheckAllItems(true);
		            		cbSelectAll.setChecked(false);
		            		removeFilter();
		            	
		             } }
		            
		            );		
		}
		 
	
		 
		 public void removeFilter(){
			 
			 if(citHand.getFilterLevel()==3){
				 
				 tvFilterMessage2.setText("");
				 citHand.removeFilter();

				 
			 }
			 else if(citHand.getFilterLevel()==2){

				 tvFilterMessage1.setText("");
				 citHand.removeFilter();

				 
			 }
			 else if(citHand.getFilterLevel()==1){
				 
				 citHand.removeFilter();
				 tvFilterMessage.setText("");
				 llFilter.setVisibility(View.GONE);
				 
			 }
			 
			 loadMainCitations(false);
		 		 
			 
		 }
		 
		 private boolean isFiltered(){
			 
			 return llFilter.getVisibility()==View.VISIBLE;
			 
		 }
		  
	
		 /*
		  *  Get all data from filter dialogs and apply filters checking all params 
		  * 
		  */
		    
			private void applyChosenFilter(Dialog dialog) {

				
				if(chosenFieldType.equals("date")){
					
					chosenFieldLabel=getString(R.string.slDate);
	
					Date d= new Date();
					d.setMonth(datePicker.getMonth());
					d.setYear(datePicker.getYear()-1900);
					d.setDate(datePicker.getDayOfMonth());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());	 

					filterValue=sdf.format(d);
					
				}
				else if (chosenFieldType.equals("complex")){
					
					filterValue=spListValues.getSelectedItem().toString();
					
				}
				else if(chosenFieldType.equals("boolean") || chosenFieldType.equals("photo")){
					
					if(cbFilter.isChecked()) filterValue="true";
					else filterValue="false";
					
				}
				else if(chosenFieldType.equals("thesaurus")){
					
					filterValue=etValueAuto.getText().toString();
					chosenFieldId=thFieldId;
							
				}
				else{
					
					filterValue=etValue.getText().toString();
					
				}
				
				if(filterValue.equals("")){
					
					Utilities.showToast(getString(R.string.filterDialogEmptyValue), this);
					
				}
				else{
					
					//apply filter
					dialog.dismiss();
					
					if(chosenFieldType.equals("date")){
						
						citHand.loadFilteredCitationsByDate(projId, comparator, filterValue);
						
					}
					else if(chosenFieldType.equals("photo")){
						
						citHand.loadFilteredCitationsByPhoto(projId, chosenFieldId , filterValue);
						
					}
					else{
					
						citHand.loadFilteredCitationsByTextField(projId, chosenFieldId, filterValue);
						
					}
					
					updateUIFilterBar(chosenFieldLabel,comparator,filterValue);
					llFilter.setVisibility(View.VISIBLE);
					
					
					citHand.unCheckAllItems(false);
					cbSelectAll.setChecked(false);
					
			  	    citListAdap= new CitationListAdapter(getBaseContext(),citHand,projId);
		    	       
		    	    mainCitListView.setAdapter(citListAdap);

		    	    updateUICounters(true);
					
				}
				
				
			}
			
			private void updateUIFilterBar(String chosenFieldLabel,String comparator, String filterValue) {
				
				if(citHand.getFilterLevel()==1) tvFilterMessage.setText(Html.fromHtml("<b>"+chosenFieldLabel+"</b> "+comparator+" "+filterValue));
				else if(citHand.getFilterLevel()==2) tvFilterMessage1.setText(Html.fromHtml("<b>"+chosenFieldLabel+"</b> "+comparator+" "+filterValue));
				else if(citHand.getFilterLevel()==3) tvFilterMessage2.setText(Html.fromHtml("<b>"+chosenFieldLabel+"</b> "+comparator+" "+filterValue));
				
			}
			
			
			/*
			 * Dialog builder and handler of location filter
			 * It's possible to filter by UTM and lat/long location
			 * 
			 */

			private OnClickListener filterByLocationListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		        	
		        	locationFilterdialog = new Dialog(v.getContext());
		 		   	locationFilterdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 		   	locationFilterdialog.setContentView(R.layout.citationfilterbylocation);
		 		 
		 		   	Button applyFilter = (Button)locationFilterdialog.findViewById(R.id.btApplyFilterByLocation);
		 		   
		 		   	final RadioButton rbFilterByUTM = (RadioButton) locationFilterdialog.findViewById(R.id.rbFilterByUTM);
		 		   	final RadioButton rbFilterByLL = (RadioButton) locationFilterdialog.findViewById(R.id.rbFilterByLatLong);
		 		   	final TextView tvFilterByLocTV= (TextView) locationFilterdialog.findViewById(R.id.tvFilterByLocHint);
		 		   	final EditText etFilterValue=(EditText) locationFilterdialog.findViewById(R.id.etFilterLocationValue);
		 		   	
		    	   rbFilterByUTM.setOnClickListener(new RadioButton.OnClickListener(){
			             
		    	    	public void onClick(View v){

		    	    		tvFilterByLocTV.setText("ex. 31TCF55 | 31TCF524534");
		    	    	                
		    	    	}
		    	    	             
		    	    });
		    	    
		 		  rbFilterByLL.setOnClickListener(new RadioButton.OnClickListener(){
			             
		    	    	public void onClick(View v){

		    	    			tvFilterByLocTV.setText("ex. 41.23123 1.213123 (lat/long)");
		    	    	                
		    	    	    }
		    	    	             
		    	    });
		 		  
					  applyFilter.setOnClickListener(new Button.OnClickListener(){
	
			    	    	public void onClick(View v){
			    	    		
		    	    			String locationValue=etFilterValue.getText().toString();
			    	    			
		    	    			boolean utmData=rbFilterByUTM.isChecked();
			    	    			
			    	    		if(!Utilities.checkCoordinates(locationValue,utmData)){
			    	    				
			    	    			Utilities.showToast(getString(R.string.alertWrongCoordinates), v.getContext());
			    	    				
			    	    		}
			    	    		else{
			    	    				
			    	    			citHand.unCheckAllItems(false);
				    				cbSelectAll.setChecked(false);
			    	    					
				    	    		if(utmData) citHand.filterByUTM(locationValue);    		
				    	    		else citHand.filterByLatLong(locationValue);
				    	    							    					
				    			    citListAdap= new CitationListAdapter(getBaseContext(),citHand,projId);
				    		    	       
				    		        mainCitListView.setAdapter(citListAdap);
			    	    				
				    		    	    
				    		        locationFilterdialog.dismiss();
				    	
				    		        chosenFieldType="location";
				    		        
				        			updateUIFilterBar("UTM", "=", locationValue);
				    					
				       			 	llFilter.setVisibility(View.VISIBLE);
				    					
				    				updateUICounters(true);
				    					
			    	    				
			    	    		}
			    	    							    	    		
			    	    	}
	
			    	    	             
			    	    });

		 		   locationFilterdialog.show();
		        	
		        }
		    };
		    
		    
		    
			/*
			 * 	Remove citations listener. 
			 * 	Builds confirm dialog and if it's confirmed it calls the removeCitations Thread 
			 * 
			 */
			
			private OnClickListener removeCitListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		        	    	
		        	if(citHand.getSelectionList().size()>0){
		        		
		        		
		        		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		   			    
		   			    builder.setMessage(String.format(getString(R.string.removeAllCitationsQuestion), citHand.getSelectionList().size()))
		   			           .setCancelable(false)
		   			           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		   			               public void onClick(DialogInterface dialog, int id) {
		   			            	   
		   			            	   removeCitacionsThreadCreator();
		   			   	  		  	
		   			               }
		   			           })
		   			           .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		   			               public void onClick(DialogInterface dialog, int id) {
		   			                    dialog.cancel();
		   			               }
		   			           });
		   			   
		   			    
		   				 AlertDialog alert = builder.create();
		   				 alert.show();
		        		
		        		
		        	}
		        	else{
		        		
			        	Utilities.showToast(getString(R.string.noCitationsSelected), v.getContext());

		        	}
		        	
		        	
		        }
		    };
		    
		    
		    
			/*
			 * 	Show photo citations listener. 
			 * 
			 */
			
			private OnClickListener photoCitListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {        
		        	   	
					showGallery();
					
		        }
		    };
		    
		    
		    /*
			 * Dialog builder and handler of Taxon filter
			 * 
			 * 	It's possible to filter by:
			 * 		+ A concrete gender
			 * 		+ A concrete taxon Name
			 * 		+ Taxons not belonging to the project thesaurus
			 * 
			 */
			
			
			private OnClickListener filterByTaxonListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		        	
		        	taxonFilterdialog = new Dialog(v.getContext());
		        	taxonFilterdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 		   	taxonFilterdialog.setContentView(R.layout.citationfiltertaxon);
		 		 
		 		   	Button applyFilter = (Button)taxonFilterdialog.findViewById(R.id.btApplyTaxonFilter);
		 		   
		 		   	final RadioButton rbTaxonComplete = (RadioButton) taxonFilterdialog.findViewById(R.id.rbFilterCompleteTaxon);
		 		   	final RadioButton rbTaxonGenus = (RadioButton) taxonFilterdialog.findViewById(R.id.rbFilterTaxonGenus);
		 		   	final RadioButton rbTaxonNoTh = (RadioButton) taxonFilterdialog.findViewById(R.id.rbFilterWrongNames);

		 		   	etValueAuto=(AutoCompleteTextView) taxonFilterdialog.findViewById(R.id.tvTaxonName);
		 		   	
		 			String thName=projCnt.getThName();				   
					final boolean thStatus=tC.initThReader(thName);
					
					 if(thStatus){
						   
						  ThesaurusAutoCompleteAdapter elements = tC.fillData(etValueAuto);
						  etValueAuto.setAdapter(elements);
		
					  }
		 		   	
		 		   	
		 		   rbTaxonNoTh.setOnClickListener(new RadioButton.OnClickListener(){
			             
		    	    	public void onClick(View v){

		    	    	        
		    	    		etValueAuto.setVisibility(View.GONE);
		    	    		
		    	    	} 
		    	    	             
		    	    });
		    	    
		 		  rbTaxonGenus.setOnClickListener(new RadioButton.OnClickListener(){
			             
		    	    	public void onClick(View v){

		    	    		etValueAuto.setVisibility(View.VISIBLE);
		    	    		etValueAuto.setText("");
		    	    		
		    	    		 ThesaurusGenusAutoCompleteAdapter elements = tC.fillGenusData(etValueAuto);
							 etValueAuto.setAdapter(elements);

		    	    	                
		    	    	}
		    	    	             
		    	    });
		 		  
		 		  rbTaxonComplete.setOnClickListener(new RadioButton.OnClickListener(){
			             
		    	    	public void onClick(View v){

		    	    		etValueAuto.setVisibility(View.VISIBLE);
		    	    		etValueAuto.setText("");
						
					  
							  if(thStatus){
							   
								  ThesaurusAutoCompleteAdapter elements = tC.fillData(etValueAuto);
								  etValueAuto.setAdapter(elements);
				
							  }

		    	    	                
		    	    	    }
		    	    	             
		    	    });
		 		  
					  applyFilter.setOnClickListener(new Button.OnClickListener(){
	
			    	    	public void onClick(View v){
			    	    		
			    	    			citHand.unCheckAllItems(false);
				    				cbSelectAll.setChecked(false);
			    			    	
				    			    if(rbTaxonGenus.isChecked()){
				    			    	
				    			    	String genus=etValueAuto.getText().toString();
				    			    	chosenFieldType="genus";
				    			    	filterThesaurus(chosenFieldType, genus);
				    			    	
				    			    }
				    			    /* New list with wrong taxon names */
				    			    else if(rbTaxonNoTh.isChecked()){
				    			    	
				    			    	chosenFieldType="notExists";
				    			    	filterThesaurus(chosenFieldType, "");
				    			    	
				    			    }
				    			    /* Common thSearch */
				    			    else if(rbTaxonComplete.isChecked()){
				    			    	
				    			    	chosenFieldType="thesaurus";
				    			    	filterThesaurus(chosenFieldType, etValueAuto.getText().toString());

				    			    }
				    			    
				    			    else{
				    			    	
					    				taxonFilterdialog.dismiss();

				    			    }
				    			    	    
			    	    		}
	
			    	    	             
			    	    });

		 		   taxonFilterdialog.show();
		        	
		        }
		    };
		    

		    
		    
		    
		    private void filterThesaurus(String type, final String value){
		    	
		    	this.filterValue=value;
		    	
			    if(type.equals("genus")){
		    	
			        pdCheckingTh = new ProgressDialog(this);
    			    pdCheckingTh.setMessage(getString(R.string.filterTaxonGenusMessage)+" "+value);
    			    pdCheckingTh.show();
    				

    				                 Thread thread = new Thread(){
    				  	        	   
    					                 @Override
										public void run() {
    					               	  
    						    				citHand.filterThByGenus(value,thCheckHandler);    		
					               	  
    					                 }
    					           };
    					           
    					           
    				   thread.start();
    				   
    				  if(taxonFilterdialog.isShowing()) taxonFilterdialog.dismiss();
			    	
			    }
			    /* New list with wrong taxon names */
			    else if(type.equals("notExists")){
			    	
			    	
			        pdCheckingTh = new ProgressDialog(this);
    			    pdCheckingTh.setMessage(getString(R.string.filterNotInThesaurusMessage));
    			    pdCheckingTh.show();
    				

    				                 Thread thread = new Thread(){
    				  	        	   
    					                 @Override
										public void run() {
    					               	  
    						    				citHand.filterThMissing(tC, thCheckHandler);	
					               	  
    					                 }
    					           };
    					           
    					           
    				   thread.start();
    				   if(taxonFilterdialog.isShowing())  taxonFilterdialog.dismiss();
			    	
			    }
			    else if(type.equals("thesaurus")){
			    	
			    	applyChosenFilter(taxonFilterdialog);		    	
			    	
			    }
		    	
		    }
		    
		    
			
			/*
			 *  Dialog builder and handler of field filter method
			 *  
			 *  	Depending on the type of field the value will be chosen from:
			 *  	+ number & text (EditText)
			 *  	+ thesaurus (AutoComplete)
			 *  	+ complex (Spinner)
			 *  	+ boolean (CheckBox)
			 *  
			 */
			
		 
			private void handleFieldType(String type,String fieldId, LinearLayout layoutFilter) {

				chosenFieldId=Long.valueOf(fieldId);
				chosenFieldType=type;
				
				etValue=(EditText)layoutFilter.findViewById(R.id.etFilterFieldValue);
				etValueAuto=(AutoCompleteTextView)layoutFilter.findViewById(R.id.etFilterFieldValAuto);
				spListValues=(Spinner)layoutFilter.findViewById(R.id.spComplexValues);
				cbFilter=(CheckBox)layoutFilter.findViewById(R.id.cbFilterBool);

				etValue.setVisibility(View.VISIBLE);
				etValueAuto.setVisibility(View.GONE);
				
				etValue.setText("");
				etValueAuto.setText("");
				
				cbFilter.setVisibility(View.GONE);

				
				if(type.equals("number")){
					
					etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

					
				}
				else{
				
					spListValues.setVisibility(View.GONE);
			 		 
					
					if(type.equals("complex")){
						
						//carregar llista de complexos
						spListValues.setVisibility(View.VISIBLE);
						etValue.setVisibility(View.GONE);
						
						 DataTypeControler dtH=new DataTypeControler(this);

						String[] items=dtH.getItemsbyFieldId(Long.valueOf(fieldId));
							
						ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
					                    android.R.layout.simple_spinner_item,items);
					
							  
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							 
						spListValues.setAdapter(adapter);
						spListValues.setPrompt(getString(R.string.chooseItem));
						
					}
					else if(type.equals("thesaurus")){
						
						etValueAuto.setVisibility(View.VISIBLE);
						etValue.setVisibility(View.GONE);

						ProjectControler rsC=new ProjectControler(this);
						rsC.loadProjectInfoById(projId);

						String thName=rsC.getThName();				   
						boolean thStatus=tC.initThReader(thName);
				  
						  if(thStatus){
						   
							  ThesaurusAutoCompleteAdapter elements = tC.fillData(etValueAuto);
							  etValueAuto.setAdapter(elements);
			
						  }
					
						
					}
					else if(type.equals("photo")){
						
						etValue.setVisibility(View.GONE);
						cbFilter.setVisibility(View.VISIBLE);
						cbFilter.setText("Conté fotografia");
						
					}
					else if(type.equals("boolean")){
						
						etValue.setVisibility(View.GONE);
						cbFilter.setVisibility(View.VISIBLE);
						
						
						
					}	
					else{
						
						
					}
					
				}
				
				
			}


		    /*
		     * Citation removal panel and thread
		     * 
		     */
			
			private void removeCitacionsThreadCreator() {
				 
				
			    pdRemove = new ProgressDialog(this);
			    pdRemove.setCancelable(true);
			    pdRemove.setMessage(getString(R.string.citRemovalLoading));
			    pdRemove.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			    pdRemove.setProgress(0);
			    pdRemove.setMax(citHand.getSelectionList().size());
			    pdRemove.show();
				
				
				// pd = ProgressDialog.show(this, getString(R.string.citRemovalLoading), getString(R.string.citRemovalTxt), true,false);

				                 Thread thread = new Thread(){
				  	        	   
					                 @Override
									public void run() {
					               	  
					                	 removeCitationsThread();
					               	  
					                 }
					           };
					           
					           
				   thread.start();
				}	
			 
			 
				/*
				 * Remove citations thread
				 * 
				 * Adapted to handle only citations chosen
				 * 
				 */

			   private void removeCitationsThread(){
				   
				   ProjectSecondLevelControler slPC=new ProjectSecondLevelControler(getBaseContext());
				   CitationControler sC=new CitationControler(this);
		    	   long[] slIds=slPC.getTwoLevelFieldListByProjId(projId);

		    	
		    	   // selectionIds: list of citations chosen
		    	  Set<Long> selectionIds=citHand.getSelectedCitationsId();
		    	
		    	  Iterator<Long> iter = selectionIds.iterator();
		    	  
		    	  int i=0;
			    	   
		    	  while (iter.hasNext()) {
			    	   		
		    		  	long citationId=iter.next();
		    		  
			   			//remove secondary citations
			   			if(slIds.length>0){
			   				
			   				CitationSecondLevelControler sL=new CitationSecondLevelControler(getBaseContext());
			   				sL.deleteCitationFromProject(citationId,slIds);
			          
			   			}
					
					   sC.deleteCitation(citationId);
				   
					   i++;
					   
					   handlerRemove.sendMessage(handlerRemove.obtainMessage());
					   
		    	    }
	
	        	   
				   handlerRemove.sendEmptyMessage(1);
	     			

			   }
			   
			   
			    
			    private Handler thCheckHandler = new Handler() {

			    	@Override
					public void handleMessage(Message msg) {
	                 
	                	
	                	citListAdap= new CitationListAdapter(getBaseContext(),citHand,projId);		    	       
		    		    mainCitListView.setAdapter(citListAdap);
	    					
		    		    if(chosenFieldType.equals("genus")) updateUIFilterBar(getString(R.string.filterTaxonGenus),"", filterValue);
		    		    else if(chosenFieldType.equals("notExists")) updateUIFilterBar(getString(R.string.filterNotInThesaurus),"", filterValue);
		    		    		    		    
		   			 	llFilter.setVisibility(View.VISIBLE);
	    					
	    				updateUICounters(true);
	    					
			    		pdCheckingTh.dismiss();

	    				tC.closeThReader();

	                	
	 
	                }
			    };
			    
			   
			   
			   
			   /*
			    * This handler handles the result of the remove dialog:
			    * 
			    * 	+ Incrementing progress bar msg.what==0
			    * 	+ Ending the process msg.what==1
			    * 
			    */
			   
				 private Handler handlerRemove = new Handler() {

						@Override
						public void handleMessage(Message msg) {	

							if(msg.what==1){
								
								pdRemove.dismiss();

								llFilter.setVisibility(View.GONE);
								cbSelectAll.setChecked(false);
								citHand.unCheckAllItems(false);
								
				        		loadMainCitations(false);
								
							}
							else{
								
								pdRemove.incrementProgressBy(1);
								
							}

						}
					};
		 
			/*
			 * Check all citations shown
			 * 
			 */
		    
		    private OnCheckedChangeListener selectAllListener = new OnCheckedChangeListener(){

				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					if(isChecked) citHand.checkAllItems(llFilter.getVisibility()==View.VISIBLE);
					else citHand.unCheckAllItems(llFilter.getVisibility()==View.VISIBLE);
					
					
			  	    citListAdap= new CitationListAdapter(getBaseContext(),citHand,projId);
		    	       
		    	    mainCitListView.setAdapter(citListAdap);
		    	    
		    	    updateUICounters(llFilter.getVisibility()==View.VISIBLE);
					
				}
		    	
		    };
		    
		    /*
		     * 
		     * Export chosen citations
		     * It will call the method that shows the list of export formats
		     * 
		     */
		    
			private OnClickListener exportButtonListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		        	    
		        	if(citHand.getSelectionList().size()==0){
		        		
		        		
		        		Utilities.showToast(getString(R.string.noCitationsSelected), v.getContext());
		        		
		        		
		        	}
		        	else{
		        		
		        		   if(Utilities.isSdPresent()) exportCitationsDestination();

						   else {
					        	
					        	Toast.makeText(getBaseContext(), 
					                    R.string.noSdAlert, 
					                    Toast.LENGTH_SHORT).show();
					        	
					        }
		        				        		
		        	}

			        	
		        }
		    };
		    
		    
		    /*
		     *  Show googleMapsListener
		     * 		     
		     */
            
        	private OnClickListener showMapListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {        
		        	
		        	// if all citations are selected
		        	
		        	if(citHand.getMainCitationList().size()==citHand.getSelectionList().size()){
		        		
		        	   	Intent myIntent = new Intent(v.getContext(), CitationMap.class);
			        	myIntent.putExtra("id", projId);

			            startActivityForResult(myIntent, 0);
		        		
		        	}
		        	
		        	//only chosen citations
		        	else if(citHand.getSelectionList().size()>0){
		        		
		            	Intent myIntent = new Intent(v.getContext(), CitationMap.class);
			        	myIntent.putExtra("id", projId);
			        	myIntent.putExtra("idSelection",citHand.createIdString());

			            startActivityForResult(myIntent, 0);
		        		
		        	}
		        	else{
		        		
		        		Utilities.showToast(getString(R.string.noCitationsSelected), v.getContext());

		        		
		        	}
		        }
		    };
		    

		    /*
		     * Order all citations on the list alphabetically
		     * 
		     */
		    
		    
			private OnClickListener orderAlphaListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		        	    	
		        	if(citHand.isChronoOrder()) {
		        		
		        		btOrderAlpha.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));
    	    			btOrderCron.getBackground().invalidateSelf();
    	    			btOrderCron.getBackground().setColorFilter(null);	
		        
		        		citHand.setAlphaOrder(true);
		        
		        		loadMainCitations(false);
		        		
		        	}
		        	else{
		        		
		        		if(citHand.isAlphaAsc()){
		        			
		        			btOrderAlpha.setBackgroundResource(R.drawable.order_alpha_des);
			        		btOrderAlpha.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));
		        			citHand.setAlphaAsc(false);
		        			
		        		}
		        		else{
		        			
		        			btOrderAlpha.setBackgroundResource(R.drawable.order_alpha);
			        		btOrderAlpha.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));
		        			citHand.setAlphaAsc(true);

		        		}
		        		
		        		loadMainCitations(true);
		        				        		
		        	}
		        	
		        }
		    };
		    
		    
		    /*
		     * Order all citations on the list chronologically
		     * 
		     */
		    
		    
			private OnClickListener orderCronListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		        	    	
		        	if(citHand.isAlphaOrder()) {
		        		
		        		btOrderCron.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));
		        		btOrderAlpha.getBackground().invalidateSelf();
		        		btOrderAlpha.getBackground().setColorFilter(null);	
		        		
		        		citHand.setAlphaOrder(false);
			    	    mainCitListView.setTextFilterEnabled(false);

		        		loadMainCitations(false);
		        		
		        	} 
		        	else{
		        		
		        		if(citHand.isChronoAsc()){
		        			
		        			btOrderCron.setBackgroundResource(R.drawable.ordre_cron_asc);
			        		btOrderCron.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));
		        			citHand.setChronoAsc(false);
		        			
		        		}
		        		else{
		        			
		        			btOrderCron.setBackgroundResource(R.drawable.ordre_cron);
			        		btOrderCron.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF89CC62));
		        			citHand.setChronoAsc(true);
		        			
		        		}
		        		
		        		loadMainCitations(true);

		        		
		        		
		        	}
		        	
		        }
		    };
		    
		    
			private OnClickListener applyFilterListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		        	    	
		
		        	final Dialog dialog = new Dialog(v.getContext());
		 		    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 		   	dialog.setContentView(R.layout.citationfilterfield);

		 		   Button applyFilter = (Button)dialog.findViewById(R.id.btApplyFilter);
		 		   final LinearLayout llFieldSelection = (LinearLayout) dialog.findViewById(R.id.llFieldSelection);
		 		   Spinner fieldsList=(Spinner)dialog.findViewById(R.id.spFieldsList);

		    	  
		 		   final String[] predValues=loadFields();

		 		  ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item,predValues);
		 		  m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 		  
		 		  fieldsList.setAdapter(m_adapterForSpinner);
	    			
		 		  fieldsList.setOnItemSelectedListener(new OnItemSelectedListener() {


						public void onItemSelected(AdapterView<?> arg0,View arg1, int arg2, long arg3) {
					
							chosenFieldLabel=predValues[arg2];
							
							String[] fieldType=projFieldsPairs.get(chosenFieldLabel).split(":");
							handleFieldType(fieldType[0],fieldType[1],llFieldSelection);
							
						}


						public void onNothingSelected(AdapterView<?> arg0) {
							
						}

					});
				 
				  applyFilter.setOnClickListener(new Button.OnClickListener(){

		    	    	public void onClick(View v){
		    	    		
		    	    		applyChosenFilter(dialog);
	    	                 			
		    	    	}

		    	    	             
		    	    });

		 	 		 	dialog.show();
		        	
		        	
		        }
		    };
		    
		    
		    private OnClickListener filterByDateListener = new OnClickListener()
		    {
		        public void onClick(View v)
		        {                        
		        	    	
		
		        	dateFilterDialog = new Dialog(v.getContext());
		 		   	dateFilterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 		   	dateFilterDialog.setContentView(R.layout.citationfilterdate);
		 		 
		 		   //  dialog.setTitle(R.string.projCreationTitle);

		 		   Button applyFilter = (Button)dateFilterDialog.findViewById(R.id.btApplyFilter);
		 		   final Button btGreater=(Button)dateFilterDialog.findViewById(R.id.btGreaterFilter);
		 		   final Button btLess=(Button)dateFilterDialog.findViewById(R.id.btLessFilter);
		 		   final Button btEq=(Button)dateFilterDialog.findViewById(R.id.btEqualFilter);
		 		   final LinearLayout llDateSelection = (LinearLayout) dateFilterDialog.findViewById(R.id.llDateSelection);
		      
		    	   datePicker=(DatePicker)dateFilterDialog.findViewById(R.id.datePickerFilter);
		    	  
    			
		 		  btEq.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));

		 		  chosenFieldType="date";
		 		  comparator="=";
		    	    
		 	    
		 		  btGreater.setOnClickListener(new Button.OnClickListener(){

		    	    	public void onClick(View v){
		    	    		
		    	    			btGreater.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
		    	    			btLess.getBackground().invalidateSelf();
		    	    			btLess.getBackground().setColorFilter(null);
		    	    			btEq.getBackground().invalidateSelf();
		    	    			btEq.getBackground().setColorFilter(null);
		    	    			comparator=">";
		    	    			
		    	    		}
		    	    	             
		    	    });
		 		  
				  btEq.setOnClickListener(new Button.OnClickListener(){

		    	    	public void onClick(View v){
		    	    		
		    	    			btEq.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
		    	    			btLess.getBackground().invalidateSelf();
		    	    			btLess.getBackground().setColorFilter(null);
		    	    			btGreater.getBackground().invalidateSelf();
		    	    			btGreater.getBackground().setColorFilter(null);
		    	    			comparator="=";
		
		    	    		}
		    	    	             
		    	    });
				  
				  btLess.setOnClickListener(new Button.OnClickListener(){

		    	    	public void onClick(View v){
		    	    		
		    	    			btLess.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
		    	    			btEq.getBackground().invalidateSelf();
		    	    			btGreater.getBackground().setColorFilter(null);	
		    	    			btGreater.getBackground().invalidateSelf();
		    	    			btEq.getBackground().setColorFilter(null);
		    	    			comparator="<";
		    	    			
		    	    		}
		    	    	             
		    	    });
				  
							 
				  applyFilter.setOnClickListener(new Button.OnClickListener(){

		    	    	public void onClick(View v){
		    	    		
		    	    			applyChosenFilter(dateFilterDialog);
	    	                 			
		    	    		}

		    	    	             
		    	    });

		 			 dateFilterDialog.show();
		        	
		        }
		    };
		 

	    protected void exportFormatChoose() {

	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			   builder.setTitle(getString(R.string.chooseExportFormat));
							   
			   final String[] formats =excludeFormats(getResources().getStringArray(R.array.mainExportFormats),projectType);   
			   
			   builder.setSingleChoiceItems(formats,-1, new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int item) {
			           
			    	   dialog.cancel();
			    	   exportDialogCreate(formats[item]);
			    	   
			       }
			   });
			   AlertDialog alert = builder.create();
			   alert.show();
	    	
	    	
		}
	    
	    protected void exportCitationsDestination() {

	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.citationExportDestination));
			
							   
			   final String[] formats =getResources().getStringArray(R.array.exportDestination);   
			   
			   builder.setSingleChoiceItems(formats,-1, new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int item) {
			           
			    	   dialog.cancel();
			    	   
			    	   	if(item==1) exportMail=true;
			    	   	else exportMail=false;
			    	   
			    	   exportFormatChoose();
			    	   
			       }
			   });
			   AlertDialog alert = builder.create();
			   alert.show();
	    	
	    	
		}
	    
		private String[] excludeFormats(String[] formats,String projectType) {

			String [] result;
			
			if(projectType==null){
				
				result=formats;
				
			}			
			else if(projectType.equals("Fagus")){
			
				result= new String[formats.length+1];
				
				for(int i=0; i<formats.length; i++){
					
					result[i]=formats[i];
					
				}

				result[formats.length]="Xflora";
				
				
			}
			else if(projectType.equals("Quercus")){
				
				result= new String[formats.length+1];
				
				for(int i=0; i<formats.length; i++){
					
					result[i]=formats[i];
					
				}
				
				result[formats.length]="Quercus";
			}
			else{
				
				result=formats;
				
			}
			
			return result;
		}
	    
		private void exportDialogCreate(final String format){
			
			dialog = new Dialog(this);
        	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        	dialog.setContentView(R.layout.exportdialog);
    	   	
    	   	Button bExportFagus = (Button)dialog.findViewById(R.id.bExportFagus);
  	   	  	final Spinner fieldsList=(Spinner)dialog.findViewById(R.id.spFieldsListReport);
    	   	EditText name=(EditText)dialog.findViewById(R.id.fileName);
    	   	final CheckBox cbSendByMail = (CheckBox)dialog.findViewById(R.id.cbExportSendByMail);

    	   	
    	   	if(format.contains("report")){
    	   		   	   		
        	   	TextView tvReportLabel=(TextView)dialog.findViewById(R.id.tvCreateReportInfo);

	    	   	cbSendByMail.setVisibility(View.VISIBLE);
      	   	  	fieldsList.setVisibility(View.VISIBLE);
      	   	  	tvReportLabel.setVisibility(View.VISIBLE);
      	   	  	
      	   	  	
      	   	  	/* populating fieldChooser */
      	   	  	final String[] predValues=loadFields();

      	   	  	ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,predValues);
      	   	  	m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		  
      	   	  	fieldsList.setAdapter(m_adapterForSpinner);
        	   	bExportFagus.setText(getString(R.string.btCreateReport));
        	   	
        	   	name.setText(projectName+"_"+ this.getString(R.string.etReport));
        	   	
        	   	

    	   	}
    	   	else{
    	   		
      	   	  	fieldsList.setVisibility(View.GONE);
        	   	name.setText(projectName+"_"+format);

        	   	bExportFagus.setText(bExportFagus.getText()+" "+format);
    	   		
    	   	}
    	   	
    	   	
    	    bExportFagus.setOnClickListener(new Button.OnClickListener(){
    	    	             
    	    	
    	    	public void onClick(View v){
    	    		
    	    				long fieldId=-1;
    	    		
    	    		
    	    				if(format.contains("report")){
    	    			
			    	    		String[] fieldType=projFieldsPairs.get(fieldsList.getSelectedItem().toString()).split(":");
			    	    			
		    	    			fieldId=Long.decode(fieldType[1]); 
		    	    			
		    	    			exportMail=cbSendByMail.isChecked();
			            	   	
    	    				}

    	    	                 EditText et=(EditText)dialog.findViewById(R.id.fileName);
    	    	                 exportFileCheck(et.getText().toString(),format,fieldId);
    	    	                 
    	    	              }
    	    	             
    	    });
    	    
    	    dialog.show();
			
		}  
		
		  private Handler filterThAdapter = new Handler() {

		    	@Override
				public void handleMessage(Message msg) {
             
		    		pdMain.dismiss();
				    filterThesaurus(chosenFieldType, filterValue);
		    		  	 
      
            }
		    };
		    
		    
		  private Handler setCitationAdapter = new Handler() {

		    	@Override
				public void handleMessage(Message msg) {
               
		
		    		Bundle b=msg.getData();
		    		boolean forceReload=b.getBoolean("forceReload");
		    		boolean filtered=b.getBoolean("filtered");		    		
		    		
				    citListAdap=citHand.getListAdapter(filtered);
				    
				    				    
			    	mainCitListView.setAdapter(citListAdap);
			    	
		    		
		    		//when list is ordered alphabetically we set the TextFilter
			    	if(citHand.isAlphaOrder()) mainCitListView.setTextFilterEnabled(true);
			    	else mainCitListView.setTextFilterEnabled(false);
			    	 
			    	 
			    	updateUICounters(isFiltered());
			    	 
			    	if(forceReload){
			    		 
			    		int pastPostion=citHand.getListPosition();
			    		 
			    		if(pastPostion <= mainCitListView.getCount()) {
			    			 
			    			mainCitListView.setSelection(pastPostion);
			    			 
			    		}
			    		
			    	 }
			    	
			    	pdMain.dismiss();
			    	 
        
              }
		    };
		
		
		   /*
		    * This handler handles the result of the import dialog:
		    * 
		    * 	+ Incrementing progress bar msg.what==0
		    * 	+ Ending the process msg.what==1
		    * 
		    */
		   
			 private Handler handlerExportProcessDialog = new Handler() {

					@Override
					public void handleMessage(Message msg) {	

						if(msg.what==1){
							
							pdCitationExport.dismiss();
							
							dialog.dismiss();
							
							String toastText=getString(R.string.bFagusFileExported);

							
				           Toast.makeText(getBaseContext(), 
				       	                toastText+fileName, 
				       	                 Toast.LENGTH_SHORT).show();
				           
				           
				           if(exportMail){
				        	   
				        	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				        	   String curentDateandTime = sdf.format(new Date());
				        	   
				        	   String exportSubject = String.format(getString(R.string.citationExportEmailSubject),projectName);  
				        	   String exportSubjectText = String.format(getString(R.string.citationExportEmailText), projectName,curentDateandTime,fExp.getFormat());  
				        	   

				        	   Intent sendIntent = new Intent(Intent.ACTION_SEND);
				               sendIntent.setType(fExp.getExportMimeType());
				               sendIntent.putExtra(Intent.EXTRA_SUBJECT,exportSubject);
				               sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fExp.getFile().getAbsolutePath()+""));
				               sendIntent.putExtra(Intent.EXTRA_TEXT, exportSubjectText);
				               startActivity(Intent.createChooser(sendIntent, "Email:"));
				        	   
				           }
							
										
						}
						else{
							
							pdCitationExport.incrementProgressBy(1);
							
						}


					}
				};
	    
		
		
		 private int exportCitations(String format, long fieldId){
			 
			 Log.i("Export","Format:"+format+" | (A) Action: Importing Citations ("+citHand.getSelectedCitationsId().size()+")");
			 Log.i("Export","Format:"+format+" | (A) Info-> projectName:"+projectName+" fileName: "+fileName);	

			 int statusFinal=0;
			 
			 /* When our project is exportable to Quercus we'll use the method implemented in the subclass SecondLevelSampleControler */
			 
			 if(format.equals("Quercus")){
								 
				 CitationSecondLevelControler sC=new CitationSecondLevelControler(this);
				 statusFinal=sC.exportProjectQuercus(projId, this,citHand.getSelectedCitationsId(), fileName, format,handlerExportProcessDialog);
				 
			 }
			 else if(format.equals("reportDocumentLabel")){
				 
				 ReportControler repCnt=new ReportControler(this);	
				 
				 statusFinal=repCnt.exportProject(projId,citHand.getSelectedCitationsId(),fileName, format,handlerExportProcessDialog,fieldId);

				 
			 }
			 else{ 
		
					CitationControler sC=new CitationControler(this);
					statusFinal=sC.exportProject(projId,citHand.getSelectedCitationsId(),fileName, format,handlerExportProcessDialog);
					
					
					ProjectSecondLevelControler slP=new ProjectSecondLevelControler(this);
				
					if(slP.isQuercusExportable(projId)>-1){
						
						CitationSecondLevelControler slC=new CitationSecondLevelControler(this);
						
						slC.exportSubCitations(projId,citHand.getSelectedCitationsId());
						
					
				}
				 
			 }

			 

				handlerExportProcessDialog.sendEmptyMessage(1);

				 Log.i("Export","Format:"+format+" | (A) Action: Citations Imported");	
				 
				 return statusFinal;
		 }
		 
		 
			
			
			
			private void exportFileCheck(String fileName, final String format, final long fieldId){
				
				//checking that file exists
				
				fExp=new FileExporter(this);
				boolean exists=fExp.createFile(format, fileName);
				
				this.fileName=fileName;
		        
		        if(exists) {
		        	
		        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        	
					    builder.setMessage(getString(R.string.citFileExists))
					           .setCancelable(false)
					           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					               public void onClick(DialogInterface dialog, int id) {
					   
						   				dialog.dismiss();
						   				createExportThread(format,fieldId);
					   	  		  	
					               }
					           })
					           .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					               public void onClick(DialogInterface dialog, int id) {
					            	   	
						   				dialog.dismiss();
						
					               }
					           });
					   
					    
						 AlertDialog alert = builder.create();
						 alert.show();
		        	
		        }
		        else{
		
					createExportThread(format,fieldId);
		
		        }
				
			}
			
		 
			private void createExportThread(final String format, final long fieldId){
				
			    pdCitationExport = new ProgressDialog(this);
			    pdCitationExport.setCancelable(true);
			    
			    if(format.contains("report")){
			    	
				    pdCitationExport.setMessage(getString(R.string.pdCreateReportTitle));
			    	
			    }
			    else{
			    	
				    pdCitationExport.setMessage(getString(R.string.citExportLoading)+" "+format);
				    
			    }
			    			    
			    pdCitationExport.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			    pdCitationExport.setProgress(0);
			    pdCitationExport.setMax(citHand.getSelectedCitationsId().size());
			    pdCitationExport.show();
								 
				                 Thread thread = new Thread(){
				  	        	   
					                 @Override
									public void run() {
					               	  
					                	exportCitations(format,fieldId); 
					               	  
					                 }
					           };
					           
					           
				   thread.start();
			}

			
			private String[] loadFields() {

				ProjectControler projCnt=new ProjectControler(this);
				
				ArrayList<ProjectField> fieldList=projCnt.getAllProjectFields(projId);
				Iterator<ProjectField> iterator=fieldList.iterator();
		
				String[] items=new String[fieldList.size()];
				int i=0;
				
				while(iterator.hasNext()){
					
					ProjectField tmpPF=iterator.next();
					items[i]=tmpPF.getLabel();
					
					//we store fieldType + ":" + fieldID to handle Spinner click
					projFieldsPairs.put(tmpPF.getLabel(),tmpPF.getType()+":"+tmpPF.getId());
					
					i++;
					
				}

				return items;
			}
			
			    
		    
		
		    
		    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		        // super.onActivityResult(requestCode, resultCode, intent);
		    	
		        	
		      switch(requestCode) {
		             
		                 
		         case EDIT_CITATION :
	
		        	citHand.loadSurenessValues(); 	

		        	 int count =  citListAdap.getGroupCount();
		        	 for (int i = 0; i <count ; i++) mainCitListView.collapseGroup(i);
		            
		             break;
		             
		         case UPDATE_LIST:
		        	 	    
		        	citHand.loadSurenessValues(); 	
		        	//refreshing Label structure -> necessary when new fields are created
		 	        fieldsLabelNames=projCnt.getProjectFieldsPair(projId);
		 	        refreshList=true;
		        	 		        	 
		        	break;

		             
		        default:
		             	
		   
		      }
		         

		     }
			
	
	
}
