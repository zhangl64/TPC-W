package edu.nyu.pdsg.tpcw.ejb.order;

import java.io.Serializable;
import java.util.Date;

/**
 * Order bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class OrderModel implements Serializable {

	private Integer O_ID;
	private Integer O_C_ID;
	private Date O_DATE;
	private Double O_SUB_TOTAL;
	private Double O_TAX;
	private Double O_TOTAL;
	private String O_SHIP_TYPE;
	private Date O_SHIP_DATE;
	private Integer O_BILL_ADDR_ID;
	private Integer O_SHIP_ADDR_ID;
	private String O_STATUS;
	
	public OrderModel(Integer O_ID, Integer O_C_ID, Date O_DATE, Double O_SUB_TOTAL, Double O_TAX, 
					Double O_TOTAL, String O_SHIP_TYPE, Date O_SHIP_DATE, Integer O_BILL_ADDR_ID,
					Integer O_SHIP_ADDR_ID, String O_STATUS) {
		this.O_ID = O_ID;
		this.O_C_ID = O_C_ID;
		this.O_DATE = O_DATE;
		this.O_SUB_TOTAL = O_SUB_TOTAL;
		this.O_TAX = O_TAX;
		this.O_TOTAL = O_TOTAL;
		this.O_SHIP_TYPE = O_SHIP_TYPE;
		this.O_SHIP_DATE = O_SHIP_DATE;
		this.O_BILL_ADDR_ID = O_BILL_ADDR_ID;
		this.O_SHIP_ADDR_ID = O_SHIP_ADDR_ID;
		this.O_STATUS = O_STATUS;
	}

	public OrderModel(Integer O_ID) {
		this.O_ID = O_ID;
	}
	/**
	 * @return
	 */
	public Integer getO_BILL_ADDR_ID() {
		return O_BILL_ADDR_ID;
	}

	/**
	 * @return
	 */
	public Integer getO_C_ID() {
		return O_C_ID;
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
	public Integer getO_SHIP_ADDR_ID() {
		return O_SHIP_ADDR_ID;
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
	 * @param integer
	 */
	public void setO_BILL_ADDR_ID(Integer integer) {
		O_BILL_ADDR_ID = integer;
	}

	/**
	 * @param integer
	 */
	public void setO_C_ID(Integer integer) {
		O_C_ID = integer;
	}

	/**
	 * @param date
	 */
	public void setO_DATE(Date date) {
		O_DATE = date;
	}

	/**
	 * @param integer
	 */
	public void setO_SHIP_ADDR_ID(Integer integer) {
		O_SHIP_ADDR_ID = integer;
	}

	/**
	 * @param date
	 */
	public void setO_SHIP_DATE(Date date) {
		O_SHIP_DATE = date;
	}

	/**
	 * @param string
	 */
	public void setO_SHIP_TYPE(String string) {
		O_SHIP_TYPE = string;
	}

	/**
	 * @param integer
	 */
	public void setO_STATUS(String s) {
		O_STATUS = s;
	}

	/**
	 * @param double1
	 */
	public void setO_SUB_TOTAL(Double double1) {
		O_SUB_TOTAL = double1;
	}

	/**
	 * @param double1
	 */
	public void setO_TAX(Double double1) {
		O_TAX = double1;
	}

	/**
	 * @param double1
	 */
	public void setO_TOTAL(Double double1) {
		O_TOTAL = double1;
	}

}
