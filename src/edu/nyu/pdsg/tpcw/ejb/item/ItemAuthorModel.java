package edu.nyu.pdsg.tpcw.ejb.item;

import java.io.Serializable;
import java.util.Date;

/**
 * Item bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class ItemAuthorModel implements Serializable {

	private Integer I_ID;
	private String I_TITLE;
	private Integer I_A_ID;
	private Date I_PUB_DATE;
	private String I_PUBLISHER;
	private String I_SUBJECT; 
	private String I_DESC;
	private Integer I_RELATED1;
	private Integer I_RELATED2;
	private Integer I_RELATED3;
	private Integer I_RELATED4;
	private Integer I_RELATED5;
	private String I_THUMBNAIL;
	private String I_IMAGE;
	private Double I_SRP;
	private Double I_COST;
	private Date I_AVAIL;
	private Integer I_STOCK;
	private String I_ISBN;
	private Integer I_PAGE;
	private String I_BACKING;
	private String I_DIMENSIONS;
	
	private String A_FNAME;
	private String A_LNAME;

	public ItemAuthorModel(Integer I_ID, String I_TITLE, Integer I_A_ID, Date I_PUB_DATE, String I_PUBLISHER,
				String I_SUBJECT, String I_DESC, Integer I_RELATED1, Integer I_RELATED2, Integer I_RELATED3,
				Integer I_RELATED4, Integer I_RELATED5, String I_THUMBNAIL, String I_IMAGE, Double I_SRP, 
				Double I_COST, Date I_AVAIL, Integer I_STOCK, String I_ISBN, Integer I_PAGE,
				String I_BACKING, String I_DIMENSIONS, String A_FNAME, String A_LNAME) {
		this.I_ID = I_ID;
		this.I_TITLE = I_TITLE;
		this.I_A_ID = I_A_ID;
		this.I_PUB_DATE = I_PUB_DATE;
		this.I_PUBLISHER = I_PUBLISHER;
		this.I_SUBJECT = I_SUBJECT; 
		this.I_DESC = I_DESC;
		this.I_RELATED1 = I_RELATED1;
		this.I_RELATED2 = I_RELATED2;
		this.I_RELATED3 = I_RELATED3;
		this.I_RELATED4 = I_RELATED4;
		this.I_RELATED5 = I_RELATED5;
		this.I_THUMBNAIL = I_THUMBNAIL;
		this.I_IMAGE = I_IMAGE;
		this.I_SRP = I_SRP;
		this.I_COST = I_COST;
		this.I_AVAIL = I_AVAIL;
		this.I_STOCK = I_STOCK;
		this.I_ISBN = I_ISBN;
		this.I_PAGE = I_PAGE;
		this.I_BACKING = I_BACKING;
		this.I_DIMENSIONS = I_DIMENSIONS;
		
		this.A_FNAME = A_FNAME;
		this.A_LNAME = A_LNAME;
	}
	
	public ItemAuthorModel(ItemModel model) {
		this.I_ID = model.getI_ID();
		this.I_TITLE = model.getI_TITLE();
		this.I_A_ID = model.getI_A_ID();
		this.I_PUB_DATE = model.getI_PUB_DATE();
		this.I_PUBLISHER = model.getI_PUBLISHER();
		this.I_SUBJECT = model.getI_SUBJECT(); 
		this.I_DESC = model.getI_DESC();
		this.I_RELATED1 = model.getI_RELATED1();
		this.I_RELATED2 = model.getI_RELATED2();
		this.I_RELATED3 = model.getI_RELATED3();
		this.I_RELATED4 = model.getI_RELATED4();
		this.I_RELATED5 = model.getI_RELATED5();
		this.I_THUMBNAIL = model.getI_THUMBNAIL();
		this.I_IMAGE = model.getI_IMAGE();
		this.I_SRP = model.getI_SRP();
		this.I_COST = model.getI_COST();
		this.I_AVAIL = model.getI_AVAIL();
		this.I_STOCK = model.getI_STOCK();
		this.I_ISBN = model.getI_ISBN();
		this.I_PAGE = model.getI_PAGE();
		this.I_BACKING = model.getI_BACKING();
		this.I_DIMENSIONS = model.getI_DIMENSIONS();
	}

	/**
	 * @return
	 */
	public String getA_FNAME() {
		return A_FNAME;
	}

	/**
	 * @return
	 */
	public String getA_LNAME() {
		return A_LNAME;
	}

	/**
	 * @return
	 */
	public Integer getI_A_ID() {
		return I_A_ID;
	}

	/**
	 * @return
	 */
	public Date getI_AVAIL() {
		return I_AVAIL;
	}

	/**
	 * @return
	 */
	public String getI_BACKING() {
		return I_BACKING;
	}

	/**
	 * @return
	 */
	public Double getI_COST() {
		return I_COST;
	}

	/**
	 * @return
	 */
	public String getI_DESC() {
		return I_DESC;
	}

	/**
	 * @return
	 */
	public String getI_DIMENSIONS() {
		return I_DIMENSIONS;
	}

	/**
	 * @return
	 */
	public Integer getI_ID() {
		return I_ID;
	}

	/**
	 * @return
	 */
	public String getI_IMAGE() {
		return I_IMAGE;
	}

	/**
	 * @return
	 */
	public String getI_ISBN() {
		return I_ISBN;
	}

	/**
	 * @return
	 */
	public Integer getI_PAGE() {
		return I_PAGE;
	}

	/**
	 * @return
	 */
	public Date getI_PUB_DATE() {
		return I_PUB_DATE;
	}

	/**
	 * @return
	 */
	public String getI_PUBLISHER() {
		return I_PUBLISHER;
	}

	/**
	 * @return
	 */
	public Integer getI_RELATED1() {
		return I_RELATED1;
	}

	/**
	 * @return
	 */
	public Integer getI_RELATED2() {
		return I_RELATED2;
	}

	/**
	 * @return
	 */
	public Integer getI_RELATED3() {
		return I_RELATED3;
	}

	/**
	 * @return
	 */
	public Integer getI_RELATED4() {
		return I_RELATED4;
	}

	/**
	 * @return
	 */
	public Integer getI_RELATED5() {
		return I_RELATED5;
	}

	/**
	 * @return
	 */
	public Double getI_SRP() {
		return I_SRP;
	}

	/**
	 * @return
	 */
	public Integer getI_STOCK() {
		return I_STOCK;
	}

	/**
	 * @return
	 */
	public String getI_SUBJECT() {
		return I_SUBJECT;
	}

	/**
	 * @return
	 */
	public String getI_THUMBNAIL() {
		return I_THUMBNAIL;
	}

	/**
	 * @return
	 */
	public String getI_TITLE() {
		return I_TITLE;
	}

	/**
	 * @param string
	 */
	public void setA_FNAME(String string) {
		A_FNAME = string;
	}

	/**
	 * @param string
	 */
	public void setA_LNAME(String string) {
		A_LNAME = string;
	}

	/**
	 * @param integer
	 */
	public void setI_A_ID(Integer integer) {
		I_A_ID = integer;
	}

	/**
	 * @param date
	 */
	public void setI_AVAIL(Date date) {
		I_AVAIL = date;
	}

	/**
	 * @param string
	 */
	public void setI_BACKING(String string) {
		I_BACKING = string;
	}

	/**
	 * @param double1
	 */
	public void setI_COST(Double double1) {
		I_COST = double1;
	}

	/**
	 * @param string
	 */
	public void setI_DESC(String string) {
		I_DESC = string;
	}

	/**
	 * @param string
	 */
	public void setI_DIMENSIONS(String string) {
		I_DIMENSIONS = string;
	}

	/**
	 * @param integer
	 */
	public void setI_ID(Integer integer) {
		I_ID = integer;
	}

	/**
	 * @param string
	 */
	public void setI_IMAGE(String string) {
		I_IMAGE = string;
	}

	/**
	 * @param string
	 */
	public void setI_ISBN(String string) {
		I_ISBN = string;
	}

	/**
	 * @param integer
	 */
	public void setI_PAGE(Integer integer) {
		I_PAGE = integer;
	}

	/**
	 * @param date
	 */
	public void setI_PUB_DATE(Date date) {
		I_PUB_DATE = date;
	}

	/**
	 * @param string
	 */
	public void setI_PUBLISHER(String string) {
		I_PUBLISHER = string;
	}

	/**
	 * @param integer
	 */
	public void setI_RELATED1(Integer integer) {
		I_RELATED1 = integer;
	}

	/**
	 * @param integer
	 */
	public void setI_RELATED2(Integer integer) {
		I_RELATED2 = integer;
	}

	/**
	 * @param integer
	 */
	public void setI_RELATED3(Integer integer) {
		I_RELATED3 = integer;
	}

	/**
	 * @param integer
	 */
	public void setI_RELATED4(Integer integer) {
		I_RELATED4 = integer;
	}

	/**
	 * @param integer
	 */
	public void setI_RELATED5(Integer integer) {
		I_RELATED5 = integer;
	}

	/**
	 * @param double1
	 */
	public void setI_SRP(Double double1) {
		I_SRP = double1;
	}

	/**
	 * @param integer
	 */
	public void setI_STOCK(Integer integer) {
		I_STOCK = integer;
	}

	/**
	 * @param string
	 */
	public void setI_SUBJECT(String string) {
		I_SUBJECT = string;
	}

	/**
	 * @param string
	 */
	public void setI_THUMBNAIL(String string) {
		I_THUMBNAIL = string;
	}

	/**
	 * @param string
	 */
	public void setI_TITLE(String string) {
		I_TITLE = string;
	}

}
