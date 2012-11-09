package uni.projecte.Activities.Citations;

import java.util.ArrayList;
import java.util.Iterator;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectSecondLevelControler;
import uni.projecte.controler.CitationSecondLevelControler;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.ui.CustomEditableCell;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



public class SecondLevelList extends Activity {
	
	
		private long projId;
		private long subProjId;
		private String subLevelTag;
		private  TableLayout aTable;
		private ArrayList<Long> fieldIdList;

		
		private int numElem;
		private int numRows;
		
		
	
	  @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        Utilities.setLocale(this);
	        setContentView(R.layout.secondfieldlist);
	        
	        /*  identificador element segon nivell  */
	        /* identificador del camp de segon nivell */
	        /* identificador del projecte  */
	        
	        
	        projId=getIntent().getExtras().getLong("id");
	        
	        /* fieldId */
	        
	        subProjId=getIntent().getExtras().getLong("subProjId");
	       
	        subLevelTag=getIntent().getExtras().getString("subLevelTag");
	        
	        aTable=(TableLayout)findViewById(R.id.amortization);
	        
	        TextView subProjectTv = (TextView)findViewById(R.id.suBprojectName);
	        subProjectTv.setText(subLevelTag);
	        
	        Button modifySL=(Button) findViewById(R.id.bModifySecLev);
	        modifySL.setOnClickListener(modifySecLevelListener);

	        Button addNewSL=(Button) findViewById(R.id.btAddNewList);
	        addNewSL.setOnClickListener(addNewSecLevelListener);

	        setFinishButton();
	    
	        
	        createRowTitles();
	        
	        addSecondFieldRows();
	        
	        numRows=aTable.getChildCount()-1;
	        
	        
	        
	  }
	  
	  
	    private void setFinishButton() {

	        
            ImageButton imgButton = (ImageButton)findViewById(R.id.btFinishActivity);
            imgButton.setBackgroundResource(android.R.drawable.ic_notification_clear_all);
            
            imgButton.setOnClickListener(new OnClickListener() {  
	            	
	            	public void onClick(View v) { 
	            		
	            		
	    	        	returnElements();

	            	
	             } }
	            
	            );		
	}


		@Override
	    public void onResume()
	    {
	             super.onResume();
	             
	             aTable.removeAllViewsInLayout();
	             
	             createRowTitles();
	 	        
	 	        addSecondFieldRows();	  
	 	        
	 	       numRows=aTable.getChildCount()-1;
	 	        
	    }
	    
		
		private void returnElements(){
			
      	   	Intent intent = new Intent();
   	    	
    		Bundle b = new Bundle();
    		b.putInt("numSecCit", numRows);
    			
    		intent.putExtras(b);
    			
    		b = new Bundle();
    		b.putLong("subProjId", subProjId);
    						
    		intent.putExtras(b);

    	    setResult(5, intent);
    		
    		finish();
			
		}
	    
	  
		private OnClickListener modifySecLevelListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                        
	        
	        	updateModifiedTable();
	        	
	        	returnElements();
	        	
	        }
	    };
	    
		private OnClickListener addNewSecLevelListener = new OnClickListener()
	    {
	        public void onClick(View v)
	        {                        
	        
	            Intent myIntent = new Intent(v.getContext(), SecondLevelSampling.class);
        		
        		Utilities.showToast("Element id "+v.getId(), v.getContext());
        		
	        	myIntent.putExtra("id", projId);
	        	
	        	myIntent.putExtra("subProjId", subProjId);
	        
	        	myIntent.putExtra("subLevelTag", subLevelTag);
	        	
	
	            startActivityForResult(myIntent, 0);
	        	
	        }
	    };
	    
	  
	  private void addSecondFieldRows() {

		  CitationSecondLevelControler scndLevCnt= new CitationSecondLevelControler(this);
		  
		  Cursor cur=scndLevCnt.getFieldValuesBySLId(subLevelTag);
		  
		  while (cur.isAfterLast() == false) {
			  
			  
			   TableRow row = new TableRow(this);

					  
			   CustomEditableCell c = new CustomEditableCell(this);
					  
//			   c.setText(cur.getString(2)+"\n"+cur.getString(3));
	//		   c.setPadding(3, 3, 3, 3);
//			   row.addView(c);
			   
		//	   c = new CustomEditableCell(this);
			   
		//	   c.setText(cur.getString(4));
		//	   c.setPadding(3, 3, 3, 3);
		//	   row.addView(c);
			   
		
				   
			 createRowFromCitation(cur.getString(5),row,cur.getLong(0),setLocation(cur.getString(2),cur.getString(3)),cur.getString(4));

			   
			   	
	//		   	c = new CustomEditableCell(this);
			   
	//		   c.setText(cur.getString(1));
	//		   c.setPadding(3, 3, 3, 3);
	//		   row.addView(c);
	
				  
			   aTable.addView(row, new TableLayout.LayoutParams());
			  
	       	    cur.moveToNext();
	        }
	        cur.close();

		  
		  
	}
	  
	  
	  private String setLocation(String latitude, String longitude){
		  		  
		  
		  if(latitude.equals("100") && longitude.equals("190")){
			  
			return "";
			  
		  }
		  else{
			  
			  String location="";
			  
			 /*  if(pm.isUTM()){
	      	    	 
	      	         CoordinateUTM utm = CoordConverter.getInstance().toUTM(new CoordinateLatLon(latitude,longitude));
	      		     location=utm.getShortForm();

	      	     }
	      	     else { */
	      	    	 
	      	    	 location=latitude+"\n"+longitude;
	      	    	 
	      	 //    }
			  
			  return location;
		  }
		  
		  
		  
	  }
	  
	  
	  public void createRowFromCitation(String list, TableRow row, long id,String position,String date){
		  
		  CustomEditableCell c;
		   
		   String[] splited=list.split(":");
		   
		   for(int i=0; i<numElem;i++){
			   
			   c = new CustomEditableCell(this);
			   
			  
			  
			   if(i>=splited.length){
				   
				   c.setText("",id,fieldIdList.get(i));
				   
			   }
			   else{
				   
				   c.setText(splited[i],id,fieldIdList.get(i));
				   
			   }
			   
			   
			   
			   
			   c.setPadding(1, 1, 1, 1);
			   row.addView(c);
			   
		   }
		   
		   TextView tv= new TextView(this);
		   tv.setText(position);
		   row.addView(tv);

		   tv= new TextView(this);
		   tv.setText(date);
		   row.addView(tv);

		  
	  }

	public void createRowTitles(){
		
		  numElem=0;
		  
		  ProjectSecondLevelControler rsC=new ProjectSecondLevelControler(this);
		  rsC.loadProjectInfoById(projId);
		   
		   		   
		   ArrayList<ProjectField> projFieldList=rsC.getProjFields(subProjId);
		   Iterator<ProjectField> it=projFieldList.iterator();
		   
		   //for each field we will create the TextView with the label and an "special" view where the user will provide the field value
		   
		   TableRow row = new TableRow(this);
		   	  
		   TextView c = new TextView(this);
			  
	//	   c.setText("Localització");
	//	   c.setPadding(3, 3, 3, 3);
		   
	//	   row.addView(c);
			
	//	   c = new TextView(this);
			  
	//	   c.setText("Data");
	//	   c.setPadding(3, 3, 3, 3);
	//	   row.addView(c);
			
		   fieldIdList= new ArrayList<Long>();
		  
		   
		  while(it.hasNext()){
			
			  ProjectField att=it.next();
			  
			  c = new TextView(this);
			  c.setTextColor(Color.BLACK);
			  
			  c.setText(att.getLabel());
			  c.setPadding(3, 3, 3, 3);
			  row.addView(c);
			  
			  fieldIdList.add(att.getId());
			  
			  numElem++;
			  
		  }
		  

		  c = new TextView(this);
		  c.setTextColor(Color.BLACK);
		  
		  c.setText(this.getString(R.string.slLocation));
		  c.setPadding(3, 3, 3, 3);
		  row.addView(c);
		  
		  c = new TextView(this);
		  c.setTextColor(Color.BLACK);
		  
		  c.setText(this.getString(R.string.slDate));
		  c.setPadding(3, 3, 3, 3);
		  row.addView(c);
		  
		  
		  row.setBackgroundColor(Color.LTGRAY);

		  
		  aTable.addView(row, new TableLayout.LayoutParams());
		 		  
	  }
	
	public void updateModifiedTable(){
		
		  CitationSecondLevelControler rsC=new CitationSecondLevelControler (this);
		  rsC.startTransaction();
		  
		
		int rows=aTable.getChildCount();
		
		for(int i=1; i<rows; i++){
			
			TableRow tR=(TableRow) aTable.getChildAt(i);
			
			
			for(int j=0;j<tR.getChildCount();j++){
				
				View ce= tR.getChildAt(j);
				
				if(ce instanceof CustomEditableCell){
				
					rsC.updateCitationField(((CustomEditableCell)ce).getCitationId(), ((CustomEditableCell)ce).getFieldId(),((CustomEditableCell)ce).getText());
				
				}

				
			}
			
			
			
		}
		
		rsC.EndTransaction();
		
		
	}
	
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
    		
        	returnElements();
        		
	        return true;

        	
        }
        
        return false;

    }
	  
	  

}
