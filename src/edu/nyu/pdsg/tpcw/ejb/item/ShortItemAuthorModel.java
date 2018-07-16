package edu.nyu.pdsg.tpcw.ejb.item;

import java.io.Serializable;

/**
 * Item bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class ShortItemAuthorModel implements Serializable {

	private Integer I_ID;
	private String I_TITLE;
	private Integer I_A_ID;
	
	private String A_FNAME;
	private String A_LNAME;

	public ShortItemAuthorModel(Integer I_ID, String I_TITLE, Integer I_A_ID) {
		this.I_ID = I_ID;
		this.I_TITLE = I_TITLE;
		this.I_A_ID = I_A_ID;
	}
	
	public ShortItemAuthorModel() {
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
	public Integer getI_ID() {
		return I_ID;
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
	 * @param integer
	 */
	public void setI_ID(Integer integer) {
		I_ID = integer;
	}

	/**
	 * @param string
	 */
	public void setI_TITLE(String string) {
		I_TITLE = string;
	}

}
