package uni.projecte.ui.multiphoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import uni.projecte.R;
import uni.projecte.controler.MultiPhotoControler;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.ProjectField;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class MultiPhotoFieldForm extends PhotoFieldForm {

	public static int CREATE_MODE = 1;
	public static int EDIT_MODE = 2;
	
	private int PHOTO_FIELD_MODE;
	
	public static int RESULT_LOAD_IMAGE = 8;
	 
	/* Main container */
	private LinearLayout llPhotosList;

	/* View elements */
	private ImageButton rmPhotoButton;
	private ImageButton editOkButton;
	private ImageButton viewPhotoButton;
	private ImageButton showPathButton;
	private ImageButton showGallery;
	
	private View llPhotoField;
	private TextView etPhotoPath;
	
	private HorizontalScrollView imageScroll;
	
	private ArrayList<String> photoList;
	private ArrayList<String> newPhotos;
	
	private String secondLevelId;
	private long citationId;
	
		
	public MultiPhotoFieldForm(Context baseContext, long projId, ProjectField field, LinearLayout llField, int mode){
		
		super(baseContext,projId,field,llField);
		
		LayoutInflater inflater = (LayoutInflater) baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		llPhotoField=(View) inflater.inflate(R.layout.field_photo,null);		
		
		PHOTO_FIELD_MODE=mode;
	
		photoList=new ArrayList<String>();
		newPhotos=new ArrayList<String>();		

		/* Layout resources */
		imageScroll=(HorizontalScrollView) llPhotoField.findViewById(R.id.hsImageScroll);
		llPhotosList= (LinearLayout) llPhotoField.findViewById(R.id.llPhotosList);

		imageScroll.setTag(field.getName());

		
	    createLayoutButtons();
		llField.addView(llPhotoField);
		
		if(PHOTO_FIELD_MODE==CREATE_MODE){

			secondLevelId=createSecondLevelIdentifier(field.getName());
			
		}
			
	}
	
	private void createLayoutButtons() {
		
		//buttons
		photoButton=(ImageButton)llPhotoField.findViewById(R.id.btPhotoButton);
		showGallery=(ImageButton) llPhotoField.findViewById(R.id.btAddFromGallery);

		
		rmPhotoButton=(ImageButton) llPhotoField.findViewById(R.id.btRmPhoto);
		viewPhotoButton=(ImageButton) llPhotoField.findViewById(R.id.btViewPhoto);
		editOkButton=(ImageButton) llPhotoField.findViewById(R.id.btEditOk);
		showPathButton=(ImageButton) llPhotoField.findViewById(R.id.btShowPath);
		etPhotoPath=(TextView)llPhotoField.findViewById(R.id.etPhotoPath);
		
		//by default rmButton is gone
		rmPhotoButton.setVisibility(View.GONE);
		viewPhotoButton.setVisibility(View.GONE);
		editOkButton.setVisibility(View.GONE);
		showPathButton.setVisibility(View.GONE);
		etPhotoPath.setVisibility(View.GONE);
		
		photoButton.setTag(field.getName());
		showGallery.setTag(field.getName());
		
		viewPhotoButton.setOnClickListener(viewPhoto);
		editOkButton.setOnClickListener(removePhotoActions);
		rmPhotoButton.setOnClickListener(removePhoto);
		showPathButton.setOnClickListener(showPath);
		etPhotoPath.setOnClickListener(openGallery);
				
	}
	

	
	public void clearForm() {

		photoList=new ArrayList<String>();
		llPhotosList.removeAllViews();
		secondLevelId=createSecondLevelIdentifier(field.getName());
		
	}
	
	private String createSecondLevelIdentifier(String fieldName){
				
    	SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd_kk:mm:ss");
    	Calendar c = Calendar.getInstance(); 
    	String date=dfDate.format(c.getTime());
    	
    	return fieldName.toLowerCase()+"_"+date;
		
	}	


	public void addPhoto(String imageFilePath) {

		Log.i("Photo","Intentant obrir: "+imageFilePath);
	        
        	Bitmap b=PhotoUtils.decodeBitmap(imageFilePath,(int)(IMAGE_MAX_SIZE*0.70),true);
        	
	        ImageView newImage=new ImageView(baseContext);
	    	LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			llp.setMargins(5, 5, 5, 5);
	        newImage.setImageBitmap(b);		
	        newImage.setLayoutParams(llp);
	
	        newImage.setTag(imageFilePath);
	        newImage.setAdjustViewBounds(true);
	        
	        newImage.setOnClickListener(showPhotoActions);
	        
			llPhotosList.addView(newImage,0);	
	        
	        photoList.add(0, imageFilePath);
        
		
	}
	
	public void addNewPhoto(String photoPath) {

		addPhoto(photoPath);
			        	
		newPhotos.add(0, photoPath);
			
		
	}
	
	public void setCitationData(List<String> list,String photoIdTag) {
		
		secondLevelId=photoIdTag;
		
		Iterator<String> it=list.iterator();
		
		while(it.hasNext()){
			
			String photoPath=(String)it.next();
			
			addPhoto(photoPath);
			
		}
		
		
	}
	
	public int removeAllPhotos(){
		
		int removed=0;	

		Iterator<String> it=photoList.iterator();

		while(it.hasNext()){

			String photoPath=(String)it.next();

			if (PhotoUtils.removePhoto(photoPath)) removed++;

		}

		return removed;
		
	}
	
	
	
	private OnClickListener showPhotoActions= new OnClickListener() {

        public void onClick(View v) {
          		                	
        	if(rmPhotoButton.getVisibility()==View.VISIBLE) {
        		
        		hideButtons();

            	int position=getImagePosition((String)rmPhotoButton.getTag());
            	((ImageView) llPhotosList.getChildAt(position)).setAlpha(255);
            	
        	}
        	else {
        		
        		showButtons((String) v.getTag());
        		((ImageView)v).setAlpha(125);
        	
        	}
            	
        }
   };
   
   private void showButtons(String photoPath){
	   
	 	rmPhotoButton.setVisibility(View.VISIBLE);
    	rmPhotoButton.setTag(photoPath);
    	      
    	editOkButton.setVisibility(View.VISIBLE);
    	editOkButton.setTag(photoPath);
    	
    	viewPhotoButton.setVisibility(View.VISIBLE);
    	viewPhotoButton.setTag(photoPath);
    	
		showPathButton.setVisibility(View.VISIBLE);
		showPathButton.setTag(photoPath);
		
		//photoButton.setVisibility(View.GONE);
		//showGallery.setVisibility(View.GONE);
		
	}
   
   private void hideButtons(){
	   
	   	rmPhotoButton.setVisibility(View.GONE);
	    
		editOkButton.setVisibility(View.GONE);
		
		viewPhotoButton.setVisibility(View.GONE);
		
		showPathButton.setVisibility(View.GONE);
    	etPhotoPath.setVisibility(View.GONE);
    	
    	//photoButton.setVisibility(View.VISIBLE);
		//showGallery.setVisibility(View.VISIBLE);
   		   
   }
   
   private int getImagePosition(String imagePath){
	   
	   boolean finish=false;
	   int i=0;
	   
	   Iterator<String> posIt=photoList.iterator();
	   
	   while(posIt.hasNext() && !finish){
		   
		   if(imagePath.equals(posIt.next())){
			   
			   finish=true;
		   }
		   else{
			   
			   i++;

		   }
		   		   
	   }
	   
	   return i;
	   
   }
   

	private OnClickListener removePhoto= new OnClickListener() {

        public void onClick(View v) {
          		                	
        	String imagePath=(String)v.getTag();
        	
        	int position=getImagePosition(imagePath);
        	
        	llPhotosList.removeViewAt(position);
        	
        	photoList.remove(position);
        	
        	PhotoUtils.removePhoto(imagePath);
        	
        	hideButtons();
        	       	
        	//we need to remove subcitation values
        	if(PHOTO_FIELD_MODE==EDIT_MODE){
        		
        		removeSubCitation(imagePath);
        		
        	}
        	
        }
   };
   
	private OnClickListener showPath= new OnClickListener() {

        public void onClick(View v) {
          		 
        	etPhotoPath.setVisibility(View.VISIBLE);
        	
        	String imagePath=(String)v.getTag();
        	etPhotoPath.setText(imagePath);
        	etPhotoPath.setTag(imagePath);
    		

        }
   };
   
	private OnClickListener openGallery= new OnClickListener() {

        public void onClick(View v) {
        	   
        	   Intent intent = new Intent();
        	   intent.setAction(android.content.Intent.ACTION_VIEW);
        	   intent.setDataAndType(Uri.fromFile(new File((String)v.getTag())),"image/jpeg");
        	   baseContext.startActivity(intent);

        }
   };
   
   
   public void setPickImageGallery(OnClickListener pickGallery) {
		
		showGallery.setOnClickListener(pickGallery);
		
	}
	
   
   /*
    * 	When cross button is clicked. Close image actions...
    * 
    */
   
	private OnClickListener removePhotoActions= new OnClickListener() {

        public void onClick(View v) {
        	
        	hideButtons();
        	
        	int position=getImagePosition((String)rmPhotoButton.getTag());
        	((ImageView) llPhotosList.getChildAt(position)).setAlpha(255);
            	
        }
   };
   
	public void setAddPhotoEvent(OnClickListener onClick){
		
		photoButton.setOnClickListener(onClick);
		
	}
	
   
	private void removeSubCitation(String imagePath) {

		MultiPhotoControler multiPhotoCnt=new MultiPhotoControler(baseContext);
		multiPhotoCnt.removeMultiPhoto(imagePath);		
		
	}

	public LinearLayout getLlField() {
		return llField;
	}

	public HorizontalScrollView getImageScroll() {
		return imageScroll;
	}

	public ArrayList<String> getPhotoList() {
		
		if(PHOTO_FIELD_MODE==EDIT_MODE) return newPhotos;
		else return photoList;
		
	}

	public long getFieldId() {
		return field.getId();
	}
	
	public String getFieldName(){
		
		return field.getName();
		
	}

	public String getSecondLevelId() {
		return secondLevelId;
	}

	public void setSecondLevelId(String secondLevelId) {
		this.secondLevelId = secondLevelId;
	}

	public void setCitationId(long citationValueId) {

		this.citationId=citationValueId;
		
	}
	
	public long getCitationid(){
		
		return citationId;
		
	}

	





}
