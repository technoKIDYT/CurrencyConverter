package com.technokid.model;

public class TKCurrency {
	private String unit;
	private double amount;
	private String strDesc;

	public String getDisplayName() {
		return strDesc;
	}

	public void setStrDesc(String strDesc) {
		this.strDesc = strDesc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return getUnit() + " - " + getDisplayName();
	}

}
