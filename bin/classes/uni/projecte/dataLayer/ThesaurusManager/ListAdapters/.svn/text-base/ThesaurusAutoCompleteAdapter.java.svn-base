package uni.projecte.dataLayer.ThesaurusManager.ListAdapters;

import uni.projecte.R;
import uni.projecte.controler.ThesaurusControler;
import uni.projecte.dataLayer.utils.StringUtils;
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

public  class ThesaurusAutoCompleteAdapter extends CursorAdapter{
	
	private AutoCompleteTextView auto;
	private String result="";
	private ThesaurusControler thCnt;

	
  public ThesaurusAutoCompleteAdapter(Context context, Cursor c, AutoCompleteTextView auto, ThesaurusControler thCnt)  {
          super(context, c);
          this.auto=auto;
          this.thCnt=thCnt;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor)
  {
	  

 	 boolean acceptat=true;
 	 String icode=cursor.getString(5);
 	 String nameCode=cursor.getString(6);
 	 String authorEp=cursor.getString(4);
 	 
 	  if(icode!=null && nameCode!=null && !icode.equals("") && !nameCode.equals("")){
 	  
 		  acceptat=cursor.getString(5).equals(cursor.getString(6));
 	  
 	  }
 	  
  		if(cursor.getString(3).equals("")){
  			
  			result=cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(4);
  			
  		}
  		else{
  			
  			if(authorEp.equals("")) result=cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(8)+" "+cursor.getString(3)+" "+cursor.getString(7);
  			else result=cursor.getString(1)+" "+cursor.getString(2)+" "+authorEp+" "+cursor.getString(8)+" "+cursor.getString(3)+" "+cursor.getString(7);
  			
  		}
    
  		
  		View tv= ((LinearLayout) view).getChildAt(1);
  		View image= ((LinearLayout) view).getChildAt(0);
		View icodeTv= ((LinearLayout) view).getChildAt(2);

			
  		if(acceptat) image.setVisibility(View.VISIBLE);
  		else image.setVisibility(View.INVISIBLE);
  		
  		
        ((TextView) tv).setText(result);
        ((TextView)icodeTv).setText(icode);
        ((TextView)icodeTv).setVisibility(View.INVISIBLE);

  }

  @Override
  public String convertToString(Cursor cursor)
  {
		
	  String result="";
		
	  if(cursor.getColumnCount()>1){
	  
    	  	String gender=cursor.getString(1);
    	  	gender=gender.replaceAll(" ", "");
    		String specie=cursor.getString(2).replaceAll(" ", "");
    		specie=specie.replaceAll(" ", "");
    	  
    		String authorEp=cursor.getString(4);
    		String authorSubEp=cursor.getString(7);
    		
    	  	if(cursor.getString(3).equals("")){
	  			
    	  		if(authorEp.equals("")) result=gender+" "+specie;
    	  		else result=gender+" "+specie+" "+cursor.getString(4);
	  			
	  		}
	  		else{
	  			
	  			
	  			//create regExp tot fix...
	  			String subSpecie=cursor.getString(3).replaceAll(" ", "");
	  			
	  			if(authorEp.equals("") && authorSubEp.equals("")) result=gender+" "+specie+" "+cursor.getString(8)+" "+subSpecie;
	  			else if(authorSubEp.equals(""))	result=gender+" "+specie+" "+cursor.getString(4)+" "+cursor.getString(8)+" "+subSpecie;
	  			else if(authorEp.equals("")) result=gender+" "+specie+" "+cursor.getString(8)+" "+subSpecie+" "+cursor.getString(7);
	  			else result=gender+" "+specie+" "+cursor.getString(4)+" "+cursor.getString(8)+" "+subSpecie+" "+cursor.getString(7);
	  			
	  		}
    	  	
	  }

          
        return result;
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent)
  {
          final LayoutInflater inflater = LayoutInflater.from(context);
          
          final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.th_row, parent, false);
          
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
  public Cursor runQueryOnBackgroundThread(CharSequence constraint)
  {                if (constraint == null){
                  return  null;
                  
          }
          
          else{
        	  
        	  
        	  // Ammannia baccifera aegyptiaca
        	  // * baccifera aegyptiaca
        	  // * * ae
        	  // 
        	  
        	  String[] spec1=constraint.toString().split(" ");
        	  String[] spec=StringUtils.removeElements(spec1, "");
        	  
        	  String selection;
        	    
        	    if(spec.length>2){
        	    	
        	    	if(spec[0].equals("*")){

        	    		selection = "Specie like \'" + spec[1]
        	    		+"%\' and Subspecie like \'" + spec[2]
        	    		+"%\' ";
        	    		
        	    	}
        	    	else if(spec[1].equals("*")){
        	    		
        	    		selection = "Genus like \'" + spec[0]
        	    		+"%\' and Subspecie like \'" + spec[2]
        	    		+"%\' ";
        	    		
        	    	}
        	    	else{
        	    		
        	    		selection = "Genus like \'" + spec[0]
        	    		+"%\' and Specie like \'" + spec[1]
        	    		+"%\' and Subspecie like \'" + spec[2]
        	    		+"%\' ";
        	    		
        	    	}
        	    		
        	    
        	    	
        	    }	 
        	    else if(spec.length==2){
        	    	
        	    	if(spec[0].equals("*")){
        	    		
             	       selection = "Specie like \'" + spec[1]
             	                                          +"%\' ";

        	    	}
        	    	else if(spec[1].equals("*")){
        	    		
              	       selection = "Genus like \'" + spec[0]
              	                                          +"%\' ";

         	    	}
        	    	else{
        	    		
        	    		selection = "Genus like \'" + spec[0]
        	    		             +"%\' and Specie like \'" + spec[1]
        	    		              +"%\' ";
        	    		
        	    	}
        	    	
        	    	
        	    	
        	    }
        	    else{
        	    
        	       if(spec[0].equals("*")) spec[0]="";
        	    	
        	       selection = "Genus like \'" + spec[0]
 	               // +"%\' or Specie like \'" + constraint.toString()
 	                +"%\' ";

        	    }
        	    
        	    Cursor c= thCnt.getNextItems(selection);
        	    
        	    c.moveToFirst();
        	    
                return c;
          
          }
  }
  
}