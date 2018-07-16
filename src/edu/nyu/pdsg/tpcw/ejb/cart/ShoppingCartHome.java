package edu.nyu.pdsg.tpcw.ejb.cart;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Shopping Cart bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface ShoppingCartHome extends EJBHome {

    public ShoppingCart create() throws CreateException, RemoteException;
    
}
