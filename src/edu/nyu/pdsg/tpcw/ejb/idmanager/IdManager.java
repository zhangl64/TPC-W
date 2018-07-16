package edu.nyu.pdsg.tpcw.ejb.idmanager;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * Remote interface of IdManager bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface IdManager extends EJBObject {

	public Integer getNewAddressId() throws RemoteException;
	
	public Integer getNewCustomerId() throws RemoteException;
	
	public Integer getNewOrderId() throws RemoteException;
}
