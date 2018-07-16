package edu.nyu.pdsg.tpcw.ejb.customer;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBObject;

/**
 * Customer bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface Customer extends EJBObject {
	
	public CustomerModel getCustomer() throws RemoteException;
    
	public Integer getC_ADDR_ID() throws RemoteException;

	public void setC_LOGIN(Date date) throws RemoteException;

	public void setC_EXPIRATION(Date date) throws RemoteException;

	public void setC_UNAME(String string) throws RemoteException;

	public void setC_PASSWD(String string) throws RemoteException;
}
