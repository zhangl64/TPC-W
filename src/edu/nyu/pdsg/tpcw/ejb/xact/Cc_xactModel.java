package edu.nyu.pdsg.tpcw.ejb.xact;

import java.io.Serializable;
import java.util.Date;

/**
 * CC_XACT bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class Cc_xactModel implements Serializable {

	private Integer CX_O_ID;
	private String CX_TYPE;
	private String CX_NUM;
	private String CX_NAME;
	private Date CX_EXPIRE;
	private String CX_AUTH_ID;
	private Double CX_XACT_AMT;
	private Date CX_XACT_DATE;
	private Integer CX_CO_ID;

	public Cc_xactModel(Integer CX_O_ID, String CX_TYPE, String CX_NUM, String CX_NAME, Date CX_EXPIRE,
						String CX_AUTH_ID, Double CX_XACT_AMT, Date CX_XACT_DATE, Integer CX_CO_ID) {							
		this.CX_O_ID = CX_O_ID;
		this.CX_TYPE = CX_TYPE;
		this.CX_NUM = CX_NUM;
		this.CX_NAME = CX_NAME;
		this.CX_EXPIRE = CX_EXPIRE;
		this.CX_AUTH_ID = CX_AUTH_ID;
		this.CX_XACT_AMT = CX_XACT_AMT;
		this.CX_XACT_DATE = CX_XACT_DATE;
		this.CX_CO_ID = CX_CO_ID;
	}

	public Cc_xactModel(Integer CX_O_ID) {							
		this.CX_O_ID = CX_O_ID;
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
	public Integer getCX_CO_ID() {
		return CX_CO_ID;
	}

	/**
	 * @return
	 */
	public Date getCX_EXPIRE() {
		return CX_EXPIRE;
	}

	/**
	 * @return
	 */
	public String getCX_NAME() {
		return CX_NAME;
	}

	/**
	 * @return
	 */
	public String getCX_NUM() {
		return CX_NUM;
	}

	/**
	 * @return
	 */
	public Integer getCX_O_ID() {
		return CX_O_ID;
	}

	/**
	 * @return
	 */
	public String getCX_TYPE() {
		return CX_TYPE;
	}

	/**
	 * @return
	 */
	public Double getCX_XACT_AMT() {
		return CX_XACT_AMT;
	}

	/**
	 * @return
	 */
	public Date getCX_XACT_DATE() {
		return CX_XACT_DATE;
	}

	/**
	 * @param string
	 */
	public void setCX_AUTH_ID(String string) {
		CX_AUTH_ID = string;
	}

	/**
	 * @param integer
	 */
	public void setCX_CO_ID(Integer integer) {
		CX_CO_ID = integer;
	}

	/**
	 * @param date
	 */
	public void setCX_EXPIRE(Date date) {
		CX_EXPIRE = date;
	}

	/**
	 * @param string
	 */
	public void setCX_NAME(String string) {
		CX_NAME = string;
	}

	/**
	 * @param string
	 */
	public void setCX_NUM(String string) {
		CX_NUM = string;
	}

	/**
	 * @param string
	 */
	public void setCX_TYPE(String string) {
		CX_TYPE = string;
	}

	/**
	 * @param double1
	 */
	public void setCX_XACT_AMT(Double double1) {
		CX_XACT_AMT = double1;
	}

	/**
	 * @param date
	 */
	public void setCX_XACT_DATE(Date date) {
		CX_XACT_DATE = date;
	}

}
