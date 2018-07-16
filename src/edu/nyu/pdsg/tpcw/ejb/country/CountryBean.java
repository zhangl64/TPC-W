package edu.nyu.pdsg.tpcw.ejb.country;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

/**
 * Country bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public abstract class CountryBean implements EntityBean {

	private EntityContext ctx;
	
	// CMP field accessors -----------------------------------------------------

	public abstract String getCO_CURRENCY();
	public abstract Double getCO_EXCHANGE();
	public abstract Integer getCO_ID();
	public abstract String getCO_NAME();

	public abstract void setCO_CURRENCY(String string);
	public abstract void setCO_EXCHANGE(Double double1);
	public abstract void setCO_ID(Integer integer);
	public abstract void setCO_NAME(String string);

	// create methods ----------------------------------------------------------
    
	// model object accessors ------------------------------------------------------------

	/**
	 * Returns full Model Object.
	 */
	public CountryModel getCountry() {
		return new CountryModel(getCO_ID(), getCO_NAME(), getCO_EXCHANGE(), getCO_CURRENCY());
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
