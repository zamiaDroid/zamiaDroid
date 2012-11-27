package uni.projecte.dataLayer.CitationManager.Xflora;

import uni.projecte.dataLayer.utils.TaxonUtils;
import uni.projecte.dataTypes.TaxonElement;

public class XfloraTags {

	/* ASCII code's list for Xflora tags */
	/* More info: http://biodiver.bio.ub.es/biocat/pdf/Manual_Xflora.pdf */
	
	private static String CIT_INI="0219";
	private static String SPEC_AUTHOR="018";
	private static String RANK_SUBSP="0220";
	private static String SUBSP_AUTHOR="023";
	private static String RANK_INF="0223";
	private static String INF_AUTHOR="024";
	
	public static String NATURENESS="015";
	
	private static String CF_SP_EP="0168";
	private static String CF_SUB_SP="0156";
	private static String CF_INFERIOR="0155";
	private static String CF_GEN="063";
	private static String CF_SENSULATO="0225";
	private static String CF_GRUP="042";

		
	public static String FIELDS_START="001";
	private static String DATE_START="0195";
	private static String DATE_END="0180";
	private static String COMMENT_START="0174";
	private static String COMMENT_END="0175";
	private static String CIT_AUTHOR="0221";
	
	
	public String createTaxonLine(String taxonName){
		
		//separar autor
		String taxonLine="";
		
		TaxonElement taxElem=TaxonUtils.mapThesaurusElement(taxonName);
		
		if(taxElem.getInfraspecificRank().equals("")){
			
			taxonLine =	taxElem.getGenus()+" "+ taxElem.getSpecificEpithet() +" "+
						"{"+SPEC_AUTHOR+"}" + taxElem.getSpecificEpithetAuthor();
						
		}
		else if(taxElem.getInfraspecificRank().equals("subsp.")){
			
			taxonLine =taxElem.getGenus()+" "+ taxElem.getSpecificEpithet() +" "+
					"{"+SPEC_AUTHOR+"}" + taxElem.getSpecificEpithetAuthor()+" "+
					"{"+RANK_SUBSP+"}" + taxElem.getInfraspecificEpithet()+" "+
					"{"+SUBSP_AUTHOR+"}" + taxElem.getInfraspecificEpithetAuthor();
			
		}
		else if(taxElem.getInfraspecificRank().equals("form.")){
			
			taxonLine =	taxElem.getGenus()+" "+ taxElem.getSpecificEpithet() +" "+
					"{"+SPEC_AUTHOR+"}" + taxElem.getSpecificEpithetAuthor()+" "+
					"{"+RANK_INF+"}" +"f_"+ taxElem.getInfraspecificEpithet()+" "+
					"{"+INF_AUTHOR+"}" + taxElem.getInfraspecificEpithetAuthor();
			
		}
		else if(taxElem.getInfraspecificRank().equals("subvar.")){
			
			taxonLine =	taxElem.getGenus()+" "+ taxElem.getSpecificEpithet() +" "+
					"{"+SPEC_AUTHOR+"}" + taxElem.getSpecificEpithetAuthor()+" "+
					"{"+RANK_INF+"}" +"s_"+ taxElem.getInfraspecificEpithet()+" "+
					"{"+INF_AUTHOR+"}" + taxElem.getInfraspecificEpithetAuthor();
			
		}
		else if(taxElem.getInfraspecificRank().equals("var.")){
			
			taxonLine =	taxElem.getGenus()+" "+ taxElem.getSpecificEpithet() +" "+
					"{"+SPEC_AUTHOR+"}" + taxElem.getSpecificEpithetAuthor()+" "+
					"{"+RANK_INF+"}" + taxElem.getInfraspecificEpithet()+
					"{"+INF_AUTHOR+"}" + taxElem.getInfraspecificEpithetAuthor();
			
		}
	
		
		return taxonLine;
		
	}
	
	public String getCitationInit(){
		
		return "{"+CIT_INI+"}";
		
	}
	
	public String createNaturenessLine(String naturenessValue){
		
		return "{"+NATURENESS+"}"+" "+naturenessValue+"\n";
		
	}

	public String getUTMCod(String utmShortForm) {
		
		return utmShortForm;
		
		
	}

	public String getDateCod(String xfloraDate) {

		return "{"+DATE_START+"}"+xfloraDate+"{"+DATE_END+"}";
		
	}

	public String getObservationAuthor(String value) {
		
		if(value.equals("")) return "";
		else return "{"+CIT_AUTHOR+"}"+value+"{"+CIT_AUTHOR+"}";
	}

	public String getAltitude(String value) {
		
		return value+" "+"m";
		
	}
	
	public String getObservationComment(String comment){
		
		if(comment.equals("")) return "";
		else return "{"+COMMENT_START+"}"+comment+"{"+COMMENT_END+"}";
		
	}

	public String getSurenessCod(String sureness) {

		String surenessCode="";
		
		if(sureness.equals("OK")){
						
		}
		else if(sureness.equals("Sp. confer")){
			
			surenessCode="{"+CF_SP_EP+"}";
			
		}
		else if(sureness.equals("Gen. confer")){
			
			surenessCode="{"+CF_GEN+"}";
			
		}
		else if(sureness.equals("Infrasp. confer")){
			
			surenessCode="{"+CF_SUB_SP+"}";
			
		}
		else if(sureness.equals("Grup")){
			
			surenessCode="{"+CF_GRUP+"}";
			
		}
		
		else if(sureness.equals("Sensulato")){
			
			sureness="{"+CF_SENSULATO+"}";
			
		}

		
		return surenessCode;
	}

	public String getFieldsInit() {

		return "{"+FIELDS_START+"}";
		
	}
	
	
	
	
	
}
