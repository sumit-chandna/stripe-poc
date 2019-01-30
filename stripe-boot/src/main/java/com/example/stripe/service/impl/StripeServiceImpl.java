package com.example.stripe.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.stripe.exception.BusinessException;
import com.example.stripe.model.CaptureRequest;
import com.example.stripe.model.ChargeRequest;
import com.example.stripe.model.RefundRequest;
import com.example.stripe.repository.StripeDataRepository;
import com.example.stripe.service.StripeService;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions;

@Service
public class StripeServiceImpl implements StripeService {

	@Value("${STRIPE_SECRET_KEY}")
	String stripeSecretKey;

	@Autowired
	StripeDataRepository stripeDataRepository;

	private RequestOptions getRequestOptions() {
		return RequestOptions.builder().setApiKey(stripeSecretKey).build();
	}

	@Override
	public Charge charge(final ChargeRequest chargeRequest) {
		String token = chargeRequest.getToken();
		Map<String, Object> params = new HashMap<>();
		params.put("amount", chargeRequest.getAmount());
		params.put("currency", chargeRequest.getCurrency());
		params.put("description", chargeRequest.getCustomMessage());
		params.put("source", token);
		params.put("capture", chargeRequest.isCapture());
		try {
			Charge charge = Charge.create(params, getRequestOptions());
			chargeRequest.setChargeId(charge.getId());
			stripeDataRepository.saveChargeRequest(chargeRequest);
			return charge;
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
				| APIException e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

	@Override
	public Charge capture(CaptureRequest captureRequest) {
		try {
			Charge charge = Charge.retrieve(stripeDataRepository.RetrieveChargeId(captureRequest));
			return charge.capture(getRequestOptions());
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
				| APIException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@Override
	public Refund refund(RefundRequest refundRequest) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("charge", stripeDataRepository.RetrieveChargeId(refundRequest));
			return Refund.create(params, getRequestOptions());
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
				| APIException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

}
