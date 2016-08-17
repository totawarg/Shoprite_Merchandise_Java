package za.co.invictus.shoprite;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;



public class FlattenPriceGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<PriceGroup> list = new ArrayList<PriceGroup>();
	private static final String VKP0 = "VKP0";
	private static final String VKA0 = "VKA0";
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	public void addPriceGroup(PriceGroup priceGroup) {
		if (list == null) {
			list = new ArrayList<PriceGroup>();
		}
		list.add(priceGroup);
	}

	public ArrayList<PriceGroup> getPriceGroup() {
		return list;
	}
	
	public ArrayList<PriceGroup> flatneenPriceGroup() throws ParseException {
		
		ArrayList<PriceGroup> nonVKA0andVKP0List=filterNonVKA0andVKP0(list);
		list.removeAll(nonVKA0andVKP0List);
		
		Collections.sort(list);
		ArrayList<PriceGroup> flattenList = new ArrayList<PriceGroup>();
		PriceGroup removedvka0PG=null;
		boolean overlap=false;
		for(int i=0;i<list.size();i++){
			PriceGroup pg=list.get(i);
			DateRange pgDateRange=new DateRange();
			pgDateRange.setStartDate(pg.getStartDate());
			pgDateRange.setEndDate(pg.getEndDate());
			DateRange pgNextDateRange=new DateRange();
			if(i<list.size()-1){
				PriceGroup pgNext=list.get(i+1);
				pgNextDateRange=new DateRange();
				pgNextDateRange.setStartDate(pgNext.getStartDate());
				pgNextDateRange.setEndDate(pgNext.getEndDate());
				System.out.println("Debug: PG *" +pg.getStartDate()+" "+pg.getEndDate());
				System.out.println("Debug: PG Next *" +pgNext.getStartDate()+" "+pgNext.getEndDate());
				overlap=DateRange.dateRangeOverlap(pgDateRange, pgNextDateRange);
				boolean enddatesSame=DateRange.date1EndDateEqualsDate2SEndDate(pgDateRange, pgNextDateRange);
				boolean startdatesSame=DateRange.date1StartDateEqualsDate2StartDate(pgDateRange, pgNextDateRange);
				boolean pgStartDateBeforepgNextDate=DateRange.date1StartDateBeforeDate2StartDate(pgDateRange, pgNextDateRange);
				if(startdatesSame || enddatesSame){
					overlap=true;
				}
				if(startdatesSame && enddatesSame){
					
					if(pg.getType().equals(VKA0) && pgNext.getType().equals(VKP0)){
						System.out.println("1Same dates");
						list.remove(pgNext);
						i=-1;
						continue;
					}else if(pg.getType().equals(VKA0) && pgNext.getType().equals(VKA0)){
						System.out.println("2Same dates");
						list.remove(pgNext);
						i=-1;
						continue;
					}else if(pg.getType().equals(VKP0) && pgNext.getType().equals(VKP0)){
						System.out.println("3Same dates");
						list.remove(pgNext);
						i=-1;
						continue;   
					}else if(pg.getType().equals(VKP0) && pgNext.getType().equals(VKA0) ){
						System.out.println("4Same dates");
						list.remove(pg);
						System.out.println("4Same dates "+list.size());
						i=-1;
						continue;
					}
				}
				System.out.println("Overlap: "+overlap);
				if(overlap){
					System.out.println("InOverlap If Statement: "+"1: "+pg.getType()+" 2: "+pgNext.getType());
					
					if(pg.getType().equals(VKA0) && pgNext.getType().equals(VKP0)){
						
							System.out.println("PG Next: "+ pgNext.getStartDate() + pgNext.getEndDate());
							flattenList.add(pg);
							list.remove(pg);
							pgNext.setStartDate(DateRange.getPreviousOrFutureDate(dateFormat.format(pg.getEndDate()), 1));
							Collections.sort(list);
							i=-1;
						
					}else if(pg.getType().equals(VKP0) && pgNext.getType().equals(VKA0)){
						System.out.println("Else if 1");
						removedvka0PG=PriceGroup.getPriceGroup(pgNext);
						boolean pgEndDateEndsBeforepgNextEndDate=DateRange.date1EndDateBeforeDate2SEndDate(pgDateRange, pgNextDateRange);
						if(pgEndDateEndsBeforepgNextEndDate || enddatesSame){
							pg.setEndDate(DateRange.getPreviousOrFutureDate(dateFormat.format(pgNext.getStartDate()), -1));
							flattenList.add(pg);
							list.remove(pg);
							Collections.sort(list);
							i=-1;
						}else if(!pgEndDateEndsBeforepgNextEndDate && startdatesSame ){
							System.out.println("PG Next Enddate greater and pg and pgnext startdatesame");
							flattenList.add(pgNext);
							list.remove(pgNext);
							pg.setStartDate(DateRange.getPreviousOrFutureDate(dateFormat.format(pgNext.getEndDate()), 1));
							Collections.sort(list);
							i=-1;
						}else{
						
							System.out.println("%%%%");
							flattenList.add(pgNext);
							list.remove(pgNext);
							PriceGroup pgCopy=PriceGroup.getPriceGroup(pg);
							pg.setEndDate(DateRange.getPreviousOrFutureDate(dateFormat.format(pgNext.getStartDate()), -1));
							flattenList.add(pg);
							list.remove(pg);
							pgCopy.setStartDate(DateRange.getPreviousOrFutureDate(dateFormat.format(pgNext.getEndDate()), 1));
							list.add(pgCopy);
							Collections.sort(list);
							System.out.println(list.size());
							i=-1;
						}
					}else if(pg.getType().equals(VKP0) && pgNext.getType().equals(VKP0)){
						System.out.println("VKP0 : VKP0");
						boolean pgEndDateEndsBeforepgNextEndDate=DateRange.date1EndDateBeforeDate2SEndDate(pgDateRange, pgNextDateRange);
						if(pgEndDateEndsBeforepgNextEndDate){
							System.out.println("1****");
							pgNext.setStartDate(DateRange.getPreviousOrFutureDate(dateFormat.format(pg.getEndDate()), 1));
							flattenList.add(pg);
							list.remove(pg);
							Collections.sort(list);
							i=-1;
						}else if(pgStartDateBeforepgNextDate && !pgEndDateEndsBeforepgNextEndDate){
							if(removedvka0PG!=null){
								System.out.println("2" + pgNext.getStartDate() + " "+ pgNext.getEndDate() + pgEndDateEndsBeforepgNextEndDate);
								pg.setStartDate(DateRange.getPreviousOrFutureDate(dateFormat.format(removedvka0PG.getEndDate()), 1));
								list.remove(pgNext);
								Collections.sort(list);
								i=-1;
							}else if(enddatesSame){
								System.out.println("End Date are same *************");
								pg.setEndDate(DateRange.getPreviousOrFutureDate(dateFormat.format(pgNext.getStartDate()), -1));
								flattenList.add(pg);
								list.remove(pg);
								Collections.sort(list);
								i=-1;
							}else{
								System.out.println("Debug PG: "+ pg.getStartDate() +" "+pg.getEndDate());
								System.out.println("Debug PG Next: "+ pgNext.getStartDate() +" "+pgNext.getEndDate());
								PriceGroup pgCopyEndBeforePgNext=PriceGroup.getPriceGroup(pg);
								PriceGroup pgCopyStartsAfterPgNext=PriceGroup.getPriceGroup(pg);
								pgCopyEndBeforePgNext.setEndDate(DateRange.getPreviousOrFutureDate(dateFormat.format(pgNext.getStartDate()), -1));
								flattenList.add(pgCopyEndBeforePgNext);
								pgCopyStartsAfterPgNext.setStartDate(DateRange.getPreviousOrFutureDate(dateFormat.format(pgNext.getEndDate()), 1));
								list.add(pgCopyStartsAfterPgNext);
								list.remove(pg);
								System.out.println("Start and End: "+ pgCopyStartsAfterPgNext.getStartDate() +" "+pgCopyStartsAfterPgNext.getEndDate() );
								Collections.sort(list);
								i=-1;
							}
						}
					}
				}else{
					if(list.size()>2){
						System.out.println("No Overlap");
						flattenList.add(pg);
						list.remove(pg);
						Collections.sort(list);
					}else{
						System.out.println("Overlap False: "+ list.size());
						flattenList.addAll(list);
						break;
					}
				}
			}else{
				flattenList.add(pg);
			}
			
		
		}
		flattenList.addAll(nonVKA0andVKP0List);
		Collections.sort(flattenList);
		return flattenList;
	
	}

	
	
	private ArrayList<PriceGroup> filterNonVKA0andVKP0(ArrayList<PriceGroup> list){
		ArrayList<PriceGroup> nonVKP0VKA0List=new ArrayList<PriceGroup>();
		for(PriceGroup nonVKP0VKA0:list){
			if(!nonVKP0VKA0.getType().equals(VKA0) && !nonVKP0VKA0.getType().equals(VKP0)){
				nonVKP0VKA0List.add(nonVKP0VKA0);
			}
		}
		return nonVKP0VKA0List;
	}

	
	public static void main(String[] a) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/02/01");
		Date datee1 = format.parse("9999/12/31");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKP0");
		pg1.setPrice(100.00);
		
		PriceGroup pg2 = new PriceGroup();
		Date dates2 = format.parse("2016/02/03");
		Date datee2 = format.parse("2016/02/07");
		pg2.setStartDate(dates2);
		pg2.setEndDate(datee2);
		pg2.setType("VKP0");
		pg2.setPrice(90.00);
		
		PriceGroup pg3 = new PriceGroup();
		Date dates3 = format.parse("2016/02/05");
		Date datee3 = format.parse("2016/02/07");
		pg3.setStartDate(dates3);
		pg3.setEndDate(datee3);
		pg3.setType("VKP0");
		pg3.setPrice(85.00);
		
		PriceGroup pg4 = new PriceGroup();
		Date dates4 = format.parse("2016/02/07");
		Date datee4 = format.parse("2016/02/07");
		pg4.setStartDate(dates4);
		pg4.setEndDate(datee4);
		pg4.setType("VKA0");
		pg4.setPrice(80.00);
		

		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		fg.addPriceGroup(pg2);
		fg.addPriceGroup(pg3);
		fg.addPriceGroup(pg4);
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
