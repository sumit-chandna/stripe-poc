export class PaymentRequest {
    currency: string;
    amount: number;
    customMessage: string;
    capture: boolean;
    email: string;
    chargeId: string;
    paymentIntentId: string;
    paymentMethod: string;
}
