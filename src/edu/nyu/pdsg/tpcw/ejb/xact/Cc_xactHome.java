package edu.nyu.pdsg.tpcw.ejb.xact;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * CC_XACT bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface Cc_xactHome extends EJBHome {
    
    public Cc_xact create(Integer CX_O_ID, String CX_TYPE, String CX_NUM, String CX_NAME, Date CX_EXPIRE,
				String CX_AUTH_ID, Double CX_XACT_AMT, Date CX_XACT_DATE, Integer CX_CO_ID) throws CreateException, RemoteException;
    
    public Cc_xact findByPrimaryKey(Integer CX_O_ID) throws FinderException, RemoteException;

}
