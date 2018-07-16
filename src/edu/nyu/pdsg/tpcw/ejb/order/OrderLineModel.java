package edu.nyu.pdsg.tpcw.ejb.order;

import java.io.Serializable;

/**
 * Order bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class OrderLineModel implements Serializable {

	private Integer OL_ID;
	private Integer OL_O_ID;
	private Integer OL_I_ID;
	private Integer OL_QTY;
	private Double OL_DISCOUNT;
	private String OL_COMMENTS;
	
	public OrderLineModel(Integer OL_ID, Integer OL_O_ID, Integer OL_I_ID, Integer OL_QTY, Double OL_DISCOUNT, String OL_COMMENTS) {
		this.OL_ID = OL_ID;
		this.OL_O_ID = OL_O_ID;
		this.OL_I_ID = OL_I_ID;
		this.OL_QTY = OL_QTY;
		this.OL_DISCOUNT = OL_DISCOUNT;
		this.OL_COMMENTS = OL_COMMENTS;
	}

	public OrderLineModel(Integer OL_ID, Integer OL_O_ID) {
		this.OL_ID = OL_ID;
		this.OL_O_ID = OL_O_ID;
	}

	/**
	 * @return
	 */
	public String getOL_COMMENTS() {
		return OL_COMMENTS;
	}

	/**
	 * @return
	 */
	public Double getOL_DISCOUNT() {
		return OL_DISCOUNT;
	}

	/**
	 * @return
	 */
	public Integer getOL_I_ID() {
		return OL_I_ID;
	}

	/**
	 * @return
	 */
	public Integer getOL_ID() {
		return OL_ID;
	}

	/**
	 * @return
	 */
	public Integer getOL_O_ID() {
		return OL_O_ID;
	}

	/**
	 * @return
	 */
	public Integer getOL_QTY() {
		return OL_QTY;
	}

	/**
	 * @param string
	 */
	public void setOL_COMMENTS(String string) {
		OL_COMMENTS = string;
	}

	/**
	 * @param double1
	 */
	public void setOL_DISCOUNT(Double double1) {
		OL_DISCOUNT = double1;
	}

	/**
	 * @param integer
	 */
	public void setOL_I_ID(Integer integer) {
		OL_I_ID = integer;
	}

	/**
	 * @param integer
	 */
	public void setOL_QTY(Integer integer) {
		OL_QTY = integer;
	}

}
