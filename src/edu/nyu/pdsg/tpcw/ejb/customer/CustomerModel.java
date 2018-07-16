package edu.nyu.pdsg.tpcw.ejb.customer;

import java.io.Serializable;
import java.util.Date;

/**
 * Customer bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class CustomerModel implements Serializable {

	private Integer C_ID;
	private String C_UNAME;
	private String C_PASSWD;
	private String C_FNAME;
	private String C_LNAME;
	private Integer C_ADDR_ID;
	private String C_PHONE;
	private String C_EMAIL;
	private Date C_SINCE;
	private Date C_LAST_VISIT;
	private Date C_LOGIN; 
	private Date C_EXPIRATION;
	private Double C_DISCOUNT;
	private Double C_BALANCE;
	private Double C_YTD_PMT;
	private Date C_BIRTHDATE;
	private String C_DATA;
	
	public CustomerModel(Integer C_ID, String C_UNAME, String C_PASSWD, String C_FNAME, String C_LNAME,
						Integer C_ADDR_ID, String C_PHONE, String C_EMAIL, Date C_SINCE, Date C_LAST_VISIT,
						Date C_LOGIN, Date C_EXPIRATION, Double C_DISCOUNT, Double C_BALANCE, 
						Double C_YTD_PMT, Date C_BIRTHDATE, String C_DATA) {
		this.C_ID = C_ID;
		this.C_UNAME = C_UNAME;
		this.C_PASSWD = C_PASSWD;
		this.C_FNAME = C_FNAME;
		this.C_LNAME = C_LNAME;
		this.C_ADDR_ID = C_ADDR_ID;
		this.C_PHONE = C_PHONE;
		this.C_EMAIL = C_EMAIL;
		this.C_SINCE = C_SINCE;
		this.C_LAST_VISIT = C_LAST_VISIT;
		this.C_LOGIN = C_LOGIN; 
		this.C_EXPIRATION = C_EXPIRATION;
		this.C_DISCOUNT = C_DISCOUNT;
		this.C_BALANCE = C_BALANCE;
		this.C_YTD_PMT = C_YTD_PMT;
		this.C_BIRTHDATE = C_BIRTHDATE;
		this.C_DATA = C_DATA;
	}

	public CustomerModel(Integer C_ID) {
		this.C_ID = C_ID;
	}

	/**
	 * @return
	 */
	public Integer getC_ADDR_ID() {
		return C_ADDR_ID;
	}

	/**
	 * @return
	 */
	public Double getC_BALANCE() {
		return C_BALANCE;
	}

	/**
	 * @return
	 */
	public Date getC_BIRTHDATE() {
		return C_BIRTHDATE;
	}

	/**
	 * @return
	 */
	public String getC_DATA() {
		return C_DATA;
	}

	/**
	 * @return
	 */
	public Double getC_DISCOUNT() {
		return C_DISCOUNT;
	}

	/**
	 * @return
	 */
	public String getC_EMAIL() {
		return C_EMAIL;
	}

	/**
	 * @return
	 */
	public Date getC_EXPIRATION() {
		return C_EXPIRATION;
	}

	/**
	 * @return
	 */
	public String getC_FNAME() {
		return C_FNAME;
	}

	/**
	 * @return
	 */
	public Integer getC_ID() {
		return C_ID;
	}

	/**
	 * @return
	 */
	public Date getC_LAST_VISIT() {
		return C_LAST_VISIT;
	}

	/**
	 * @return
	 */
	public String getC_LNAME() {
		return C_LNAME;
	}

	/**
	 * @return
	 */
	public Date getC_LOGIN() {
		return C_LOGIN;
	}

	/**
	 * @return
	 */
	public String getC_PASSWD() {
		return C_PASSWD;
	}

	/**
	 * @return
	 */
	public String getC_PHONE() {
		return C_PHONE;
	}

	/**
	 * @return
	 */
	public Date getC_SINCE() {
		return C_SINCE;
	}

	/**
	 * @return
	 */
	public String getC_UNAME() {
		return C_UNAME;
	}

	/**
	 * @return
	 */
	public Double getC_YTD_PMT() {
		return C_YTD_PMT;
	}

	/**
	 * @param integer
	 */
	public void setC_ADDR_ID(Integer integer) {
		C_ADDR_ID = integer;
	}

	/**
	 * @param double1
	 */
	public void setC_BALANCE(Double double1) {
		C_BALANCE = double1;
	}

	/**
	 * @param date
	 */
	public void setC_BIRTHDATE(Date date) {
		C_BIRTHDATE = date;
	}

	/**
	 * @param string
	 */
	public void setC_DATA(String string) {
		C_DATA = string;
	}

	/**
	 * @param double1
	 */
	public void setC_DISCOUNT(Double double1) {
		C_DISCOUNT = double1;
	}

	/**
	 * @param string
	 */
	public void setC_EMAIL(String string) {
		C_EMAIL = string;
	}

	/**
	 * @param date
	 */
	public void setC_EXPIRATION(Date date) {
		C_EXPIRATION = date;
	}

	/**
	 * @param string
	 */
	public void setC_FNAME(String string) {
		C_FNAME = string;
	}

	/**
	 * @param date
	 */
	public void setC_LAST_VISIT(Date date) {
		C_LAST_VISIT = date;
	}

	/**
	 * @param string
	 */
	public void setC_LNAME(String string) {
		C_LNAME = string;
	}

	/**
	 * @param date
	 */
	public void setC_LOGIN(Date date) {
		C_LOGIN = date;
	}

	/**
	 * @param string
	 */
	public void setC_PASSWD(String string) {
		C_PASSWD = string;
	}

	/**
	 * @param string
	 */
	public void setC_PHONE(String string) {
		C_PHONE = string;
	}

	/**
	 * @param date
	 */
	public void setC_SINCE(Date date) {
		C_SINCE = date;
	}

	/**
	 * @param string
	 */
	public void setC_UNAME(String string) {
		C_UNAME = string;
	}

	/**
	 * @param double1
	 */
	public void setC_YTD_PMT(Double double1) {
		C_YTD_PMT = double1;
	}

}
