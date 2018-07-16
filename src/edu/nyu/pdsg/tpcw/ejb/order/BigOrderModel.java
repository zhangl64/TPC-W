package edu.nyu.pdsg.tpcw.ejb.order;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import edu.nyu.pdsg.tpcw.ejb.address.AddressModel;

/**
 * Order bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class BigOrderModel implements Serializable {

	private Integer O_ID;

	private Integer C_ID;
	private String C_FNAME;
	private String C_LNAME;
	private String C_PHONE;
	private String C_EMAIL;

	private Date O_DATE;
	private Double O_SUB_TOTAL;
	private Double O_TAX;
	private Double O_TOTAL;
	private String O_SHIP_TYPE;
	private Date O_SHIP_DATE;
	private String O_STATUS;

	private AddressModel BILL_ADDR;
	private String BILL_CO_NAME;
	
	private AddressModel SHIP_ADDR;
	private String SHIP_CO_NAME;

	/**
	 * Lineitems, each of BigOrderLineModel object
	 */	
	private Collection lineItems;

	private String CX_TYPE;
	private String CX_AUTH_ID;

	public BigOrderModel() {
	}

	public BigOrderModel(Integer O_ID, Integer C_ID, Date O_DATE, Double O_SUB_TOTAL, Double O_TAX, 
						Double O_TOTAL, String O_SHIP_TYPE, Date O_SHIP_DATE, String O_STATUS) {
		this.O_ID = O_ID;
		this.C_ID = C_ID;
		this.O_DATE = O_DATE;
		this.O_SUB_TOTAL = O_SUB_TOTAL;
		this.O_TAX = O_TAX;
		this.O_TOTAL = O_TOTAL;
		this.O_SHIP_TYPE = O_SHIP_TYPE;
		this.O_SHIP_DATE = O_SHIP_DATE;
		this.O_STATUS = O_STATUS;
	}

	/**
	 * @return
	 */
	public AddressModel getBILL_ADDR() {
		return BILL_ADDR;
	}

	/**
	 * @return
	 */
	public String getBILL_CO_NAME() {
		return BILL_CO_NAME;
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
	public String getC_PHONE() {
		return C_PHONE;
	}

	/**
	 * @return
	 */
	public Date getO_DATE() {
		return O_DATE;
	}

	/**
	 * @return
	 */
	public Integer getO_ID() {
		return O_ID;
	}

	/**
	 * @return
	 */
	public Date getO_SHIP_DATE() {
		return O_SHIP_DATE;
	}

	/**
	 * @return
	 */
	public String getO_SHIP_TYPE() {
		return O_SHIP_TYPE;
	}

	/**
	 * @return
	 */
	public String getO_STATUS() {
		return O_STATUS;
	}

	/**
	 * @return
	 */
	public Double getO_SUB_TOTAL() {
		return O_SUB_TOTAL;
	}

	/**
	 * @return
	 */
	public Double getO_TAX() {
		return O_TAX;
	}

	/**
	 * @return
	 */
	public Double getO_TOTAL() {
		return O_TOTAL;
	}

	/**
	 * @return
	 */
	public String getSHIP_CO_NAME() {
		return SHIP_CO_NAME;
	}

	/**
	 * @return
	 */
	public AddressModel getSHIP_ADDR() {
		return SHIP_ADDR;
	}

	/**
	 * @param model
	 */
	public void setBILL_ADDR(AddressModel model) {
		BILL_ADDR = model;
	}

	/**
	 * @param string
	 */
	public void setBILL_CO_NAME(String string) {
		BILL_CO_NAME = string;
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
	public void setC_PHONE(String string) {
		C_PHONE = string;
	}

	/**
	 * @param string
	 */
	public void setSHIP_CO_NAME(String string) {
		SHIP_CO_NAME = string;
	}

	/**
	 * @param model
	 */
	public void setSHIP_ADDR(AddressModel model) {
		SHIP_ADDR = model;
	}

	/**
	 * @return
	 */
	public Collection getLineItems() {
		return lineItems;
	}

	/**
	 * @param collection
	 */
	public void setLineItems(Collection collection) {
		lineItems = collection;
	}
	
	public boolean isEmpty() {
		return (lineItems == null || lineItems.isEmpty());
	}

	/**
	 * @return
	 */
	public String getCX_AUTH_ID() {
		return CX_AUTH_ID;
	}

	/**
	 * @return
	 */
	public String getCX_TYPE() {
		return CX_TYPE;
	}

	/**
	 * @param string
	 */
	public void setCX_AUTH_ID(String string) {
		CX_AUTH_ID = string;
	}

	/**
	 * @param string
	 */
	public void setCX_TYPE(String string) {
		CX_TYPE = string;
	}

}
