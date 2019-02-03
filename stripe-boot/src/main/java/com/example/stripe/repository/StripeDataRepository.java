package com.example.stripe.repository;

import com.example.stripe.model.CaptureRequest;
import com.example.stripe.model.ChargeRequest;
import com.example.stripe.model.PaymentRequest;
import com.example.stripe.model.RefundRequest;

public interface StripeDataRepository {

	void saveChargeRequest(ChargeRequest chargeRequest);

	String RetrieveChargeId(CaptureRequest captureRequest);

	String RetrieveChargeId(RefundRequest refundRequest);

	void savePaymentRequest(PaymentRequest paymentRequest);
}
