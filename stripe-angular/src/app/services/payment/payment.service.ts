import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { DataService } from '../data/data.service';
import { ChargeRequest } from 'src/app/model/chargerequest';
import { IntentRequest } from 'src/app/model/IntentRequest';

@Injectable({
  providedIn: 'root'
})
export class PaymentService extends DataService {
  stripe: any;
  url = "http://localhost:5001/api";
  constructor(_http: HttpClient) {
    super(_http);
    this.stripe = Stripe('pk_test_7vAw4X2ah9dKeNZsQvHYRumx', {
      betas: ['payment_intent_beta_3']
    });
  }
  public charge(request: ChargeRequest): Observable<any> {
    return this.post(this.url + "/charge", request);
  }
  public createIntent(request: IntentRequest): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'text/plain; charset=utf-8');
    return this.post(this.url + "/intent/create", request, options: {
      headers?: HttpHeaders | {
        [header: string]: string | string[];
    };
  });
}
}

