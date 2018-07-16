package edu.nyu.pdsg.tpcw.ejb.address;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

/**
 * Entity bean corresponding to the ADDRESS table.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public abstract class AddressBean implements EntityBean {

	private EntityContext ctx;
	
	// CMP field accessors -----------------------------------------------------

	public abstract String getADDR_CITY();
	public abstract Integer getADDR_ID();
	public abstract String getADDR_STATE();
	public abstract String getADDR_STREET1();
	public abstract String getADDR_STREET2();
	public abstract String getADDR_ZIP();
	public abstract Integer getADDR_CO_ID();

	public abstract void setADDR_CITY(String string);
	public abstract void setADDR_ID(Integer i);
	public abstract void setADDR_STATE(String string);
	public abstract void setADDR_STREET1(String string);
	public abstract void setADDR_STREET2(String string);
	public abstract void setADDR_ZIP(String string);
	public abstract void setADDR_CO_ID(Integer i);

	// create methods ----------------------------------------------------------
    
	public Integer ejbCreate(String ADDR_STREET1, String ADDR_STREET2,
			String ADDR_CITY, String ADDR_STATE, String ADDR_ZIP, Integer ADDR_CO_ID) throws CreateException {
		setADDR_STREET1(ADDR_STREET1);
		setADDR_STREET2(ADDR_STREET2);
		setADDR_CITY(ADDR_CITY);
		setADDR_STATE(ADDR_STATE);
		setADDR_ZIP(ADDR_ZIP);
		setADDR_CO_ID(ADDR_CO_ID);
		return null;
	}

	public void ejbPostCreate(String ADDR_STREET1, String ADDR_STREET2,
			String ADDR_CITY, String ADDR_STATE, String ADDR_ZIP, Integer ADDR_CO_ID) {}

	// model object accessors ------------------------------------------------------------

	/**
	 * Returns full Model Object.
	 */
	public AddressModel getAddress() {
		return new AddressModel(getADDR_ID(), getADDR_STREET1(), getADDR_STREET2(),
								getADDR_CITY(), getADDR_STATE(), getADDR_ZIP(), getADDR_CO_ID());
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
