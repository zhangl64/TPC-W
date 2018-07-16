package edu.nyu.pdsg.tpcw.ejb.cart;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import edu.nyu.pdsg.tpcw.ejb.adminportal.AdminPortal;
import edu.nyu.pdsg.tpcw.ejb.adminportal.AdminPortalHome;
import edu.nyu.pdsg.tpcw.ejb.catalog.Catalog;
import edu.nyu.pdsg.tpcw.ejb.catalog.CatalogHome;
import edu.nyu.pdsg.tpcw.ejb.item.ItemModel;

/**
 * Shopping Cart bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public class ShoppingCartBean implements SessionBean {
	
	// C_ID of the customer
	private Integer C_ID;

	private Date SC_DATE;
	
	// integer in cents
	private int SC_SUB_TOTAL;
	
	// integer in cents
	private int SC_TAX;
	
	// integer in cents
	private int SC_SHIP_COST;

	// integer in cents
	private int SC_TOTAL;
	private String SC_C_FNAME;
	private String SC_C_LNAME;

	// integer in percents
	private int SC_C_DISCOUNT;
	
	// cart items
	private HashMap cart;

	// reference to catalog
	private Catalog catalog;
	
	// reference to the admin portal
	private AdminPortal adminPortal;
	
	private SessionContext ctx;
	
	public void ejbCreate()  throws CreateException {
		cart = new HashMap();
		try {
			InitialContext initial = new InitialContext();
			Object objref = initial.lookup("java:comp/env/ejb/Catalog");
			catalog = ((CatalogHome)PortableRemoteObject.narrow(objref, CatalogHome.class)).create();
			
			objref = initial.lookup("java:comp/env/ejb/AdminPortal");
			adminPortal = ((AdminPortalHome)PortableRemoteObject.narrow(objref, AdminPortalHome.class)).create();
		} catch (Exception ex) {
			throw new EJBException(ex.getMessage());
		}
	}
	
	// --------------------- business methods

	/**
	 * @return customer's ID
	 */
	public Integer getC_ID() {
		return C_ID;
	}

	/**
	 * @return customer's first name
	 */
	public String getSC_C_FNAME() {
		return SC_C_FNAME;
	}
	
	/**
	 * @return customer's last name
	 */
	public String getSC_C_LNAME() {
		return SC_C_LNAME;
	}
	
	/**
	 * sets first name
	 */
	public void setSC_C_FNAME(String lname) {
		this.SC_C_FNAME = lname;
	}
	
	/**
	 * sets ID
	 */
	public void setC_ID(Integer id) {
		this.C_ID = id;
	}
	
	/**
	 * sets last name
	 */
	public void setSC_C_LNAME(String fname) {
		this.SC_C_LNAME = fname;
	}

	/**
	 * sets discount
	 */
	public void setSC_C_DISCOUNT(int i) {
		this.SC_C_DISCOUNT = i;
	}

	/**
	 * Returns the full data of the cart
	 */	
	public ShoppingCartModel getModel() {
		return new ShoppingCartModel(C_ID, SC_DATE, SC_SUB_TOTAL, SC_TAX, SC_SHIP_COST,
										SC_TOTAL, SC_C_FNAME,	SC_C_LNAME, SC_C_DISCOUNT, cart, null);
	}

	/**
	 * Adds apecified item with qty to the shopping cart, and returns the full
	 * shopping cart data.
	 * @param I_ID item's ID
	 * @param qty quantity to add
	 * @return  full cart data
	 */
	public ShoppingCartModel addItem(Integer I_ID, int qty) {
		Object obj = cart.get(I_ID);
		if (obj == null) {
			// there is no such item in the shopping cart - obtain data from the catalog 
			// and add it to the cart
			ItemModel item = null; 
			try {
				item = catalog.getItemForCart(I_ID);
			} catch (RemoteException re) {
				throw new EJBException(re);
			}
			CartItem cartItem = new CartItem(I_ID, qty, (int)((item.getI_COST().doubleValue())*100), 
											(int)((item.getI_SRP().doubleValue())*100), item.getI_TITLE(), 
											item.getI_BACKING());
			cart.put(I_ID, cartItem);
		} else {
			// there is such an item in the cart already - update quantity
			CartItem cartItem = (CartItem)obj;
			cartItem.setSCL_QTY(cartItem.getSCL_QTY() + qty);
		}
		updateFields();
		return new ShoppingCartModel(C_ID, SC_DATE, SC_SUB_TOTAL, SC_TAX, SC_SHIP_COST,
										SC_TOTAL, SC_C_FNAME,	SC_C_LNAME, SC_C_DISCOUNT, 
										cart, null);
	}

	/**
	 * Updates the quantities in the shopping cart.
	 * @param ids Collection of Integer objects with I_IDs
	 * @param quantities Collection of Integer objects with new quantities
	 * @return
	 * @throws RemoteException
	 */
	public ShoppingCartModel update(Vector ids, Vector quantities) {
		int size = ids.size();

		// update quantities
		for (int i = 0; i < size; i++) {
			Object obj = cart.get(ids.elementAt(i));
			if (obj != null) {
				CartItem cartItem = (CartItem)obj;
				int qty = ((Integer)(quantities.elementAt(i))).intValue();
				if (qty == 0) {
					cart.remove(ids.elementAt(i));
				} else {
					cartItem.setSCL_QTY(qty);
				}
			}
		}
		updateFields();
		// return cart model
		return new ShoppingCartModel(C_ID, SC_DATE, SC_SUB_TOTAL, SC_TAX, SC_SHIP_COST,
										SC_TOTAL, SC_C_FNAME,	SC_C_LNAME, SC_C_DISCOUNT, cart, null);
	}
	
	/**
	 * Does the preparation work on the shopping cart, before checkout,
	 * as described in the clause 2.6.3.3.
	 * 
	 * @return the updated ShoppingCartModel object
	 */
	public ShoppingCartModel prepareToBuy() {
		int sub_total = 0;
		int sum_qty = 0;
		Iterator items = cart.values().iterator();
		while (items.hasNext()) {
			CartItem ci = (CartItem)items.next();
			Integer i = ci.getSCL_I_ID();
			
			// update item's cost
			try {
				ItemModel itemModel = catalog.getItemForCart(i);
				ci.setSCL_COST((int)(itemModel.getI_COST().doubleValue()*100));
			} catch (RemoteException re) {
				throw new EJBException(re);
			}
			
			sub_total += ci.getSCL_COST()*ci.getSCL_QTY();
			sum_qty += ci.getSCL_QTY();
		}
		
		// update all other fields of the Shopping Cart
		SC_DATE = new Date();
		SC_SUB_TOTAL = (sub_total*(100-SC_C_DISCOUNT))/100;
		SC_TAX = (int)(((double)SC_SUB_TOTAL)*0.0825);
		SC_SHIP_COST = 300 + 100*sum_qty;
		SC_TOTAL = SC_SUB_TOTAL + SC_SHIP_COST + SC_TAX;
		
		// return cart model
		return new ShoppingCartModel(C_ID, SC_DATE, SC_SUB_TOTAL, SC_TAX, SC_SHIP_COST,
										SC_TOTAL, SC_C_FNAME,	SC_C_LNAME, SC_C_DISCOUNT, cart, null);
	}

	/**
	 * 
	 * Performs order creation - as specified in the Buy Confirm WI 
	 * (clause 2.7.3)
	 * 
	 * @return  the full shopping cart model object with O_ID
	 */
	public ShoppingCartModel processOrder(String CC_TYPE, String CC_NUMBER, String CC_NAME, Date CC_EXPIRY, String SHIPPING, String STREET_1, String STREET_2, 
				String CITY, String STATE, String ZIP, Integer CO_ID) {
		ShoppingCartModel cartModel = null;

		// create the order through admin portal		
		Integer O_ID;
		try {
			O_ID = adminPortal.createOrder(C_ID, cart.values().iterator(), SC_SUB_TOTAL, SC_TAX, SC_SHIP_COST, SC_TOTAL, SC_C_DISCOUNT,
								CC_TYPE, CC_NUMBER, CC_NAME, CC_EXPIRY, SHIPPING, STREET_1, STREET_2, 
								CITY, STATE, ZIP, CO_ID);
		} catch (RemoteException re) {
			throw new EJBException(re);
		}
		
		// form ShoppingCartModel object to return
		cartModel = new ShoppingCartModel(C_ID, SC_DATE, SC_SUB_TOTAL, SC_TAX, SC_SHIP_COST,
									SC_TOTAL, SC_C_FNAME, SC_C_LNAME, SC_C_DISCOUNT, cart, O_ID);
									
		// update the QTY in the inventory
		Iterator iter = cart.values().iterator();
		try {
			while (iter.hasNext()) {
				CartItem lineItem = (CartItem)iter.next();
				catalog.reduceItemQty(lineItem.getSCL_I_ID(), lineItem.getSCL_QTY());
			}
		} catch (RemoteException re) {
			throw new EJBException(re);
		}
		
		// emptyy the cart, update fields
		cart = new HashMap();
		SC_DATE = new Date();
		SC_SUB_TOTAL = 0;
		SC_TAX = 0;
		SC_SHIP_COST = 0;
		SC_TOTAL = 0;
		
		return cartModel;
	}

	// ------------------------- private methods ---------------------------------------
	
	/**
	 * updates  SC_SUB_TOTAL and SC_DATE
	 */
	private void updateFields() {
		int newSubTotal = 0;
		Collection set = cart.values();
		Iterator iter = set.iterator();
		while(iter.hasNext()) {
			CartItem cartItem = (CartItem)(iter.next());
			newSubTotal += cartItem.getSCL_COST()*cartItem.getSCL_QTY();
		}
		SC_SUB_TOTAL = newSubTotal;
		SC_DATE = new Date();
	}

	//-------------------------- utility methods ------------------------------------------

	public void setSessionContext(SessionContext context) {
		this.ctx = context;
	}

	public void unsetSessionContext() {
		ctx = null;
	}
	
	public void ejbActivate() {}
	
	public void ejbPassivate() {}
	
	public void ejbRemove() {
		cart.clear();
		cart = null;
		catalog = null;
		adminPortal = null;
	}

}
