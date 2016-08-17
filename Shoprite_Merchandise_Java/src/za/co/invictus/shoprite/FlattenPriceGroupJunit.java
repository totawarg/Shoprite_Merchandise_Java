package za.co.invictus.shoprite;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.junit.Test;

import za.co.invictus.shoprite.javamapping.MerchandiseException;

public class FlattenPriceGroupJunit {
	
	@Test
	public void testSingleRegularPrice() throws ParseException, MerchandiseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		System.out.println("testSingleRegularPrice");
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/07/27");
		Date datee1 = format.parse("9999/12/31");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKP0");
		pg1.setPrice(99.00);
		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		ArrayList<PriceGroup> pgList=fg.flatneenPriceGroup();
		assertEquals(pgList, fg.getPriceGroup());
		System.out.println("Arrays match");
		
		for (PriceGroup pg : pgList) {
			System.out.println("*************************");
			System.out.println(pg.getType());
			System.out.println(pg.getStartDate());
			System.out.println(pg.getEndDate());
			System.out.println(pg.getPrice());
			System.out.println("*************************");
		}
		
	}
	
	
	
	@Test
	public void testSinglePromotionPrice() throws ParseException, MerchandiseException {
		System.out.println("testSinglePromotionPrice");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/07/27");
		Date datee1 = format.parse("9999/12/31");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKA0");
		pg1.setPrice(99.00);
		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		ArrayList<PriceGroup> pgList=fg.flatneenPriceGroup();
		assertEquals(pgList, fg.getPriceGroup());
		System.out.println("Arrays match");
		
		for (PriceGroup pg : pgList) {
			System.out.println("*************************");
			System.out.println(pg.getType());
			System.out.println(pg.getStartDate());
			System.out.println(pg.getEndDate());
			System.out.println(pg.getPrice());
			System.out.println("*************************");
		}
		System.out.println("Arrays match");
	}
	
	@Test
	public void testSinglePromotionPriceWithCurrentSellPrice() throws ParseException, MerchandiseException {
		System.out.println("testSinglePromotionPriceWithCurrentSellPrice");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/07/27");
		Date datee1 = format.parse("9999/12/31");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKP0");
		pg1.setPrice(99.00);
		PriceGroup pg2 = new PriceGroup();
		Date dates2 = format.parse("2016/07/28");
		Date datee2 = format.parse("2016/07/31");
		pg2.setStartDate(dates2);
		pg2.setEndDate(datee2);
		pg2.setType("VKA0");
		pg2.setPrice(95.00);
		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		fg.addPriceGroup(pg2);
		ArrayList<PriceGroup> pgList=fg.flatneenPriceGroup();
		System.out.println("XXXXXXXXXXX: "+pgList.size());
		/***Expected Result**/
		PriceGroup epg1=new PriceGroup();
		epg1.setStartDate(format.parse("2016/07/27"));
		epg1.setEndDate(format.parse("2016/07/27"));
		epg1.setPrice(99.00);
		epg1.setType("VKP0");
		PriceGroup epg2=new PriceGroup();
		epg2.setStartDate( format.parse("2016/07/28"));
		epg2.setEndDate(format.parse("2016/07/31"));
		epg2.setType("VKA0");
		epg2.setPrice(95.00);
		PriceGroup epg3=new PriceGroup();
		epg3.setStartDate(format.parse("2016/08/01"));
		epg3.setEndDate(format.parse("9999/12/31"));
		epg3.setPrice(99.00);
		epg3.setType("VKP0");
		ArrayList<PriceGroup> elist=new ArrayList<PriceGroup>();
		elist.add(epg1);
		
		elist.add(epg2);
		elist.add(epg3);
		Collections.sort(elist);
		/** ****/
		for (PriceGroup pg : pgList) {
			System.out.println("*************************");
			System.out.println(pg.getType());
			System.out.println(pg.getStartDate());
			System.out.println(pg.getEndDate());
			System.out.println(pg.getPrice());
			System.out.println("*************************");
		}
			System.out.println("BBBBBBBBBB: "+elist.equals(pgList));
		assertEquals("Arrays do not match", elist,pgList );
		
		System.out.println("Arrays match");
	}
	
	@Test
	public void testPromotionPriceWithChangingSellPrice1() throws ParseException , MerchandiseException{
		System.out.println("testPromotionPriceWithChangingSellPrice1");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/07/27");
		Date datee1 = format.parse("2016/07/31");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKP0");
		pg1.setPrice(97.00);
		
		PriceGroup pg2 = new PriceGroup();
		Date dates2 = format.parse("2016/07/28");
		Date datee2 = format.parse("2016/07/31");
		pg2.setStartDate(dates2);
		pg2.setEndDate(datee2);
		pg2.setType("VKA0");
		pg2.setPrice(95.00);
		
		PriceGroup pg3 = new PriceGroup();
		Date dates3 = format.parse("2016/08/01");
		Date datee3 = format.parse("9999/12/31");
		pg3.setStartDate(dates3);
		pg3.setEndDate(datee3);
		pg3.setType("VKP0");
		pg3.setPrice(99.00);
		
		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		fg.addPriceGroup(pg2);
		fg.addPriceGroup(pg3);
		ArrayList<PriceGroup> pgList=fg.flatneenPriceGroup();
		
		
		/** EXPECTED RESULT **/
		
		PriceGroup epg1 = new PriceGroup();
		Date edates1 = format.parse("2016/07/27");
		Date edatee1 = format.parse("2016/07/27");
		epg1.setStartDate(edates1);
		epg1.setEndDate(edatee1);
		epg1.setType("VKP0");
		epg1.setPrice(97.00);
		
		PriceGroup epg2 = new PriceGroup();
		Date edates2 = format.parse("2016/07/28");
		Date edatee2 = format.parse("2016/07/31");
		epg2.setStartDate(edates2);
		epg2.setEndDate(edatee2);
		epg2.setType("VKA0");
		epg2.setPrice(95.00);
		
		PriceGroup epg3 = new PriceGroup();
		Date edates3 = format.parse("2016/08/01");
		Date edatee3 = format.parse("9999/12/31");
		epg3.setStartDate(edates3);
		epg3.setEndDate(edatee3);
		epg3.setType("VKP0");
		epg3.setPrice(99.00);
		
		ArrayList<PriceGroup> elist=new ArrayList<PriceGroup>();
		elist.add(epg1);
		elist.add(epg2);
		elist.add(epg3);
		Collections.sort(elist);
		/** **/
		
		for (PriceGroup pg : pgList) {
			System.out.println("###########################");
			System.out.println(pg.getType());
			System.out.println(pg.getStartDate());
			System.out.println(pg.getEndDate());
			System.out.println(pg.getPrice());
			System.out.println("############################");
		}

		assertEquals("Arrays do not match", elist,pgList);
		System.out.println("Arrays match");
	}
	
	@Test
	public void testPromotionPriceWithChangingSellPrice2() throws ParseException, MerchandiseException {
		System.out.println("testPromotionPriceWithChangingSellPrice2");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/07/27");
		Date datee1 = format.parse("2016/07/31");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKP0");
		pg1.setPrice(97.00);
		
		PriceGroup pg2 = new PriceGroup();
		Date dates2 = format.parse("2016/07/27");
		Date datee2 = format.parse("2016/07/31");
		pg2.setStartDate(dates2);
		pg2.setEndDate(datee2);
		pg2.setType("VKA0");
		pg2.setPrice(95.00);
		
		PriceGroup pg3 = new PriceGroup();
		Date dates3 = format.parse("2016/08/01");
		Date datee3 = format.parse("9999/12/31");
		pg3.setStartDate(dates3);
		pg3.setEndDate(datee3);
		pg3.setType("VKP0");
		pg3.setPrice(99.00);
		

		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		fg.addPriceGroup(pg2);
		fg.addPriceGroup(pg3);
		ArrayList<PriceGroup> pgList=fg.flatneenPriceGroup();
		
		
		PriceGroup epg1 = new PriceGroup();
		Date edates1 = format.parse("2016/07/27");
		Date edatee1 = format.parse("2016/07/31");
		epg1.setStartDate(edates1);
		epg1.setEndDate(edatee1);
		epg1.setType("VKA0");
		epg1.setPrice(95.00);
		
		PriceGroup epg2 = new PriceGroup();
		Date edates2 = format.parse("2016/08/01");
		Date edatee2 = format.parse("9999/12/31");
		epg2.setStartDate(edates2);
		epg2.setEndDate(edatee2);
		epg2.setType("VKP0");
		epg2.setPrice(99.00);
	
		ArrayList<PriceGroup> elist=new ArrayList<PriceGroup>();
		elist.add(epg1);
		elist.add(epg2);
		Collections.sort(elist);

		assertEquals("Arrays do not match", elist, pgList);
		System.out.println("Arrays match");
	}
	
	@Test
	public void testPromotionPriceWithChangingSellPrice3() throws ParseException, MerchandiseException {
		System.out.println("testPromotionPriceWithChangingSellPrice3");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/07/27");
		Date datee1 = format.parse("2016/07/31");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKP0");
		pg1.setPrice(97.00);
		
		PriceGroup pg2 = new PriceGroup();
		Date dates2 = format.parse("2016/07/27");
		Date datee2 = format.parse("2016/07/29");
		pg2.setStartDate(dates2);
		pg2.setEndDate(datee2);
		pg2.setType("VKA0");
		pg2.setPrice(95.00);
		
		PriceGroup pg3 = new PriceGroup();
		Date dates3 = format.parse("2016/08/01");
		Date datee3 = format.parse("9999/12/31");
		pg3.setStartDate(dates3);
		pg3.setEndDate(datee3);
		pg3.setType("VKP0");
		pg3.setPrice(99.00);
		
		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		fg.addPriceGroup(pg2);
		fg.addPriceGroup(pg3);
		ArrayList<PriceGroup> pgList=fg.flatneenPriceGroup();
		
		/** EXPECTED RESULT **/
		
		PriceGroup epg1 = new PriceGroup();
		Date edates1 = format.parse("2016/07/30");
		Date edatee1 = format.parse("2016/07/31");
		epg1.setStartDate(edates1);
		epg1.setEndDate(edatee1);
		epg1.setType("VKP0");
		epg1.setPrice(97.00);
		
		PriceGroup epg2 = new PriceGroup();
		Date edates2 = format.parse("2016/07/27");
		Date edatee2 = format.parse("2016/07/29");
		epg2.setStartDate(edates2);
		epg2.setEndDate(edatee2);
		epg2.setType("VKA0");
		epg2.setPrice(95.00);
		
		PriceGroup epg3 = new PriceGroup();
		Date edates3 = format.parse("2016/08/01");
		Date edatee3 = format.parse("9999/12/31");
		epg3.setStartDate(edates3);
		epg3.setEndDate(edatee3);
		epg3.setType("VKP0");
		epg3.setPrice(99.00);
		
		ArrayList<PriceGroup> elist=new ArrayList<PriceGroup>();
		elist.add(epg1);
		elist.add(epg2);
		elist.add(epg3);
		Collections.sort(elist);
		/** **/

		assertEquals("Arrays do not match", elist, pgList);
		System.out.println("Arrays match");
	}
	
	@Test
	public void testPromotionPriceWithChangingSellPriceOverlap() throws ParseException , MerchandiseException{
		System.out.println("testPromotionPriceWithChangingSellPriceOverlap");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/07/27");
		Date datee1 = format.parse("2016/08/02");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKP0");
		pg1.setPrice(97.00);
		
		PriceGroup pg2 = new PriceGroup();
		Date dates2 = format.parse("2016/07/28");
		Date datee2 = format.parse("2016/08/05");
		pg2.setStartDate(dates2);
		pg2.setEndDate(datee2);
		pg2.setType("VKA0");
		pg2.setPrice(95.00);
		
		PriceGroup pg3 = new PriceGroup();
		Date dates3 = format.parse("2016/08/03");
		Date datee3 = format.parse("9999/12/31");
		pg3.setStartDate(dates3);
		pg3.setEndDate(datee3);
		pg3.setType("VKP0");
		pg3.setPrice(99.00);
		
		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		fg.addPriceGroup(pg2);
		fg.addPriceGroup(pg3);
		ArrayList<PriceGroup> pgList=fg.flatneenPriceGroup();
		
		/** EXPECTED RESULT **/
		
		PriceGroup epg1 = new PriceGroup();
		Date edates1 = format.parse("2016/07/27");
		Date edatee1 = format.parse("2016/07/27");
		epg1.setStartDate(edates1);
		epg1.setEndDate(edatee1);
		epg1.setType("VKP0");
		epg1.setPrice(97.00);
		
		PriceGroup epg2 = new PriceGroup();
		Date edates2 = format.parse("2016/07/28");
		Date edatee2 = format.parse("2016/08/05");
		epg2.setStartDate(edates2);
		epg2.setEndDate(edatee2);
		epg2.setType("VKA0");
		epg2.setPrice(95.00);
		
		PriceGroup epg3 = new PriceGroup();
		Date edates3 = format.parse("2016/08/06");
		Date edatee3 = format.parse("9999/12/31");
		epg3.setStartDate(edates3);
		epg3.setEndDate(edatee3);
		epg3.setType("VKP0");
		epg3.setPrice(99.00);
		
		ArrayList<PriceGroup> elist=new ArrayList<PriceGroup>();
		elist.add(epg1);
		elist.add(epg2);
		elist.add(epg3);
		Collections.sort(elist);
		/** **/
		

		assertEquals("Arrays do not match", elist, pgList);
		System.out.println("Arrays match");
	}
	
	@Test
	public void testMutliplePromotionsAndMultiRegularPrice() throws ParseException, MerchandiseException {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		PriceGroup pg1 = new PriceGroup();
		Date dates1 = format.parse("2016/05/01");
		Date datee1 = format.parse("2016/05/10");
		pg1.setStartDate(dates1);
		pg1.setEndDate(datee1);
		pg1.setType("VKA0");
		pg1.setPrice(95.00);
		
		PriceGroup pg2 = new PriceGroup();
		Date dates2 = format.parse("2016/05/05");
		Date datee2 = format.parse("2016/06/05");
		pg2.setStartDate(dates2);
		pg2.setEndDate(datee2);
		pg2.setType("VKP0");
		pg2.setPrice(96.00);
		
		PriceGroup pg3 = new PriceGroup();
		Date dates3 = format.parse("2016/05/20");
		Date datee3 = format.parse("2016/05/25");
		pg3.setStartDate(dates3);
		pg3.setEndDate(datee3);
		pg3.setType("VKA0");
		pg3.setPrice(97.00);
		
		PriceGroup pg4 = new PriceGroup();
		Date dates4 = format.parse("2016/05/23");
		Date datee4 = format.parse("9999/12/31");
		pg4.setStartDate(dates4);
		pg4.setEndDate(datee4);
		pg4.setType("VKP0");
		pg4.setPrice(98.00);
		

		FlattenPriceGroup fg = new FlattenPriceGroup();
		fg.addPriceGroup(pg1);
		fg.addPriceGroup(pg2);
		fg.addPriceGroup(pg3);
		fg.addPriceGroup(pg4);
		ArrayList<PriceGroup> pgList=fg.flatneenPriceGroup();
	     
/** EXPECTED RESULT **/
		
		PriceGroup epg1 = new PriceGroup();
		Date edates1 = format.parse("2016/05/01");
		Date edatee1 = format.parse("2016/05/10");
		epg1.setStartDate(edates1);
		epg1.setEndDate(edatee1);
		epg1.setType("VKA0");
		epg1.setPrice(95.00);
		
		PriceGroup epg2 = new PriceGroup();
		Date edates2 = format.parse("2016/05/11");
		Date edatee2 = format.parse("2016/05/19");
		epg2.setStartDate(edates2);
		epg2.setEndDate(edatee2);
		epg2.setType("VKP0");
		epg2.setPrice(96.00);
		
		PriceGroup epg3 = new PriceGroup();
		Date edates3 = format.parse("2016/05/20");
		Date edatee3 = format.parse("2016/05/25");
		epg3.setStartDate(edates3);
		epg3.setEndDate(edatee3);
		epg3.setType("VKA0");
		epg3.setPrice(97.00);
		
		PriceGroup epg4 = new PriceGroup();
		Date edates4 = format.parse("2016/05/26");
		Date edatee4 = format.parse("9999/12/31");
		epg4.setStartDate(edates4);
		epg4.setEndDate(edatee4);
		epg4.setType("VKP0");
		epg4.setPrice(98.00);
		
		ArrayList<PriceGroup> elist=new ArrayList<PriceGroup>();
		elist.add(epg1);
		elist.add(epg2);
		elist.add(epg3);
		elist.add(epg4);
		Collections.sort(elist);
		/** **/
		assertEquals("Arrays do not match", elist, pgList);
		System.out.println("Arrays match");
		
	}
	
	
	@Test
	public void testSinglePromotionsAndMultiOverlappingRegularPriceWithSameEndDates() throws ParseException, MerchandiseException {
		
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
	     
/** EXPECTED RESULT **/
		
		PriceGroup epg1 = new PriceGroup();
		Date edates1 = format.parse("2016/02/01");
		Date edatee1 = format.parse("2016/02/02");
		epg1.setStartDate(edates1);
		epg1.setEndDate(edatee1);
		epg1.setType("VKP0");
		epg1.setPrice(100.00);
		
		PriceGroup epg2 = new PriceGroup();
		Date edates2 = format.parse("2016/02/03");
		Date edatee2 = format.parse("2016/02/04");
		epg2.setStartDate(edates2);
		epg2.setEndDate(edatee2);
		epg2.setType("VKP0");
		epg2.setPrice(90.00);
		
		PriceGroup epg3 = new PriceGroup();
		Date edates3 = format.parse("2016/02/05");
		Date edatee3 = format.parse("2016/02/06");
		epg3.setStartDate(edates3);
		epg3.setEndDate(edatee3);
		epg3.setType("VKP0");
		epg3.setPrice(85.00);
		
		PriceGroup epg4 = new PriceGroup();
		Date edates4 = format.parse("2016/02/07");
		Date edatee4 = format.parse("2016/02/07");
		epg4.setStartDate(edates4);
		epg4.setEndDate(edatee4);
		epg4.setType("VKA0");
		epg4.setPrice(80.00);
		
		PriceGroup epg5 = new PriceGroup();
		Date edates5 = format.parse("2016/02/08");
		Date edatee5 = format.parse("9999/12/31");
		epg5.setStartDate(edates5);
		epg5.setEndDate(edatee5);
		epg5.setType("VKP0");
		epg5.setPrice(100.00);
		
		ArrayList<PriceGroup> elist=new ArrayList<PriceGroup>();
		elist.add(epg1);
		elist.add(epg2);
		elist.add(epg3);
		elist.add(epg4);
		elist.add(epg5);
		Collections.sort(elist);
		/** **/
		assertEquals("Arrays do not match", elist, pgList);
		System.out.println("Arrays match");
		
	}
	

}
