package za.co.invictus.shoprite;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
/**
 * General utility class to format dates.
 * @author I045193
 *
 */
public class DateFormat {
	
	
	public static Date formatDate(String inputDate)
			throws MerchandiseException {

		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String outdate = null;
		Date outputDate = null;
		try {
			Calendar cal = Calendar.getInstance();
			Date indate = null;

			indate = inDateFormat.parse(inputDate);
			cal.setTime(indate);
			cal.add(Calendar.DATE, 0);
			outdate = outDateFormat.format(cal.getTime());
			outputDate = outDateFormat.parse(outdate);
		} catch (ParseException e) {
			throw new MerchandiseException(
					"Invalid input Date Format expected in yyyyMMdd");
		}
		return outputDate;
	}

	public static String formatDate(Date date) {
		String outdate = null;

		SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 0);
		outdate = outDateFormat.format(cal.getTime());

		return outdate;
	}


}
