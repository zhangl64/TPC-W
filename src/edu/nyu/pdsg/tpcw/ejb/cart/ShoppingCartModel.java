package edu.nyu.pdsg.tpcw.ejb.cart;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Shopping Cart bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public class ShoppingCartModel implements Serializable {

	private Integer C_ID;
	private Date SC_DATE;
	private int SC_SUB_TOTAL;
	private int SC_TAX;
	private int SC_SHIP_COST;
	private int SC_TOTAL;
	private String SC_C_FNAME;
	private String SC_C_LNAME;
	private int SC_C_DISCOUNT;
	
	// ID of the order when order with this shopping cart is committed
	// needed to present to the client
	private Integer O_ID;
	
	// cart items
	private HashMap cart;
	
	public ShoppingCartModel(Integer C_ID, Date SC_DATE, int SC_SUB_TOTAL, int SC_TAX, int SC_SHIP_COST,
							int SC_TOTAL, String SC_C_FNAME,	String SC_C_LNAME, int SC_C_DISCOUNT, HashMap cart, Integer O_ID) {
		
		this.C_ID = C_ID;
		this.SC_DATE = SC_DATE;
		this.SC_SUB_TOTAL = SC_SUB_TOTAL;
		this.SC_TAX = SC_TAX;
		this.SC_SHIP_COST = SC_SHIP_COST;
		this.SC_TOTAL = SC_TOTAL;
		this.SC_C_FNAME = SC_C_FNAME;
		this.SC_C_LNAME = SC_C_LNAME;
		this.SC_C_DISCOUNT = SC_C_DISCOUNT;
		this.cart = cart;
		this.O_ID = O_ID;
	}
	
	public Iterator lineItems() {
		return cart.values().iterator();
	}

	public Date getSC_DATE() {
		return SC_DATE;
	}

	public int getSC_SHIP_COST() {
		return SC_SHIP_COST;
	}

	public int getSC_SUB_TOTAL() {
		return SC_SUB_TOTAL;
	}

	public int getSC_TAX() {
		return SC_TAX;
	}

	public int getSC_TOTAL() {
		return SC_TOTAL;
	}

	public int getSC_C_DISCOUNT() {
		return SC_C_DISCOUNT;
	}

	public String getSC_C_FNAME() {
		return SC_C_FNAME;
	}

	public String getSC_C_LNAME() {
		return SC_C_LNAME;
	}
	public Integer getC_ID() {
		return C_ID;
	}

	public void setC_ID(Integer integer) {
		C_ID = integer;
	}

	public Integer getO_ID() {
		return O_ID;
	}

}
