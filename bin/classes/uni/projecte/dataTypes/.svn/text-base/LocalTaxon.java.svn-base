package uni.projecte.dataTypes;

import uni.projecte.dataLayer.utils.TaxonUtils;

public class LocalTaxon {
	
	private String taxon;
	private String cleanTaxon;
	private double latitude;
	private double longitude;
	private String timeStamp;
	private long citationId;
	
	
	public LocalTaxon(long id,String taxon, double latitude, double longitude, String timestamp){
		
		this.citationId=id;
		this.taxon=taxon;
		this.latitude=latitude;
		this.longitude=longitude;
		this.timeStamp=timestamp;
		this.cleanTaxon=TaxonUtils.removeAuthors(taxon);
		
	}
	
	public String getTaxon() {
		return taxon;
	}

	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getTimestamp() {
		return timeStamp;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setTimestamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getCitationId() {
		return citationId;
	}

	public void setCitationId(long id) {
		this.citationId = id;
	}

	public void setCleanTaxon(String cleanTaxon) {
		this.cleanTaxon = cleanTaxon;
	}

	public String getCleanTaxon() {
		return cleanTaxon;
	}


	

}
