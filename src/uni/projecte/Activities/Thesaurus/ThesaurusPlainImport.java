package uni.projecte.Activities.Thesaurus;

import java.util.ArrayList;
import java.util.Iterator;

import uni.projecte.R;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.ThesaurusManager.xml.PlainThesaurusReader;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ThesaurusPlainImport extends Activity{
	

	  private CheckBox cbFirstFieldLabels;
	  private CheckBox cbScape;	  
	  private Spinner separator;
	  private Spinner  thTypes;
	  private Button btImportTh;
	  private EditText etThName;

	  private String separatorString="\",\"";
	  private String firstLine;
	  private TableLayout fieldsMapper;
	  private String thName;
	  private String fileNamePath;
	  private String fileName;
	  
	  private PlainThesaurusReader ptR;
	  private ThesaurusControler thCnt;
	  
	  private ArrayList<Spinner> spinnerList;
	  
	  private int nFields;
	  private long thId;
	  
	  private ProgressDialog pd;

    	    
		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    
		    setContentView(R.layout.thesaurus_import_plain);
		  		    
	        thTypes = (Spinner) findViewById(R.id.spTaxonomicalKingdoms);
	        separator = (Spinner) findViewById(R.id.spElementsSeparator);
	        fieldsMapper=(TableLayout)findViewById(R.id.llFieldsMapper);
	        fieldsMapper.setColumnShrinkable(0, true);
	        
	        cbFirstFieldLabels=(CheckBox)findViewById(R.id.cbFirstLabelsColumn);
	        cbScape=(CheckBox)findViewById(R.id.cbScape);
	        btImportTh=(Button)findViewById(R.id.btImportPlainTh);
	        
	        cbFirstFieldLabels.setOnCheckedChangeListener(cbFirstLineListener);
	        cbScape.setOnCheckedChangeListener(cbScapeListener);
	        btImportTh.setOnClickListener(btImportThListener);
	        
	        etThName=(EditText) findViewById(R.id.etThName);
	        
	        thCnt= new ThesaurusControler(this);
	        
	        fileNamePath=getIntent().getExtras().getString("filePath");
	        fileName=getIntent().getExtras().getString("fileName");
	        
	        etThName.setText(fileName);

	        ptR= new PlainThesaurusReader(fileNamePath,separatorString);
	        ptR.setOmitFirstLine(cbFirstFieldLabels.isChecked());
	        ptR.setScape(cbScape.isChecked());
	        
	        firstLine=ptR.readFileFirstLine();
        
	        loadSpinners();
	        
	        separator.setOnItemSelectedListener(thSeparatorListener);

	        
		}
		
		private void loadSpinners() {

			   ArrayAdapter<CharSequence> adapterThTypes = ArrayAdapter.createFromResource(
		        		  this, R.array.thesaurusFilums, android.R.layout.simple_spinner_item );
		        
		        adapterThTypes.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		        thTypes.setAdapter( adapterThTypes );
		  
		        
		        ArrayAdapter<CharSequence> adapterFieldSeparators = ArrayAdapter.createFromResource(
		      		  this, R.array.thesaurusFieldsSeparators, android.R.layout.simple_spinner_item );
		      
		        adapterFieldSeparators.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		        
		        separator = (Spinner) findViewById( R.id.spElementsSeparator);
		        separator.setAdapter( adapterFieldSeparators );
			
		}

		private OnItemSelectedListener thSeparatorListener = new OnItemSelectedListener() {
            
	     	   public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

	     		   separatorString=Utilities.translateThFieldsSepartor(parentView.getContext(), separator.getSelectedItem().toString());
	     		   ptR.setFieldSeparator(separatorString);
					
	     		   fillFieldsSelector(firstLine);
	     	   
	     	   }

	             public void onNothingSelected(AdapterView<?> parentView) {

	             }

	         };
		
		private OnClickListener btImportThListener = new OnClickListener() {
			
			public void onClick(View v) {
		
				thName=etThName.getText().toString();
				
				if(thName.equals("")){
					
					Utilities.showToast(getString(R.string.thName),v.getContext());
					
				}
				else{		
					
					thId=thCnt.createThesaurus(thName,"",Utilities.translateThTypeToFilumLetter(v.getContext(),thTypes.getSelectedItem().toString()),fileName,"localPlain");
	            
		            if(thId>0){

		            	importTh();
		           	 
		            }
		            else{
	           	 
		               String sameTh=getBaseContext().getString(R.string.sameThName);
		
		            	Toast.makeText(getBaseContext(), 
			   		              sameTh+" "+thName, 
			   		              Toast.LENGTH_LONG).show();
		                	
	           	 
		            }
	
				}
				
				
			}
		};
		
		
		
		 private void importTh() {
			 
			 pd = ProgressDialog.show(this, getString(R.string.thLoading), getString(R.string.thLoadingTxt), true,false);
			 
			                 Thread thread = new Thread(){
			  	        	   
			  	        	   
				                 @Override
								public void run() {
				               	  
				               	  
				                	importThThread(); 
				               	  
				                 }
				           };
				           
				           
			   thread.start();
			 
			}
		 
		 private void importThThread(){
			 
			 String[] orderedChoosenFields=createFieldsOrderedArray();
				
			 thName=thName.replace(".", "_");

			 //thId,thName,
			 
			boolean error=thCnt.addThItemsPlainTh(thId, thName, orderedChoosenFields, ptR);

			if(!error) handler.sendEmptyMessage(0);
			else handler.sendEmptyMessage(1);
			 


			 
		 }
		 
		 
		 
		 private Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					
					
					pd.dismiss();
					
					if (msg.what==0){
						
						Intent intent = new Intent();
				    	
						Bundle b = new Bundle();
						b.putString("thName", thName);
									
						intent.putExtras(b);

						setResult(1, intent);
						
					}
					else{
						
					  	Toast.makeText(getBaseContext(), 
	  	   		              getBaseContext().getString(R.string.thWrongFile), 
	  	   		              Toast.LENGTH_LONG).show();
						
					}
					
			
			        finish();


				}
			};
		 
		
		
		private OnCheckedChangeListener cbFirstLineListener = new CheckBox.OnCheckedChangeListener() {
	        

			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				
				ptR.setOmitFirstLine(isChecked);
				
			}

		};
		
		private OnCheckedChangeListener cbScapeListener = new CheckBox.OnCheckedChangeListener() {
	        

			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				
				ptR.setScape(isChecked);
				fillFieldsSelector(firstLine);
				
			}

		};

		private void fillFieldsSelector(String firstLine) {
			
			spinnerList= new ArrayList<Spinner>();
			
			
			String[] fields=firstLine.split(separatorString);
			nFields=fields.length;
			
			fieldsMapper.removeAllViews();
			
			for(int i=0; i<nFields; i++){
				
				   TableRow lp=new TableRow(this);
			       lp.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
				   
				   TextView t=new TextView(getBaseContext());
				   Spinner sp= new Spinner(this);
				      
				   		ArrayAdapter<CharSequence> adapterFields = ArrayAdapter.createFromResource(
				        		  this, R.array.thesaurusFields, android.R.layout.simple_spinner_item);
				        
				       adapterFields.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					   sp.setAdapter(adapterFields);
					   
					   spinnerList.add(sp);
					   
					  if((sp.getCount()-1)>i) sp.setSelection(i);
					  else sp.setSelection(sp.getCount()-1);
					  
				   
					if(ptR.isScape()) t.setText(fields[i].replace("\"", ""));
					else t.setText(fields[i]);
				   
				   lp.addView(t);
				   lp.addView(sp);
				   
				   fieldsMapper.addView(lp);
				
			}
			
					
			
			
		}
		
		private String[] createFieldsOrderedArray(){
			
			Iterator<Spinner> it=spinnerList.iterator();
			String[] choosenFieldsList=new String[nFields];
			int i=0;
			
			while(it.hasNext()){
				
				Spinner tmpSp=it.next();
				
				choosenFieldsList[i]=Utilities.translateThFieldType(this, tmpSp.getSelectedItem().toString());
				                  
				 i++;
				
			}
			
			return choosenFieldsList;
			
			
		}
		
	
}
