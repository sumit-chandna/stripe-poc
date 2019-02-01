package com.example.stripe.model;

import java.util.ArrayList;
import java.util.List;

public class PaymentIntentRequest {
	private String currency;
	private int amount;
	List<String> sources = new ArrayList<>();

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}
}
