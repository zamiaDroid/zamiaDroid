package uni.projecte.maps;


public class UTMDisplay {
	
	
	public static String convertUTM(String utm, String res, boolean blanks){
		
		String[] splittedUTMString=utm.split("_");
		String resultUTM="";
		String space="";
		if(blanks) space=" ";
		
		if(splittedUTMString.length>0){
			
			resultUTM=splittedUTMString[0]+" ";
			
			if(splittedUTMString.length>1){
				
				resultUTM+=splittedUTMString[1]+space;
				
				if(splittedUTMString.length==4){
					
					String x=splittedUTMString[2];
					String y=splittedUTMString[3];			
					
					if(res.equals("10km")){
						
						resultUTM+=x.substring(0, 1)+space+y.substring(0,1);
						
					}
					else if(res.equals("1km")){
						
						resultUTM+=x.substring(0, 2)+space+y.substring(0,2);
							
					}
					else { //(res.equals("1m"))

						resultUTM+=x+space+y;
	
					}
		
				}
			
			}
			
		}
		
		return resultUTM;
			
	}
	
	public static String getBdbcUTM10x10(String utm) {

		
		String[] splittedUTMString=utm.split("_");
		String resultUTM="";
		
		if(splittedUTMString.length>0){
			
			resultUTM=splittedUTMString[0]+"_";
			
			if(splittedUTMString.length>1){
				
				resultUTM+=splittedUTMString[1]+"_";
				
				if(splittedUTMString.length==4){
					
					String x=splittedUTMString[2];
					String y=splittedUTMString[3];		
					
					resultUTM+=x.substring(0, 1)+y.substring(0,1);
		
				}
			
			}
			
		}
		
		return resultUTM;
		
		/*if(shortForm.length()>=7){
		
			String prettyString=shortForm.substring(0, 3)+"_"+shortForm.substring(3, 5)+"_"+shortForm.substring(5,6)+shortForm.substring(shortForm.length()-5,shortForm.length()-4);
	    	return prettyString;

		}
		else return "";*/
		
		
		

	}
	
	public static String get1x1UTM(String utm) {

		String[] splittedUTMString=utm.split("_");
		String resultUTM="";
		
		if(splittedUTMString.length>0){
			
			resultUTM=splittedUTMString[0]+"_";
			
			if(splittedUTMString.length>1){
				
				resultUTM+=splittedUTMString[1]+"_";
				
				if(splittedUTMString.length==4){
					
					String x=splittedUTMString[2];
					String y=splittedUTMString[3];		
					
					resultUTM+=x.substring(0, 2)+y.substring(0,2);
		
				}
			
			}
			
		}
		
		return resultUTM;
		
		
    	//String utm1x1=shortForm.substring(0, 3)+"_"+shortForm.substring(3, 5)+"_"+shortForm.substring(5,7)+shortForm.substring(shortForm.length()-5,shortForm.length()-3);
    	//return utm1x1;

	}
	
	
	

}
