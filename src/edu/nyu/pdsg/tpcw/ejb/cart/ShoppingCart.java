package edu.nyu.pdsg.tpcw.ejb.cart;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Vector;

import javax.ejb.EJBObject;

/**
 * Shopping Cart bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface ShoppingCart extends EJBObject {
	
	/**
	 * @return customer's ID
	 */
	public Integer getC_ID() throws RemoteException;

	/**
	 * @return customer's first name
	 */
	public String getSC_C_FNAME() throws RemoteException;
	
	/**
	 * @return customer's last name
	 */
	public String getSC_C_LNAME() throws RemoteException;
	
	/**
	 * sets first name
	 */
	public void setSC_C_FNAME(String lname) throws RemoteException;
	
	/**
	 * sets last name
	 */
	public void setSC_C_LNAME(String fname) throws RemoteException;

	/**
	 * sets discount
	 */
	public void setSC_C_DISCOUNT(int i) throws RemoteException;

	/**
	 * sets ID
	 */
	public void setC_ID(Integer id) throws RemoteException;
	
	/**
	 * Returns the full data of the cart
	 */	
	public ShoppingCartModel getModel() throws RemoteException;

	/**
	 * Adds apecified item with qty to the shopping cart, and returns the full
	 * shopping cart data.
	 * @param I_ID item's ID
	 * @param qty quantity to add
	 * @return  full cart data
	 */    
	public ShoppingCartModel addItem(Integer I_ID, int qty) throws RemoteException;

	/**
	 * Updates the quantities in the shopping cart.
	 * @param ids Collection of Integer objects with I_IDs
	 * @param quantities Collection of Integer objects with new quantities
	 * @return
	 * @throws RemoteException
	 */
	public ShoppingCartModel update(Vector ids, Vector quantities) throws RemoteException;

	/**
	 * Does the preparation work on the shopping cart, before checkout,
	 * as described in the clause 2.6.3.3.
	 * 
	 * @return the updated ShoppingCartModel object
	 */
	public ShoppingCartModel prepareToBuy() throws RemoteException;

	/**
	 * 
	 * Performs order creation - as specified in the Buy Confirm WI 
	 * (clause 2.7.3)
	 * 
	 * @param CC_TYPE credit card stuff
	 * @param CC_NUMBER
	 * @param CC_NAME
	 * @param CC_EXPIRY
	 * @param SHIPPING type of shipping
	 * @param STREET_1 shipping address stuff
	 * @param STREET_2
	 * @param CITY
	 * @param STATE
	 * @param ZIP
	 * @param CO_ID
	 * @return  the full shopping cart model object with O_ID
	 */	
	public ShoppingCartModel processOrder(String CC_TYPE, String CC_NUMBER, String CC_NAME, Date CC_EXPIRY, String SHIPPING, String STREET_1, String STREET_2, 
				String CITY, String STATE, String ZIP, Integer CO_ID) throws RemoteException;
}