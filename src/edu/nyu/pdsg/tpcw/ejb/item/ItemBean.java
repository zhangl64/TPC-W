package edu.nyu.pdsg.tpcw.ejb.item;

import java.util.Date;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

/**
 * Item bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public abstract class ItemBean implements EntityBean {

	private EntityContext ctx;
	
	// CMP field accessors -----------------------------------------------------

	public abstract Integer getI_A_ID();
	public abstract Date getI_AVAIL();
	public abstract String getI_BACKING();
	public abstract Double getI_COST();
	public abstract String getI_DESC();
	public abstract String getI_DIMENSIONS();
	public abstract Integer getI_ID();
	public abstract String getI_IMAGE();
	public abstract String getI_ISBN();
	public abstract Integer getI_PAGE();
	public abstract Date getI_PUB_DATE();
	public abstract String getI_PUBLISHER();
	public abstract Integer getI_RELATED1();
	public abstract Integer getI_RELATED2();
	public abstract Integer getI_RELATED3();
	public abstract Integer getI_RELATED4();
	public abstract Integer getI_RELATED5();
	public abstract Double getI_SRP();
	public abstract Integer getI_STOCK();
	public abstract String getI_SUBJECT();
	public abstract String getI_THUMBNAIL();
	public abstract String getI_TITLE();

	public abstract void setI_A_ID(Integer integer);
	public abstract void setI_AVAIL(Date date);
	public abstract void setI_BACKING(String string);
	public abstract void setI_COST(Double double1);
	public abstract void setI_DESC(String string);
	public abstract void setI_DIMENSIONS(String string);
	public abstract void setI_ID(Integer integer);
	public abstract void setI_IMAGE(String string);
	public abstract void setI_ISBN(String string);
	public abstract void setI_PAGE(Integer integer);
	public abstract void setI_PUB_DATE(Date date);
	public abstract void setI_PUBLISHER(String string);
	public abstract void setI_RELATED1(Integer integer);
	public abstract void setI_RELATED2(Integer integer);
	public abstract void setI_RELATED3(Integer integer);
	public abstract void setI_RELATED4(Integer integer);
	public abstract void setI_RELATED5(Integer integer);
	public abstract void setI_SRP(Double double1);
	public abstract void setI_STOCK(Integer integer);
	public abstract void setI_SUBJECT(String string);
	public abstract void setI_THUMBNAIL(String string);
	public abstract void setI_TITLE(String string);

	// create methods ----------------------------------------------------------
    
	// model object accessors ------------------------------------------------------------

	/**
	 * Returns full Model Object.
	 */
	public ItemModel getItem() {
		return new ItemModel(getI_ID(), getI_TITLE(), getI_A_ID(), getI_PUB_DATE(), getI_PUBLISHER(),
				getI_SUBJECT(), getI_DESC(), getI_RELATED1(), getI_RELATED2(), getI_RELATED3(),
				getI_RELATED4(), getI_RELATED5(), getI_THUMBNAIL(), getI_IMAGE(), getI_SRP(), 
				getI_COST(), getI_AVAIL(), getI_STOCK(), getI_ISBN(), getI_PAGE(),
				getI_BACKING(), getI_DIMENSIONS());
	}

	/**
	 * 
	 * Returns short model object.
	 */	
	public ShortItemAuthorModel getShortModel() {
		return new ShortItemAuthorModel(getI_ID(), getI_TITLE(), getI_A_ID());
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
