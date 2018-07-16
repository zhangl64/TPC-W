package edu.nyu.pdsg.tpcw.ejb.catalog;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.EJBObject;

import edu.nyu.pdsg.tpcw.ejb.item.ItemAuthorModel;
import edu.nyu.pdsg.tpcw.ejb.item.ItemModel;

/**
 * Catalog bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface Catalog extends EJBObject {
	
	public Vector itemFindRelated(Integer itemId) throws RemoteException;

	/**
     * Returns the set (max `limit`) of items with the author's last name starting with the specified string.
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param author  author'a last name
	 * @param limit  max number of results
	 */
	public Collection searchItemsByAuthor(String author, int limit) throws RemoteException;

	/**
     * Returns the set (max `limit`) of items with the title containing the specified string.
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param title  title keyword
	 * @param limit  max number of results
	 */
	public Collection searchItemsByTitle(String title, int limit) throws RemoteException;

	/**
	 * Returns the set (max `limit`) of items with the subject equal to the specified string. 
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param subject  desired subject
	 * @param limit   max number of results
	 */
	public Collection searchItemsBySubject(String subject, int limit) throws RemoteException;

	/**
	 * Returns the set of (max `limit`) items with the latest publication date (I_PUB_DATE)
	 * and the specified subject. 
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param subject  desired subject
	 * @param limit   max number of results
	 */
	public Collection searchNewItemsBySubject(String subject, int limit) throws RemoteException;
	
	/**
	 * Returns the set of (max 'limit') items with the specified subject, such that they were 
	 * bought in recent 'numRecentOrders' orders, ordered by the total quantity bought (DESC).
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param subject  desired subject
	 * @param numRecentOrders  number of most recent orders to search through
	 * @param limit   max number of results
	 */
	public Collection searchRecentlyPurchasedItemsBySubject(String subject, int numRecentOrders, int limit) throws RemoteException;

	/**
	 * 
	 * Returns full utem and author information.
	 * 
	 */
	public ItemAuthorModel getItemAuthor(Integer i) throws RemoteException;
	
	/**
	 * 
	 * Returns utem model for adding to the shopping cart, with only a limited number of fields filled:
	 * I_ID, I_COST, I_SRP, I_TITLE, I_BACKING.
	 * 
	 */
	public ItemModel getItemForCart(Integer i) throws RemoteException;
	
	/**
	 * 
	 * Reduces the quantity of the item.
	 * 
	 * @param id   I_ID of the item
	 * @param qty  qty to reduce by
	 */
	public void reduceItemQty(Integer id, int qty) throws RemoteException;
}
 