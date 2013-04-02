package uni.projecte.dataLayer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	
	
	public static void deleteRecursive(File fileOrDirectory) {
	   
		if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            deleteRecursive(child);

	    fileOrDirectory.delete();
	}
	
	public static boolean moveFileToDir(File origin, File destFolder){
		
		File newFile=new File(destFolder.getAbsolutePath()+"/"+origin.getName());
		
		return origin.renameTo(newFile);
		
	}
	
	
	public static boolean copyFile(File origin, File dest) {
        

		try {

			FileInputStream in = new FileInputStream(origin);
			
			File destFile=new File(dest.getAbsolutePath()+"/"+origin.getName());
			
			FileOutputStream out = new FileOutputStream(destFile);
          
			byte[] buf = new byte[1024];
			int i = 0;

				while ((i = in.read(buf)) != -1) {
				
					out.write(buf, 0, i);
				}
		
          
			in.close();
			out.close();

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
        return true;
		
	}

	public static boolean copyFileToDir(File origin, File destFolder) {
        

		try {

			FileInputStream in = new FileInputStream(origin);
			FileOutputStream out = new FileOutputStream(destFolder);
          
			byte[] buf = new byte[1024];
			int i = 0;

				while ((i = in.read(buf)) != -1) {
				
					out.write(buf, 0, i);
				}
		
          
			in.close();
			out.close();

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
        return true;
		
	}

}
