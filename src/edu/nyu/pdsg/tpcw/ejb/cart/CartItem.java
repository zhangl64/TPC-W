package edu.nyu.pdsg.tpcw.ejb.cart;

import java.io.Serializable;

/**
 * Shopping Cart bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public class CartItem implements Serializable {
	
	private Integer SCL_I_ID;
	private int SCL_QTY;
	private int SCL_COST;
	private int SCL_SRP;
	private String SCL_TITLE;
	private String SCL_BACKING;
	
	public CartItem(Integer SCL_I_ID, int SCL_QTY, int SCL_COST, int SCL_SRP, String SCL_TITLE, String SCL_BACKING) {
		this.SCL_I_ID = SCL_I_ID;
		this.SCL_QTY = SCL_QTY;
		this.SCL_COST = SCL_COST;
		this.SCL_SRP = SCL_SRP;
		this.SCL_TITLE = SCL_TITLE;
		this.SCL_BACKING = SCL_BACKING;
	}

	public CartItem(Integer SCL_I_ID) {
		this.SCL_I_ID = SCL_I_ID;
	}

	public Integer getSCL_I_ID() {
		return SCL_I_ID;
	}

	public int getSCL_QTY() {
		return SCL_QTY;
	}

	public void setSCL_QTY(int i) {
		SCL_QTY = i;
	}

	public String getSCL_BACKING() {
		return SCL_BACKING;
	}

	public int getSCL_COST() {
		return SCL_COST;
	}

	public int getSCL_SRP() {
		return SCL_SRP;
	}

	public String getSCL_TITLE() {
		return SCL_TITLE;
	}

	public void setSCL_COST(int sCL_COST) {
		SCL_COST = sCL_COST;
	}

}
