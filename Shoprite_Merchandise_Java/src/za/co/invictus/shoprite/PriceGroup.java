package za.co.invictus.shoprite;

import java.io.Serializable;

import java.util.Date;
/**
 * Wrapper class for Price Group
 * @author I045193
 *
 */
public class PriceGroup implements Serializable, Comparable<PriceGroup> {

	private double price = 0.00d;
	private String promotionId = "";
	private Date startDate = new Date();
	private Date endDate = new Date();
	private String type = "";

	private DateRange dateRange = new DateRange();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;

	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		dateRange.setStartDate(startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		dateRange.setEndDate(endDate);
	}

	public DateRange getDateRange() {
		return dateRange;
	}

	public static PriceGroup getPriceGroup(PriceGroup pg) {
		PriceGroup p = new PriceGroup();
		p.setStartDate(pg.getStartDate());
		p.setEndDate(pg.getEndDate());
		p.setType(pg.getType());
		p.setPromotionId(pg.getPromotionId());
		p.setPrice(pg.getPrice());
		return p;
	}

	public String toString() {

		return startDate + "\t" + endDate + "\t" + type + "\t" + promotionId
				+ "\t" + price;
	}

	@Override
	public int compareTo(PriceGroup o) {
		// TODO Auto-generated method stub
		return getStartDate().compareTo(o.getStartDate());
	}

	public boolean equals(Object o) {
		PriceGroup pg = (PriceGroup) o;
		boolean flag = false;
		if (startDate.equals(pg.getStartDate())
				&& endDate.equals(pg.getEndDate())
				&& promotionId.equals(pg.getPromotionId())
				&& type.equals(pg.getType()) && new Double(price).equals(new Double(pg.getPrice())))
			return true;
			return flag;
	}

}
