package uni.projecte.ui.multiphoto;

import uni.projecte.dataTypes.ProjectField;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class PhotoFieldForm {

	protected Context baseContext;

	/* Main container */
	protected LinearLayout llField;
	
	protected long projId;
	
	/* ProjectField object */
	protected ProjectField field;
	
	protected View photoButton;
	
	protected int IMAGE_MAX_SIZE;

	

	public PhotoFieldForm(Context baseContext, long projId, ProjectField field, LinearLayout llField){
		
		this.baseContext=baseContext;
		this.field=field;
		this.llField=llField;
		this.projId=projId;
		
	    IMAGE_MAX_SIZE = baseContext.getResources().getDisplayMetrics().widthPixels;

		
	}


	public void clearForm() {

		
		
	}


	public void removePhoto() {

			
		
	}


	public void addPhoto(String photoPath) {

		
		
		
	}
	
	
	public void setAddPhotoEvent(OnClickListener onClick){
		
		photoButton.setOnClickListener(onClick);
		
	}
	
	
	
	protected OnClickListener viewPhoto= new OnClickListener() {

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
	



}
