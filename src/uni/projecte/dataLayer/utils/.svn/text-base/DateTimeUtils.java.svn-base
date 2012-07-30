package uni.projecte.dataLayer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtils {
	
	/*
	 * 
	 * @return 0 if the times of the two Calendars are equal, -1 if the time of firstDate is 
	 * before the secondDae, 1 if the time of firstDate is after the secondDate.
	 * 
	 */
	
	public static int compareDate(String firstDate, String secondDate){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		Calendar date0 = Calendar.getInstance();
		Calendar date1= Calendar.getInstance();
		
		try {
			
			date0.setTime(sdf.parse(firstDate));
			date1.setTime(sdf.parse(secondDate));
					
			
		} catch (ParseException e) {
			
			System.out.println(e.getMessage());
			
		}

		return date0.compareTo(date1);
		
		
	}
	
	

}
