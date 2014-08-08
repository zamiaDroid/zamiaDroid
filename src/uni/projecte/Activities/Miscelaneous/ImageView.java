package uni.projecte.Activities.Miscelaneous;

import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.controler.MultiPhotoControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.CitationPhoto;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.ui.zoomImage.TouchImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ImageView extends Activity {

    private ProjectControler projCnt;	
    private MultiPhotoControler photoCnt;
        
	private long projId=-1;
	private String photoPath;
	private String photoLabel;

	private HashMap<String, String> fieldsLabelNames;
	
	
	public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.field_imageview);

    	LinearLayout ll=(LinearLayout) findViewById(R.id.llTouchImage);
    	TextView thPhoto=(TextView) findViewById(R.id.tvPhotoInfo);
    	TouchImageView tIv=new TouchImageView(this);

    	/* Activity Intents */
        photoPath=getIntent().getExtras().getString("photoPath");
        projId=getIntent().getExtras().getLong("projId");
        photoLabel=getIntent().getExtras().getString("photoLabel");

        /* Data Controllers */
        projCnt=new ProjectControler(this);
        photoCnt= new MultiPhotoControler(this);
        
        projCnt.loadProjectInfoById(projId);
        fieldsLabelNames=projCnt.getProjectFieldsPair(projId);
  	   	
        int height=getResources().getDisplayMetrics().heightPixels;
        
		Bitmap bm = PhotoUtils.decodeBitmap(photoPath,height, true);
		
		if(photoPath!=null && bm!=null){
			
			if(photoLabel==null){
			
				CitationPhoto citationPhoto=photoCnt.getCitationByPhotoPath(photoPath, true, fieldsLabelNames);
			
				if(citationPhoto!=null) photoLabel=citationPhoto.getLabel();
				else photoLabel=getString(R.string.photoNotLinked);
			
			}
			
			thPhoto.setText(photoLabel);
			
			tIv.setImageBitmap(bm);
	    	ll.addView(tIv);

		}
		else{
			
			Utilities.showToast(getString(R.string.photoCantBeFound), this);
			finish();
			
		}
		
	}

	
}
