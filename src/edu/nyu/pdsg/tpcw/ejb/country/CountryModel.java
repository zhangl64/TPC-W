package edu.nyu.pdsg.tpcw.ejb.country;

import java.io.Serializable;

/**
 * Country bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class CountryModel implements Serializable {

	private Integer CO_ID;
	private String CO_NAME;
	private Double CO_EXCHANGE;
	private String CO_CURRENCY;
	
	public CountryModel(Integer CO_ID, String CO_NAME, Double CO_EXCHANGE, String CO_CURRENCY) {
		this.CO_ID = CO_ID;
		this.CO_NAME = CO_NAME;
		this.CO_EXCHANGE = CO_EXCHANGE;
		this.CO_CURRENCY = CO_CURRENCY;
	}
	
	public CountryModel(Integer CO_ID) {
		this.CO_ID = CO_ID;
	}
	
	// getters and setters

	public String getCO_CURRENCY() {
		return CO_CURRENCY;
	}

	public Double getCO_EXCHANGE() {
		return CO_EXCHANGE;
	}

	public Integer getCO_ID() {
		return CO_ID;
	}

	public String getCO_NAME() {
		return CO_NAME;
	}

	public void setCO_CURRENCY(String string) {
		CO_CURRENCY = string;
	}

	public void setCO_EXCHANGE(Double double1) {
		CO_EXCHANGE = double1;
	}

	public void setCO_NAME(String string) {
		CO_NAME = string;
	}

}
