package edu.nyu.pdsg.tpcw.ejb.xact;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * CC_XACT bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface Cc_xact extends EJBObject {
    
    public Cc_xactModel getCc_xact() throws RemoteException;
    
}
