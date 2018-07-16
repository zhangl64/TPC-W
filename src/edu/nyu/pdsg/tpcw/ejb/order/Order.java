package edu.nyu.pdsg.tpcw.ejb.order;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * Order bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface Order extends EJBObject {

    public OrderModel getOrder() throws RemoteException;
    
	public Integer getO_ID() throws RemoteException;
}
