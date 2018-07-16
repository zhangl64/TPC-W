package edu.nyu.pdsg.tpcw.ejb.address;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Home interface of Address Entity Bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface AddressHome extends EJBHome {
    
    public Address create(String ADDR_STREET1, String ADDR_STREET2,
							String ADDR_CITY, String ADDR_STATE, String ADDR_ZIP, Integer ADDR_CO_ID)
        					throws CreateException, RemoteException;

	public Address findByPrimaryKey(Integer id) throws FinderException, RemoteException;
}
