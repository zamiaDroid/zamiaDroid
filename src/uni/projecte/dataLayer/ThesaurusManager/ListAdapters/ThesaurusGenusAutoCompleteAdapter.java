package uni.projecte.dataLayer.ThesaurusManager.ListAdapters;

import uni.projecte.R;
import uni.projecte.controler.ThesaurusControler;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public  class ThesaurusGenusAutoCompleteAdapter extends CursorAdapter{
		
	private AutoCompleteTextView auto;
	private String result="";
	private ThesaurusControler thCnt;
	

	public ThesaurusGenusAutoCompleteAdapter(Context context, Cursor c, AutoCompleteTextView auto, ThesaurusControler thCnt)
    {
            super(context, c);
            this.auto=auto;
            this.thCnt=thCnt;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {

	  	  	  		
	  			View tv= ((LinearLayout) view).getChildAt(1);
    		((TextView) tv).setText(cursor.getString(1));

			View image= ((LinearLayout) view).getChildAt(0);
	  			image.setVisibility(View.INVISIBLE);
          

    }

    @Override
    public String convertToString(Cursor cursor){
  	      	  
    	String result="";
		
  	  	if(cursor.getColumnCount()>1){
  		  
    	  	result=cursor.getString(1).replaceAll(" ", "");
  		  
  	  	}
	  	              
        return result;
        
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
            final LayoutInflater inflater = LayoutInflater.from(context);
            
            final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.thesaurus_row, parent, false);
            
            View tv= view.getChildAt(1);
            ImageView imgSyn=(ImageView) view.getChildAt(0);
            
            ((TextView) tv).setText(cursor.getString(1));
            ((TextView) tv).setTextColor(Color.BLACK);
            ((TextView) tv).setTextSize(16.0f);
            

            if(cursor.getCount()==1) {
          	  
          	  String proposed=convertToString(cursor);
          	  
          	  if(!proposed.equals(result) && imgSyn.isEnabled()){
          		  
          		  auto.setListSelection(0);
          		  auto.setText(proposed);          		  
          		  auto.setSelection(auto.length());
          		  
              	  result=proposed;
              	
              	  
          	  }
                      	
          	  
            }
            
            
            
            return view;
    }



  
  /**
   * 
   * It's called when the string in the Autocomplete View has changed. 
   * New Query in Background is proceeded for new typed Genus and Specie.
   * 
   * @return A Cursor with the list of possible Thesaurus Item
   * 
   */
  
  @Override
  public Cursor runQueryOnBackgroundThread(CharSequence constraint){                
	  
	  	if (constraint == null){
  
                  return  null;
                  
          }
          
          else{
        	  
        	    String[] spec=constraint.toString().split(" ");
        	    String selection;
        	    
     	    	
       	       selection = "Genus like \'" + spec[0]
 	               // +"%\' or Specie like \'" + constraint.toString()
 	                +"%\' ";

        	    Cursor c= thCnt.getGenusNextItems(selection);
        	    
        	    c.moveToFirst();
        	    
                return c;
          
          }
  }

  }