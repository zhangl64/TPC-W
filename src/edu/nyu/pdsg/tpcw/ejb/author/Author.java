package edu.nyu.pdsg.tpcw.ejb.author;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * Remote interface of the Author Entity bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public interface Author extends EJBObject {
    
    public AuthorModel getAuthor() throws RemoteException;

	public String getA_FNAME() throws RemoteException;
	public String getA_LNAME() throws RemoteException;
}
