package com.example.stripe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stripe.model.PaymentIntentRequest;
import com.example.stripe.service.StripeService;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/intent")
public class StripeIntentController {

	@Autowired
	StripeService stripeService;

	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentIntentRequest paymentIntentRequest) {
		PaymentIntent paymentIntent = stripeService.createPaymentIntent(paymentIntentRequest);
		return new ResponseEntity<String>(paymentIntent.getClientSecret(), HttpStatus.OK);
	}
}
