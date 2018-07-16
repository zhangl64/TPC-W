package edu.nyu.pdsg.tpcw.ejb.order;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Order bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface OrderLineHome extends EJBHome {

    public OrderLine create(Integer OL_ID, Integer OL_O_ID, Integer OL_I_ID, Integer OL_QTY, Double OL_DISCOUNT, String OL_COMMENTS) throws CreateException, RemoteException;
    
    public OrderLine findByPrimaryKey(OrderLinePK pk) throws FinderException, RemoteException;

	public Collection findByOrderId(Integer O_ID) throws FinderException, RemoteException;
}
