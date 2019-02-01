import { Component, OnDestroy, ViewChild, ChangeDetectorRef } from '@angular/core';
import { AfterViewInit, ElementRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { PaymentService } from '../services/payment/payment.service';
import { ChargeRequest } from '../model/chargerequest';
import { IntentRequest } from '../model/IntentRequest';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements AfterViewInit, OnDestroy {
  @ViewChild('cardInfo') cardInfo: ElementRef;

  card: any;
  cardHandler = this.onChange.bind(this);
  error: string;
  stripe: any;
  elements: any;
  orderAmt = 323;
  checkoutForm: FormGroup;
  ngOnInit() {
    this.checkoutForm = this.formBuilder.group({
      // firstName: ['', Validators.required],
      // lastName: ['', Validators.required],
      // email: ['', [Validators.required, Validators.email]],
      // password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }
  get f() { return this.checkoutForm.controls; }
  constructor(private cd: ChangeDetectorRef, private pmt: PaymentService, private formBuilder: FormBuilder) { }
  client_secret: string;
  ngAfterViewInit() {
    this.stripe = this.pmt.stripe;
    var elements = this.stripe.elements();
    this.createCard(elements);
    this.pmt.createIntent(this.createMockDataIntent())
      .subscribe(response => this.client_secret = response.text());
  }
  createCard(element: any) {
    this.card = element.create('card');
    this.card.mount(this.cardInfo.nativeElement);
    this.card.addEventListener('change', this.cardHandler);
  }
  ngOnDestroy() {
    this.card.removeEventListener('change', this.cardHandler);
    this.card.destroy();
  }

  onChange({ error }) {
    if (error) {
      this.error = error.message;
    } else {
      this.error = null;
    }
    this.cd.detectChanges();
  }
  private createMockData(token: any) {
    let request: ChargeRequest = new ChargeRequest();
    request.currency = "usd";
    request.amount = 99;
    request.customMessage = "custom message";
    request.capture = false;
    request.email = "test@mail.com";
    request.token = token.id;
    return request;
  }
  private createMockDataIntent() {
    let request: IntentRequest = new IntentRequest();
    request.currency = "usd";
    request.amount = 99;
    request.sources.push('card');
    return request;
  }
  async onSubmit() {
    // const { token, error } = await this.stripe.createToken(this.card);
    // if (error) {
    //   console.log('Something is wrong:', error);
    // } else {
    //   console.log('Success!', token);
    //   this.pmt.charge(this.createMockData(token)).subscribe((res) => {
    //     console.log("response:", res);
    //   });
    // }

    const { paymentIntent, error } = await this.stripe.handleCardPayment(
      this.client_secret, this.card, {
        source_data: {
          owner: {
            name: 'Jane Doe',
            email: 'janedoe@example.com',
            address: {
              line1: '123 Foo St.',
              postal_code: '94103',
              country: 'US'
            }
          }
        }
      }
    );

    if (error) {
      console.log("error :", error);
    } else {
      console.log("Success :", paymentIntent);
    }
  }
}