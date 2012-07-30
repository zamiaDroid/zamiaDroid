package uni.projecte.dataLayer.RemoteDBManager.utils;

import android.util.Log;

public class GbifFilumConverter {
	
	//http://data.gbif.org/species/browse/resource/1/
	
		private static String[] Flora={"103","61","11","78","12","49","101","60","59"};
		private static String[] Bryophytes={"35","9","13"};
		
		// el 109 i el 41 s'han de determinar amb més criteri
		private static String[] Algae={"100","36","102","37","104","106","69","70","98","109","41","40"};
		private static String[] Fungi={"5", "33","32","92"};
		private static String[] Lichens={""};
		
		private static String[] Vertebrates={"44"};
	
			private static String[] Aves={"212"};
			private static String[] Actinopterygii={"204"};
			private static String[] Amphibia={"131"};
			private static String[] Mammalia={"359"};
			private static String[] Cephalaspidomorphi={"239"};
			private static String[] Reptilia={"358"};
			
		private static String[] Invertebrates={"97","10","39","72","56","71","67","16","42","38","110","53","114","55","43",
				"51","45","57","50","20","15","22","77","75","21","52","66","65","64","63","62","19","76","108","105","91","74","14"};
		
		private static String[] Arthropods={"54"};
		
		private static String connectionParam="&taxonconceptkey=";
	
		
		
		/*
		 * 
		 * 
		 */
		
		public static String getGbifFilumCorresondance(String filum){
			
			Log.i("BD",filum);
			
			return translateFilumString(getFilumList(filum));
			
		}
		
		public static String[] getFilumList(String filum){
			
			if(filum.equals("Flora")) return Flora;
			
			else if(filum.equals("Bryophytes"))	return Bryophytes;
			else if(filum.equals("Algae")) return Algae;
			else if(filum.equals("Fungi")) return Fungi;
			else if(filum.equals("Lichens")) return Lichens;
			else if(filum.equals("Vertebrates")) return Vertebrates;
				else if(filum.equals("Aves")) return Aves;
				else if(filum.equals("Actinopterygii")) return Actinopterygii;
				else if(filum.equals("Amphibia")) return Amphibia;
				else if(filum.equals("Mammalia")) return Mammalia;
				else if(filum.equals("Cephalaspidomorphi")) return Cephalaspidomorphi;
				else if(filum.equals("Reptilia")) return Reptilia;
			else if(filum.equals("Invertebrate")) return Invertebrates;
			else if(filum.equals("Arthropods")) return Arthropods;
			
			else return Flora;
			
		}
		
		
		
		private static String translateFilumString(String[] filumString){
			
			String result="";
			
			for (int i=0; i<filumString.length ; i++ ){
				
				result=result+connectionParam+filumString[i];
				
			}
			
			Log.i("BD","-----> "+result);
			
			return result;

			
		}
	
	//6 Plantae
	//5 fungi
	//1 Animalia
	
	/*	taxonconceptkey
	 * 	
	 *    
	 * <string-array name="thesaurusFilumsEnglish">
    		<item>Flora</item>
    		<item>Bryophytes</item> 35 ,9 
    		<item>Algae</item>
    		<item>Fungi</item> 5
    		<item>Lichens</item> 
    		<item>Vertebrates</item>
            	<item>Aves</item> 212
            	<item>Actinopterygii</item> 204
            	<item>Amphibia</item> 131
            	<item>Mammalia</item> 359
            	<item>Cephalaspidomorphi</item> 239
            	<item>Reptilia</item>   358          
    	<item>Invertebrate</item> 
    	<item>Arthropods</item> 54
	</string-array> 
	 * 
	 * 
	 */
	
	
	
	

}
