package edu.nyu.pdsg.tpcw.ejb.order;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

/**
 * Order bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public abstract class OrderBean implements EntityBean {

	private EntityContext ctx;
	
	// CMP field accessors -----------------------------------------------------

	public abstract Integer getO_BILL_ADDR_ID();
	public abstract Integer getO_C_ID();
	public abstract Date getO_DATE();
	public abstract Integer getO_ID();
	public abstract Integer getO_SHIP_ADDR_ID();
	public abstract Date getO_SHIP_DATE();
	public abstract String getO_SHIP_TYPE();
	public abstract String getO_STATUS();
	public abstract Double getO_SUB_TOTAL();
	public abstract Double getO_TAX();
	public abstract Double getO_TOTAL();

	public abstract void setO_BILL_ADDR_ID(Integer integer);
	public abstract void setO_C_ID(Integer integer);
	public abstract void setO_DATE(Date date);
	public abstract void setO_ID(Integer integer);
	public abstract void setO_SHIP_ADDR_ID(Integer integer);
	public abstract void setO_SHIP_DATE(Date date);
	public abstract void setO_SHIP_TYPE(String string);
	public abstract void setO_STATUS(String integer);
	public abstract void setO_SUB_TOTAL(Double double1);
	public abstract void setO_TAX(Double double1);
	public abstract void setO_TOTAL(Double double1);

	// create methods ----------------------------------------------------------
    
	public Integer ejbCreate(Integer O_C_ID, Date O_DATE, Double O_SUB_TOTAL, Double O_TAX, 
						Double O_TOTAL, String O_SHIP_TYPE, Date O_SHIP_DATE, Integer O_BILL_ADDR_ID,
						Integer O_SHIP_ADDR_ID, String O_STATUS) throws CreateException {
		setO_BILL_ADDR_ID(O_BILL_ADDR_ID);
		setO_C_ID(O_C_ID);
		setO_DATE(O_DATE);
		setO_SHIP_ADDR_ID(O_SHIP_ADDR_ID);
		setO_SHIP_DATE(O_SHIP_DATE);
		setO_SHIP_TYPE(O_SHIP_TYPE);
		setO_STATUS(O_STATUS);
		setO_SUB_TOTAL(O_SUB_TOTAL);
		setO_TAX(O_TAX);
		setO_TOTAL(O_TOTAL);
		return null;
	}

	public void ejbPostCreate(Integer O_C_ID, Date O_DATE, Double O_SUB_TOTAL, Double O_TAX, 
				Double O_TOTAL, String O_SHIP_TYPE, Date O_SHIP_DATE, Integer O_BILL_ADDR_ID,
				Integer O_SHIP_ADDR_ID, String O_STATUS) {}

	// model object accessors ------------------------------------------------------------

	/**
	 * Returns full Model Object.
	 */
	public OrderModel getOrder() {
		return new OrderModel(getO_ID(), getO_C_ID(), getO_DATE(), getO_SUB_TOTAL(), getO_TAX(), 
							getO_TOTAL(), getO_SHIP_TYPE(), getO_SHIP_DATE(), getO_BILL_ADDR_ID(),
							getO_SHIP_ADDR_ID(), getO_STATUS());
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
