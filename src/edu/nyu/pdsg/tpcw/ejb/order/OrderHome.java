package edu.nyu.pdsg.tpcw.ejb.order;

import java.rmi.RemoteException;
import java.util.Date;

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
public interface OrderHome extends EJBHome {
    
    public Order create(Integer O_C_ID, Date O_DATE, Double O_SUB_TOTAL, Double O_TAX, 
				Double O_TOTAL, String O_SHIP_TYPE, Date O_SHIP_DATE, Integer O_BILL_ADDR_ID,
				Integer O_SHIP_ADDR_ID, String O_STATUS) throws CreateException, RemoteException;
    
    public Order findByPrimaryKey(Integer O_ID) throws FinderException, RemoteException;
    
}
