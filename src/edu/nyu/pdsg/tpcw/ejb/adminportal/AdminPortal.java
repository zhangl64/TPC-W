package edu.nyu.pdsg.tpcw.ejb.adminportal;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.EJBObject;

import edu.nyu.pdsg.tpcw.ejb.customer.BigCustomerModel;
import edu.nyu.pdsg.tpcw.ejb.item.ItemAuthorModel;
import edu.nyu.pdsg.tpcw.ejb.order.BigOrderModel;

/**
 * Admin portal session bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface AdminPortal extends EJBObject {
	
	/**
	 * Authenticates a returning customer.
	 * @param uname UNAME of the customer
	 * @param passwd PASSWD of the customer
	 * @return  the Big Customer Model object (with adress and coubtry name fields), 
	 * if authentication is successful, null otherwise
	 */
	public BigCustomerModel authenticate(String uname, String passwd) throws RemoteException;

	/**
	 * Creates a new Customer according to the spec. clause 2.6.3.2
	 * @return  the Big Customer Model object (with adress and coubtry name fields)
	 */
	public BigCustomerModel createNewCustomer(String C_FNAME, String C_LNAME, String ADDR_STREET1, 
							String ADDR_STREET2, String ADDR_CITY, String ADDR_STATE, String ADDR_ZIP, Integer CO_ID, 
							String C_PHONE, String C_EMAIL, Date C_BIRTHDATE, String C_DATA) throws RemoteException;
	
	/**
	 * Creates order in the database.
	 * 
	 * @param C_ID id of the customer
	 * @param lineItems iterator of line items
	 * @param SC_SUB_TOTAL  in cents
	 * @param SC_TAX in cents
	 * @param SC_SHIP_COST in cents 
	 * @param SC_TOTAL in cents 
	 * @param SC_C_DISCOUNT in percents
	 * @param CC_TYPE credit card type
	 * @param CC_NUMBER
	 * @param CC_NAME
	 * @param CC_EXPIRY
	 * @param SHIPPING  shipping type
	 * @param STREET_1 shipping address stuff
	 * @param STREET_2
	 * @param CITY
	 * @param STATE
	 * @param ZIP
	 * @param CO_ID
	 * @return id of the irder (O_ID)
	 */
	public Integer createOrder(Integer C_ID, Iterator lineItems, int SC_SUB_TOTAL, int SC_TAX, int SC_SHIP_COST, int SC_TOTAL, int SC_C_DISCOUNT,
							String CC_TYPE, String CC_NUMBER, String CC_NAME, Date CC_EXPIRY, String SHIPPING, String STREET_1, String STREET_2, 
							String CITY, String STATE, String ZIP, Integer CO_ID) throws RemoteException;

	/**
	 * Returns the last order placed by the customer, by first authenticating him.
	 * 
	 * @param C_UNAME name of the customer
	 * @param S_PASSWD password supplied by the customer
	 * @return the BigOrderModel object, if successfully authenticated, null otherwise
	 */
	public BigOrderModel getLastOrder(String C_UNAME, String S_PASSWD) throws RemoteException;

	/**
	 * Performs update of the specified item, as defined in the processing definition of 
	 * the Admin Confirm Web Interaction. 
	 * It DOES NOT PERFORM updates of the I_RELATED* fields, as prescribed in the clause 2.16.3.3 of the
	 * spec (version 1.8). It rather updates these values with rabdomly selected IDs.
	 * 
	 * @param I_ID  id of the item to update
	 * @param I_NEW_COST  item's new cost
	 * @param I_NEW_STOCK  item's new stock
	 * @param I_NEW_IMAGE  item's new image
	 * @param I_NEW_THUMBNAIL  item's new thumbnail
	 * @return  updated item and author info
	 */	
	public ItemAuthorModel updateItem(Integer I_ID, Double I_NEW_COST, Integer I_NEW_STOCK, String I_NEW_IMAGE, String I_NEW_THUMBNAIL) throws RemoteException;

}
 