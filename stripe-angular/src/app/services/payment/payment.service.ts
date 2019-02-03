import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { DataService } from '../data/data.service';
import { IntentRequest } from 'src/app/model/IntentRequest';
import { AppSettings } from 'src/app/model/AppSettings';
import { PaymentRequest } from 'src/app/model/PaymentRequest';

@Injectable({
  providedIn: 'root'
})
export class PaymentService extends DataService {
  stripe: any;
  CREATE_INTENT_URL = '/intent/create';
  SAVE_PAYMENT_REQ = '/savePaymentRequest';
  GET_PAYMENT_METHODS_URL = '/getPaymentmethods/';
  PAYMENT_BETAS = 'payment_intent_beta_3';
  constructor(_http: HttpClient) {
    super(_http);
    this.stripe = Stripe(AppSettings.STRIP_PUBLIC_KEY, {
      betas: [this.PAYMENT_BETAS]
    });
  }

  public createIntent(request: IntentRequest): Observable<any> {
    return this.post(AppSettings.API_ENDPOINT + this.CREATE_INTENT_URL, request);
  }
  public getPaymentMethods(currency: string, country: string): Observable<any> {
    return this.get(AppSettings.API_ENDPOINT + this.GET_PAYMENT_METHODS_URL + currency + '/' + country);
  }
  public savePaymentRequest(request: PaymentRequest): Observable<any> {
    return this.post(AppSettings.API_ENDPOINT + this.SAVE_PAYMENT_REQ, request);
  }
}

