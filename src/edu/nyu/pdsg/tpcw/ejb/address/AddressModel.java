package edu.nyu.pdsg.tpcw.ejb.address;

import java.io.Serializable;

/**
 * Model class for address.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public class AddressModel implements Serializable {
    
    private Integer ADDR_ID;
	private String ADDR_STREET1;
	private String ADDR_STREET2;
	private String ADDR_CITY;
	private String ADDR_STATE;
	private String ADDR_ZIP;
	private Integer ADDR_CO_ID;
	
	public AddressModel(Integer ADDR_ID, String ADDR_STREET1, String ADDR_STREET2,
						String ADDR_CITY, String ADDR_STATE, String ADDR_ZIP, Integer ADDR_CO_ID) {
		this.ADDR_ID = ADDR_ID;
		this.ADDR_STREET1 = ADDR_STREET1;
		this.ADDR_STREET2 = ADDR_STREET2;
		this.ADDR_CITY = ADDR_CITY;
		this.ADDR_STATE = ADDR_STATE;
		this.ADDR_ZIP = ADDR_ZIP;
		this.ADDR_CO_ID = ADDR_CO_ID;
	}
	
	public AddressModel(Integer id) {
		this.ADDR_ID = id;
	}
	
	// getters and setters
    
	public String getADDR_CITY() {
		return ADDR_CITY;
	}

	public Integer getADDR_CO_ID() {
		return ADDR_CO_ID;
	}

	public Integer getADDR_ID() {
		return ADDR_ID;
	}

	public String getADDR_STATE() {
		return ADDR_STATE;
	}

	public String getADDR_STREET1() {
		return ADDR_STREET1;
	}

	public String getADDR_STREET2() {
		return ADDR_STREET2;
	}

	public String getADDR_ZIP() {
		return ADDR_ZIP;
	}

	public void setADDR_CITY(String string) {
		ADDR_CITY = string;
	}

	public void setADDR_CO_ID(Integer integer) {
		ADDR_CO_ID = integer;
	}

	public void setADDR_STATE(String string) {
		ADDR_STATE = string;
	}

	public void setADDR_STREET1(String string) {
		ADDR_STREET1 = string;
	}

	public void setADDR_STREET2(String string) {
		ADDR_STREET2 = string;
	}

	public void setADDR_ZIP(String string) {
		ADDR_ZIP = string;
	}

}
