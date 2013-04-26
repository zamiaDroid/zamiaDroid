/*    	This file is part of ZamiaDroid.
*
*	ZamiaDroid is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*    	ZamiaDroid is distributed in the hope that it will be useful,
*    	but WITHOUT ANY WARRANTY; without even the implied warranty of
*    	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    	GNU General Public License for more details.
*
*    	You should have received a copy of the GNU General Public License
*    	along with ZamiaDroid.  If not, see <http://www.gnu.org/licenses/>.
*/


package uni.projecte.Activities.Citations;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import uni.projecte.R;
import uni.projecte.Activities.Maps.CitationMap;
import uni.projecte.Activities.RemoteDBs.TaxonRemoteTab;
import uni.projecte.controler.CitationControler;
import uni.projecte.controler.CitationSecondLevelControler;
import uni.projecte.controler.DataTypeControler;
import uni.projecte.controler.MultiPhotoControler;
import uni.projecte.controler.PolygonControler;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.ThesaurusManager.ListAdapters.ThesaurusAutoCompleteAdapter;
import uni.projecte.dataLayer.bd.ProjectDbAdapter;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.AttributeValue;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.maps.utils.LatLonParcel;
import uni.projecte.ui.multiphoto.MultiPhotoFieldForm;
import uni.projecte.ui.multiphoto.PhotoFieldForm;
import uni.projecte.ui.polygon.PolygonField;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.ub.bio.biogeolib.CoordConverter;
import edu.ub.bio.biogeolib.CoordinateLatLon;
import edu.ub.bio.biogeolib.CoordinateUTM;

public class CitationEditor extends Activity {

	
	   public final static int SUCCESS_RETURN_CODE = 1;
	   public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
	   public static final int CAPTURE_IMAGE_MULTI_PHOTO = 3;
	   public static final int POLYGON_EDIT = 4;

	   private static final int REMOVE_CITATION = Menu.FIRST;
	   private static final int SHOW_MAP =Menu.FIRST+1;
	   public static final int SHOW_TAXON_INFO=Menu.FIRST+2;
	
	   /* DataControlers */
	   private ProjectDbAdapter mDbAttributeType;
	   private ProjectControler projCont;
	   private ThesaurusControler tC;
	   private CitationControler sC;
	   private PreferencesControler prefCnt;

	   private ListView attListView;
	   
	   private long citationId=-1;
	   private int numAttr;
	   private long projId=-1;
	   
	   private String thName;
	   private String projType;
	   private String thType;
	   private String projName;
	   
	   private ArrayList<String> attList;
	   private TextView txtName;
	   private ArrayList<AttributeValue> attValuesList;
	   private ArrayList<ProjectField> fieldList;
   	   private Hashtable<String, PhotoFieldForm> photoFieldsList;
   	   private PolygonField polygonField;
	   
	   private TextView mDateDisplay;
	   private TextView mLocationDisplay;
	   private TextView tvCounter;
	   private LinearLayout llNavigation;
	   private ImageButton ibNavRight;
	   private ImageButton ibNavLeft;
	   
	   private EditText etPhotoValue;
	   private ImageView photoTV;
	   private AutoCompleteTextView thElem;
	   
	   private Button bModifyCitation;
	   
	   private ArrayList<String> formValues;
	   
	   private Hashtable<Integer, String> secLevFields;

	   	private String fileName="";
		private Uri imageUri;
    	private String photoPath;
		private String _path;
    	private String lastPhotoField;

		
		private ImageButton rmPhotoButton;
		private ImageButton photoButton;
    	private EditText etPhotoPath;
    	private TextView tvPhotoError;

	   private ArrayList<View> elementsList;
	   private int n;
	   private boolean thStatus;
		
	   private int numTotal;
	   private int position;
	   
	   private Bundle extras;

 
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Utilities.setLocale(this);

        setContentView(R.layout.citationeditor);
        
        /*DB access*/
        
        projCont=new ProjectControler(this);
        sC=new CitationControler(this);
        tC= new ThesaurusControler(this);
        prefCnt=new PreferencesControler(this);

        
        mDbAttributeType= new ProjectDbAdapter(this);
        
        
        bModifyCitation = (Button)findViewById(R.id.bModifySample);
        bModifyCitation.setOnClickListener(bModifyCitationListener);

        
        /* button Listeners*/ 
        mDateDisplay = (TextView) findViewById(R.id.citationDate);
        mLocationDisplay = (TextView) findViewById(R.id.tvLocation);
        tvCounter = (TextView) findViewById(R.id.tvNavigationCounter);
        llNavigation = (LinearLayout) findViewById(R.id.llNavigationBar);
        ibNavLeft = (ImageButton) findViewById(R.id.ibCitEditLeft);
        ibNavRight = (ImageButton) findViewById(R.id.ibCitEditRight);
        txtName = (TextView)findViewById(R.id.projectName);

         
        extras=getIntent().getExtras();     
              
        // we assign the adapter to the researchList
        projId=getIntent().getExtras().getLong("id");
        citationId=getIntent().getExtras().getLong("idSample");

        numTotal=getIntent().getExtras().getInt("count");
        position=getIntent().getExtras().getInt("position");
        

        
        if (projId!=-1){
        	
        	/*in case that we have chosen another research in the past*/
        	        	
        	projCont.loadProjectInfoById(projId);
        	
        	projName=projCont.getName();
        	
        	txtName.setText(projName);
        	
        	createForm(projId);
        	
        }
        
        updateDisplay();

    }

    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	

    	menu.add(0, REMOVE_CITATION, 0,R.string.mRemoveCitation).setIcon(android.R.drawable.ic_menu_delete);
    	menu.add(0, SHOW_MAP, 0,R.string.mShowUniqueCitation).setIcon(android.R.drawable.ic_menu_mapmode);
		menu.add(0, SHOW_TAXON_INFO, 0,R.string.mShowTaxonInfo).setIcon(android.R.drawable.ic_dialog_info);


    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
    		
        	if(changesDone()){
        	
        	 	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            	
            	builder.setMessage(R.string.backFromCitationEditor)
            	       .setCancelable(false)
            	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            
          
            	        	   finish();
            	   
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
        	else {
        		
        		finish();
        		
        	}
        		
	        return true;

        	
        }
        
        return false;

    }


	private boolean changesDone(){
    	
    	boolean modified=false;
    	int i=0;
    	
    	Iterator<View> elemIt=elementsList.iterator();
	
		while ( elemIt.hasNext() && !modified){
			
			String value="";
			View et=elemIt.next();
			
			
			if (et instanceof EditText){
			
				value= ((TextView) et).getText().toString();
				
			}
			
			else if(et instanceof CheckBox){
				
				if (((CheckBox) et).isChecked()) value="true";
				else value="false";
				
			}
			else if(et instanceof TextView){
				
				value =((TextView) et).getText().toString();
			}
			
			else if(et instanceof Spinner){
				
				if(((Spinner) et).getSelectedItem()==null) value="";
				else value =((Spinner) et).getSelectedItem().toString();
				
			}
			else{
				
				
				
			}

			
			if(!value.equals(formValues.get(i))) {
				
				modified=true;
				
				Log.i("BD2","Not equals: ->"+value+"<- != ->"+formValues.get(i)+"<-");								
			
			}
			
			i++;
			
		}
		
		
    	return modified;
    	    	
    }
    
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
    	
		switch (item.getItemId()) {
		case REMOVE_CITATION:
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	
	    	
	    	builder.setMessage(R.string.deleteCitationQuestion)
	    	       .setCancelable(false)
	    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    
	    	        	removeSample();
	    	        	    	        	   
	    	           }

					
	    	       })
	    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                
	    	        	   dialog.cancel();
	    	                
	    	           }
	    	       });
	    	
	    	AlertDialog alert = builder.create();
	    	
	    	alert.show();
			 			 
			break;
			
		case SHOW_MAP:
			
			
				showMap();

		
	        break;
	        
		case SHOW_TAXON_INFO:
			
			if (!emptyThesaurus()){
        	    
       	    	 Toast.makeText(getBaseContext(), 
       	                R.string.fieldMissing, 
       	                 Toast.LENGTH_SHORT).show();
   	    	
			}
			else{
				
			
									 
				 if(thStatus){
					 
					 String taxonName=thElem.getText().toString();
					 
					 if(tC.checkTaxonBelongs(taxonName)){
						 
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

			
			
			break;
	        
	        
		}
		return super.onOptionsItemSelected(item);
	}
    
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
    
    
	private void showMap() {
		
		Intent myIntent = new Intent(this, CitationMap.class);
    	myIntent.putExtra("idSample", citationId);
    	myIntent.putExtra("id", projCont.getProjectId());


        startActivityForResult(myIntent, 0);
		
		
	}	    
   
	
	private void removeSample() {
		
	 	   ProjectSecondLevelControler slPC=new ProjectSecondLevelControler(getBaseContext());
   		
    	   long[] slIds=slPC.getTwoLevelFieldListByProjId(projCont.getProjectId());
   			
   			    		
   			//remove secondary citations
   			if(slIds.length>0){
   				
   				CitationSecondLevelControler sL=new CitationSecondLevelControler(getBaseContext());
   				sL.deleteCitationFromProject(citationId,slIds);
     				
          
   			}
		
		
		   CitationControler sC=new CitationControler(this);
		   sC.deleteCitation(citationId);
		   
		   
		   
		   finish();

		
	}
    
    private boolean emptyThesaurus(){
    	
    	
    	boolean notEmpty=true;
    	
    	
    	
    	//are all the attributes filled?
    	
    	for (int i=0;i<n;i++){
    		
    		View vi=elementsList.get(i);
    		
    		if (vi instanceof EditText){
				
    		//	if (((EditText)vi).length()==0) notEmpty=false;	
			}
    		
    		else if((vi instanceof AutoCompleteTextView)){
    			
    			if (((AutoCompleteTextView)vi).length()==0) notEmpty=false;	

    		}
			
    		
    		else if((vi instanceof CheckBox)){
    			
    			
    			//rubish
    		}
			
			else {
				
				//if (((Spinner)vi).getSelectedItem().toString().length()==0) notEmpty=false;	
				
			}
    		
    	   		
    		
    	}
    	
    	return notEmpty;
    	
    }
    
    private OnClickListener bModifyCitationListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
            
        	if (!emptyThesaurus()){
        	    	
        	    	 Toast.makeText(getBaseContext(), 
        	                R.string.fieldMissing, 
        	                 Toast.LENGTH_SHORT).show();
        	    	
        	    }
        	  
        	  else{
        		 
        			if(txtName.length()==0){
        	    		
        	    		Toast.makeText(getBaseContext(), 
        	                    R.string.projNameMissing, 
        	                    Toast.LENGTH_SHORT).show();
        	    	}
        	    	
        	    	else  {
        	    		
        	    		updateFieldValues(citationId, sC);
        	    		
        	    		addCitationSubFields(citationId);
        	    		
        	    	      String toastText=v.getContext().getString(R.string.tModifiedCitation);
	    	                 
	    	                 Toast.makeText(getBaseContext(), 
	    	        	                toastText, 
	    	        	                 Toast.LENGTH_SHORT).show();
        	    		
        	    		finish();
        	    			
        	    		}
        	    		
        	    		
        	          
        	    		        	    	
        	    	}

        	  }
        	
    };
    
    private void addCitationSubFields(long parentId) {
    	
    	//Adding MultiPhoto: photoList
    	
    	MultiPhotoControler multiProjCnt= new MultiPhotoControler(this);
    	   	   	
    	Iterator<String> photoFields=photoFieldsList.keySet().iterator();
		
		while(photoFields.hasNext()){
			
			PhotoFieldForm tmpField=photoFieldsList.get(photoFields.next());
							
			if(tmpField instanceof MultiPhotoFieldForm){
				
				long subFieldId=multiProjCnt.getMultiPhotoSubFieldId(((MultiPhotoFieldForm) tmpField).getFieldId());
				
				multiProjCnt.addPhotosList((MultiPhotoFieldForm) tmpField,subFieldId,projId,parentId);				
				
			}						
		}
		
		//Updating Polygon
		if(polygonField!=null){
		
			PolygonControler polygonCnt= new PolygonControler(this);
	
			polygonCnt.updatePolygonList(polygonField,projId,citationId);
	
		}

	}

    
    private void updateFieldValues(long idSample, CitationControler smplCntr){
    	
		
		n=elementsList.size();
		
		smplCntr.startTransaction();
		
		
		for (int i=0;i<n;i++){
			
			String value="";
			
			View et=elementsList.get(i);
							
			if (et instanceof EditText){
				value= ((TextView) et).getText().toString();
			}
			
			else if(et instanceof CheckBox){
				
				if (((CheckBox) et).isChecked()) value="true";
				else value="false";
				
			}
			else if(et instanceof TextView){
				
				value =((TextView) et).getText().toString();
			}
			else if(et instanceof HorizontalScrollView){
								
				MultiPhotoFieldForm multiPhoto=(MultiPhotoFieldForm) photoFieldsList.get(et.getTag());
				value=multiPhoto.getSecondLevelId();
				
			}
			else if(et instanceof Spinner){
				
				if(((Spinner) et).getSelectedItem()==null) value="";
				else value =((Spinner) et).getSelectedItem().toString();

				
			}
			else if(et instanceof ListView){
				
				value=polygonField.getSecondLevelId();
				
			}
			else{
				
				
				
			}
			
			int id= et.getId();

			
			if(value.equals(formValues.get(i))){
				
				
			}
			else smplCntr.updateCitationField(idSample, id, value,fieldList.get(i).getName());
			
			

		}
		
		smplCntr.EndTransaction();



	
}
    
    
 
    private void updateDisplay() {
    	
    	sC.loadCitation(citationId);

	     
	     SharedPreferences settings = getSharedPreferences("uni.projecte_preferences", 0);
	     String coorSystem = settings.getString("listPrefCoord", "UTM");
	     
    	 
	    	Double lat=sC.getLatitude();
	    	Double longitude=sC.getLongitude();
	     
	    	
	    //no location 	
	 	if(lat>90 || longitude>180){
    		
	    	mLocationDisplay.setText(R.string.noGPS);

    	}
	 	else{

		     if(coorSystem.equals("UTM")){
		    	 
		         CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(lat,longitude));
			     mLocationDisplay.setText(utm.getShortForm().replace("_", " "));
	
		     }
		     else {
	
			    mLocationDisplay.setText(lat.toString().subSequence(0, 7)+" - "+longitude.toString().subSequence(0, 7));
	
		     }
		     
	 	}
	     
        mDateDisplay.setText(sC.getDate());
        
        handleNavigation();        
        
        
    }

        
    
    private void handleNavigation() {

    	if(numTotal>0){ 
    		
    		if(position == 0) ibNavRight.setVisibility(View.GONE);
    		else if(position+1 == numTotal) ibNavLeft.setVisibility(View.GONE);
    		
    		tvCounter.setText(" "+(numTotal-position)+"/"+numTotal+" ");
    		
    		final long nextCitation=sC.getNextCitationId(citationId);
    		final long previousCitation=sC.getPreviousCitationId(citationId);
    		
    		if(nextCitation>-1){
    		
	    		ibNavRight.setOnClickListener(new OnClickListener() {
					
					public void onClick(final View v) {
						
						
						if(changesDone()){
				        	
			        	 	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
			            	
			            	builder.setMessage(R.string.backFromCitationEditor)
			            	       .setCancelable(false)
			            	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			            	           public void onClick(DialogInterface dialog, int id) {
			            
			          
			            	        		Intent intent = new Intent(v.getContext(), CitationEditor.class);
			            	        		
			        						extras.putLong("idSample", nextCitation);
			        						extras.putInt("position", position-1);
			        						
			        						intent.putExtras(extras);
			        						
			        						startActivityForResult(intent, 2);
			        						
			        						finish();
			            	   
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
							
							Intent intent = new Intent(v.getContext(), CitationEditor.class);
        	        		
    						extras.putLong("idSample", nextCitation);
    						extras.putInt("position", position-1);
    						
    						intent.putExtras(extras);
    						
    						startActivityForResult(intent, 2);
    						
    						finish();
							
						}
						
						
					
					}
				});
    		
    		}
    		
    		if(previousCitation>-1){
        		
	    		ibNavLeft.setOnClickListener(new OnClickListener() {
					
					public void onClick(final View v) {
						
						if(changesDone()){
				        	
			        	 	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
			            	
			            	builder.setMessage(R.string.backFromCitationEditor)
			            	       .setCancelable(false)
			            	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			            	           public void onClick(DialogInterface dialog, int id) {
			            
			          
			            	        	Intent intent = new Intent(v.getContext(), CitationEditor.class);
			            	        		
			       							extras.putLong("idSample",previousCitation);
			       							extras.putInt("position", position+1);
			       						
			       						intent.putExtras(extras);
			       						
			       						startActivityForResult(intent, 2);
			       						finish();
			            	   
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
							
						  	Intent intent = new Intent(v.getContext(), CitationEditor.class);
        	        		
       							extras.putLong("idSample",previousCitation);
       							extras.putInt("position", position+1);
       						
       						intent.putExtras(extras);
       						
       						startActivityForResult(intent, 2);
       						finish();
							
						}
						
				        
						
					}
				});
    		
    		}
    		
    	
    	}
    	else llNavigation.setVisibility(View.GONE);
	}


	private void createForm(long id){
		   
		   elementsList=new ArrayList<View>();
		   formValues=new ArrayList<String>();

		   secLevFields = new Hashtable<Integer, String>();
		   photoFieldsList = new Hashtable<String, PhotoFieldForm>();


		   LinearLayout l= (LinearLayout)findViewById(R.id.atributsS);
		   LinearLayout lPhoto=null;

		   CitationSecondLevelControler citSLCnt=new CitationSecondLevelControler(this);
		   
		   
		   ProjectControler rsC=new ProjectControler(this);
		   rsC.loadProjectInfoById(id);
		   sC.startTransaction();
		   
		   thName=rsC.getThName();
		   projType=rsC.getProjType();
		   
		   thStatus=tC.initThReader(rsC.getThName());
		   
			Cursor thInfo=tC.getThInfo(thName);
		  	thInfo.moveToFirst();
		  	
		  	if(thInfo.getCount()>0){
		  		
		  	   thType=Utilities.translateThTypeToCurrentLanguage(this, thInfo.getString(4));	        
		  		
		  	}
		   

		   
		   DataTypeControler dtH=new DataTypeControler(this);
		   
		   fieldList=rsC.getAllProjectFields(id);		   
		   Iterator<ProjectField> it=fieldList.iterator();
		   
		   LinearLayout llsL = null;
		   
		   int i=0;
		   
		  while(it.hasNext()){
			  
			   LinearLayout llField=new LinearLayout(this);
			   
			   TextView t=new TextView(getBaseContext());
			   
			   ProjectField att=it.next();
			   
			   String fieldType =att.getType();
			   String titol= att.getLabel();
			   t.setText(titol);

			   //t.setWidth(LayoutParams.WRAP_CONTENT);
			   
			   llField.addView(t);
			   llField.setPadding(4, 4, 4, 4);

			   //lp.setBackgroundColor(Color.argb(120, 120, 120, 120));
		   
			   //simple types
			   if (fieldType.equals("simple") || fieldType.equals("number")){
				   
				   EditText e=new EditText(getBaseContext());	   
				   
				   int idD= (int) att.getId();
				   e.setId(idD);
				   
				   String pred=sC.getFieldValue(citationId,att.getId());
				   
				   if(pred!=null && pred.length()>0) {
					   e.setText(pred);
					   formValues.add(pred);
				   }
				   else{
					   
					   formValues.add("");

				   }
				   
				   e.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				   e.setLayoutParams(new LayoutParams
					        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
					                WRAP_CONTENT));
				   
				  llField.addView(e);
				   elementsList.add(e);

				   
			   }
			   
			   else if (fieldType.equals("photo")){
				   
				   EditText tvFieldValue=new EditText(getBaseContext());
				  
				   photoTV= new ImageView(getBaseContext());
				   rmPhotoButton=new ImageButton(getBaseContext());
				   photoButton=new ImageButton(getBaseContext());
				   
				   
				   int idD= (int) att.getId();
				   
				   tvFieldValue.setId(idD);
				   tvFieldValue.setEnabled(false);
				   tvFieldValue.setTag(i);
				   
				   etPhotoValue=tvFieldValue;
				   
				   String pred=sC.getFieldValue(citationId,att.getId());
				   
				   etPhotoPath=tvFieldValue;
				   tvFieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		 	       lPhoto=new LinearLayout(this);
 				   lPhoto.setOrientation(LinearLayout.VERTICAL);
				   lPhoto.setGravity(Gravity.CENTER);

 				   
				   photoButton.setBackgroundResource(android.R.drawable.ic_menu_camera);
				   photoButton.setOnClickListener(new OnClickListener() {

		                public void onClick(View v) {
		                  
		             	   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
		                	
		                	
		                	String currentTime = formatter.format(new Date());
		                	String rsName=projCont.getName().replace(" ", "_");
		                	
		                	prefCnt.setLastPhotoPath(rsName + currentTime + ".jpg");
		                	fileName = rsName + currentTime + ".jpg";
		                	
		                	//create parameters for Intent with filename
		                	ContentValues values = new ContentValues();
		                	values.put(MediaStore.Images.Media.TITLE, fileName);
		                	values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
		                	
		                	//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		                	_path=Environment.getExternalStorageDirectory() + "/zamiaDroid/Photos/"+fileName;
		                	
		                  	File file = new File( _path );
	                	    imageUri = Uri.fromFile( file );
		                	
		                	//create new Intent
		                	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		 
	                	    intent.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );		                	
		                	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		                	startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

		                }
		        });
				   
				   
				   rmPhotoButton.setBackgroundResource(android.R.drawable.ic_input_delete);
				   rmPhotoButton.setTag(pred);

				   rmPhotoButton.setOnClickListener(new OnClickListener() {

		                public void onClick(View v) {
		                  
		                	String photoPath=v.getTag().toString();
		                	etPhotoValue.setText("");
		   		         
		                	PhotoUtils.removePhoto(photoPath);
		                	  
		                	photoTV.setVisibility(View.GONE);
		                	photoButton.setVisibility(View.VISIBLE);
		                	rmPhotoButton.setVisibility(View.GONE);
		                	
		                	etPhotoValue.setVisibility(View.GONE);
		                	
		                	int idField= etPhotoValue.getId();

		                	updateFieldValue(citationId,idField,"",(Integer)etPhotoValue.getTag());
		                	
		                	if(tvPhotoError!=null) tvPhotoError.setVisibility(View.GONE);
		                	
		                }
				   });
				   
				   
				   
					  photoTV.setTag(pred);
					  photoTV.setOnClickListener(new OnClickListener() {

			                public void onClick(View v) {
			                  		                	
			                	Intent viewPhIntent = new Intent(v.getContext(), uni.projecte.Activities.Miscelaneous.ImageView.class);
							       
					 			Bundle b = new Bundle();
					 			b.putString("photoPath", v.getTag().toString());
					 			viewPhIntent.putExtras(b);
					 			
					 			b = new Bundle();
					 			b.putLong("projId", projId);
					 			viewPhIntent.putExtras(b);
					 							 			
					 			startActivity(viewPhIntent);  
			                	
			                }
					   });
					  
					  
					    LinearLayout lButtons=new LinearLayout(this);
					    lButtons.setGravity(Gravity.RIGHT);
			 				
			 				LayoutParams param = new LinearLayout.LayoutParams(
		                            LayoutParams.FILL_PARENT,
		                            LayoutParams.WRAP_CONTENT, 0.0f);
			 				
			 			lButtons.setLayoutParams(param);
		 				
		 				lButtons.addView(rmPhotoButton);
		 				lButtons.addView(photoButton);
		 				
		 				param = new LinearLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT, 1.0f);
			      	       
						tvFieldValue.setLayoutParams(param);
						   
						lPhoto=new LinearLayout(this);
						lPhoto.setOrientation(LinearLayout.VERTICAL);
						lPhoto.setGravity(Gravity.CENTER);	   
							   
							   
						lPhoto.addView(tvFieldValue);
		        	    llField.addView(lButtons);

				   
				   if(pred!=null && pred.length()>0) {

					   
					   tvFieldValue.setText(pred);
					   lPhoto.addView(photoTV);

						
					   LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					   llp.setMargins(8, 0, 0, 0);
					   tvFieldValue.setLayoutParams(llp);
					   
					   formValues.add(pred);
					   

					   BitmapFactory.Options options = new BitmapFactory.Options();
	        		   options.inSampleSize = 5;
	        		   Bitmap PhotoFromCamera = BitmapFactory.decodeFile(pred, options);
	        		    
	        				 				
		 				//has photo and can be found
	        		    if(PhotoFromCamera!=null){
	        		    
		        			photoTV.setImageBitmap(PhotoFromCamera);
		        			photoTV.setScaleType(ScaleType.CENTER_CROP);
		        	
							photoButton.setVisibility(View.GONE);


	        		    }
	        		    //has photo but can't be found on sdCard
	        		    else {
	        		    	
	        		    	tvPhotoError=new TextView(this);
	        		    	tvPhotoError.setText(getString(R.string.photoCantBeFound));
	        		    	tvPhotoError.setTextColor(Color.RED);
	        		    	
	        		 	   	llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        		 	   	llp.setMargins(8, 10, 5, 0);
						   	tvPhotoError.setLayoutParams(llp);
						   
						   	photoTV.setVisibility(View.GONE);
						   	tvPhotoError.setGravity(Gravity.RIGHT);
	        		    	lPhoto.addView(tvPhotoError);
	        		    	
	        		    	photoButton.setVisibility(View.GONE);
	        		    	
	        		    }
	       					   
				   }
				   //has no linked photo
				   else{
					   
					tvFieldValue.setVisibility(View.GONE);
					lPhoto.addView(photoTV);
					   
					rmPhotoButton.setVisibility(View.GONE);

					formValues.add("");
					

				   }
				   
				 
				   elementsList.add(tvFieldValue);

				   
			   }
			   else if(fieldType.equals("multiPhoto")){
				   
				   String pred=sC.getFieldValue(citationId,att.getId());			   
				   String photos=citSLCnt.getMultiPhotosValues(pred);				   
				   
				   MultiPhotoFieldForm multiPhotoFieldForm = new MultiPhotoFieldForm(this, projId, att, llField,MultiPhotoFieldForm.EDIT_MODE);
				   
				   multiPhotoFieldForm.setCitationData(Utilities.splitToArrayList(photos),pred);
				   
				   multiPhotoFieldForm.setAddPhotoEvent(takePicture);

				   photoFieldsList.put(att.getName(), multiPhotoFieldForm);

				   elementsList.add(multiPhotoFieldForm.getImageScroll());
				   
				   formValues.add(pred);

				   
			   }
			   else if(fieldType.equals("polygon")){
				   
				   String pred=sC.getFieldValue(citationId,att.getId());			   
				   
				   polygonField = new PolygonField(this, id, att, llField, PolygonField.EDIT_MODE);
				   polygonField.loadPoints(pred);
			   
				   elementsList.add(new ListView(this));
				   
				   formValues.add(pred);

				   
			   }
			   else if(fieldType.equals("secondLevel")){
				   
				   Button showList=new Button(getBaseContext());
				   showList.setText("Veure Llistat");
			   
				   TextView e=new TextView(getBaseContext());	   
				   
				   int idD= (int) att.getId();
				   e.setId(idD);
				   
				   String pred=sC.getFieldValue(citationId,att.getId());
				   
				   if(pred!=null && pred.length()>0) {
					   
					   e.setText(Html.fromHtml("<b>"+pred+"</b>"));
					   formValues.add(pred);
					   
				   }
				   else{
					   
					   formValues.add("");
					   pred="";

				   }
				   
				   e.setPadding(3,3,3,10);

				   
				   showList.setId(idD);
				   secLevFields.put(idD, pred);
				   
				   showList.setOnClickListener(new OnClickListener() {

		                public void onClick(View v) {
		                 
		                Intent myIntent = new Intent(v.getContext(), SecondLevelList.class);
		            				            		
		    	        	myIntent.putExtra("id", projCont.getProjectId());
		    	        	
		    	        	myIntent.putExtra("subProjId", (long)v.getId());
		    	        	
		    	        	String subLevelTag=secLevFields.get(v.getId());

		    	        	myIntent.putExtra("subLevelTag", subLevelTag);
		    	
		    	            startActivityForResult(myIntent, 0);

		                }
				   });
				   
				   
				   e.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				   e.setLayoutParams(new LayoutParams
					        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
					                WRAP_CONTENT));
				   
				  llField.addView(e);
				   
				  llsL= new LinearLayout(this);
				   llsL.setOrientation(LinearLayout.HORIZONTAL);
				   
				   llsL.addView(showList);
				  
				  // l.addView(ll);
				  
				   elementsList.add(e);
				   
				   
			   }
			   
			   
			   else if (fieldType.equals("boolean")){
				   
				   CheckBox e=new CheckBox(getBaseContext());	   
				   
				   int idD= (int) att.getId();
				   e.setId(idD);
				   e.setLayoutParams(new LayoutParams
					        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
					                WRAP_CONTENT));
				   
				   String pred=sC.getFieldValue(citationId,att.getId());
				   if(pred.equals("true")) e.setChecked(true);
				   
				   formValues.add(pred);
				   
				   
				  llField.addView(e);
				   elementsList.add(e);
			   }
			   
			   else if(fieldType.equals("thesaurus")){
				   
				   View e;
				   
				   if(thStatus){
					   
	
					   e=new AutoCompleteTextView(getBaseContext());
					   e.setLayoutParams(new LayoutParams
						        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
						                WRAP_CONTENT));
					   
					   ThesaurusAutoCompleteAdapter thItems = tC.fillData((AutoCompleteTextView) e);

					    ((AutoCompleteTextView) e).setAdapter(thItems);
					    
					    thElem=((AutoCompleteTextView) e);

				   
				   }
				   
				   else{
					   
					   e=new EditText(getBaseContext());
					   
				   }
				   
				   int idD= (int) att.getId();
				   e.setId(idD);
				  
				   ((TextView) e).setImeOptions(EditorInfo.IME_ACTION_NEXT);
				   
				   e.setLayoutParams(new LayoutParams
						        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
						                WRAP_CONTENT));

				   ((TextView) e).setHint(R.string.taxonHint);
				      
				   String pred=sC.getFieldValue(citationId,att.getId());
					   
					  if(pred.length()>0){ 
						  
						  						  
						  ((TextView) e).setText(pred);
						  formValues.add(pred);
			   		}
			   			else{
				   
			   			formValues.add("");

			   		}

					  
					  LinearLayout llCont=new LinearLayout(this);
					  llCont.setOrientation(LinearLayout.HORIZONTAL);
					  llCont.setGravity(Gravity.CENTER_VERTICAL);
					  
					  
					  LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	                           LayoutParams.FILL_PARENT,
	                           LayoutParams.WRAP_CONTENT);
					  
					  llCont.setLayoutParams(param);
					  
						  LinearLayout llAuto=new LinearLayout(this);
						  llAuto.setOrientation(LinearLayout.VERTICAL);
						 
						  	param = new LinearLayout.LayoutParams(
		                           LayoutParams.FILL_PARENT,
		                           LayoutParams.WRAP_CONTENT, 1.0f);
						  
						  llAuto.setLayoutParams(param);
						  llAuto.addView(e);
					  

						  ImageButton ibClear=new ImageButton(getBaseContext());
						  ibClear.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
						  
					   
						  LinearLayout llActions=new LinearLayout(this);
						  llActions.setGravity(Gravity.RIGHT);
							 							   
						   param = new LinearLayout.LayoutParams(
		                           LayoutParams.WRAP_CONTENT,
		                           LayoutParams.WRAP_CONTENT, 0.0f);
		      	       
						   llActions.setLayoutParams(param);
						   llActions.addView(ibClear);
						   
						   ibClear.setId(i);
						   ibClear.setOnClickListener(new OnClickListener() {

				                public void onClick(View v) {
				                 
				                	int numElm=v.getId();
				                	((TextView)elementsList.get(numElm)).setText("");

				                }
						   });
						   
					   
						   
					   llCont.addView(llAuto);
					   llCont.addView(llActions);

					   
					   llField.addView(llCont);

					   elementsList.add(e);
			   }
			   
			 else{
				   
					
			     List<CharSequence> values = dtH.getItemsArrayListbyFieldId(att.getId());
			     
				// String[] items=dtH.getItemsbyFieldId(att.getId());
				 
				 Spinner e=new Spinner(this);
				 e.setPrompt(getString(R.string.chooseItem));
				 
				   e.setLayoutParams(new LayoutParams
					        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
					                WRAP_CONTENT));

				   ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
				                    android.R.layout.simple_spinner_item,values);
				   
			        adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, values);  
				
				   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				   e.setAdapter(adapter);

				   String pred=sC.getFieldValue(citationId,att.getId());
						   
					if(pred!=null && pred.length()>0) {
						
						setDefaultSpinnerItem(e,pred,values);
						formValues.add(pred);
			   		}
			   		else{
			   			
			   			CharSequence textHolder = "";  
			   	        adapter.add(textHolder);  
			   	        e.setSelection(e.getCount()-1);
			   	        
			   			formValues.add("");

			   		}
				
						  
						   int idD= (int) att.getId();
						   e.setId(idD);
						   
						  llField.addView(e);
						  elementsList.add(e);

			   }
			   
			   if(fieldType.equals("secondLevel")){
				   
				   LinearLayout ll= new LinearLayout(this);
				   ll.setOrientation(LinearLayout.VERTICAL);
				   ll.setPadding(3,3,3,3);

				   ll.addView(llField);
				   ll.addView(llsL);
				   
				   ll.setBackgroundColor(Color.parseColor("#ff333333"));
				   
				   l.addView(ll);
				   
			   }
			   else if (fieldType.equals("photo")){ 
				   
				   
				   LinearLayout ll= new LinearLayout(this);
				   ll.setOrientation(LinearLayout.VERTICAL);
				   ll.setPadding(3,3,3,3);
				   
				   ll.addView(llField);
				   ll.addView(lPhoto);
				   ll.setBackgroundColor(Color.parseColor("#ff333333"));
				  
				   l.addView(ll);

				   
			   }
			   else{
			   
		   
				   l.addView(llField,i);
				   i++;
			   
			   }
			   
			   
			 
				
		   }
		   
		  n=i;
		  sC.EndTransaction();
		  
			if(n>=1) elementsList.get(1).requestFocus();


		   
	   }
	
	
    /*private OnClickListener removePicture = new OnClickListener() {

        public void onClick(View v) {

        	PhotoFieldForm photoField=photoFieldsList.get(v.getTag());
        	photoField.removePhoto();
 
        	PhotoUtils.removePhoto(Environment.getExternalStorageDirectory()+"/"+prefCnt.getDefaultPath()+"/Photos/"+fileName);
        	  
        	        	
        }
    };*/
    
	
    private OnClickListener takePicture = new OnClickListener() {

        public void onClick(View v) {
          
     	   	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
 	       	String currentTime = formatter.format(new Date());

 	       	projName=projName.replace(" ", "_");
 	       	fileName = projName + currentTime + ".jpg";

 	       	prefCnt.setLastPhotoPath(fileName);
 	       	
 	       	lastPhotoField=(String) v.getTag();		
 	       	 	       	
 	       	//create parameters for Intent with filename
 	       	ContentValues values = new ContentValues();
 	       	values.put(MediaStore.Images.Media.TITLE, fileName);
 	       	
 	       	String imageDesc=String.format(getString(R.string.photoDescription),projName);
 	       	
 	       	values.put(MediaStore.Images.Media.DESCRIPTION,imageDesc);
 	       	
 	       	//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
 	       	photoPath=Environment.getExternalStorageDirectory() + "/zamiaDroid/Photos/"+fileName;
 	       	 	       	
         	File file = new File(photoPath);
 	   	    imageUri = Uri.fromFile( file );
 	       	
 	       	//create new Intent
 	       	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
 	
 	   	    intent.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );		                	
 	       	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
 	       	startActivityForResult(intent, CAPTURE_IMAGE_MULTI_PHOTO);
 	

        }
        
    };
    	
    protected void updateFieldValue(long citationId, int idField, String value, int i) {

    	sC.startTransaction();
    	
    		sC.updateCitationField(citationId, idField, value,fieldList.get(i).getName());
    	
    	sC.EndTransaction();
    	
    	formValues.set(i, value);
    	
	}

	
	private void setDefaultSpinnerItem(Spinner e, String defaultValue, List<CharSequence> items){
	    
		Iterator<CharSequence> it= items.iterator();
		
    	boolean trobat=false;
    	int pos=-1;
    	int i=0;
    	
    	while ( it.hasNext()  && !trobat ){
    		
    		if (it.next().toString().compareTo(defaultValue)==0){ trobat=true; pos=i;}
    		i++;
    	}
    	
        	
    	if(trobat) e.setSelection(pos);    	
    	
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

      	
        	
        switch(requestCode) {
        case 0 :
            
        	//back from research choice
        	
        /*	nomEstudi = extras.getString(KEY_NOM);
            idRs=extras.getLong(KEY_ID);
            desc=extras.getString(DESCRIPCIO);

            txtName.setText(nomEstudi);*/
            
            break;
            
            
        case 1 :
        	
        	//back from filled attributes
        	
        	 if(intent!=null){
        	
        	 Bundle extras = intent.getExtras();
        	
        	
            numAttr= extras.getInt("nAttributes");
            
           
           
	            mDbAttributeType.open();
	
	            
	            attValuesList=new ArrayList<AttributeValue>();
	            
	            //we get value-attName pairs from Attribute Activity
	
	            for (int i=0; i<numAttr; i++){
	        	
	            	int id= extras.getInt("Id"+i);
	            	Cursor c=mDbAttributeType.fetchField(id);
	            	c.moveToFirst();
	            	
	            	attList.add(c.getString(2)+" : "+extras.getString("attId"+i));    	
	            	attValuesList.add(new AttributeValue((int) extras.getDouble("Id"+i),extras.getString("attId"+i)));
	            	
	            	
	            }
	            
	            //fill the list
	        	
	        	attListView.setAdapter(new ArrayAdapter<String>(this, R.layout.atrib_row, attList));
	        
	            mDbAttributeType.close();
	            
            }
           
            break;
        
        case CAPTURE_IMAGE_MULTI_PHOTO :	

        	if (resultCode == RESULT_OK) {
        		
    			if(photoPath==null){

    				String fileName=prefCnt.getLastPhotoPath();

    				photoPath=Environment.getExternalStorageDirectory().toString();
    				photoPath=photoPath + "/zamiaDroid/Photos/"+ fileName;

    			}
    			
    			PhotoFieldForm photoFieldForm=photoFieldsList.get(lastPhotoField);
    			((MultiPhotoFieldForm) photoFieldForm).addNewPhoto(photoPath);
    			    			
    			break;


    		} else if (resultCode == RESULT_CANCELED) {
    			Utilities.showToast("Picture was not taken", this);
    		} else {
    			Utilities.showToast("Picture was not taken", this);
    		}

        	
        	break;
            
        case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE :
        	
       	 if (resultCode == RESULT_OK) {
       		   
       		// thumbnail = (Bitmap) intent.getExtras().get("data");  
       		 
       		 if(_path==null){
       			         			
       			 String fileName=prefCnt.getLastPhotoPath();
       			 
       			 _path=Environment.getExternalStorageDirectory().toString();
       			 _path=_path + "/zamiaDroid/Photos/"+ fileName;
       			 
	               Utilities.showToast("Path: "+fileName, getBaseContext());

       			 
       		 }
       		 
       		 etPhotoPath.setText(_path);
       		 
       		 photoTV.setTag(_path);
       		 photoTV.setVisibility(View.VISIBLE);
       		 
       		 LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
   		 	 llp.setMargins(5, 5, 5, 5);
				 photoTV.setLayoutParams(llp);
       		 
       		 etPhotoPath.setVisibility(View.VISIBLE);
       		 
       		  BitmapFactory.Options options = new BitmapFactory.Options();
       		  options.inSampleSize = 5;

       		  Bitmap PhotoFromCamera = BitmapFactory.decodeFile(_path, options );

       		  MediaStore.Images.Media.insertImage(getContentResolver(), PhotoFromCamera, "fileName", "");

       		  //rmPhotoButton.setEnabled(true);
       		  rmPhotoButton.setVisibility(View.VISIBLE);
       		  rmPhotoButton.setTag(_path);
       		  
       		  photoButton.setVisibility(View.GONE);
       		  
   				
       		  //photoButton.setEnabled(false);
   				
       		  photoTV.setImageBitmap(PhotoFromCamera);
       	      photoTV.setScaleType(ScaleType.CENTER_CROP);
       	      photoTV.setVisibility(View.VISIBLE);
       	        
       	      photoTV.invalidate();
       			
       			
       			break;

       			

       	    } else if (resultCode == RESULT_CANCELED) {
       	    	
       	    	Utilities.showToast("Picture was not taken", this);
       	    	
       	    } else {
       	    	Utilities.showToast("Picture was not taken", this);
       	    }

       	
       	break;
       
    		case Sampling.POLYGON_EDIT :
    		
	    		if(resultCode != RESULT_CANCELED){
	    			    			
	            	ArrayList<LatLonParcel> pointsExtra = intent.getParcelableArrayListExtra("polygon_path");
	            	
	            	boolean modifiedPolygon=intent.getBooleanExtra("polygon_modified", true);
	            	
	            	
	            	if(polygonField!=null && modifiedPolygon) {
	            		
	            		polygonField.updatePath(pointsExtra);
	            		polygonField.setModified(true);
	            		
	            	}
	        	
	    		}
        		
    		break;
     
        }

        
    }
    
}

    

    