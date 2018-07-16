package edu.nyu.pdsg.tpcw.ejb.country;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * Country bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface Country extends EJBObject {
    
    public CountryModel getCountry() throws RemoteException;

}
