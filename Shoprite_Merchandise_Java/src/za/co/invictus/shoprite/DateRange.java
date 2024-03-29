package za.co.invictus.shoprite;
 
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Wrapper class for Date Range, 
 * with utility methods to calculate date overlaps
 * @author I045193
 *
 */
public class DateRange {
	

	private Date startDate = null;
	private Date endDate = null;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Returns true if date range overlap
	 * @param date1
	 * @param date2
	 * @return
	 */

	public static boolean dateRangeOverlap(DateRange date1, DateRange date2) {
		boolean overlap = false;
		boolean isStartDate1BeforeEnddate2 = (date1.getStartDate()
				.compareTo(date2.getEndDate())) < 0;
		boolean isEndDate1AfterStartDate2 = (date1.getEndDate().compareTo(date2
				.getStartDate())) > 0;
		overlap = isStartDate1BeforeEnddate2 && isEndDate1AfterStartDate2;
		return overlap;
	}
	
	/**
	 * Returns True if DateRange 1 and 2 have same start dates
	 * @param date1 DateRange
	 * @param date2 DateRange
	 * @return
	 */
	public static boolean date1StartDateEqualsDate2StartDate(DateRange date1,
			DateRange date2) {
		return date1.getStartDate().compareTo(date2.getStartDate()) == 0;
	}

	/**

	 * Returns True if DateRange 1 and 2 have same end dates
	 * @param date1 DateRange
	 * @param date2 DateRange
	 * @return
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean date1EndDateEqualsDate2SEndDate(DateRange date1,
			DateRange date2) {
		return date1.getEndDate().compareTo(date2.getEndDate()) ==0;
	}
	
	/**
	 * Returns True if DateRange 1 start before DateRange 2
	 * @param date1 DateRange
	 * @param date2 DateRange
	 * @return
	 */
	public static boolean date1StartDateBeforeDate2StartDate(DateRange date1,
			DateRange date2) {
		return date1.getStartDate().compareTo(date2.getStartDate()) < 0;
	}
	
	/**
	 * Returns True if DateRange 1 ends before DateRange 2
	 * @param date1 DateRange
	 * @param date2 DateRange
	 * @return
	 */

	public static boolean date1EndDateBeforeDate2SEndDate(DateRange date1,
			DateRange date2) {
		return date1.getEndDate().compareTo(date2.getEndDate()) < 0;
	}

	public static String getPreviousOrFutureDate(String inputDateFormat,
			String inputDate, String outputDateFormat, int numberOfDays) {

		DateFormat inDateFormat = new SimpleDateFormat(inputDateFormat);
		DateFormat outDateFormat = new SimpleDateFormat(outputDateFormat);

		Calendar cal = Calendar.getInstance();
		Date indate = null;
		String outdate = null;
		try {
			indate = inDateFormat.parse(inputDate);
			cal.setTime(indate);
			cal.add(Calendar.DATE, numberOfDays);
			outdate = outDateFormat.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outdate;
	}
	
	public static Date getPreviousOrFutureDate(String inputDate, int numberOfDays) throws  MerchandiseException {

		DateFormat inDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat outDateFormat = new SimpleDateFormat("yyyy/MM/dd");

		Calendar cal = Calendar.getInstance();
		Date indate = null;
		String outdate = null;
		Date output=null;
		try {
			indate = inDateFormat.parse(inputDate);
			cal.setTime(indate);
			cal.add(Calendar.DATE, numberOfDays);
			outdate = outDateFormat.format(cal.getTime());
			output=outDateFormat.parse(outdate);
		} catch (ParseException e) {
			throw new MerchandiseException("Invalid Date Format, expected format is yyyy/MM/dd");
		}
		return output;
	}

	public static void main(String a[]) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date dates1 = format.parse("2016/05/01");
		Date datee1 = format.parse("2016/05/25");
		DateRange r1 = new DateRange();
		r1.setStartDate(dates1);
		r1.setEndDate(datee1);
		DateRange r2 = new DateRange();
		Date dates2 = format.parse("2016/05/20");
		Date datee2 = format.parse("2016/05/28");
		r2.setStartDate(dates2);
		r2.setEndDate(datee2);
		System.out.println(DateRange.dateRangeOverlap(r2, r1));
	}

}
