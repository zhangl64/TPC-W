package edu.nyu.pdsg.tpcw.ejb.order;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

/**
 * Order bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public abstract class OrderLineBean implements EntityBean {

	private EntityContext ctx;

	// CMP field accessors -----------------------------------------------------

	public abstract String getOL_COMMENTS();
	public abstract Double getOL_DISCOUNT();
	public abstract Integer getOL_I_ID();
	public abstract Integer getOL_ID();
	public abstract Integer getOL_O_ID();
	public abstract Integer getOL_QTY();

	public abstract void setOL_COMMENTS(String string);
	public abstract void setOL_DISCOUNT(Double double1);
	public abstract void setOL_I_ID(Integer integer);
	public abstract void setOL_ID(Integer integer);
	public abstract void setOL_O_ID(Integer integer);
	public abstract void setOL_QTY(Integer integer);

	// create methods ----------------------------------------------------------
    
	public OrderLinePK ejbCreate(Integer OL_ID, Integer OL_O_ID, Integer OL_I_ID, Integer OL_QTY, 
								Double OL_DISCOUNT, String OL_COMMENTS) throws CreateException {
		setOL_COMMENTS(OL_COMMENTS);
		setOL_DISCOUNT(OL_DISCOUNT);
		setOL_I_ID(OL_I_ID);
		setOL_ID(OL_ID);
		setOL_O_ID(OL_O_ID);
		setOL_QTY(OL_QTY);
		return null;
	}

	public void ejbPostCreate(Integer OL_ID, Integer OL_O_ID, Integer OL_I_ID, Integer OL_QTY, 
								Double OL_DISCOUNT, String OL_COMMENTS) {}

	// model object accessors ------------------------------------------------------------

	/**
	 * Returns full Model Object.
	 */
	public OrderLineModel getOrderLine() {
		return new OrderLineModel(getOL_ID(), getOL_O_ID(), getOL_I_ID(), getOL_QTY(), 
									getOL_DISCOUNT(), getOL_COMMENTS());
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
