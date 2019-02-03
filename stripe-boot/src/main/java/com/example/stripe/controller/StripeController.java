package com.example.stripe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stripe.exception.BusinessException;
import com.example.stripe.model.CaptureRequest;
import com.example.stripe.model.ChargeRequest;
import com.example.stripe.model.PaymentIntentRequest;
import com.example.stripe.model.PaymentRequest;
import com.example.stripe.model.RefundRequest;
import com.example.stripe.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.Token;

@RestController
@RequestMapping("/api")
public class StripeController {
	@Autowired
	StripeService stripeService;

	@GetMapping(value = "/getPaymentmethods/{currencyCode}/{countryCode}")
	public ResponseEntity<List<String>> getPaymentMethods(@PathVariable("currencyCode") String currencyCode,
			@PathVariable("countryCode") String countryCode) {
		return new ResponseEntity<List<String>>(
				stripeService.getPaymentMethodsByCurrencyCode(currencyCode, countryCode), HttpStatus.OK);
	}

	@GetMapping("/mockToken")
	public ResponseEntity<Token> generateMockToken() throws StripeException {
		return new ResponseEntity<Token>(stripeService.generateMockToken(), HttpStatus.OK);
	}

//	@PostMapping(value = "/charge")
//	public ResponseEntity<Charge> charge(@RequestBody ChargeRequest chargeRequest) {
//		try {
//			return new ResponseEntity<Charge>(stripeService.charge(chargeRequest), HttpStatus.OK);
//		} catch (BusinessException e) {
//			e.printStackTrace();
//			return new ResponseEntity<Charge>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

//	@PostMapping(value = "/capture")
//	public ResponseEntity<Charge> capture(@RequestBody CaptureRequest captureRequest) {
//		try {
//			return new ResponseEntity<Charge>(stripeService.capture(captureRequest), HttpStatus.OK);
//		} catch (BusinessException e) {
//			return new ResponseEntity<Charge>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	@PostMapping(value = "/intent/create", produces = "application/json")
	public ResponseEntity<Map<String, String>> createPaymentIntent(
			@RequestBody PaymentIntentRequest paymentIntentRequest) {
		PaymentIntent paymentIntent = stripeService.createPaymentIntent(paymentIntentRequest);
		Map<String, String> response = new HashMap<>();
		response.put("client_secret", paymentIntent.getClientSecret());
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/savePaymentRequest", produces = "application/json")
	public ResponseEntity<PaymentRequest> savePaymentRequest(@RequestBody PaymentRequest paymentRequest) {
		stripeService.savePaymentRequest(paymentRequest);
		return new ResponseEntity<PaymentRequest>(paymentRequest, HttpStatus.OK);
	}

	@PostMapping(value = "/refund")
	public ResponseEntity<Refund> refund(@RequestBody RefundRequest refundRequest) {
		try {
			return new ResponseEntity<Refund>(stripeService.refund(refundRequest), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Refund>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
