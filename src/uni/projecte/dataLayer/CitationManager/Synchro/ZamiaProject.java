package uni.projecte.dataLayer.CitationManager.Synchro;

import com.google.gson.annotations.Expose;

public class ZamiaProject {

	@Expose
    private String project_name;

	@Expose
	private String user;

	@Expose	   
    private String mod_date;

	@Expose
    private String syncro_date;
    
	@Expose
    private int count_all;
	
	@Expose
    private int count_unsynchro;
    
    
    public String getSyncro_date() {
        return syncro_date;
    }

    public void setSyncro_date(String syncro_date) {
        this.syncro_date = syncro_date;
    }

    public int getCount_all() {
        return count_all;
    }

    public void setCount_all(int count_all) {
        this.count_all = count_all;
    }


    public int getCount_unsynchro() {
        return count_unsynchro;
    }

    public void setCount_unsynchro(int count_unsynchro) {
        this.count_unsynchro = count_unsynchro;
    }
    
    
    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMod_date() {
        return mod_date;
    }

    public void setMod_date(String mod_date) {
        this.mod_date = mod_date;
    }

    
}