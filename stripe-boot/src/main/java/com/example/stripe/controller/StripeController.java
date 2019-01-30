package com.example.stripe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stripe.exception.BusinessException;
import com.example.stripe.model.CaptureRequest;
import com.example.stripe.model.ChargeRequest;
import com.example.stripe.model.RefundRequest;
import com.example.stripe.service.StripeService;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.stripe.model.Token;

@RestController
@RequestMapping("/api")
public class StripeController {
	@Autowired
	StripeService stripeService;

	@GetMapping(value = "/check")
	public ResponseEntity<String> check() {
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}

	@GetMapping("/mockToken")
	public ResponseEntity<Token> generateMockToken() throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException {
		return new ResponseEntity<Token>(stripeService.generateMockToken(), HttpStatus.OK);
	}

	@PostMapping(value = "/charge")
	public ResponseEntity<Charge> charge(@RequestBody ChargeRequest chargeRequest) {
		try {
			return new ResponseEntity<Charge>(stripeService.charge(chargeRequest), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Charge>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/capture")
	public ResponseEntity<Charge> capture(@RequestBody CaptureRequest captureRequest) {
		try {
			return new ResponseEntity<Charge>(stripeService.capture(captureRequest), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Charge>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
