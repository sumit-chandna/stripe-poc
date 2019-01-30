import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { DataService } from '../data/data.service';
import { ChargeRequest } from 'src/app/model/chargerequest';

@Injectable({
  providedIn: 'root'
})
export class PaymentService extends DataService {
  stripe: any;
  url = "http://localhost:5001/api";
  constructor(_http: HttpClient) {
    super(_http);
    this.stripe = Stripe('pk_test_key');
  }
  public charge(request: ChargeRequest): Observable<any> {
    return this.post(this.url + "/charge", request);
  }
}

