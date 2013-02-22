package uni.projecte.dataTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import uni.projecte.R;
import uni.projecte.controler.PreferencesControler;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.widget.Spinner;
import android.widget.Toast;

public class Utilities 
{	
	public static void showToast(CharSequence message, Context appContext)
    {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(appContext, message, duration);
		toast.show();
    }
	
	
	
    /*
     * This method sets the default spinner item using defValue parameter provided.
     * If the spinner doesn't contain the value, spinner is not modified. 
     * 
     */

	public static void setDefaultSpinnerItem(Spinner e, String defaultValue, String[] items){
    
    	int n=items.length;
    	boolean found=false;
    	int pos=-1;
    	int i;
    	
    	for(i=0; i<n && !found;i++){
    		
    		if (items[i].compareTo(defaultValue)==0){ found=true; pos=i;}
    		
    	}
    	
    	if(found) e.setSelection(pos);
    	
    	
    }
	
	public static int findString(String[] items, String item){
	    
    	int pos=-1;

		
		if(item!=null && !item.equals("")){
			
	    	int n=items.length;
	    	boolean found=false;
	    	int i;
	    	
	    	for(i=0; i<n && !found;i++){
	    		
	    		if (items[i].compareTo(item)==0){ found=true; pos=i;}
	    		
	    	}

	    	
		}
    	
    	return pos;

    	
    }
	
	public static int findStringArrayList(ArrayList<String> items, String item){
		
		int pos=-1;
		int i=0;
    	boolean found=false;

		
		Iterator<String> it=items.iterator();
		
		while(it.hasNext() && !found){
			
			String value=it.next();
			
			if(value.equals(item)) {
				
				found=true;
				pos=i;
			}
			else i++;
			
		}
		
		return pos;
		
	}
	
	
    public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader;
		StringBuilder sb = new StringBuilder();

		try {
			reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		
 	        String line = null;
	
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
         	        }
        return sb.toString();
    }


	

	/*public static String prettyUTM(String shortForm) {

		if(shortForm.length()>=7){
		
	    	String prettyString=shortForm.substring(0, 3)+" "+shortForm.substring(3, 5)+" "+shortForm.substring(5,shortForm.length()-5)+" "+shortForm.substring(shortForm.length()-5);
	    	return prettyString;

		}
		else return "";

	}*/
	

	
	  public static void setLocale(Context context) {

		  PreferencesControler pC=new PreferencesControler(context);
		  String localName=pC.getLang();
		  Locale locale = new Locale(localName);
	      Locale.setDefault(locale);
	      Configuration config = new Configuration();
	      config.locale = locale;
	      context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
	    
	        
		}
	  
	  
	  
	  public static String translateThTypeToCurrentLanguage(Context c, String englishName){
		  
		  String[] biocatNamesEnglish = c.getResources().getStringArray(R.array.thesaurusFilumsEnglish);
		  int pos=findString(biocatNamesEnglish, englishName);
		   	
		  if(pos>-1) {
			  
			  String[] biocatFilumsNames = c.getResources().getStringArray(R.array.thesaurusFilums);
			  
			  return biocatFilumsNames[pos];
			  
		  }
		  else return "";
		  
			
		  
		  
	  }
	  
	  
	  public static String translateThTypeToFilumLetter(Context c, String currentLanguageName){
		  
		  String[] biocatNamesEnglish = c.getResources().getStringArray(R.array.thesaurusFilums);
		  int pos=findString(biocatNamesEnglish, currentLanguageName);
		   	
		  if(pos>-1) {
			  
			  String[] biocatFilumsLetters = c.getResources().getStringArray(R.array.thesaurusFilumsLetters);
			  
			  return biocatFilumsLetters[pos];
			  
		  }
		  else return "";
		  
		  
	  }

	  
	  public static String translateThFieldType(Context c, String currentLanguageType){
		  
		  String[] thFieldsIds = c.getResources().getStringArray(R.array.thesaurusFields);
		  int pos=findString(thFieldsIds, currentLanguageType);
		   	
		  if(pos>-1) {
			  
			  String[] thFieldsTypesIds = c.getResources().getStringArray(R.array.thesaurusFieldsIds);
			  
			  return thFieldsTypesIds[pos];
			  
		  }
		  else return "";
		  
		  
	  }
	  
	  public static String translateThFieldsSepartor(Context c, String currentFieldSeparatorType){
		  
		  String[] fieldsSeparators = c.getResources().getStringArray(R.array.thesaurusFieldsSeparators);
		  int pos=findString(fieldsSeparators, currentFieldSeparatorType);
		   	
		  if(pos>-1) {
			  
			  String[] thFieldsSeparatorsIds = c.getResources().getStringArray(R.array.thesaurusFieldsSeparatorsUniqueNames);
			  
			  return thFieldsSeparatorsIds[pos];
			  
		  }
		  else return "";
		  
		  
	  }



	public static boolean isSdPresent() {

			      return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

	}



	public static boolean checkCoordinates(String locationValue, boolean utmData) {

		if(locationValue.length()>0){
			
			if(utmData){
				
				String numbers[] =locationValue.split("[A-Z]+");
				
				String zone=numbers[0];
				
				if(zone==null || Integer.valueOf(zone)<1 || Integer.valueOf(zone)>60){
					
					return false;
					
				}
				
				String letters="";
				
				if(numbers.length>1) letters=locationValue.substring(zone.length(),locationValue.length()-numbers[1].length());
				else letters=locationValue.substring(zone.length());
				
				if(letters.length()!=3 || letters.charAt(0)<'C' || letters.charAt(0)>'X' || letters.charAt(0)=='I' ){
					
					return false;
					
				}
				
				
			}
			else{
				
				String numbers[]=locationValue.split(" ");
				
				if(numbers.length!=2) return false;
				
				if(!isNum(numbers[0]) || !isNum(numbers[1])) return false;
				
				
			}
			
		}

		return true;
	}
	
	public static boolean isNum(String s) {
		try {
			Double.parseDouble(s);
		}
			catch (NumberFormatException nfe) {
				return false;
		}
		return true;
		}



	public static String translateLangBiocat(String lang) {

		String transLag="cat";
		
		if(lang.equals("ca")) transLag="cat";
		else if (lang.equals("es")) transLag="cas";
		else if (lang.equals("en")) transLag="ang";
		else if (lang.equals("fr")) transLag="fra";

		return transLag;
	}
	
	public static boolean availableInternetConnection(Context baseContext){
		
		ConnectivityManager connectivityManager =  (ConnectivityManager)baseContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		if(connectivityManager!=null) return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
		else return false;
	}



	public static List<String> splitToArrayList(String photos) {
	      
		String[] splitted=photos.split("; ");
		
		List<String> wordList = Arrays.asList(splitted);  

		return wordList;
	}


	  
	
	
}
