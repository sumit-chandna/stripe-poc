package com.example.stripe.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.stripe.model.CaptureRequest;
import com.example.stripe.model.ChargeRequest;
import com.example.stripe.model.RefundRequest;

@Repository
public class StripeDataRepositoryMock implements StripeDataRepository {

	Map<String, String> chargeMap = new HashMap<>();

	@Override
	public void saveChargeRequest(ChargeRequest chargeRequest) {
		chargeMap.put(chargeRequest.getEmail(), chargeRequest.getChargeId());
	}

	@Override
	public String RetrieveChargeId(CaptureRequest captureRequest) {
		return chargeMap.get(captureRequest.getEmail());
	}

	@Override
	public String RetrieveChargeId(RefundRequest refundRequest) {
		return chargeMap.get(refundRequest.getEmail());
	}

}
