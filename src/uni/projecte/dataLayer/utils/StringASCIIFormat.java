package uni.projecte.dataLayer.utils;

import android.annotation.TargetApi;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;


@TargetApi(9)

public class StringASCIIFormat {

	 private static final char[] hexChar = {
	        '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
	    };

	 public static String toASCII(String input){
		 
		 String s1 = Normalizer.normalize(input, Normalizer.Form.NFKD);
		 String regex = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";

		 String s2="";
		try {
			
			s2 = new String(s1.replaceAll(regex, "").getBytes("ascii"), "ascii");
			
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		  
		 return s2;
		 
	 }
	    
	    
	}
