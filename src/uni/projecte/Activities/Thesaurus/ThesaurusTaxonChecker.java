package uni.projecte.Activities.Thesaurus;

import uni.projecte.R;
import uni.projecte.Activities.Projects.ProjectManagement;
import uni.projecte.controler.CitationControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.CitationManager.ListAdapter.CitationListAdapter;
import uni.projecte.dataLayer.RemoteDBManager.objects.LocalTaxonNoThListAdapter;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusElement;
import uni.projecte.dataLayer.utils.TaxonUtils;
import uni.projecte.dataTypes.LocalTaxonSet;
import uni.projecte.dataTypes.TaxonElement;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class ThesaurusTaxonChecker extends Activity {
	
	public final static int EDIT_CITATION = 1;

	public static final String PREF_FILE_NAME = "PredProject";
	private SharedPreferences preferences;
	
	private CitationControler citCnt;
	private ProjectControler projCnt;
	private ThesaurusControler thCnt;
	
	private long projId;
	private String thName;
	
	private ListView lvNoThTaxonList;
	private TextView tvNoThTaxonInfo;
	
	private LocalTaxonSet localTaxonList;
	
	private EditText etGenusEt;
	private EditText etSpEpithet;
	private EditText etSpEpithetAuhtor;
	
	private EditText etInfraSpEpithet;
	private EditText etInfraSpEpithetAuthor;
	
	private EditText etPrimaryKey;
	private EditText etSecondayKey;
	
	private Spinner spRank;
	
	private Button addButton;
	
	private Dialog addItemDialog;
	private ProgressDialog pdCheckingTh;
	
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        Utilities.setLocale(this);
        setContentView(R.layout.thtaxonchecker);
        
        lvNoThTaxonList=(ListView) findViewById(R.id.lvLocalTaxonsNoTh);
        tvNoThTaxonInfo=(TextView) findViewById(R.id.tvNoThTaxons);
        
    	preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        projId = preferences.getLong("predProjectId", -1);

   	 	citCnt= new CitationControler(this);
   	 	projCnt= new ProjectControler(this);
   	 	thCnt= new ThesaurusControler(this);
   	 	
   	 	projCnt.loadProjectInfoById(projId);
   	 	
   	 	thName=projCnt.getThName();
   	 	
   	 	String tvInfo=String.format(getString(R.string.tvNoThTaxonsInfo),projCnt.getName());
   	 	tvNoThTaxonInfo.setText(Html.fromHtml(tvInfo));
   	 	
   	 	boolean status=thCnt.initThReader(thName);
   	 	
   	 	if(status){
   	 		
   	 		createAddItemDialog();
   	 		thCnt.closeThReader();
   	 		
   	 		loadThList();
   	 		
   	 	}
   	 	
   	 	else { 
   	 		
   	 		Utilities.showToast(getString(R.string.toastNotThesarus), this);
   	 		
   	 		finish();
   	 		
   	 	}
   	 	
    }
    
    private void createAddItemDialog() {

     	addItemDialog= new Dialog(this);
    	addItemDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	addItemDialog.setContentView(R.layout.thadditemdialog);

        etGenusEt=(EditText)addItemDialog.findViewById(R.id.etAddItemGender);
        etSpEpithet=(EditText)addItemDialog.findViewById(R.id.etAddItemSpecEp);
        etSpEpithetAuhtor=(EditText)addItemDialog.findViewById(R.id.etAddItemSpecEpAuth);
        
        etInfraSpEpithet=(EditText)addItemDialog.findViewById(R.id.etAddItemInfraSpecEp);
        etInfraSpEpithetAuthor=(EditText)addItemDialog.findViewById(R.id.etAddItemInfraSpecEpAuth);
        
        etPrimaryKey=(EditText)addItemDialog.findViewById(R.id.etAddItemPrimKey);
        etSecondayKey=(EditText)addItemDialog.findViewById(R.id.etAddItemSecKey);
        
        addButton = (Button)addItemDialog.findViewById(R.id.btAddThItem);
        addButton.setOnClickListener(addNewThListener);
        
        spRank=(Spinner)addItemDialog.findViewById(R.id.spThRank);
        
   	 	spRank.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> arg0,View arg1, int position, long arg3) {
		
				String spValue=spRank.getSelectedItem().toString();
				
				if(spValue.equals("")){
					
					etInfraSpEpithet.setEnabled(false);
					etInfraSpEpithet.setText("");
					etInfraSpEpithetAuthor.setEnabled(false);
					etInfraSpEpithetAuthor.setText("");
					
				}
				else{
					
					etInfraSpEpithet.setEnabled(true);
					etInfraSpEpithetAuthor.setEnabled(true);
				}
							
			}


			public void onNothingSelected(AdapterView<?> arg0) {
				
			}

		});
    	
	}
    
	private OnClickListener showAddThDialogListener = new OnClickListener(){
		
        public void onClick(View v){
        	
        	loadDialogData(v.getId());
        	
        	addItemDialog.show();
        	
        }
	
    };
    
    
	private OnClickListener addNewThListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
         
        	if(etGenusEt.length()>0 && etSpEpithet.length()>0){
        		
        		String newGenus=etGenusEt.getText().toString();
        		String newSpEpithet=etSpEpithet.getText().toString();
        		String newSpEpithetAuthor=etSpEpithetAuhtor.getText().toString();
        		String newInfraSpEpithet=etInfraSpEpithet.getText().toString();
        		String newInfraSpEpithetAuthor=etInfraSpEpithetAuthor.getText().toString();
        		String newPrimaryKey=etPrimaryKey.getText().toString();
        		String newSecondaryKey=etSecondayKey.getText().toString();
        		String thRank=spRank.getSelectedItem().toString();
        		
        		String taxonName="";
        		
        		if(newInfraSpEpithet.equals("")) taxonName=newGenus+" "+newSpEpithet+" "+newSpEpithetAuthor;
        		else taxonName=newGenus+" "+newSpEpithet+" "+thRank+" "+newInfraSpEpithet+" "+newInfraSpEpithetAuthor;
        		
    
        		if(!thCnt.checkTaxonBelongs(taxonName)){
        			
        			thCnt.addElement(newGenus, newSpEpithet, newInfraSpEpithet, newPrimaryKey, newSecondaryKey, newSpEpithetAuthor, newInfraSpEpithetAuthor,thRank);
        			
            		Utilities.showToast(getString(R.string.thNewItemAdded), v.getContext());
            		
            		addItemDialog.dismiss();
            		loadThList();
        			
        		}
        		
        		else{
        			
        			Utilities.showToast(getString(R.string.thNewItemExists),v.getContext());
        	
        		}
        		
        		
        	}
        	else{
        	
        		Utilities.showToast(getString(R.string.thNewItemGenusEpMissing), v.getContext());
        		
        	}
        	
        	
        }
    };


    
	private void loadDialogData(int elemPos) {

		String taxonName=localTaxonList.getTaxonList().get(elemPos).getTaxon();
		
		TaxonElement taxElem=TaxonUtils.mapThesaurusElement(taxonName);
		
		etGenusEt.setText(taxElem.getGenus());
        etSpEpithet.setText(taxElem.getSpecificEpithet());
        etSpEpithetAuhtor.setText(taxElem.getSpecificEpithetAuthor());
        
        Utilities.setDefaultSpinnerItem(spRank, taxElem.getInfraspecificRank(), getResources().getStringArray(R.array.thesaurusRank));
        
        etInfraSpEpithet.setText(taxElem.getInfraspecificEpithet());
        etInfraSpEpithetAuthor.setText(taxElem.getInfraspecificEpithetAuthor());
		
	}
    
	private void loadThList() {
		
        pdCheckingTh = new ProgressDialog(this);
	    pdCheckingTh.setMessage(getString(R.string.filterNotInThesaurusMessage));
	    
	    pdCheckingTh.show();

		                 Thread thread = new Thread(){
		  	        	   
			                 @Override
							public void run() {
			               	  
			                	 	localTaxonList=citCnt.getLocalWrongTaxon(projId,thName);
			        	    	 	thCheckHandler.sendEmptyMessage(0);
			        	    	 	
			                 }
			           };
			           
		   thread.start();
		
   	 	
	}
	
	 private Handler thCheckHandler = new Handler() {

	    	@Override
			public void handleMessage(Message msg) {
          
	    		LocalTaxonNoThListAdapter adapter=new LocalTaxonNoThListAdapter(getBaseContext(), localTaxonList.getTaxonList(), projId, showAddThDialogListener);
	    	 	lvNoThTaxonList.setAdapter(adapter);			    		
	    		pdCheckingTh.dismiss();

         }
	    	
	    	
	 };
	 
	    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	        	
	      switch(requestCode) {
	                 
	         case EDIT_CITATION :

	        	 loadThList();
	            
	             break;
	             
             
	        default:
	   
	      }
	         
	     }

}
