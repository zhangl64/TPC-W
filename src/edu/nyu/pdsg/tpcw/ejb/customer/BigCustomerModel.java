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
public class BigCustomerModel implements Serializable {

	private Integer C_ID;
	private String C_UNAME;
	private String C_PASSWD;
	private String C_FNAME;
	private String C_LNAME;

	private Integer ADDR_ID;
	private String ADDR_STREET1;
	private String ADDR_STREET2;
	private String ADDR_CITY;
	private String ADDR_STATE;
	private String ADDR_ZIP;
	
	private Integer CO_ID;
	private String CO_NAME;
	
	private String C_PHONE;
	private String C_EMAIL;
	private Double C_DISCOUNT;
	private Date C_BIRTHDATE;
	private String C_DATA;
	
	public BigCustomerModel(Integer C_ID) {
		this.C_ID = C_ID;
	}

	public BigCustomerModel(Integer C_ID, String C_UNAME, String C_PASSWD, String C_FNAME, String C_LNAME, Integer ADDR_ID, String C_PHONE, String C_EMAIL, Double C_DISCOUNT, Date C_BIRTHDATE, String C_DATA) {
		this.C_ID = C_ID;
		this.C_UNAME = C_UNAME; 
		this.C_PASSWD = C_PASSWD;
		this.C_FNAME = C_FNAME;
		this.C_LNAME = C_LNAME;
		this.ADDR_ID = ADDR_ID;
		this.C_PHONE = C_PHONE;
		this.C_EMAIL = C_EMAIL;
		this.C_DISCOUNT = C_DISCOUNT; 
		this.C_BIRTHDATE = C_BIRTHDATE;
		this.C_DATA = C_DATA;
	}
	/**
	 * @return
	 */
	public String getADDR_CITY() {
		return ADDR_CITY;
	}

	/**
	 * @return
	 */
	public Integer getADDR_ID() {
		return ADDR_ID;
	}

	/**
	 * @return
	 */
	public String getADDR_STATE() {
		return ADDR_STATE;
	}

	/**
	 * @return
	 */
	public String getADDR_STREET1() {
		return ADDR_STREET1;
	}

	/**
	 * @return
	 */
	public String getADDR_STREET2() {
		return ADDR_STREET2;
	}

	/**
	 * @return
	 */
	public String getADDR_ZIP() {
		return ADDR_ZIP;
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
	public String getC_LNAME() {
		return C_LNAME;
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
	public String getC_UNAME() {
		return C_UNAME;
	}

	/**
	 * @return
	 */
	public Integer getCO_ID() {
		return CO_ID;
	}

	/**
	 * @return
	 */
	public String getCO_NAME() {
		return CO_NAME;
	}

	/**
	 * @param string
	 */
	public void setADDR_CITY(String string) {
		ADDR_CITY = string;
	}

	/**
	 * @param integer
	 */
	public void setADDR_ID(Integer integer) {
		ADDR_ID = integer;
	}

	/**
	 * @param string
	 */
	public void setADDR_STATE(String string) {
		ADDR_STATE = string;
	}

	/**
	 * @param string
	 */
	public void setADDR_STREET1(String string) {
		ADDR_STREET1 = string;
	}

	/**
	 * @param string
	 */
	public void setADDR_STREET2(String string) {
		ADDR_STREET2 = string;
	}

	/**
	 * @param string
	 */
	public void setADDR_ZIP(String string) {
		ADDR_ZIP = string;
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
	 * @param string
	 */
	public void setC_FNAME(String string) {
		C_FNAME = string;
	}

	/**
	 * @param string
	 */
	public void setC_LNAME(String string) {
		C_LNAME = string;
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
	 * @param string
	 */
	public void setC_UNAME(String string) {
		C_UNAME = string;
	}

	/**
	 * @param integer
	 */
	public void setCO_ID(Integer integer) {
		CO_ID = integer;
	}

	/**
	 * @param string
	 */
	public void setCO_NAME(String string) {
		CO_NAME = string;
	}

}
