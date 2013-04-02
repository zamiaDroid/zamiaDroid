package uni.projecte.dataLayer.utils;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

 
public class IoUtils {
        private static final int BUFFER_SIZE = 1024 * 2;
 
               
        public static boolean unpackZip(String fileZip,String destPath)
        {       
             InputStream is;
             ZipInputStream zis;
             try 
             {
                 is = new FileInputStream(fileZip);
                 zis = new ZipInputStream(new BufferedInputStream(is));          
                 ZipEntry ze;
                 java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                 byte[] buffer = new byte[1024];
                 int count;

                 while ((ze = zis.getNextEntry()) != null) 
                 {
                     // zapis do souboru
                     String filename = ze.getName();
                     Log.v("Backup", "(B1) Unzipping " + filename); 

                     FileOutputStream fout = new FileOutputStream(destPath + filename);

                     // cteni zipu a zapis
                     while ((count = zis.read(buffer)) != -1) 
                     {
                         baos.write(buffer, 0, count);
                         fout.write(baos.toByteArray());             
                         baos.reset();
                     }

                     fout.close();               
                     zis.closeEntry();
                 }

                 zis.close();
                 baos.close();
             } 
             catch(IOException e)
             {
                 e.printStackTrace();
                 return false;
             }

            return true;
        }
        
  
 
}
