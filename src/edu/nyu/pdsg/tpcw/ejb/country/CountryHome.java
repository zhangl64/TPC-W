package edu.nyu.pdsg.tpcw.ejb.country;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Country bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface CountryHome extends EJBHome {
    
    public Country findByPrimaryKey(Integer CO_ID) throws FinderException, RemoteException;
    
}
