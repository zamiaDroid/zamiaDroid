package uni.projecte.ui.multiphoto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import uni.projecte.controler.MultiPhotoControler;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MultiPhotoFieldForm extends PhotoFieldForm {

	public static int CREATE_MODE = 1;
	public static int EDIT_MODE = 2;
	
	private int PHOTO_FIELD_MODE;
	
	
	/* Main container */
	private LinearLayout llPhotosList;
	private LinearLayout llActionButtons;

	/* View elements */
	private ImageButton rmPhotoButton;
	private ImageButton editOkButton;
	private ImageButton viewPhotoButton;

	
	private HorizontalScrollView imageScroll;
	
	private ArrayList<String> photoList;
	private ArrayList<String> newPhotos;
	
	private String secondLevelId;
	private long citationId;
	
	
	
		
	public MultiPhotoFieldForm(Context baseContext, long projId, ProjectField field, LinearLayout llField, int mode){
		
		super(baseContext,projId,field,llField);
		
		PHOTO_FIELD_MODE=mode;
	
		photoList=new ArrayList<String>();
		newPhotos=new ArrayList<String>();
		

		/* Layout resources */
		
		imageScroll=new HorizontalScrollView(baseContext);
		llPhotosList= new LinearLayout(baseContext);

		
		imageScroll.addView(llPhotosList);
		imageScroll.setTag(field.getName());

		
	    createLayoutButtons();
		llField.addView(imageScroll);
		
		if(PHOTO_FIELD_MODE==CREATE_MODE){

			secondLevelId=createSecondLevelIdentifier(field.getName());
			
		}
			
	}
	
	private void createLayoutButtons() {
		
		//buttons
		photoButton=(ImageButton) new ImageButton(baseContext);
		photoButton.setBackgroundResource(android.R.drawable.ic_menu_camera);
		   
		rmPhotoButton=(ImageButton) new ImageButton(baseContext);
		rmPhotoButton.setBackgroundResource(android.R.drawable.ic_menu_delete);
				
		viewPhotoButton=(ImageButton) new ImageButton(baseContext);
		viewPhotoButton.setBackgroundResource(android.R.drawable.ic_menu_view);
		
		editOkButton=(ImageButton) new ImageButton(baseContext);
		editOkButton.setBackgroundResource(android.R.drawable.ic_menu_close_clear_cancel);
		   
		//by default rmButton is gone
		rmPhotoButton.setVisibility(View.GONE);
		viewPhotoButton.setVisibility(View.GONE);
		editOkButton.setVisibility(View.GONE);
		
				
		photoButton.setTag(field.getName());
		
		viewPhotoButton.setOnClickListener(viewPhoto);
		editOkButton.setOnClickListener(removePhotoActions);
		rmPhotoButton.setOnClickListener(removePhoto);


		llActionButtons=new LinearLayout(baseContext);
		llActionButtons.setOrientation(1);

		LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 0.0f);

		llActionButtons.setLayoutParams(param);

		llActionButtons.addView(photoButton);
		llActionButtons.addView(rmPhotoButton);
		llActionButtons.addView(viewPhotoButton);
		llActionButtons.addView(editOkButton);
		
		llField.addView(llActionButtons);
				
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

		File photos = new File(imageFilePath);
        Bitmap b = decodeFile(photos);
         
        if(b!=null){
        
	        b= createScaledBitmap(b);
	        
	        ImageView newImage=new ImageView(baseContext);
	    	LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			llp.setMargins(5, 5, 5, 5);
	        newImage.setImageBitmap(b);		
	        newImage.setLayoutParams(llp);
	
	        newImage.setTag(imageFilePath);
	        
	        newImage.setOnClickListener(showPhotoActions);
	        
			llPhotosList.addView(newImage,0);	
	        
	        photoList.add(0, imageFilePath);
        
        }
  
		
	}
	
	public void addNewPhoto(String photoPath) {

		addPhoto(photoPath);
			        	
		newPhotos.add(0, photoPath);
			
		
	}
	
	public void setCitationData(List<String> list,String photoIdTag) {

		//photoList.addAll(list);
		
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
	
	private Bitmap createScaledBitmap(Bitmap decodedBitmap) {
		 
		int x= decodedBitmap.getWidth();
		int y = decodedBitmap.getHeight();
		
		if (x < y){
			
			x=decodedBitmap.getHeight();
			y=decodedBitmap.getWidth();
			
		}
		
        decodedBitmap = Bitmap.createScaledBitmap(decodedBitmap, x, y, true);
                
        return decodedBitmap;
        
	}

	private Bitmap decodeFile(File f) {
	    try {
	        // decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f), null, o);

	        // Find the correct scale value. It should be the power of 2.
	        final int REQUIRED_SIZE = 70;
	        int width_tmp = o.outWidth, height_tmp = o.outHeight;
	        int scale = 1;
	        while (true) {
	            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
	                break;
	            width_tmp /= 2;
	            height_tmp /= 2;
	            scale++;
	        }

	        // decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    }
	    catch (FileNotFoundException e) {
	    }
	    return null;
	}
	
	
	
	private Bitmap adjustOpacity(Bitmap bitmap, int opacity){
		
		Bitmap mutableBitmap = bitmap.isMutable() 
				? bitmap
				: bitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		Canvas canvas = new Canvas(mutableBitmap);
		int colour = (opacity & 0xFF) << 24;
		
		canvas.drawColor(colour, PorterDuff.Mode.DST_IN);
		
		return mutableBitmap;
	
	}
	
	private OnClickListener showPhotoActions= new OnClickListener() {

        public void onClick(View v) {
          		                	
        	if(rmPhotoButton.getVisibility()==View.VISIBLE) hideButtons();
        	else showButtons((String) v.getTag());
            	
        }
   };
   
   private void showButtons(String photoPath){
	   
	 	rmPhotoButton.setVisibility(View.VISIBLE);
    	rmPhotoButton.setTag(photoPath);
   	
    	      
    	editOkButton.setVisibility(View.VISIBLE);
    	editOkButton.setTag(photoPath);
    	
    	viewPhotoButton.setVisibility(View.VISIBLE);
    	viewPhotoButton.setTag(photoPath);
	   
   }
   
   private void hideButtons(){
	   
	   	rmPhotoButton.setVisibility(View.GONE);
	    
		editOkButton.setVisibility(View.GONE);
		
		viewPhotoButton.setVisibility(View.GONE);
	   
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
	
	private OnClickListener removePhotoActions= new OnClickListener() {

        public void onClick(View v) {
          		                	
        	hideButtons();
            	
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
