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

package uni.projecte.Activities.Projects;

import uni.projecte.R;
import uni.projecte.R.layout;
import uni.projecte.R.string;
import uni.projecte.controler.ProjectControler;
import uni.projecte.dataTypes.Utilities;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;


	
public class ProjectManagement extends TabActivity {
	
	private TabHost mTabHost;
	private ProjectControler projCnt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   
        Utilities.setLocale(this);
	    setContentView(R.layout.tabtext);
	    
        int choosedTab=getIntent().getExtras().getInt("tab");
        projCnt=new ProjectControler(this);
        
        int numProj=projCnt.getProjCount();
        if(numProj>0) choosedTab=0;

	    
	    mTabHost = getTabHost();
	    CharSequence str;
	    
	    setDefaultTab(0);

        TabHost tabs = getTabHost();

        TabHost.TabSpec   tab = tabs.newTabSpec("research list");
        tab.setContent(new Intent().setClassName("uni.projecte","uni.projecte.Activities.Projects.ProjectList"));
        str = this.getString(R.string.projectListTabName);
        tab.setIndicator(str);
        tabs.addTab(tab);
        
        tab = tabs.newTabSpec("new research");
        tab.setContent(new Intent().setClassName("uni.projecte","uni.projecte.Activities.Projects.ProjectTemplateCreator"));
        str = this.getString(R.string.newProjectTabName);
        tab.setIndicator(str);
        tabs.addTab(tab);
              
        
        tab = tabs.newTabSpec("th management");
        tab.setContent(new Intent().setClassName("uni.projecte","uni.projecte.Activities.Thesaurus.ThesaurusManager"));
        str = this.getString(R.string.thListTab);
        tab.setIndicator(str);
        tabs.addTab(tab); 
        
        if(choosedTab==1){
        	
        	Toast.makeText(getBaseContext(), 
                    getString(R.string.noProjectNeedCreate), 
                    Toast.LENGTH_LONG).show();
        	
        }
        
	    mTabHost.setCurrentTab(choosedTab);
	    
	}
	
	
	
}
