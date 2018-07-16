package edu.nyu.pdsg.tpcw.ejb.author;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Home interface of the Author entity bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface AuthorHome extends EJBHome {

    public Author findByPrimaryKey(Integer A_ID) throws FinderException, RemoteException;
}
