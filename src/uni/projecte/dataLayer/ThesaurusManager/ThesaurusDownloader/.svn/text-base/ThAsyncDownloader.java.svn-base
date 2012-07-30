package uni.projecte.dataLayer.ThesaurusManager.ThesaurusDownloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uni.projecte.R;
import uni.projecte.dataTypes.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


	public class ThAsyncDownloader extends AsyncTask<String, String, String> {
    
		private ProgressDialog mProgressDialog;
		public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
		private Context context;
		private Handler handler;

	   
	   public ThAsyncDownloader(Context context,Handler handler){
		   
		   	this.context=context;
		   	this.handler=handler;
		   	
	        mProgressDialog = new ProgressDialog(context);
	        mProgressDialog.setMessage(context.getString(R.string.downloadingTh));
	        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        mProgressDialog.setCancelable(false);
	        mProgressDialog.show();

	   }

	   
    @Override
	protected void onPreExecute() {
    	
        super.onPreExecute();
        mProgressDialog.show();
    
    }

    @Override
	protected String doInBackground(String... aurl) {
        
    	int count;
    	String fileName="";
        
        // aurl[0] -> url
    	// aurl[1] -> fileDest
    	// aurl[2] -> optionalFile
 
        try {

            URL url = new URL(aurl[0]);
            
        	Log.i("Thesaurus","Downloading TH: "+url);
        	
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setInstanceFollowRedirects( false );
            
            conexion.setUseCaches (false);
            conexion.setDoInput(true);
            conexion.setDoOutput(true);

           // filumLetter=aurl[2];
            
            conexion.setRequestMethod("GET");
            conexion.connect();
    
            //pirula que s'ha d'arreglar
            
            int lenghtOfFile = 1835632;
            int connexionContentLenght= conexion.getContentLength();
            
            
            if(connexionContentLenght>0) {
            	
            	lenghtOfFile=connexionContentLenght;
                fileName = url.getFile().substring( url.getFile().lastIndexOf('/')+1, url.getFile().length() );
            	
            }
            else{
                
                String header=conexion.getHeaderField("Content-Disposition");
                
                if(header!=null) {
                	
                	int position= header.indexOf("filename=");
                    fileName=header.substring(position+9);

                }
                else fileName=aurl[2];
            	
            }
            
            
          
            
            mProgressDialog.setMax(lenghtOfFile);
           
            InputStream input = new BufferedInputStream(url.openStream());
            
            File f=new File(aurl[1]+fileName);
            if (!f.exists()){
         	   
            		f.createNewFile();
            	
            }

            OutputStream output = new FileOutputStream(aurl[1]+fileName);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
            	
                total += count;
                publishProgress(""+(int)total);

                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            
            return fileName;
            
            
            
        } catch (IOException e) {
        	
            Log.d("Thesaurus", "Action ->ThDownload Error->:Host Unresearcheable");
            Utilities.showToast("Host Unresearcheable", context);
        	
        }
        catch (Exception e) {
        	
        	e.printStackTrace();
        	
        	
       
        }
        return null;

    }
    @Override
	protected void onProgressUpdate(String... progress) {
         Log.d("ANDRO_ASYNC",progress[0]);
         
         mProgressDialog.setProgress(Integer.parseInt(progress[0]));
         
    }

    @Override
	protected void onPostExecute(String fileName) {
    	
        mProgressDialog.dismiss();
        
        if(fileName==null){
     	  
			Utilities.showToast(context.getString(R.string.noThConnection), context);
     	   
        }
        else{
     	  
        	if(handler!=null){
        	    
        	    Bundle b=new Bundle();
        	    b.putString("filePath",fileName);
        	    
        	    Message msg = new Message();
        	    msg.setData(b);
        	    handler.sendMessage(msg);

        	}
     	        	   
        }
   
        
        
    }


}