package com.example.stripe.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.stripe.exception.BusinessException;
import com.example.stripe.model.CaptureRequest;
import com.example.stripe.model.ChargeRequest;
import com.example.stripe.model.PaymentIntentRequest;
import com.example.stripe.model.PaymentRequest;
import com.example.stripe.model.RefundRequest;
import com.example.stripe.repository.StripeDataRepository;
import com.example.stripe.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
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

	private Map<String, List<String>> mockMethodsMap = new HashMap<String, List<String>>() {
		{
			put("usd", new ArrayList<String>() {
				{
					add("card");
					add("klarna");
					add("wechat");
					add("paypal");
				}
			});
			put("eur", new ArrayList<String>() {
				{
					add("card");
					add("eps");
					add("iban");
					add("p24");
					add("multibanco");
					add("alipay");
					add("sofort");
					add("klarna");
					add("wechat");
					add("paypal");
					add("bancontact");
					add("giropay");
				}
			});
		}
	};

	@Override
	public List<String> getPaymentMethodsByCurrencyCode(String currencyCode, String countryCode) {
		return mockMethodsMap.get(currencyCode);
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
		} catch (StripeException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@Override
	public Charge capture(CaptureRequest captureRequest) {
		try {
			Charge charge = Charge.retrieve(stripeDataRepository.RetrieveChargeId(captureRequest));
			return charge.capture(getRequestOptions());
		} catch (StripeException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@Override
	public Refund refund(RefundRequest refundRequest) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("charge", stripeDataRepository.RetrieveChargeId(refundRequest));
			return Refund.create(params, getRequestOptions());
		} catch (StripeException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@Override
	public PaymentIntent createPaymentIntent(PaymentIntentRequest paymentIntentRequest) {
		try {
			Map<String, Object> paymentintentParams = new HashMap<String, Object>();
			paymentintentParams.put("amount", paymentIntentRequest.getAmount());
			paymentintentParams.put("currency", paymentIntentRequest.getCurrency());
			paymentintentParams.put("allowed_source_types", paymentIntentRequest.getSources());
			paymentintentParams.put("capture_method", "manual");
			return PaymentIntent.create(paymentintentParams, getRequestOptions());
		} catch (StripeException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@Override
	public void savePaymentRequest(PaymentRequest paymentRequest) {
		stripeDataRepository.savePaymentRequest(paymentRequest);
	}

}
