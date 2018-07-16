package edu.nyu.pdsg.tpcw.ejb.item;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBObject;

/**
 * Item bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public interface Item extends EJBObject {

	/**
	 * 
	 * @return full ItemModel
	 * 
	 */	
	public ItemModel getItem() throws RemoteException;
    
	public Integer getI_RELATED1() throws RemoteException;
	public Integer getI_RELATED2() throws RemoteException;
	public Integer getI_RELATED3() throws RemoteException;
	public Integer getI_RELATED4() throws RemoteException;
	public Integer getI_RELATED5() throws RemoteException;
	public String getI_THUMBNAIL() throws RemoteException;
	public Integer getI_STOCK() throws RemoteException;
	public Double getI_COST() throws RemoteException;
	public Double getI_SRP() throws RemoteException;
	public String getI_TITLE() throws RemoteException;
	public String getI_BACKING() throws RemoteException;

	public void setI_COST(Double double1) throws RemoteException;
	public void setI_IMAGE(String string) throws RemoteException;
	public void setI_PUB_DATE(Date date) throws RemoteException;
	public void setI_RELATED1(Integer integer) throws RemoteException;
	public void setI_RELATED2(Integer integer) throws RemoteException;
	public void setI_RELATED3(Integer integer) throws RemoteException;
	public void setI_RELATED4(Integer integer) throws RemoteException;
	public void setI_RELATED5(Integer integer) throws RemoteException;
	public void setI_STOCK(Integer integer) throws RemoteException;
	public void setI_THUMBNAIL(String string) throws RemoteException;
	
	/**
	 * 
	 * @return  full ShortItemModel object
	 * 
	 */	
	public ShortItemAuthorModel getShortModel() throws RemoteException;

}
