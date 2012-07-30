package uni.projecte.dataLayer.RemoteDBManager;

import uni.projecte.dataLayer.utils.TaxonUtils;

public class RemoteTaxon {

	private String taxon;
	private String cleanTaxon;
	private String taxonId;
	
	public RemoteTaxon(String taxon, String taxonId){
		
		this.taxon=taxon;
		this.cleanTaxon=TaxonUtils.removeAuthors(taxon);
		this.taxonId=taxonId;
		
		
	}
	
	public String getTaxon() {
		return taxon;
	}
	public String getTaxonId() {
		return taxonId;
	}
	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}
	public void setTaxonId(String taxonId) {
		this.taxonId = taxonId;
	}

	public void setCleanTaxon(String cleanTaxon) {
		this.cleanTaxon = cleanTaxon;
	}

	public String getCleanTaxon() {
		return cleanTaxon;
	}

}
