package za.co.invictus.shoprite;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This class Flattens overlapping price group.
 * Collection of price groups added  using addPriceGroup method will be flattened once
 * flattenPriceGroup method is called. flattenPriceGroup ArrayList of price group.
 * Depending on the start date and end date number of returned price group can be same , less or more.
 * Only VKA0 and VKP0 type are flattened other types are ignored and added back to result flattened group.
 * @author I045193
 *
 */

public class FlattenPriceGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public FlattenPriceGroup() {

	}

	private ArrayList<PriceGroup> list = new ArrayList<PriceGroup>();
	private ArrayList<PriceGroup> rawList = new ArrayList<PriceGroup>();
	
	private static final String VKP0 = "VKP0";
	private static final String VKA0 = "VKA0";
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	/**
	 * Add Price Group that need to be flattened.
	 * Only VKA0 and VKP0 type are flattened, if other type are added they are filtered before flattening
	 * and added back to result array list returned.
	 * 
	 * @param priceGroup
	 */

	public void addPriceGroup(PriceGroup priceGroup) {
		if (list == null) {
			list = new ArrayList<PriceGroup>();
		}
		if(rawList==null){
			rawList=new ArrayList<PriceGroup>();
		}
		list.add(priceGroup);
		rawList.add(priceGroup);
	}
	
	/**
	 * List of PriceGroup
	 * This returned list before flattening.
	 * @return
	 */

	public ArrayList<PriceGroup> getPriceGroup() {
		return rawList;
	}

	/**
	 * Flattens list of Price Group objects added.
	 * Any Non VKA0 and VKP0 will be filtered before flattening and 
	 * returned back AS-IS in returned array list
	 * @return
	 * @throws MerchandiseException
	 */
	public ArrayList<PriceGroup> flatneenPriceGroup()
			throws MerchandiseException {
		// Filter Price Group other than VKA0 and VKP0 .
		ArrayList<PriceGroup> nonVKA0andVKP0List = filterNonVKA0andVKP0(list);
		list.removeAll(nonVKA0andVKP0List);

		// Sort by Start Date
		Collections.sort(list);

		// Once Start Date or End Date is adjusted, the flattened price group
		// will be added to this array list
		ArrayList<PriceGroup> flattenList = new ArrayList<PriceGroup>();
		/**
		 * Previously removed vka0 price group is set to this reference before
		 * being removed, this may be required in next loop for adjusted dates
		 **/

		PriceGroup removedvka0PG = null;
		boolean overlap = false;
		for (int i = 0; i < list.size(); i++) {
			PriceGroup pg = list.get(i);
			DateRange pgDateRange = new DateRange();
			pgDateRange.setStartDate(pg.getStartDate());
			pgDateRange.setEndDate(pg.getEndDate());
			DateRange pgNextDateRange = new DateRange();
			if (i < list.size() - 1) {
				PriceGroup pgNext = list.get(i + 1);
				pgNextDateRange = new DateRange();
				pgNextDateRange.setStartDate(pgNext.getStartDate());
				pgNextDateRange.setEndDate(pgNext.getEndDate());

				overlap = DateRange.dateRangeOverlap(pgDateRange,
						pgNextDateRange);
				boolean enddatesSame = DateRange
						.date1EndDateEqualsDate2SEndDate(pgDateRange,
								pgNextDateRange);
				boolean startdatesSame = DateRange
						.date1StartDateEqualsDate2StartDate(pgDateRange,
								pgNextDateRange);
				boolean pgStartDateBeforepgNextDate = DateRange
						.date1StartDateBeforeDate2StartDate(pgDateRange,
								pgNextDateRange);
				boolean pgEndDateEndsBeforepgNextEndDate = DateRange
						.date1EndDateBeforeDate2SEndDate(pgDateRange,
								pgNextDateRange);
				if (startdatesSame || enddatesSame) {
					overlap = true;
				}
				if (startdatesSame && enddatesSame) {
					System.out
							.println("PG DEBUG : Start Date and End Date are Same for PG and PG Next "
									+ "1: "
									+ pg.getType()
									+ "2: "
									+ pgNext.getType());
					if (pg.getType().equals(VKA0)
							&& pgNext.getType().equals(VKP0)) {
						//@formatter:off
						/**-
						 * |-----VKA0-------|
						   |-----VKP0-------|
						 */
						//@formatter:on
						list.remove(pgNext);
						i = -1;
						continue;
					} else if (pg.getType().equals(VKA0)
							&& pgNext.getType().equals(VKA0)) {
						/*Not a valid scenario, this cannot be configuered in SAP
						 * Check with SAP functional team if this happens
						*/
						//@formatter:off
						/**-
						 * |-----VKA0-------|
						   |-----VKA0-------|
						 */
						//@formatter:on
						list.remove(pgNext);
						i = -1;
						continue;
					} else if (pg.getType().equals(VKP0)
							&& pgNext.getType().equals(VKP0)) {
						/*Not a valid scenario, this cannot be configuered in SAP
						 * Check with SAP functional team if this happens
						*/
						//@formatter:off
						/**-
						 * |-----VKP0-------|
						   |-----VKP0-------|
						 */
						//@formatter:on
						list.remove(pgNext);
						i = -1;
						continue;
					} else if (pg.getType().equals(VKP0)
							&& pgNext.getType().equals(VKA0)) {
						//@formatter:off
						/**-
						 * |-----VKP0-------|
						   |-----VKA0-------|
						 */
						//@formatter:on

						list.remove(pg);
						i = -1;
						continue;
					}
				}

				System.out.println("PG DEBUG: OVERLAP: " + overlap);
				System.out.println("PG DEBUG: " + pg.getType() + ":"
						+ pgNext.getType() + " " + pg.getStartDate() + " "
						+ pg.getEndDate() + " " + pgNext.getStartDate() + " "
						+ pgNext.getEndDate());
				if (overlap) {

					if (pg.getType().equals(VKA0)
							&& pgNext.getType().equals(VKP0)) {
						//@formatter:off
						/**-
						 * |-------VKA0-------|
						 * |-------VKP0------------|
						 * 
						 * or
						 * 
						 * |-------VKA0-------|
						 * |-------VKP0----|
						 * 
						 * or
						 * 
						 * |-------VKA0-------|
						 * 		|-------VKP0------------|
						 * 
						 * or
						 * 
						 *
						 *|-------VKA0-----------------|
						 * |-------VKP0------------|
						 * 
						 */
						//@formatter:on
						if (pgStartDateBeforepgNextDate && pgEndDateEndsBeforepgNextEndDate || (startdatesSame && pgEndDateEndsBeforepgNextEndDate)) {
							flattenList.add(pg);
							list.remove(pg);
							pgNext.setStartDate(DateRange
									.getPreviousOrFutureDate(
											dateFormat.format(pg.getEndDate()),
											1));
							Collections.sort(list);
							i = -1;
						}else if((startdatesSame && !pgEndDateEndsBeforepgNextEndDate) || (pgStartDateBeforepgNextDate && !pgEndDateEndsBeforepgNextEndDate)){
							
							list.remove(pgNext);
							i = -1;
							continue;
						}

					} else if (pg.getType().equals(VKP0)
							&& pgNext.getType().equals(VKA0)) {

						removedvka0PG = PriceGroup.getPriceGroup(pgNext);

						if ((pgStartDateBeforepgNextDate && pgEndDateEndsBeforepgNextEndDate) || (pgStartDateBeforepgNextDate && enddatesSame)) {
							//@formatter:off
							/**-
							 * |-----VKP0-----|
							 * 		|-------VKA0------|  
							 * or
							 * 
							 * |-----VKP0-----|
							 * 		|-VKA0----|
							 * 
							 */
							//@formatter:on
							System.out
									.println("PG DEBUG: PG End Date Ends Before PGNext or End Dates Are Same");
							pg.setEndDate(DateRange.getPreviousOrFutureDate(
									dateFormat.format(pgNext.getStartDate()),
									-1));
							flattenList.add(pg);
							list.remove(pg);
							Collections.sort(list);
							i = -1;
						} else if (!pgEndDateEndsBeforepgNextEndDate
								&& startdatesSame) {
							//@formatter:off
							/**-
							 * 
							 * |-----VKP0--------------|
							 * |---------VKA0----|
							 * 
							 */
							//@formatter:on
							System.out
									.println("PG DEBUG: PG Next End date greater than PG End Date and PG and PGNext Start Dates are Same");
							flattenList.add(pgNext);
							list.remove(pgNext);
							pg.setStartDate(DateRange.getPreviousOrFutureDate(
									dateFormat.format(pgNext.getEndDate()), 1));
							Collections.sort(list);
							i = -1;
						} else if(startdatesSame && pgEndDateEndsBeforepgNextEndDate){
							//@formatter:off
							/**-
							 * 
							 * |-----VKP0--------------|
							 * |---------VKA0----------------|
							 * 
							 */
							//@formatter:on
							list.remove(pg);
							i = -1;
							continue;
							
						}else if(pgStartDateBeforepgNextDate && !pgEndDateEndsBeforepgNextEndDate){
							//@formatter:off
							
							/**-
							 * |------------VKP0--------------------|
							 * 		|-------VKA0------|  
							 */
							//@formatter:on
							flattenList.add(pgNext);
							list.remove(pgNext);
							PriceGroup pgCopy = PriceGroup.getPriceGroup(pg);
							pg.setEndDate(DateRange.getPreviousOrFutureDate(
									dateFormat.format(pgNext.getStartDate()),
									-1));
							flattenList.add(pg);
							list.remove(pg);
							pgCopy.setStartDate(DateRange
									.getPreviousOrFutureDate(dateFormat
											.format(pgNext.getEndDate()), 1));
							list.add(pgCopy);
							Collections.sort(list);
							System.out.println(list.size());
							i = -1;
						}
					} else if (pg.getType().equals(VKP0)
							&& pgNext.getType().equals(VKP0)) {

						if (pgStartDateBeforepgNextDate && pgEndDateEndsBeforepgNextEndDate) {
							//@formatter:off
							/**-
							 *|-----VKP0-------|
							 * 		|---------VKP0----|
							 */
							//@formatter:on
							System.out
									.println("PG DEBUG: PG End Date Ends Before PGNext");
							pgNext.setStartDate(DateRange
									.getPreviousOrFutureDate(
											dateFormat.format(pg.getEndDate()),
											1));
							flattenList.add(pg);
							list.remove(pg);
							Collections.sort(list);
							i = -1;
						} else if (pgStartDateBeforepgNextDate
								&& !pgEndDateEndsBeforepgNextEndDate) {
							//@formatter:off
							/**-
							 * |-----VKP0-----------------|
							 * 		|---------VKP0----|
							 */
							//@formatter:on
							System.out
									.println("PG DEBUG: PG Start Date Before PG Nextand PG End Date Ends After PGNext");
							if (removedvka0PG != null) {
								//@formatter:off
								/**
								 * |------------VKA0-----------|(removed in previous loop)
								 * 		|--------VKAP0--------------|
								 * 			|---------VKP0-------|
								 * 			
								 * 
								 */
								//@formatter:on
								System.out
										.println("PG DEBUG: VKAO From Previous Iteration used");
								pg.setStartDate(DateRange
										.getPreviousOrFutureDate(dateFormat
												.format(removedvka0PG
														.getEndDate()), 1));
								list.remove(pgNext);
								Collections.sort(list);
								i = -1;
							} else if (enddatesSame) {
								//@formatter:off
								/**-
								 * |-----VKP0-------|
							 		|---------VKP0--|
								 */
								//@formatter:on

								System.out
										.println("PG DEBUG: End Dates are Same");
								pg.setEndDate(DateRange
										.getPreviousOrFutureDate(dateFormat
												.format(pgNext.getStartDate()),
												-1));
								flattenList.add(pg);
								list.remove(pg);
								Collections.sort(list);
								i = -1;
							} else {

								PriceGroup pgCopyEndBeforePgNext = PriceGroup
										.getPriceGroup(pg);
								PriceGroup pgCopyStartsAfterPgNext = PriceGroup
										.getPriceGroup(pg);
								pgCopyEndBeforePgNext.setEndDate(DateRange
										.getPreviousOrFutureDate(dateFormat
												.format(pgNext.getStartDate()),
												-1));
								flattenList.add(pgCopyEndBeforePgNext);
								pgCopyStartsAfterPgNext
										.setStartDate(DateRange.getPreviousOrFutureDate(
												dateFormat.format(pgNext
														.getEndDate()), 1));
								list.add(pgCopyStartsAfterPgNext);
								list.remove(pg);

								Collections.sort(list);
								i = -1;
							}
						}
					}else if (pg.getType().equals(VKA0)
							&& pgNext.getType().equals(VKA0)){
						//This scenario should happen as per SAP No over lap of Promotions
					}
				} else {
					if (list.size() > 2) {
						System.out
								.println("PG DEBUG: PG and PG Next Dont Overlap");
						flattenList.add(pg);
						list.remove(pg);
						Collections.sort(list);
						i=-1;
					} else {
						System.out
								.println("PG DEBUG: PG and PG Next Dont Overlap and total number objects is less than or equal to 2");
						flattenList.addAll(list);
						break;
					}
				}
			} else {
				flattenList.add(pg);
			}

		}
		flattenList.addAll(nonVKA0andVKP0List);
		Collections.sort(flattenList);
		return flattenList;

	}
    /**
     * Filters non VKAO and VKP0.
     * @param list
     * @return
     */
	private ArrayList<PriceGroup> filterNonVKA0andVKP0(
			ArrayList<PriceGroup> list) {
		ArrayList<PriceGroup> nonVKP0VKA0List = new ArrayList<PriceGroup>();
		for (PriceGroup nonVKP0VKA0 : list) {
			if (!nonVKP0VKA0.getType().equals(VKA0)
					&& !nonVKP0VKA0.getType().equals(VKP0)) {
				nonVKP0VKA0List.add(nonVKP0VKA0);
			}
		}
		return nonVKP0VKA0List;
	}

	public static void main(String[] a) throws ParseException,
			MerchandiseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/08/26");
		Date datee1 = format.parse("2016/09/05");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKA0");
		pg1.setPrice(10.77);
		
		PriceGroup pg2 = new PriceGroup();
		Date dates2 = format.parse("2016/08/26");
		Date datee2 = format.parse("2016/08/31");
		pg2.setStartDate(dates2);
		pg2.setEndDate(datee2);
		pg2.setType("VKP0");
		pg2.setPrice(12.99);
		
		PriceGroup pg3 = new PriceGroup();
		Date dates3 = format.parse("2016/09/01");
		Date datee3 = format.parse("9999/12/31");
		pg3.setStartDate(dates3);
		pg3.setEndDate(datee3);
		pg3.setType("VKP0");
		pg3.setPrice(30.77);
		
		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		fg.addPriceGroup(pg2);
		fg.addPriceGroup(pg3);
		ArrayList<PriceGroup> pgList=fg.flatneenPriceGroup();

		for (PriceGroup pg : pgList) {
			System.out.println("*************************");
			System.out.println(pg.getType());
			System.out.println(pg.getStartDate());
			System.out.println(pg.getEndDate());
			System.out.println(pg.getPrice());
			System.out.println("*************************");
		}

	}

}
