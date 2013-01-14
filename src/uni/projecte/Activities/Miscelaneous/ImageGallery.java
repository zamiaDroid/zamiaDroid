package uni.projecte.Activities.Miscelaneous;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uni.projecte.R;
import uni.projecte.Activities.Citations.CitationEditor;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.PhotoControler;
import uni.projecte.controler.PreferencesControler;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.CitationControler;
import uni.projecte.dataLayer.dataStructures.ImageCache;
import uni.projecte.dataLayer.utils.PhotoUtils;
import uni.projecte.dataTypes.AttributeValue;
import uni.projecte.dataTypes.Utilities;
import uni.projecte.maps.overlays.MyTracksOverlay;
import uni.projecte.ui.CitationGallery;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImageGallery extends Activity{
	
	public static final int REMOVE_PHOTO = Menu.FIRST;
	private static final int SECONDARY_STORAGE=Menu.FIRST+2;
	
	public static final int BACK_FROM_EDIT = 1;
	
	private ArrayList<String> availableImageList;  

//	private Uri[] availableImageList;  
 //   private String[] mFiles=null;  
    private String[] photoInfos;
    private long[] photosIds;
    double IMAGE_MAX_SIZE= 800;
    private String projectNameWS;
    private int total;

    private CitationControler citCnt;
    private ProjectControler projCnt;
    private PreferencesControler prefCnt;
    private PhotoControler photoCnt;
    
    private TextView tvInfo;
    private TextView counter;
    private ImageButton editButton;
    private ImageButton showInfoButton;
    private CitationGallery g;
    
    private LinearLayout llPhotoInfo;
    
    private long projId;
    private Long citationId;
    private String[] citationInfoText;
    private Integer position;
    private String choosedPhotoPath;
    private String preSettedLoc;
    private int lastKnownPosition=-1;
    private boolean secondaryStorage=false;
    private String storagePath;
    
	private HashMap<String, String> fieldsLabelNames;
    private HashMap<String, Long> selectedPhotos;


	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
        Utilities.setLocale(this);
	    setContentView(R.layout.gallery);

        prefCnt=new PreferencesControler(this);

        citCnt=new CitationControler(this);
        projCnt=new ProjectControler(this);
        photoCnt=new PhotoControler(this);
        
 	   	tvInfo=(TextView)findViewById(R.id.imgValue);
 	   	editButton=(ImageButton)findViewById(R.id.btEditField);
 	   	showInfoButton=(ImageButton)findViewById(R.id.btShowInfo);
 	   	llPhotoInfo=(LinearLayout)findViewById(R.id.llPhotoInfo);
 	   	counter=(TextView)findViewById(R.id.tvGalImageCounter);
 	   	
	   
	    g = (CitationGallery) findViewById(R.id.gallery);
        
        
	    /* Intents 
	     * 
	     * @position: ImageGalery invoked from GaleryGrid at imagePosition
	     * @citationId: ImageGalery will move to citationId 
	     * 
	     */
        projId=getIntent().getExtras().getLong("id");
        position=getIntent().getExtras().getInt("position");
        citationId=getIntent().getExtras().getLong("citationId");
        choosedPhotoPath=getIntent().getExtras().getString("photoPath");
        
        
        projCnt.loadProjectInfoById(projId);
        fieldsLabelNames=projCnt.getProjectFieldsPair(projId);
        	
        projectNameWS=projCnt.getName();
        projectNameWS=projectNameWS.replace(" ", "_");
        
        IMAGE_MAX_SIZE = getResources().getDisplayMetrics().widthPixels;
 
        preSettedLoc=getIntent().getExtras().getString("idSelection");
        
        storagePath=getIntent().getExtras().getString("storagePath");
        
        //secondaryStorage=photoCnt.isSecondaryExternalStorageDefault(projId);
        
        if(preSettedLoc!=null){
        	
        	loadSelectedPhotos();
        	
        }
        
        
		loadGalleryImages();
		
		/*choose concrete photo position*/
		if(position!=null && position>-1) g.setSelection(position);
		
		/*choosing concrete photo*/
		else if(choosedPhotoPath!=null){
			
			int position=Utilities.findStringArrayList(availableImageList, PhotoUtils.getFileName(choosedPhotoPath));
			
			if(position>-1) g.setSelection(position);
			else Utilities.showToast(getString(R.string.photoNotLinked), this);
			
		}
		
		if(citationId!=null) selectPhoto(citationId);
		
		showInfoButton.setOnClickListener(showInfoListener);
		llPhotoInfo.setVisibility(View.VISIBLE);


	    g.setOnItemSelectedListener(new OnItemSelectedListener(){
	    	
	       public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
	    	   
	    	   	if(lastKnownPosition!=position) {
	    	   		
	    	   		loadPhotoInfo(position);
	    	   		
	    	   	}

	       }

	       public void onNothingSelected(AdapterView<?> arg0){

	       }
	       
	    });  
	    
		   g.setOnItemClickListener(new OnItemClickListener() {
        
			   public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
       
				   
				   	Intent viewPhIntent = new Intent(v.getContext(), uni.projecte.Activities.Miscelaneous.ImageView.class);
				       
		 			Bundle b = new Bundle();
		 			b.putString("photoPath", availableImageList.get(position));
		 			viewPhIntent.putExtras(b);
		 			
		 			b = new Bundle();
		 			b.putLong("projId", projId);
		 			viewPhIntent.putExtras(b);
		 							 			
		 			startActivity(viewPhIntent);  
				   
        
			   }

		}); 
	    

	}
	
	
	private void selectPhoto(long citationId) {
		
	}


	private void loadGalleryImages() {
					
		loadImageList(storagePath,selectedPhotos!=null);
		g.setAdapter(new ImageGalleryAdapter(this));
		    
	}
	


	private void loadSelectedPhotos() {

		PhotoControler photoCnt=new PhotoControler(this);
	
		selectedPhotos=new HashMap<String,Long>();
		
		String[] ids=preSettedLoc.split(":");
		
		selectedPhotos=photoCnt.getSelectedPhotos(projId, ids);
		
	}


	
	
    private OnClickListener showInfoListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
       	
        	if(llPhotoInfo.isShown()) llPhotoInfo.setVisibility(View.GONE);
    		else llPhotoInfo.setVisibility(View.VISIBLE);
        	
        }
    };
    


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, REMOVE_PHOTO, 0,getString(R.string.mRemovePhoto)).setIcon(android.R.drawable.ic_menu_save);
    	if(photoCnt.hasSecondaryStorage()) menu.add(0,SECONDARY_STORAGE, 0,getString(R.string.mChangeExternalStorageSec)).setIcon(android.R.drawable.ic_menu_view);

    	return super.onCreateOptionsMenu(menu);
    }
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		
		if(photoCnt.hasSecondaryStorage()){
		
			if(secondaryStorage)  menu.findItem(SECONDARY_STORAGE).setTitle(getString(R.string.mChangeExternalStorageSec));
	    	else  menu.findItem(SECONDARY_STORAGE).setTitle(getString(R.string.mChangeExternalStoragePrim));
		
		}

		return super.onPrepareOptionsMenu(menu);
		
	}

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
		switch (item.getItemId()) {
			
			case REMOVE_PHOTO:
			
				/*
				 * Update field value
				 *  
				 */
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
   			    
   			    builder.setMessage(getString(R.string.removeCitationPhoto))
   			           .setCancelable(false)
   			           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
   			               public void onClick(DialogInterface dialog, int id) {
   			            	   
   			        		removePhoto();
   							loadGalleryImages();
   			   	  		  	
   			               }
   			           })
   			           .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
   			               public void onClick(DialogInterface dialog, int id) {
   			                    dialog.cancel();
   			               }
   			           });
   			    
   				 AlertDialog alert = builder.create();
   				 alert.show();
				 			 
			break;
			
			case SECONDARY_STORAGE:
				
				if(secondaryStorage) secondaryStorage=false;
				
				else secondaryStorage=true;

				loadGalleryImages();

				break;
				
		}
	
		return super.onOptionsItemSelected(item);
	}
    

	
	
	private void removePhoto() {

		int pos=g.getSelectedItemPosition();
		
		long citationId=photosIds[pos];
		String photoPath=availableImageList.get(pos);

		if(citationId>0){
			
			CitationControler sC=new CitationControler(this);
			sC.removePhoto(citationId, photoPath);
			
		}
		else{
			
			File f= new File(photoPath);
			f.delete();
			
		}

				
	}
	
	   private Handler updateTaxonInfoHandler = new Handler() {

	        @Override
	        public void handleMessage(Message msg) {
	 
	        	 if(citationInfoText!=null) {	 
		            
	        		 	int position=msg.what;
	        		 
		            	photoInfos[position]=citationInfoText[0];
		    			tvInfo.setText(photoInfos[position]);
		    			
		            	photosIds[position]=citCnt.getCitationIdByPhoto(availableImageList.get(position));
		            	
		            	editButton.setVisibility(View.VISIBLE);
		            	//editButton.setId((int) photosIds[position]);
		            	editButton.setTag(availableImageList.get(position));
		            	
		            	editButton.setOnClickListener(new OnClickListener() {  
		 	            	
		 	            	public void onClick(View v) { 
		 	            		

		 						Intent intent = new Intent(v.getContext(), CitationEditor.class);
		 			       
		 					 			Bundle b = new Bundle();
		 					 			b.putString("rsName", projCnt.getName());
		 					 			intent.putExtras(b);
		 					 			
		 					 			b = new Bundle();
		 					 			b.putLong("id", projId);
		 					 			intent.putExtras(b);
		 					 			
		 					 			b = new Bundle();
		 					 			b.putString("rsDescription",projCnt.getThName());
		 					 			intent.putExtras(b);
		 					 			
		 					 			String photoPath=(String)v.getTag();
		 					 			
		 					 			b = new Bundle();
		 					 			b.putLong("idSample", selectedPhotos.get(PhotoUtils.getFileName(photoPath)));
		 					 			intent.putExtras(b);
		 					 			
		 					 		   startActivityForResult(intent, BACK_FROM_EDIT); 
		 	    	      	   	
		 	            	
		 	            	} 
		 	           	}
		 	            
		 	         );
		            	
		            }
		            else{
		            	
		            	editButton.setVisibility(View.GONE);
		            	tvInfo.setText(getString(R.string.photoWithoutInfo));

		            }
	        	 
	        	 
	                
	        	}
	        	
		     
	    };
	    
	    private Handler updateCounterHandler = new Handler() {

	        @Override
	        public void handleMessage(Message msg) {
	 
         		counter.setText((msg.what+1)+" / "+total);
	               
	       	}
	        	
		     
	    };
	    
	    


	private void loadPhotoInfo(final int position){

		lastKnownPosition=position;

		/* Info not loaded */
		if(photoInfos[position]==null){
			
			
			
			   new Thread(new Runnable() {

					public void run() {
						
						String photoPath=availableImageList.get(position);
						
						citationInfoText=citCnt.getCitationValues(selectedPhotos.get(PhotoUtils.getFileName(photoPath)),fieldsLabelNames);
						updateTaxonInfoHandler.sendEmptyMessage(position);
						updateCounterHandler.sendEmptyMessage(position);
		
					}
		              
	            	
		    	}).start();
		       
		       
	           
		}
		else if(photoInfos[position].equals(getString(R.string.photoWithoutInfo))){
			
			editButton.setVisibility(View.GONE);
        	tvInfo.setText(getString(R.string.photoWithoutInfo));
			updateCounterHandler.sendEmptyMessage(position);

		}
		else{
			
			tvInfo.setText(photoInfos[position]);
			updateCounterHandler.sendEmptyMessage(position);

        	editButton.setVisibility(View.VISIBLE);
        	           	
        	//editButton.setId((int) photosIds[position]);
        	
        	editButton.setTag(availableImageList.get(position));
        	
        	editButton.setOnClickListener(new OnClickListener() {  
	            	
	            	public void onClick(View v) { 
	            		

						Intent intent = new Intent(v.getContext(), CitationEditor.class);
			       
							Bundle b = new Bundle();
				 			b.putString("rsName", projCnt.getName());
				 			intent.putExtras(b);
				 			
				 			b = new Bundle();
				 			b.putLong("id", projId);
				 			intent.putExtras(b);
				 			
				 			b = new Bundle();
				 			b.putString("rsDescription",projCnt.getThName());
				 			intent.putExtras(b);
				 			
				 			String photoPath=(String)v.getTag();
				 			
				 			b = new Bundle();
				 			b.putLong("idSample", selectedPhotos.get(PhotoUtils.getFileName(photoPath)));
				 			intent.putExtras(b);
				 			
				 		   startActivityForResult(intent, 1); 
	    	      	   	
	            	} 
	            
        	} );
			

		}
		
		
	}
	
	private int getImageCount(){
		
		 File images = new File(photoCnt.getWorkingPhotoPath(projId)); 
		    
		    File[] imagelist = images.listFiles(new FilenameFilter(){  

		        public boolean accept(File dir, String name){
		        	
		            return ((name.endsWith(".jpg")) || (name.endsWith(".png"))) && (name.startsWith(projectNameWS)) ;
		        }  
	    
		    }); 
		    
		return imagelist.length;
		
	}
	
	private void loadImageList(String storagePath,boolean filtered) {

	    File images = new File(storagePath); 
	    
	    File[] imagelist = images.listFiles(new FilenameFilter(){  

	        public boolean accept(File dir, String name){
	        	
	            return ((name.endsWith(".jpg")) || (name.endsWith(".png"))) && (name.startsWith(projectNameWS)) ;
	        }  
    
	    });  
            
	    availableImageList= new ArrayList<String>();
        
	    
        for(int i= 0 ; i< imagelist.length; i++){
        	
            String fileName = imagelist[i].getName();  
            
            if(!filtered || (filtered && selectedPhotos.get(fileName)!=null)){
            	            	
            	availableImageList.add(storagePath+fileName); 
            } 

        }  
        
        photoInfos=new String[availableImageList.size()];
        photosIds=new long[availableImageList.size()];
        total=availableImageList.size();
        
	   /* mFiles = new String[imagelist.length];  
	    
        int filteredFiles=0;
	    
        for(int i= 0 ; i< imagelist.length; i++){  
        	
            mFiles[i] = imagelist[i].getAbsolutePath();  
            if(filtered && selectedPhotos.get(mFiles[i])!=null) filteredFiles++;

        }  
        
        if(!filtered) filteredFiles=mFiles.length;
        
        availableImageList = new Uri[filteredFiles];
        
        if(availableImageList.length==0){
        	
        	Toast.makeText(this,this.getString(R.string.noPhotoInProject), Toast.LENGTH_SHORT).show();
        	
        	finish();
        	
        }
        else{
        	
            photoInfos=new String[availableImageList.length];
            photosIds=new long[availableImageList.length];
            total=availableImageList.length;
            
            filteredFiles=0;

		      for(int i=0; i < mFiles.length; i++){
		    	  
		    	  if(filtered){
		    		  
		    		  if(selectedPhotos.get(mFiles[i])!=null) {
		    			  
		    			  availableImageList[filteredFiles] = Uri.parse(mFiles[i]); 
				    	  filteredFiles++;

		    		  }
		    		  
		    	  }
		    	  else{
		    		  
			    	  availableImageList[i] = Uri.parse(mFiles[i]);   

		    	  }
		    	  
		    	  
		      }
	
        }*/
        

    
	    
		
	}
	
	

	public class ImageGalleryAdapter extends BaseAdapter {
	    
		int mGalleryItemBackground;
	    private Context mContext;

	    private ImageCache imageCache;

	    public ImageGalleryAdapter(Context c) {
	       
	    	mContext = c;
	    	imageCache=new ImageCache(mContext);
	        
	    }

	    public int getCount() {
            return availableImageList.size();  
            
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        
		    
	    	  final float density = mContext.getResources().getDisplayMetrics().density;

	    	  LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	  View layOuter = inflater.inflate(R.layout.galleryitem, null);
	    	  View layInner = layOuter.findViewById(R.id.layInner);
	    	  
	    	 
	    	  LinearLayout.LayoutParams innerLP = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    	  innerLP.leftMargin = (int) (25 * density);
	    	  innerLP.rightMargin = (int) (25 * density);

	    	  layInner.setLayoutParams(innerLP);
	    	 
	    	  String uri=availableImageList.get(position);
	    	  
	    	  
	    	  Bitmap bm=imageCache.getBitmapFromMemCache(uri);
	    	  ImageView i = (ImageView) layInner.findViewById(R.id.galleryImage);
	    	  i.setAdjustViewBounds(true);
	    	  i.setTag(availableImageList.get(position));
	    	  
	    	  if(bm!=null){
	    		  
	    		  i.setImageBitmap(bm);	    		  
	    	  }
	    	  else{
	    	  
		    	  bm = decodeFile(new File(uri));
		    	  
		    	  if(bm!=null && bm.getWidth()>0){
		    		  
		    		  i.setImageBitmap(bm);
			    	  imageCache.addBitmapToMemoryCache(uri, bm);
		    		  
		    	  }
		    	  else{
		    		  
		    		  bm=createWrongImageBitmap((int) IMAGE_MAX_SIZE/4);
		    		  
		    		  i.setImageBitmap(bm);
			    	  imageCache.addBitmapToMemoryCache(uri, bm);
		    		  
		    	  }
		    	  
		    	
	    	  
	    	  }
	        
	  
	    	  return layOuter;

	    }
	}
	
	private Bitmap createWrongImageBitmap(int width){
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		Bitmap bitmap = Bitmap.createBitmap(width, width,Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		
	    paint.setColor(Color.argb(0xff, 0x88, 0xaa, 0x00));
		paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setTextSize(14);

		c.drawColor(Color.WHITE);
		c.drawText("Foto incorrecta", width/4, width/2, paint);
		
		    
       return bitmap;
		
	}

	
	private Bitmap decodeFile(File f){
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        try {
	        	
				fis.close();
				
			} catch (IOException e) {

				e.printStackTrace();
				
			}

	        int scale = 1;
	        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
	            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        try {
				fis.close();
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
	    } catch (FileNotFoundException e) {
	    }
	    return b;
	}
	
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            
	        case BACK_FROM_EDIT :
	        	
	        	//when the photo has been removed inside CitationEditor
	        	if(getImageCount()< total) loadGalleryImages();
	        	
	        break;
     
        }
     
    }

}
