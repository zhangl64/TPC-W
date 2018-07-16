package edu.nyu.pdsg.tpcw.ejb.adminportal;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Admin portal session bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface AdminPortalHome extends EJBHome {

    public AdminPortal create() throws CreateException, RemoteException;
    
}

