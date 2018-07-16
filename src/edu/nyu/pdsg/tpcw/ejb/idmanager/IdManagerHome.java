package edu.nyu.pdsg.tpcw.ejb.idmanager;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Home interface for the IdManager bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface IdManagerHome extends EJBHome {

   public IdManager findByPrimaryKey(Integer id) throws FinderException, RemoteException;
}