package edu.nyu.pdsg.tpcw.ejb.item;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Item bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface ItemHome extends EJBHome {

    public Item findByPrimaryKey(Integer I_ID) throws FinderException, RemoteException;
    
    /**
     * Returns the set (max 'limit') of items with the title containing the specified string.
     */
    public Collection findByTitle(String str, int limit) throws FinderException, RemoteException;

	/**
	 * Returns the set (max 'limit') of items with the subject equal to the specified string. 
	 */
	public Collection findBySubject(String str, int limit) throws FinderException, RemoteException;

	/**
	 * Returns the set of (max 'limit') items with the latest publication date (I_PUB_DATE) 
	 * and the specified subject. 
	 */
	public Collection findNewBySubject(String str, int limit) throws FinderException, RemoteException;

}
