package edu.nyu.pdsg.tpcw.ejb.idmanager;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

/**
 * Entity bean corresponding to the ids table, 
 * for proper generation of promary keys (ids).
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public abstract class IdManagerBean implements EntityBean {

	private EntityContext ctx;

    // CMP field accessors -----------------------------------------------------

	public abstract Integer getId();
	public abstract void setId(Integer id);

	public abstract Integer getAddressId();
	public abstract void setAddressId(Integer id);

	public abstract Integer getCustomerId();
	public abstract void setCustomerId(Integer id);

	public abstract Integer getOrderId();
	public abstract void setOrderId(Integer id);

    // business methods
    
   	public Integer getNewAddressId() {
   		Integer i = new Integer(getAddressId().intValue() + 1);
   		setAddressId(i);
   		return i;
   	}
	
	public Integer getNewCustomerId() {
		Integer i = new Integer(getCustomerId().intValue() + 1);
		setCustomerId(i);
		return i;
	}
	
	public Integer getNewOrderId() {
		Integer i = new Integer(getOrderId().intValue() + 1);
		setOrderId(i);
		return i;
	}
	
	// EJB callbacks -----------------------------------------------------------
   
	public void setEntityContext(EntityContext context) {
		ctx = context;
	}

	public void unsetEntityContext() {
    	ctx = null;
	}

	public void ejbActivate() {
   	}

   	public void ejbPassivate() {
   	}

   	public void ejbRemove() {
   	}

   	public void ejbStore() {
   	}

   	public void ejbLoad() {
   	}
}
