package com.example.stripe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.stripe.model.CaptureRequest;
import com.example.stripe.model.ChargeRequest;
import com.example.stripe.model.PaymentIntentRequest;
import com.example.stripe.model.PaymentRequest;
import com.example.stripe.model.RefundRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.Token;

public interface StripeService {
	public default Token generateMockToken() throws StripeException {
		Map<String, Object> tokenParams = new HashMap<String, Object>();
		Map<String, Object> cardParams = new HashMap<String, Object>();
		Stripe.apiKey = "sk_test_olWX902eBsiDWjpsY5oxHBpK";
		cardParams.put("number", "4242424242424242");
		cardParams.put("exp_month", 1);
		cardParams.put("exp_year", 2020);
		cardParams.put("cvc", "314");
		tokenParams.put("card", cardParams);

		return Token.create(tokenParams);
	}

	public List<String> getPaymentMethodsByCurrencyCode(String currencyCode, String countryCode);

	Charge charge(ChargeRequest chargeRequest);

	Charge capture(CaptureRequest captureRequest);

	Refund refund(RefundRequest refundRequest);

	PaymentIntent createPaymentIntent(PaymentIntentRequest paymentIntentRequest);

	void savePaymentRequest(PaymentRequest paymentRequest);
}
