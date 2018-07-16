package edu.nyu.pdsg.tpcw.ejb.order;

import java.io.Serializable;

/**
 * Order bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class BigOrderLineModel implements Serializable {

	private Integer OL_ID;
	private Integer OL_O_ID;
	
	private Integer I_ID;
	private String I_TITLE;
	private String I_PUBLISHER;
	private Double I_COST;

	private Integer OL_QTY;
	private Double OL_DISCOUNT;
	private String OL_COMMENTS;

	public BigOrderLineModel(Integer OL_ID, Integer OL_O_ID, Integer I_ID, Integer OL_QTY,
							Double OL_DISCOUNT, String OL_COMMENTS) {
		this.OL_ID = OL_ID;
		this.OL_O_ID = OL_O_ID;
		this.I_ID = I_ID;
		this.OL_QTY = OL_QTY;
		this.OL_DISCOUNT = OL_DISCOUNT;
		this.OL_COMMENTS = OL_COMMENTS;
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
	public Integer getI_ID() {
		return I_ID;
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
	public String getI_TITLE() {
		return I_TITLE;
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
	 * @param double1
	 */
	public void setI_COST(Double double1) {
		I_COST = double1;
	}

	/**
	 * @param string
	 */
	public void setI_PUBLISHER(String string) {
		I_PUBLISHER = string;
	}

	/**
	 * @param string
	 */
	public void setI_TITLE(String string) {
		I_TITLE = string;
	}

}
