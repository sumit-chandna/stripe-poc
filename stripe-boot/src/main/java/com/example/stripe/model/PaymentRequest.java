package com.example.stripe.model;

public class PaymentRequest {
	private String token;
	private String currency;
	private int amount;
	private String customMessage;
	private boolean capture;
	private String email;
	private String chargeId;
	private String paymentIntentId;
	private String paymentMethod;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

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

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	public boolean isCapture() {
		return capture;
	}

	public void setCapture(boolean capture) {
		this.capture = capture;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

	public String getPaymentIntentId() {
		return paymentIntentId;
	}

	public void setPaymentIntentId(String paymentIntentId) {
		this.paymentIntentId = paymentIntentId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
