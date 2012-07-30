/*    	This file is part of ZamiaDroid.
*
*	ZamiaDroid is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*    	ZamiaDroid is distributed in the hope that it will be useful,
*    	but WITHOUT ANY WARRANTY; without even the implied warranty of
*    	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    	GNU General Public License for more details.
*
*    	You should have received a copy of the GNU General Public License
*    	along with ZamiaDroid.  If not, see <http://www.gnu.org/licenses/>.
*/

package uni.projecte.hardware.fileSystem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import uni.projecte.R;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FileManager {
	
	   private List<String> elements = null;
	   private ListView fileList;
	   private Context context;
	   
	   public void createFileList(Context c, ListView fileList){
		   
		   this.fileList=fileList;
		   this.context=c;
		   getRoot();
		   
	   }
	   

	
	
	  public static boolean isSdPresent() {

	      return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	
	  }

	   
	   private void fillFileList(File[] listFiles) {
			
	        elements = new ArrayList<String>();
	        elements.add(context.getString(R.string.root));
	        
	        for( File archivo: listFiles)
	            elements.add(archivo.getPath());
	       
	        ArrayAdapter<String> listaArchivos= new ArrayAdapter<String>(context, R.layout.row, elements);
	        
	        
	        fileList.setAdapter(listaArchivos);
	    
	
}
	   
	 
	      class XMLFilter implements FilenameFilter {
	    	  
	    	  
	  	              public boolean accept(File dir, String name) {
	  
	                      return (name.endsWith(".xml"));
	  
	              }
	  
	      }

	   
	   
	  private void getRoot() {
	       fillFileList(new File("/").listFiles());
	   } 

}
