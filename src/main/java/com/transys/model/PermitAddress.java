package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="permitAddress")
public class PermitAddress extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="permitId")
	@JsonBackReference
	private Permit permit;
	
	@Column(name="line1")
	private String line1;
	
	@Column(name="line2")
	private String line2;
	
	@Column(name="city")
	private String city;
	
	@ManyToOne
	@JoinColumn(name="state")
	private State state;
	
	@Column(name="zip")
	private String zipcode;

	public Permit getPermit() {
		return permit;
	}

	public void setPermit(Permit permit) {
		this.permit = permit;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}
	
	@Transient
	//TODO: Move to utils?
	public String getFullLine() {
		StringBuffer fullLineBuff = new StringBuffer();
		if (StringUtils.isNotEmpty(getLine1())) {
			fullLineBuff.append(getLine1());
		}
		if (StringUtils.isNotEmpty(getLine2())) {
			fullLineBuff.append(" " + getLine2());
		}
		
		return fullLineBuff.toString();
	}
	
	@Transient
	//TODO: Move to utils?
	public String getFullPermitAddress() {
		StringBuffer addressBuff = new StringBuffer();
		if (StringUtils.isNotEmpty(getLine1())) {
			addressBuff.append(getLine1());
		}
		if (StringUtils.isNotEmpty(getLine2())) {
			addressBuff.append(" " + getLine2());
		}
		if (StringUtils.isNotEmpty(getCity())) {
			addressBuff.append(", " + getCity());
		}
		if (getState() != null) {
			if (StringUtils.isNotEmpty(getState().getName())) {
				addressBuff.append(" " + getState().getName());
			}
		}
		if (StringUtils.isNotEmpty(getZipcode())) {
			addressBuff.append(" " + getZipcode());
		}
		
		return addressBuff.toString();
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}
