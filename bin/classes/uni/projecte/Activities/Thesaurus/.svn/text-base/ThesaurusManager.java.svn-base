package uni.projecte.Activities.Thesaurus;

import uni.projecte.R;
import uni.projecte.R.id;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.ProjectControler;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.ThesaurusManager.ThesaurusListAdapter;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;



public class ThesaurusManager extends Activity {
	
	private ThesaurusControler thCnt;
	private ProjectControler projCnt;
	
	private ImageButton btImportRemoteTh;
	private ImageButton btImportPlainTh;
	
	private ListView lvThesaurusList;
	private ThesaurusListAdapter thListAdap;
	
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
     
        Utilities.setLocale(this);
        setContentView(R.layout.thlistnew);
        
        thCnt=new ThesaurusControler(this);
        projCnt=new ProjectControler(this);

        btImportRemoteTh = (ImageButton)findViewById(R.id.btImportRemoteTh);
        btImportRemoteTh.setOnClickListener(importRemoteThListener);
        btImportRemoteTh.setBackgroundResource(android.R.drawable.ic_menu_add);
        
        btImportPlainTh = (ImageButton)findViewById(R.id.btImportPlainTh);
        btImportPlainTh.setOnClickListener(importPlainThListener);
        btImportPlainTh.setBackgroundResource(android.R.drawable.ic_menu_save);
        
        lvThesaurusList=(ListView)findViewById(R.id.lvThesaurusList);
        
        fillThList();
       
    }
    
    
    private void fillThList(){
    	
        thListAdap=new ThesaurusListAdapter(this, thCnt.fetchAllTh(),removeThListener);
        lvThesaurusList.setAdapter(thListAdap);
        
    }
    
    
		private OnClickListener removeThListener = new OnClickListener()
	    {
	        public void onClick(final View v){                           	       	
	    	    
				final String name = (String) v.getTag();
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	
				builder.setMessage(R.string.deleteThQuestion)
						.setCancelable(false)
						.setPositiveButton(R.string.yes,
								new DialogInterface.OnClickListener() {
							
									public void onClick(DialogInterface dialog,int id) {
	
										int thUsedBy=thCnt.removeTh(name);
										
										if(thUsedBy > 1) {
										
											String text = String.format(v.getResources().getString(R.string.thUsedBySeveralProj), thUsedBy);
											Toast.makeText(v.getContext(),text,Toast.LENGTH_SHORT).show();
	
										} 
										else if(thUsedBy ==1){
											
											String projName=projCnt.getProjNameByTh(name);
											String text = String.format(v.getResources().getString(R.string.thUsedByOneProj), projName);
											Toast.makeText(v.getContext(),text,Toast.LENGTH_SHORT).show();
											
										}
										else {
	
											fillThList();
	
										}
	
									}
								})
						.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
									
								public void onClick(DialogInterface dialog,int id) {
	
										dialog.cancel();
	
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
	    	       	
	    	  }
	      
	    };
		
		
	private OnClickListener importRemoteThListener = new OnClickListener()
    {
        public void onClick(View v)
        {                        
        	
        	Intent intent = new Intent(getBaseContext(), ThesaurusRemoteImport.class);
        	startActivityForResult(intent,0);
        	
    		
        }
    };
    
    
	private OnClickListener importPlainThListener = new OnClickListener()
    {
        public void onClick(View v){                        
        	    	
          	Intent intent = new Intent(getBaseContext(), ThesaurusImport.class);
        	startActivityForResult(intent,0);
        	
        }
    };
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        
        if(intent!=null){
        	
        	
        switch(requestCode) {
        	case 0 :
        	
        		fillThList();
            
        		break;
            
        	case 1 :
        	
        	
            break;


        }
    }
    
   }
	

}
