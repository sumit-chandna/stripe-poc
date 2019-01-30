package com.example.stripe.service;

import java.util.HashMap;
import java.util.Map;

import com.example.stripe.model.CaptureRequest;
import com.example.stripe.model.ChargeRequest;
import com.example.stripe.model.RefundRequest;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.stripe.model.Token;

public interface StripeService {
	public default Token generateMockToken() throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException {
		Map<String, Object> tokenParams = new HashMap<String, Object>();
		Map<String, Object> cardParams = new HashMap<String, Object>();
		Stripe.apiKey = "sk_test_u2d5PkwOdYK4T1ibHt5S73ER";
		cardParams.put("number", "4242424242424242");
		cardParams.put("exp_month", 1);
		cardParams.put("exp_year", 2020);
		cardParams.put("cvc", "314");
		tokenParams.put("card", cardParams);

		return Token.create(tokenParams);
	}

	Charge charge(ChargeRequest chargeRequest);

	Charge capture(CaptureRequest captureRequest);

	Refund refund(RefundRequest refundRequest);
}
