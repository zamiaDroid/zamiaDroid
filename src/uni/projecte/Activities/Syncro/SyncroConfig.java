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



package uni.projecte.Activities.Syncro;

import uni.projecte.R;
import uni.projecte.dataLayer.CitationManager.Synchro.SyncCitationManager;
import uni.projecte.dataTypes.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SyncroConfig extends Activity {

	private EditText etUsername;
	private EditText etPassword;
	private TextView tvInfo;
	
	private Button btnLogin;

	private SyncCitationManager synchroManager;

	
	
	public void onCreate(Bundle savedInstanceState) {
		  
	       super.onCreate(savedInstanceState);
	        
	       Utilities.setLocale(this);
	       setContentView(R.layout.project_sync_cnf);
	       
	       synchroManager= new SyncCitationManager(this,"orca");
	       
	       btnLogin=(Button)findViewById(R.id.btnLogin);
	       btnLogin.setOnClickListener(doLogin);
	       
	       etUsername=(EditText)findViewById(R.id.etUsername);
	       etPassword=(EditText)findViewById(R.id.etPassword);
	       tvInfo=(TextView)findViewById(R.id.link_to_register);
	       
	       if(synchroManager.isConfigured()){
	    	   
	    	   etUsername.setText(synchroManager.getUser().getUsername());
	    	   etPassword.setText(synchroManager.getUser().getPassword());
	    	   
	    	   btnLogin.setVisibility(View.GONE);
	    	   tvInfo.setVisibility(View.GONE);
	    	   
	       }
	       
	       

	  }

	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	
			setResult(0);
			finish();
        
	        return false;
	        
        }
        
        return true;

    }
        
	
	 public OnClickListener doLogin = new OnClickListener() {

			public void onClick(View v) {

				boolean succcess=synchroManager.checkLogin(etUsername.getText().toString(),etPassword.getText().toString());
				
				if(!succcess) Utilities.showToast("Usuari incorrecte!", v.getContext());
				else {
					
					Utilities.showToast("Usuari correcte: "+etUsername.getText().toString(), v.getContext());
					setResult(1);
					finish();
				
				}
				
			}
		  
		};
	
}
