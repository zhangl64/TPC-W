package edu.nyu.pdsg.tpcw.ejb.customer;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Customer bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface CustomerHome extends EJBHome {

    public Customer create(String C_UNAME, String C_PASSWD, String C_FNAME, String C_LNAME,
					Integer C_ADDR_ID, String C_PHONE, String C_EMAIL, Date C_SINCE, Date C_LAST_VISIT,
					Date C_LOGIN, Date C_EXPIRATION, Double C_DISCOUNT, Double C_BALANCE, 
					Double C_YTD_PMT, Date C_BIRTHDATE, String C_DATA) throws CreateException, RemoteException;

    public Customer findByPrimaryKey(Integer C_ID) throws FinderException, RemoteException;
    
    public Collection findByC_UNAME(String C_UNAME) throws FinderException, RemoteException;
    
}
