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

package uni.projecte.Activities.Thesaurus;

import uni.projecte.R;
import uni.projecte.Activities.Projects.ProjectManagement;
import uni.projecte.Activities.RemoteDBs.TaxonExplorer;
import uni.projecte.Activities.RemoteDBs.TaxonRemoteTab;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.RemoteDBManager.BiocatDBManager;
import uni.projecte.dataLayer.ThesaurusManager.ListAdapters.ThesaurusAutoCompleteAdapter;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ThesaurusInfo extends Activity{
	

		private ThesaurusControler thCnt;
		private ProjectControler projCnt;
		
		private String name;
		private AutoCompleteTextView autoThItems;
		
		private boolean thInit=false;

		
		private ImageButton rmButton;
		
		private Button btNewGender;
		private Button btNewSpEpithet;
		private Button btNewInfraSpEpithet;
		private Button btUnlinkTh;
		
		private String icodeValue;
		private ImageButton thInfoButton;
		private ImageButton thRmItemButton;
		private String filumLetter;
		
		private TableLayout tlRegisterFields;
		private LinearLayout llAddThItem;
		private LinearLayout llUnlinkTh;
		
		private String gender;
		private String spEpithet;
		private String spEpithetAuhtor;
		
		private String infraSpEpithet;
		private String infraSpEpithetAuthor;

		private EditText etGenusEt;
		private EditText etSpEpithet;
		private EditText etSpEpithetAuhtor;
		
		private EditText etInfraSpEpithet;
		private EditText etInfraSpEpithetAuthor;
		
		private EditText etPrimaryKey;
		private EditText etSecondayKey;
		
		private Spinner spRank;
		
		private Button addButton;

		private TextView tvThNumItems;
		
		private long thId;
		private int thCount;
		
		private long thItemId;
		
		private Dialog addItemDialog;

		
 
	    @Override
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	     
	        Utilities.setLocale(this);

	        setContentView(R.layout.thesaurus_info);
	        
	        thCnt=new ThesaurusControler(this);
	        projCnt=new ProjectControler(this);

	        rmButton = (ImageButton)findViewById(R.id.removeThButton);
	        rmButton.setBackgroundResource(android.R.drawable.ic_menu_delete);
	        rmButton.setOnClickListener(bRemoveThListener);
	        

	        btNewGender=(Button)findViewById(R.id.btNewGenus);
	        btNewGender.setOnClickListener(newGenusThListener);
			
	        btNewSpEpithet=(Button)findViewById(R.id.btNewSpecEpithet);
			btNewSpEpithet.setOnClickListener(newSpEpithetThListener);
			
	        btUnlinkTh=(Button)findViewById(R.id.btUnlinkTh);
	        btUnlinkTh.setOnClickListener(unlinkThListener);
			
			btNewInfraSpEpithet=(Button)findViewById(R.id.btNewIfraSpecEpithet);
			btNewInfraSpEpithet.setOnClickListener(newInfraSpEpithetThListener);
	        
	        thInfoButton = (ImageButton)findViewById(R.id.thInfoButton);
	        thInfoButton.setBackgroundResource(android.R.drawable.ic_menu_info_details);
	        thInfoButton.setOnClickListener(bThInfoListener);
	        thInfoButton.setVisibility(View.GONE);
	        
	        thRmItemButton=(ImageButton)findViewById(R.id.thInfoRemoveItem);
	        thRmItemButton.setOnClickListener(bThInfoRmItemListener);
	        thRmItemButton.setVisibility(View.GONE);
     
	        
			autoThItems=(AutoCompleteTextView) findViewById(R.id.thAutoView);	
	        autoThItems.setOnItemClickListener(autoThListener);
	        
	        tlRegisterFields=(TableLayout)findViewById(R.id.tlRegisterFields);
	        llAddThItem=(LinearLayout)findViewById(R.id.llAddRegister);
	        llUnlinkTh=(LinearLayout)findViewById(R.id.llUnlinkTh);

	        
	        name=getIntent().getExtras().getString("thName");
	        loadThInfo(name);
	        
	        createAddItemDialog();

	    }
	    

	    
	    private void createAddItemDialog() {

	     	addItemDialog= new Dialog(this);
        	addItemDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        	addItemDialog.setContentView(R.layout.thesaurus_additem_dialog);

        	
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


		@Override
	    public void onResume()
	    {
	            super.onResume();
	             
	            if(name!=null && !name.equals("")) {
	            		
	            	clearFormData();
	         
	            }
	    
	    }
	    
	    @Override
		protected void onStop(){
	    	
	    	super.onStop();
	    	
	    	thCnt.closeCursors();
	    	if(thInit) thCnt.closeThReader();
	    	
	    }
	    
	    
	    private OnItemClickListener autoThListener = new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            	
            	String value=autoThItems.getText().toString();
            	
            	/*  KEY_ROWID, GENUS,SPECIE,SUBSPECIE,AUTHOR,ICODE,NAMECODE,SUBAUTHOR,INFRA_SPEC_RANK}*/
            	Cursor c=thCnt.fetchThesaurusItembyName(value);
            	
            	String icode="";
            	String nameCode="";
            	String author="";
            	String subAuthor="";
            	
        		gender=c.getString(1);
        		spEpithet=c.getString(2);
        		spEpithetAuhtor=c.getString(4);
        		
        		infraSpEpithet=c.getString(3);
        		infraSpEpithetAuthor=c.getString(7);

           	
            	if(c.getString(5)!=null) icode=c.getString(5);
            	if(c.getString(6)!=null) nameCode=c.getString(6);
            	if(c.getString(4)!=null) author=c.getString(4);
            	if(c.getString(7)!=null) subAuthor=c.getString(7);

            	
            	
            	fillThItemData(icode,nameCode,author,subAuthor);
            	
            	thItemId=c.getLong(0);
            	
            	icodeValue=icode;
            	
            	c.close();
            
            }

		
    }; 
    
	private OnClickListener newGenusThListener = new OnClickListener()
    {
        public void onClick(View v){    
        
        	addItemDialog.show();
         
        	etGenusEt.setText("");
        	etSpEpithet.setText("");
        	etSpEpithetAuhtor.setText("");  
        	spRank.setSelection(0);

        	
        }
    };
    
	private OnClickListener unlinkThListener = new OnClickListener()
    {
        public void onClick(View v){
        	
        	thCnt.unlinkTh(thId);
        	
        	llAddThItem.setVisibility(View.VISIBLE);
        	llUnlinkTh.setVisibility(View.GONE);
        	
        }
    };
    
	private OnClickListener newSpEpithetThListener = new OnClickListener()
    {
        public void onClick(View v){
        	
        	addItemDialog.show();

        	etGenusEt.setText(gender);
        	etSpEpithet.setText("");
        	etSpEpithetAuhtor.setText(""); 
        	spRank.setSelection(0);
        	
        }
    };
    
	private OnClickListener newInfraSpEpithetThListener = new OnClickListener()
    {
        public void onClick(View v){                        
         
        	addItemDialog.show();

        	etGenusEt.setText(gender);
        	etSpEpithet.setText(spEpithet);
        	etSpEpithetAuhtor.setText(spEpithetAuhtor);    
        	spRank.setSelection(1);
        	
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
        			  
            		incrThCount();
            		        		
            		Utilities.showToast(getString(R.string.thNewItemAdded), v.getContext());

            		clearAddItemsForm();
            		
            		addItemDialog.dismiss();
        			
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
	    
	    
	    
    
    /*
     * It retrieves thesaurus items properties and show i.
     * 
     */
       
	    public void fillThItemData(String icode, String name, String author, String subAuthor){
	    	
	    	TextView tvIcode=(TextView)findViewById(R.id.tvIcode);
        	tvIcode.setText(getString(R.string.thInfoFieldPrimaryKey)+": "+icode);
        	
        	TextView tvNameCode=(TextView)findViewById(R.id.tvNameCode);
        	tvNameCode.setText(getString(R.string.thInfoFieldSecondaryKey)+": "+name);
        	
        	TextView tvAuthor=(TextView)findViewById(R.id.tvAuthor);
        	tvAuthor.setText(getString(R.string.thInfoFieldAuthorEpEsp)+": "+author);
        	
        	TextView tvSubAuthor=(TextView)findViewById(R.id.tvAuthorSub);
        	tvSubAuthor.setText(getString(R.string.thInfoFieldAuthorInfraEpEsp)+": "+subAuthor);
        	
        	TextView tvComplete=(TextView)findViewById(R.id.tvCorrect);

        	if(icode.compareTo("")==0){
	        	
        		tvComplete.setText(R.string.tvIcodeMissing);
        	
        	}
        	else{
        		
        		if(icode.compareTo(name)==0){
	        		
	        		tvComplete.setText(R.string.tvAccepted);
	        		
	        	}
	        	
	        	else{
	        		
	        		String text=getString(R.string.tvSynonymous);
	        		tvComplete.setText(text+" "+thCnt.fetchThesaurusSynonymous(icode));
	
	        	}
        		

           	//when thesaurus filum is empty connecting to database is not allowed	
        	if(!filumLetter.equals("")) thInfoButton.setVisibility(View.VISIBLE);
        	
        		
        		
        	}
        	
        	tlRegisterFields.setVisibility(View.VISIBLE);
	        thRmItemButton.setVisibility(View.VISIBLE);

        	
	    }
	    
	    
	    
	    protected void incrThCount() {
	    	
	    	thCount++;
	    	
	    	thCnt.updateThCount(thId,thCount);
	    	
		    tvThNumItems.setText(getBaseContext().getString(R.string.thInfoNumItems)+thCount);

	    	
		}
	    
	    protected void decThCount(){
	    	
	    	thCount--;
	    	
	    	thCnt.updateThCount(thId,thCount);
	    	
		    tvThNumItems.setText(getBaseContext().getString(R.string.thInfoNumItems)+thCount);
		    
	    }




		protected void clearAddItemsForm() {

	    	etGenusEt.setText("");
    		etSpEpithet.setText("");
    		etSpEpithetAuhtor.setText("");
    		etInfraSpEpithet.setText("");
    		etInfraSpEpithetAuthor.setText("");
	    	
	    }

		public void clearFormData(){
	    	
	    	TextView tvIcode=(TextView)findViewById(R.id.tvIcode);
        	tvIcode.setText("");
        	
        	TextView tvNameCode=(TextView)findViewById(R.id.tvNameCode);
        	tvNameCode.setText("");
        	
        	TextView tvAuthor=(TextView)findViewById(R.id.tvAuthor);
        	tvAuthor.setText("");
        	
        	TextView tvComplete=(TextView)findViewById(R.id.tvCorrect);
        	tvComplete.setText("");
             		
        	thInfoButton.setVisibility(View.GONE);

        	autoThItems.setText("");
        	thRmItemButton.setVisibility(View.GONE);
        	
        	tlRegisterFields.setVisibility(View.GONE);
        	
        	
	    }

	    
	    private OnClickListener bRemoveThListener = new OnClickListener()
	    {
	        public void onClick(final View v)
	        {           
	        	
	        	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	        	
		    	builder.setMessage(R.string.deleteThQuestion)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		    	    	   
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    	        	   int thUsedBy=thCnt.removeTh(name);
							
		    	        	   if(thUsedBy > 1) {
							
		    	        		   String text = String.format(v.getResources().getString(R.string.thUsedBySeveralProj), thUsedBy);
								
		    	        		   Toast.makeText(v.getContext(),text,Toast.LENGTH_SHORT).show();

		    	        	   } 
		    	        	   else if(thUsedBy ==1){
								
		    	        		   String projName=projCnt.getProjNameByTh(name);
		    	        		   
		    	        		   String text = String.format(v.getResources().getString(R.string.thUsedByOneProj), projName);
								
		    	        		   Toast.makeText(v.getContext(),text,Toast.LENGTH_SHORT).show();
								
		    	        	   }
		    	        	   else{
		    	        		   		    	        		   
		    	        		   finish();
		    	        		   
		    	        	   }
		    	        	   
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
	    };
	    
	    
	    
	    private OnClickListener bThInfoListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {      
	        	
	    		
	    		Intent intent = new Intent(getBaseContext(), TaxonRemoteTab.class);
				intent.putExtra("projId",-1);
	    		intent.putExtra("filumLetter", filumLetter);
				intent.putExtra("taxon", autoThItems.getText().toString());

				startActivityForResult(intent,0);
				
	        	
	        }
				 			 
	    };
	    
	    private OnClickListener bThInfoRmItemListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {      
	        	
	        	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	        	
	        	String thDeleteItem=String.format(getString(R.string.thDeleteItemThQuestion), autoThItems.getText().toString());
	        	
		    	builder.setMessage(Html.fromHtml(thDeleteItem))
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		    	    	   
		    	           public void onClick(DialogInterface dialog, int id) {
		    
		    		        	thCnt.removeThElement(thItemId);        	
		    		        	decThCount();
		    		        	clearFormData();
		    	        			    	        	   
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
				 			 
	    };
	    

	    
	    public void loadThInfo(String thName){
	    	
	    	thCnt=new ThesaurusControler(this);
	    	
	    	autoThItems.setText("");
	    	
	    	
	    	Cursor c=thCnt.getThInfo(thName);
	    	c.moveToFirst();
	    	
	    	String thOriginType=c.getString(6);
	    	String thRemoteId=c.getString(8);
	    		
	    	TextView tvThName = (TextView)findViewById(R.id.tvThName);
		    TextView tvThType= (TextView)findViewById(R.id.tvThType);
		    tvThNumItems = (TextView)findViewById(R.id.tvThNumItems);
		    TextView tvThOrigin = (TextView)findViewById(R.id.tvThOrigin);
		    TextView tvThUpdate = (TextView) findViewById(R.id.tvThUpdateTimestamp);
		    TextView tvThOriginType= (TextView)findViewById(R.id.tvThOriginType);
		    
 
		    thId=c.getLong(0);    
		    tvThName.setText(Html.fromHtml("<b>"+getBaseContext().getString(R.string.thInfoName)+"</b>"+thName));
		    
		    thCount=c.getInt(3);
		    tvThNumItems.setText(Html.fromHtml("<b>"+getBaseContext().getString(R.string.thInfoNumItems)+"</b>"+thCount));
		    
		    if(!thOriginType.equals("remote")) tvThOrigin.setText(Html.fromHtml("<b>"+getBaseContext().getString(R.string.thInfoOriginFile)+": </b>"+c.getString(5)));
		    else tvThOrigin.setText(Html.fromHtml("<b>"+getBaseContext().getString(R.string.thInfoOriginDB)+": </b>"+c.getString(5)));
		    
		    tvThOriginType.setText(Html.fromHtml("<b>"+getBaseContext().getString(R.string.thInfoOriginType)+": </b>"+translateThConnection(thOriginType)));
		    tvThUpdate.setText(Html.fromHtml("<b>"+getBaseContext().getString(R.string.thInfoImportTimestamp)+": </b>"+c.getString(7)));
		        
		   // if(thOriginType.contains("local")) llAddThItem.setVisibility(View.VISIBLE);
		    //else 
		    
		    if(thOriginType.equals("remote") && (thRemoteId!=null && !thRemoteId.equals(""))){
		    	
		    	llUnlinkTh.setVisibility(View.VISIBLE);
		    	llAddThItem.setVisibility(View.GONE);
		    	
		    }
		    else{
		    	
		    	llUnlinkTh.setVisibility(View.GONE);
		    }
		    
		        
		    String thType=Utilities.translateThTypeToCurrentLanguage(this, c.getString(4));   
		    tvThType.setText(getBaseContext().getString(R.string.tvThType)+" "+thType);
		        
		    filumLetter=Utilities.translateThTypeToFilumLetter(this, thType);
		        
		    
		    thCnt.initThReader(thName);
		        
		    thInit=true;
		        
		    ThesaurusAutoCompleteAdapter autoListAdapter = thCnt.fillData(autoThItems);

		    autoThItems.setAdapter(autoListAdapter);   
		    autoThItems.setWidth(300);
		    autoThItems.setHint(R.string.taxonHint);
		
				
			c.close();

	    	
	    }

	    
		 private String translateThConnection(String sourceType) {
			 
			String xmlLocalTh=getString(R.string.thSourceLocalBVegana);
			String plainTh=getString(R.string.thSourceLocalPlain);
			String remoteTh=getString(R.string.thSourceRemote);

			 if(sourceType.equals("remote")) return remoteTh;
			 else if(sourceType.equals("localBvegana")) return xmlLocalTh;
			 else return plainTh;
			
		}
		 
		    @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		        super.onActivityResult(requestCode, resultCode, intent);

		       		        	
		        	
		        switch(requestCode) {
		        case 0 :
		        	
		        	tlRegisterFields.setVisibility(View.GONE);
			        loadThInfo(name);
		            
		        	break;
		            
		        case 1 :
		        	
		        	
		           
		            break;

		        

		        }
		    }
		    
		 


	    
}
