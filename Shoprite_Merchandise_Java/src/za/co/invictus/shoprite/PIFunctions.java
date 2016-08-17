package za.co.invictus.shoprite;



import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

import org.junit.Test;

public class PIFunctions {

	public static String addDays(String dateString, int days) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date date = dateFormat.parse(dateString);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_YEAR, days);

		return dateFormat.format(cal.getTime());

	}

	public static void flattenPrices(String[] value, String[] type, String[] startDate, String[] endDate, String[] amount, ArrayList<String> result) {
		TreeMap<String, String[]> tMap = new TreeMap<String, String[]>();

		TreeMap<String, String[]> vkp0 = new TreeMap<String, String[]>();
		TreeMap<String, String[]> vka0 = new TreeMap<String, String[]>();

		int i;

		System.out.println();
		System.out.println();
		System.out.println("flatten...");

		for (i = 0; i < value.length; i++) {
			//Load the maps...
			if ("VKP0".equals(type[i])){
				vkp0.put(startDate[i], new String[]{value[i], type[i], startDate[i], endDate[i], amount[i]});

			}else if("VKA0".equals(type[i])){
				vka0.put(startDate[i], new String[]{value[i], type[i], startDate[i], endDate[i], amount[i]});
			}else{
					tMap.put(startDate[i], new String[]{value[i], type[i], startDate[i], endDate[i], amount[i]});
			
			}
		}

		//maps are now sorted by start date.
		//Loop over all regular prices:
		//  - remove if promotion overlaps regular
		//  - end early if overlap over start date
		//  - start late if overlap over end date
		for (String vkp0Key : vkp0.keySet()) {
			String[] vkp0Item = vkp0.get(vkp0Key);
			String regStart = vkp0Item[2];
			String regEnd = vkp0Item[3];

			System.out.println("-");
			System.out.println("VKP0: "+ vkp0Item[0] + ", " + vkp0Item[1] + ", " + regStart + ", " + regEnd + ", " + vkp0Item[4]);

			boolean overlap = false;

			for (String vka0Key : vka0.keySet()) {
				String[] vka0Item = vka0.get(vka0Key);
				String promoStart = vka0Item[2];
				String promoEnd = vka0Item[3];

				System.out.println("VKA0: " + vka0Item[0] + ", " + vka0Item[1] + ", " + promoStart + ", " + promoEnd + ", " + vka0Item[4]);

				overlap = (promoStart.compareTo(regEnd) <= 0 && promoEnd.compareTo(regStart) >= 0);

				//Complete coverage - promo starts before or on and ends on or after.
				if (promoStart.compareTo(regStart) <= 0 && promoEnd.compareTo(regEnd) >= 0) {
					System.out.println("VKA0 starting " + promoStart + " starts before or on " + regStart + " and ends " + promoEnd + " after or on " + regEnd);

					System.out.println("Ignore this regular price!");

					tMap.put(promoStart, vka0Item);
					continue;

				}

				if (overlap) {
					System.out.println("Overlap found!");

					//Promo starts after Regular start and ends before regular ends
					if (promoStart.compareTo(regStart) > 0 && promoEnd.compareTo(regEnd) < 0) {
						System.out.println("insert promo in regular");

						String[] regContinue = new String[] {new String(vkp0Item[0]), new String(vkp0Item[1]), new String(promoEnd), new String(vkp0Item[3]), new String(vkp0Item[4])};
						//continuation the regular price after Promo
						try {
							regContinue[2] = addDays(promoEnd, +1);
						} catch (ParseException e) {

						}  // +1
						tMap.put(regContinue[2], regContinue);


						//end the regular price before Promo
						try {
							vkp0Item[3] = addDays(vka0Item[2], -1);
						} catch (ParseException e) {
							//Do something...
						}
						tMap.put(regStart, vkp0Item);

						tMap.put(promoStart, vka0Item);

					} else {

						//Promo starts after Regular start and starts before or on Regular ends and ends after regular ends
						if (promoStart.compareTo(regStart) > 0 && promoStart.compareTo(regEnd) < 0 && promoEnd.compareTo(regEnd) >= 0) {
							System.out.println("end regular early");

							//short the regular price
							try {
								vkp0Item[3] = addDays(vka0Item[2], -1);
							} catch (ParseException e) {
								//Do something...
							}
							
							tMap.put(regStart, vkp0Item);

							tMap.put(promoStart, vka0Item);

						}

						//Promo ends after Regular start and ends before or on regular ends
						if (promoEnd.compareTo(regStart) >= 0 && promoEnd.compareTo(regEnd) < 0) {
							System.out.println("start regular later");

							//delay the regular price
							String newStart = "";
							try {
								newStart = addDays(promoEnd, +1);
								vkp0Item[2] = newStart;
							} catch (ParseException e) {
								//Do something...
							}

							tMap.put(newStart, vkp0Item);

							tMap.put(promoStart, vka0Item);

						}
					}

				}

			}

			if (overlap == false) {
				tMap.put(regStart, vkp0Item);
			}


		}
		
		//Only Promotions exist - should not overlap.
		if (vkp0.size() == 0) {
			for (String vka0Key : vka0.keySet()) {
				String[] vka0Item = vka0.get(vka0Key);
			
				tMap.put(vka0Key, vka0Item);
			}
		}



		System.out.println("____________________________________________________________________________");
		System.out.println();

		if (tMap.size() <= 0) System.out.println("Key Set empty!");

		for (String key : tMap.keySet()) {
			System.out.print(key + " : ");

			i = 0;
			for (String t : tMap.get(key)) {
				System.out.print(t);
				if (i < tMap.get(key).length - 1) System.out.print(",");
				i++;
			}

			System.out.println();

			result.add(tMap.get(key)[0]);

		}

		System.out.println("____________________________________________________________________________");
		System.out.println();

		System.out.println("flattened to " + result);

		System.out.println();


	}

	@Test
	public void testSingleRegularPrice() {
		System.out.println("testSingleRegularPrice");
		String[] value = {"99.00"};

		String[] type = {"VKP0"};
		String[] startDate = {"2016-07-27"};
		String[] endDate = {"9999-12-31"};
		String[] amount = {"99.00"};

		ArrayList<String> result = new ArrayList<String>();

		flattenPrices(value, type, startDate, endDate, amount, result);

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("99.00");

		assertEquals("Arrays do not match", expected, result);
		System.out.println("Arrays match");
	}


	@Test
	public void testSinglePromotionPrice() {
		System.out.println("testSinglePromotionPrice");
		String[] value = {"95.00"};

		String[] type = {"VKA0"};
		String[] startDate = {"2016-07-27"};
		String[] endDate = {"2016-07-31"};
		String[] amount = {"95.00"};

		ArrayList<String> result = new ArrayList<String>();

		flattenPrices(value, type, startDate, endDate, amount, result);

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("95.00");

		assertEquals("Arrays do not match", expected, result);
		System.out.println("Arrays match");
	}

	@Test
	public void testSinglePromotionPriceWithCurrentSellPrice() {
		System.out.println("testSinglePromotionPriceWithCurrentSellPrice");
		String[] value = {"99.00", "95.00"};

		String[] type = {"VKP0", "VKA0"};
		String[] startDate = {"2016-07-27", "2016-07-28"};
		String[] endDate = {"9999-12-31", "2016-07-31"};
		String[] amount = {"99.00", "95.00"};

		ArrayList<String> result = new ArrayList<String>();

		flattenPrices(value, type, startDate, endDate, amount, result);

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("99.00");
		expected.add("95.00");
		expected.add("99.00");

		assertEquals("Arrays do not match", expected, result);
		System.out.println("Arrays match");
	}

	@Test
	public void testPromotionPriceWithChangingSellPrice1() {
		System.out.println("testPromotionPriceWithChangingSellPrice1");
		String[] value = {"97.00", "95.00", "99.00"};

		String[] type = {"VKP0", "VKA0", "VKP0"};
		String[] startDate = {"2016-07-27", "2016-07-28", "2016-08-01"};
		String[] endDate = {"2016-07-31", "2016-07-31", "9999-12-31"};
		String[] amount = {"97.00", "95.00", "99.00"};

		ArrayList<String> result = new ArrayList<String>();

		flattenPrices(value, type, startDate, endDate, amount, result);

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("97.00");
		expected.add("95.00");
		expected.add("99.00");

		assertEquals("Arrays do not match", expected, result);
		System.out.println("Arrays match");
	}

	@Test
	public void testPromotionPriceWithChangingSellPrice2() {
		System.out.println("testPromotionPriceWithChangingSellPrice2");
		String[] value = {"97.00", "95.00", "99.00"};

		String[] type = {"VKP0", "VKA0", "VKP0"};
		String[] startDate = {"2016-07-27", "2016-07-27", "2016-08-01"};
		String[] endDate = {"2016-07-31", "2016-07-31", "9999-12-31"};
		String[] amount = {"97.00", "95.00", "99.00"};

		ArrayList<String> result = new ArrayList<String>();

		flattenPrices(value, type, startDate, endDate, amount, result);

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("95.00");
		expected.add("99.00");

		assertEquals("Arrays do not match", expected, result);
		System.out.println("Arrays match");
	}
	
	@Test
	public void testPromotionPriceWithChangingSellPrice3() {
		System.out.println("testPromotionPriceWithChangingSellPrice3");
		String[] value = {"97.00", "95.00", "99.00"};

		String[] type = {"VKP0", "VKA0", "VKP0"};
		String[] startDate = {"2016-07-27", "2016-07-27", "2016-08-01"};
		String[] endDate = {"2016-07-31", "2016-07-29", "9999-12-31"};
		String[] amount = {"97.00", "95.00", "99.00"};

		ArrayList<String> result = new ArrayList<String>();

		flattenPrices(value, type, startDate, endDate, amount, result);

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("95.00");
		expected.add("97.00");
		expected.add("99.00");

		assertEquals("Arrays do not match", expected, result);
		System.out.println("Arrays match");
	}
	
	@Test
	public void testPromotionPriceWithChangingSellPriceOverlap() {
		System.out.println("testPromotionPriceWithChangingSellPriceOverlap");

		String[] value = {"97.00", "95.00", "99.00"};

		String[] type = {"VKP0", "VKA0", "VKP0"};
		String[] startDate = {"2016-07-27", "2016-07-28", "2016-08-03"};
		String[] endDate = {"2016-08-02", "2016-08-05", "9999-12-31"};
		String[] amount = {"97.00", "95.00", "99.00"};

		ArrayList<String> result = new ArrayList<String>();

		flattenPrices(value, type, startDate, endDate, amount, result);

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("97.00");
		expected.add("95.00");
		expected.add("99.00");

		assertEquals("Arrays do not match", expected, result);
		System.out.println("Arrays match");
	}

}

