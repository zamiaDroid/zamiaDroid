package uni.projecte.ui.multiphoto;

import java.io.File;
import java.util.Date;

import uni.projecte.dataTypes.ProjectField;
import uni.projecte.dataTypes.Utilities;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class PhotoFieldForm {

	private Context baseContext;
	
	/* View elements */
	private ImageButton rmPhotoButton;
	private ImageButton photoButton;
	
	private EditText etPhotoPath;
	private ImageView photoView;
	
	/* Main container */
	private LinearLayout llField;
	private LinearLayout lPhoto;
	private LinearLayout lButtons;

	private long projId;
	
	/* ProjectField object */
	private ProjectField field;
	
	private static int PHOTO_SAMPLE_SIZE=5;
	

	public PhotoFieldForm(Context baseContext, long projId, ProjectField field, LinearLayout llField){
		
		this.baseContext=baseContext;
		this.field=field;
		this.llField=llField;
		this.projId=projId;
		
		
		photoView=(ImageView) new ImageView(baseContext);
		etPhotoPath= (EditText) new EditText(baseContext);
		
		createLayoutButtons(); 
	
		setPhotoPathAttributes();
		 
		addViewsToLayout();
				
		llField.addView(lButtons);
		   
		//Show photo Event
		photoView.setOnClickListener(viewPhoto);

	
	}


	private void addViewsToLayout() {

		lPhoto=new LinearLayout(baseContext);
		lPhoto.setOrientation(LinearLayout.VERTICAL);
		lPhoto.setGravity(Gravity.CENTER);

		lPhoto.addView(etPhotoPath);
		lPhoto.addView(photoView);
		
	}


	private void createLayoutButtons() {
		
		//buttons
		photoButton=(ImageButton) new ImageButton(baseContext);
		photoButton.setBackgroundResource(android.R.drawable.ic_menu_camera);
		   
		rmPhotoButton=(ImageButton) new ImageButton(baseContext);
		rmPhotoButton.setBackgroundResource(android.R.drawable.ic_input_delete);
		
		   
		//by default rmButton is gone
		rmPhotoButton.setVisibility(View.GONE);
		rmPhotoButton.setTag(field.getName());
		
		photoButton.setTag(field.getName());


		lButtons=new LinearLayout(baseContext);
		lButtons.setGravity(Gravity.RIGHT);

		LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, 0.0f);

		lButtons.setLayoutParams(param);

		lButtons.addView(photoButton);
		lButtons.addView(rmPhotoButton);
		
				
	}
	
	public void removePhoto(){
		
		etPhotoPath.setText("");
           	  
    	photoView.setVisibility(View.GONE);
	 	etPhotoPath.setVisibility(View.GONE);

    	rmPhotoButton.setVisibility(View.GONE);
    	
    	photoButton.setVisibility(View.VISIBLE);

		
	}


	private void setPhotoPathAttributes() {

		  
	/*	etPhotoPath.setLayoutParams(new LayoutParams
			        (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.
			                WRAP_CONTENT));*/
		   		   
		 etPhotoPath.setId((int)field.getId());
		 etPhotoPath.setTag(field.getName());
		   				   
		   
		 etPhotoPath.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		  
		 etPhotoPath.setEnabled(false);	
		 
		 etPhotoPath.setVisibility(View.GONE);
		 
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	                LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT, 1.0f);
	      
		etPhotoPath.setLayoutParams(param);

	}
	
	public void clearForm() {
		
		photoView.setVisibility(View.GONE);
     	etPhotoPath.setVisibility(View.GONE);

    	photoButton.setVisibility(View.VISIBLE);
    	rmPhotoButton.setVisibility(View.GONE);		
    	
    	
	}
	
	public void addPhoto(String photoPath) {
	
		etPhotoPath.setText(photoPath);
		photoView.setTag(photoPath);

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(5, 5, 5, 5);
		photoView.setLayoutParams(llp);


		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = PHOTO_SAMPLE_SIZE;

		Bitmap PhotoFromCamera = BitmapFactory.decodeFile(photoPath, options );

		MediaStore.Images.Media.insertImage(baseContext.getContentResolver(), PhotoFromCamera, photoPath, "");

		photoView.setImageBitmap(PhotoFromCamera);
		photoView.setScaleType(ScaleType.CENTER_CROP);

		photoButton.setVisibility(View.GONE);
		
		rmPhotoButton.setVisibility(View.VISIBLE);
		photoView.setVisibility(View.VISIBLE);
		etPhotoPath.setVisibility(View.VISIBLE);
		
		photoView.invalidate();
		
		
		
	}
	
	
	private OnClickListener viewPhoto= new OnClickListener() {

        public void onClick(View v) {
          		                	
        	Intent viewPhIntent = new Intent(v.getContext(), uni.projecte.Activities.Miscelaneous.ImageView.class);
		       
 			Bundle b = new Bundle();
 			b.putString("photoPath", v.getTag().toString());
 			viewPhIntent.putExtras(b);
 			
 			b = new Bundle();
 			b.putLong("projId", projId);
 			viewPhIntent.putExtras(b);
 							 			
 			baseContext.startActivity(viewPhIntent);  
        	
        }
   };
   


	public EditText getEtPhotoPath() {
		return etPhotoPath;
	}


	public ImageView getPhotoView() {
		return photoView;
	}
	
	public void setRemoveEvent(OnClickListener onClick){
		
		rmPhotoButton.setOnClickListener(onClick);
		
	}
	
	public void setAddPhotoEvent(OnClickListener onClick){
		
		photoButton.setOnClickListener(onClick);
		
	}


	public LinearLayout getLlField() {
		return llField;
	}


	public LinearLayout getlPhoto() {
		return lPhoto;
	}





	

	
	
}
