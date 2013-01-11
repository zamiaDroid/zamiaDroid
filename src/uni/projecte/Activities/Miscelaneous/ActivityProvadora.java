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


package uni.projecte.Activities.Miscelaneous;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import uni.projecte.R;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.ui.multiphoto.MultiPhotoFieldForm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;


public class ActivityProvadora extends Activity {
	
	private final int CAMERA_PICTURE = 1;
	private final int GALLERY_PICTURE = 2;
	private Intent pictureActionIntent = null;
	
	private ImageView userPictureImageView;
	private LinearLayout llMultiPhoto;
	private MultiPhotoFieldForm multi;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  
	       super.onCreate(savedInstanceState);
	        
	       Utilities.setLocale(this);
		   
	       setContentView(R.layout.actprovadora);
	       
	       llMultiPhoto=(LinearLayout)findViewById(R.id.llMultiPhoto);
	       userPictureImageView = (ImageView)findViewById(R.id.ivPhotoDownload);

	       
	       //ProjectField projField=new ProjectField(
	       multi= new MultiPhotoFieldForm(this, -1, null, llMultiPhoto,1);

	       
	      userPictureImageView.setOnClickListener(new OnClickListener() {
	           public void onClick(View v) {
	        	   
	               startDialog();
	               
	           }

	       });
	      
	      
	      // Field --> secondLevelField
	      	      
	       
		    
	  }
	  
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    super.onActivityResult(requestCode, resultCode, data);
		    		    
		    if (requestCode == GALLERY_PICTURE) {
		    	
		        Uri uri = data.getData();
		        
		        if (uri != null) {
		            // User had pick an image.
		            Cursor cursor = getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
		            cursor.moveToFirst();
		            // Link to the image
		            final String imageFilePath = cursor.getString(0);
		            
			 	    multi.addPhoto(imageFilePath);
		            
		            cursor.close();
		            
		        }
		        else {
		        	
		            Toast toast = Toast.makeText(this, "No Image is selected.", Toast.LENGTH_LONG);
		            toast.show();
		            
		        }
		    }
		    
		    else if (requestCode == CAMERA_PICTURE) {
		    	
		        if (data.getExtras() != null) {
		        	
		            // here is the image from camera
		            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

		            //multi.addPhoto(imageFilePath);
		            
		            userPictureImageView.setImageBitmap(bitmap);
		            
		        }
		    }
		    
		    
		}



		private void startDialog() {
			
			
		    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		    myAlertDialog.setTitle("Upload Pictures Option");
		    myAlertDialog.setMessage("How do you want to set your picture?");

		    myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface arg0, int arg1) {
		            pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
		            pictureActionIntent.setType("image/*");
		            pictureActionIntent.putExtra("return-data", true);
		            startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
		        }
		    });

		    myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface arg0, int arg1) {
		            pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		            startActivityForResult(pictureActionIntent, CAMERA_PICTURE);
		        }
		    });
		    myAlertDialog.show();
		}
	  

}


	
	
/*	private ThesaurusControler tC;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.projcreator);	    
	
	    
	 //   DataTypeControler dtC = new DataTypeControler(this);
	    
	  // long id= dtC.addDT("tesaurus", "lala", 2);
	   
	
	   //tsList=tc.ReadSettings(this);
	     
	  // Iterator<String> itr = tsList.iterator();
	
	   
	   //String[] items=dtC.getItemsbyDTName("tesaurus");
		 
	   // AutoCompleteTextView e = (AutoCompleteTextView) findViewById(R.id.auto1);

	   


		//	  e.setWidth(180);
			  
			//  ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(this,
	          //          android.R.layout.simple_spinner_item,items);
	
			  
			  // adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			   
			  
			   //e.setAdapter(adapter2);   
			    
		  

        
       
        /*
        String[] from= new String[] { ThesaurusItemsDbAdapter.GENUS , ThesaurusItemsDbAdapter.SPECIE, ThesaurusItemsDbAdapter.SUBSPECIE};
        int[] to = new int[] { R.id.Slat , R.id.Slong, R.id.Sdate}; 
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter mostres = new SimpleCursorAdapter(this, R.layout.types_row, c, from, to);
        
        
      llista.setAdapter(mostres);*/
      
      
      
      
      
      /*CursorToStringConverter converter = new CursorToStringConverter() {

         public CharSequence convertToString(Cursor cursor) {

            return cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
         }
      }; */

    //  notes.setCursorToStringConverter(converter);

      
	 // ThesaurusListAdapter notes = fillData();

      
     // textView.setAdapter(notes);
      
     // textView.setHint("escriu nom tàxon...");
      
    //  tC.close();
	     
	    // ProjectXMLparser pr= new ProjectXMLparser(null);
	    // pr.readXML(this, "lalaa", false);
	        
	   
     //   ZamiaProjectControler zpControler = new ZamiaProjectControler(this);
        
      ///  llegirJSONServer();

        
       // SecondLevelProjectControler slC=new SecondLevelProjectControler(this);
        
      //  slC.createField(3, "Taxon", "taxon", "", "Carduelis carduelis","simple");
       // slC.createField(3, "Descripció", "descripcio", "ajaaj","lololo", "simple");
       // slC.createField(3, "Superfície", "sup", "","25m2", "simple");
       // slC.createField(3, "Localitat", "loc", "","Torredembarra", "simple");

        
 //       QuercusWriter qw= new QuercusWriter();
        
    //    qw.openDocument();
        
   /*     qw.openReleve("p-P00104", "BB");
        
	        qw.addReleveDate("10", "05", "2<br/>011");
	        
	        qw.addReleveArea("25.0");
	        
	        qw.writeReleveCoordinate("UTMCM13"); */
	        
/*        qw.addReleveEntry("Adonis vernalis L.", "+", "");

	        qw.addReleveEntry("Euphorbia cyparissias L." , "-", "");
	        qw.addReleveEntry("Plantago media L.", "+", "GROUP");
	        qw.addReleveEntry("Asperula cynanchica L.", "3", "");
	        qw.addReleveEntry("Buxus sempervirens L.", "1", "");

       
        qw.closeReleve();
        
        
        qw.openReleve("p-P00096", "BB");
        
        qw.addReleveDate("20", "07", "2011");
        
        qw.addReleveArea("25.0");
        
        qw.writeReleveCoordinate("UTMCM13");
        
        qw.addReleveEntry("Adonis vernalis L.", "+", "");
        qw.addReleveEntry("Euphorbia cyparissias L." , "-", "");
        qw.addReleveEntry("Plantago media L.", "+", "GROUP");
        qw.addReleveEntry("Asperula cynanchica L.", "3", "");
        qw.addReleveEntry("Buxus sempervirens L.", "1", "");

        qw.createSideData("Substratum", "substratum", "Roques calcàries s.l.", false, "ECO");
       
        qw.createSideData("Aspect", "aspect", "NE", false, "GEO");
        qw.createSideData("Inclination", "inclination", "5", false, "GEO");
        qw.createSideData("Locality", "locality", "Vora Olià", true, "GEO");

        
   */
   // qw.closeReleve();
        
        
     //   qw.closeDocument();
        
        
        
        
     //  String s= qw.convertXML2String();
        
       //s=s;

        
        //ConnectionManager td= new ConnectionManager();
        
        

        
       /* try {
            Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        String response = getResponse();
        */
        
	    
	/*}
	
	public class BrowsePicture extends Activity { */
	/*	
    private Uri[] mUrls;  
    String[] mFiles=null;  
    double IMAGE_MAX_SIZE= 640;
    String projectName="floraPPCC";
    
    SampleControler sC;
    ResearchControler rsCont;

    private Button btAddFields;
    private Button btRemoveFields;
    private ListView elementsList;
    private Spinner fieldsList;
    private Spinner separator;
    private TextView thesaurusElementsList;
    private String listValue="";
    String separatorString=";";

    private ArrayAdapter<CharSequence> adapterFields;
    
    private HashMap<String,Boolean> selected;
    
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.thimportplain);
	    
	    btAddFields = (Button) findViewById(R.id.btAddThField);
	    btRemoveFields = (Button) findViewById(R.id.btRemoveThField);
        fieldsList = (Spinner) findViewById( R.id.spFields );
        thesaurusElementsList=(TextView) findViewById(R.id.tvThesaurusElments);
	    elementsList = (ListView) findViewById(R.id.lvThFieldsOrdered);

        
	    selected= new HashMap<String, Boolean>();

        PreferencesControler pC=new PreferencesControler(this);
        
        ThesaurusControler thCnt= new ThesaurusControler(this);
        
        String[] fields={"Genus","SpecificEpithet"};

        //PlainThesaurusReader ptR= new PlainThesaurusReader("\";\"");
     
      //  ptR.readFile("/mnt/sdcard/llistatPla.txt");
                
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        		  this, R.array.thesaurusFields, android.R.layout.simple_spinner_item );
        
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        		
        fieldsList.setAdapter( adapter );
        
        
        
        adapterFields = ArrayAdapter.createFromResource(
      		  this, R.array.thesaurusFields, android.R.layout.simple_spinner_item );
      
      adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
      		
      fieldsList.setAdapter( adapter );
        
        
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
      		  this, R.array.thesaurusFieldsSeparators, android.R.layout.simple_spinner_item );
      
        adapter2.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        
        separator = (Spinner) findViewById( R.id.spElementsSeparator);
        separator.setAdapter( adapter2 );
        		
        
        btAddFields.setOnClickListener(addElementListener);
        btRemoveFields.setOnClickListener(removeElementListener);
        
        
        
       fieldsList.setOnItemSelectedListener(new OnItemSelectedListener() {
           
    	   public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
           
    		  // selectedItemView.toString
    		   if(selected.get(fieldsList.getSelectedItem().toString())!=null){
    			   
    			   btRemoveFields.setVisibility(View.VISIBLE);
    			   btAddFields.setVisibility(View.GONE);
    			   
    		   }
    		   else{
    			   
    			   btRemoveFields.setVisibility(View.GONE);
    			   btAddFields.setVisibility(View.VISIBLE);
    			   
    		   }
    		   
    	   
    	   }

            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

	    
	}
	
	private OnClickListener addElementListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	    	
        	Utilities.showToast(fieldsList.getSelectedItem().toString(), v.getContext());
        	
        	listValue.replace(separatorString, separator.getSelectedItem().toString());
        	        	
        	separatorString=separator.getSelectedItem().toString();
        	
        	listValue=listValue+separatorString+fieldsList.getSelectedItem().toString();
        	
        	thesaurusElementsList.setText(listValue.substring(1));
        	
        	selected.put(fieldsList.getSelectedItem().toString(),true);
        	
        	if(fieldsList.getSelectedItemPosition()<fieldsList.getCount()-1) fieldsList.setSelection(fieldsList.getSelectedItemPosition()+1);

        	
        }
    };
    
	private OnClickListener removeElementListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	    	
        	Utilities.showToast(fieldsList.getSelectedItem().toString(), v.getContext());
        	
        	separatorString=separator.getSelectedItem().toString();
        	
        	String removeValue=separatorString+fieldsList.getSelectedItem().toString();
        	listValue=listValue.replace(removeValue,"");
        	        	       	
        	thesaurusElementsList.setText(listValue.substring(1));
        	
        	selected.remove(fieldsList.getSelectedItem().toString());
        	
   
        	
        }
    };
    */
	

	
	/*public void importThesaure(){
		
		tC= new ThesaurusControler(this);
		

		//tC.importThesaurus();
    	
    	tC.close();
		
	}
	
	
	public void llegirJSONServer(){
		
		ZamiaProjectJSON zpJSON= new ZamiaProjectJSON();
        zpJSON.connect("http://10.0.2.2:8080/ZamiaProjectProvider/GetZamiaProject?&lang=ca");
		
	}
	

*/

