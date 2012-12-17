package uni.projecte.ui.multiphoto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import uni.projecte.dataTypes.ProjectField;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MultiPhotoFieldForm {

	private long projId;
	
	private Context baseContext;
	
	/* Main container */
	private LinearLayout llField;
	private LinearLayout llPhotosList;
	private LinearLayout llActionButtons;

	/* View elements */
	private ImageButton rmPhotoButton;
	private ImageButton photoButton;
	
	private HorizontalScrollView imageScroll;
	
	/* ProjectField object */
	private ProjectField field;

	private ArrayList<String> photoList;
	
	private int IMAGE_MAX_SIZE;
	

	
		
	public MultiPhotoFieldForm(Context baseContext, long projId, ProjectField field, LinearLayout llField){
		
		this.baseContext=baseContext;
		this.field=field;
		this.llField=llField;
		this.projId=projId;
		
		photoList=new ArrayList<String>();
		
		imageScroll=new HorizontalScrollView(baseContext);
				
		
		llPhotosList= new LinearLayout(baseContext);
		
		imageScroll.addView(llPhotosList);
		
		
		
		llField.addView(imageScroll);
		
	    IMAGE_MAX_SIZE = baseContext.getResources().getDisplayMetrics().widthPixels;
	    
	    createLayoutButtons();


		
	}
	
	private void createLayoutButtons() {
		
		//buttons
		photoButton=(ImageButton) new ImageButton(baseContext);
		photoButton.setBackgroundResource(android.R.drawable.ic_menu_camera);
		   
		rmPhotoButton=(ImageButton) new ImageButton(baseContext);
		rmPhotoButton.setBackgroundResource(android.R.drawable.ic_input_delete);
		
		   
		//by default rmButton is gone
		rmPhotoButton.setVisibility(View.GONE);
		rmPhotoButton.setTag("remove");
		
		photoButton.setTag("add");


		llActionButtons=new LinearLayout(baseContext);
		//lButtons.setGravity(Gravity.RIGHT);

		LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, 0.0f);


		llActionButtons.addView(photoButton);
		llActionButtons.addView(rmPhotoButton);
		
		llField.addView(llActionButtons);
				
	}
	
	
	private void addButtons() {
		// TODO Auto-generated method stub
		
	}


	public void addPhoto(String imageFilePath) {

		File photos = new File(imageFilePath);
        Bitmap b = decodeFile(photos);
        int ratio= b.getWidth()/b.getHeight();
        b = Bitmap.createScaledBitmap(b, IMAGE_MAX_SIZE, (int)IMAGE_MAX_SIZE*ratio, true);
        
        ImageView newImage=new ImageView(baseContext);
    	LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llp.setMargins(5, 5, 5, 5);
        newImage.setImageBitmap(b);		
        newImage.setLayoutParams(llp);

		
        llPhotosList.addView(newImage,0);	
		
		
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
	
	
	public LinearLayout getLlField() {
		return llField;
	}

}
