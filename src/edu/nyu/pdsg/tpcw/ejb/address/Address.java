package edu.nyu.pdsg.tpcw.ejb.address;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * Remote interface of Adress entity Bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface Address extends EJBObject {

	public AddressModel getAddress() throws RemoteException;
	
	public Integer getADDR_ID() throws RemoteException;
}
