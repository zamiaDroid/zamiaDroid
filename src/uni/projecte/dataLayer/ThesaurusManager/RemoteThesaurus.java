package uni.projecte.dataLayer.ThesaurusManager;

public class RemoteThesaurus {

	
	private String thId;
	private String thSource;
	private String desc;
	private String lastUpdate;
	private String url;
	
	
	public RemoteThesaurus(String thId, String thSource, String desc,String lastUpdate,String url) {
		
		this.thId = thId;
		this.thSource = thSource;
		this.desc = desc;
		this.lastUpdate = lastUpdate;
		this.url=url;
		
	}
	
	
	public String getThId() {
		return thId;
	}
	public String getThSource() {
		return thSource;
	}
	public String getDesc() {
		return desc;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setThId(String thId) {
		this.thId = thId;
	}
	public void setThSource(String thSource) {
		this.thSource = thSource;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

}
