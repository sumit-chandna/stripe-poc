import { Component, OnDestroy, ViewChild, ChangeDetectorRef, OnInit } from '@angular/core';
import { AfterViewInit, ElementRef } from '@angular/core';
import { PaymentService } from './services/payment/payment.service';
import { IntentRequest } from './model/IntentRequest';
import { CartSummaryDTO } from './model/cardSummaryDTO';
import { AppSettings } from './model/AppSettings';
import { BuyerDetails } from './model/BuyerDetails';
import { Address } from './model/address';
import { PaymentRequest } from './model/PaymentRequest';
const style = {
  base: {
    // Add your base input styles here. For example:
    fontSize: '16px',
    color: '#32325d',
  }
};
const options = {
  style: style,
  supportedCountries: ['SEPA'],
  placeholderCountry: 'DE',
};
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit, OnDestroy, OnInit {
  @ViewChild('cardInfo') cardInfo: ElementRef;
  @ViewChild('ibanInfo') ibanInfo: ElementRef;
  cartSummary: CartSummaryDTO = new CartSummaryDTO();
  bank: any;
  msg: string;
  card: any;
  cardHandler = this.onChange.bind(this);
  bankHandler = this.onChange.bind(this);
  error: string;
  stripe: any;
  elements: any;
  selectedPaymentMethod;
  paymentMethods: string[] = [];
  client_secret: string;
  pollCount = 0;

  ngOnInit() {
    this.cartSummary.totalAmount = 99;
    this.cartSummary.totalQuantity = 100;
  }
  constructor(private cd: ChangeDetectorRef, private pmt: PaymentService) { }
  ngAfterViewInit() {
    this.stripe = this.pmt.stripe;
    this.elements = this.stripe.elements();
    this.pmt.getPaymentMethods(AppSettings.DEFAULT_CURRENCY, AppSettings.DEFAULT_COUNTRY).subscribe((res) => {
      this.paymentMethods = res;
      this.pmt.createIntent(this.createMockDataIntent(AppSettings.PAYMENT_INTENT_SOURCES))
        .subscribe((response) => {
          this.client_secret = response.client_secret;
        });
      if (this.paymentMethods.includes('card')) {
        this.card = this.elements.create('card');
        this.createCard();
      }
      if (this.paymentMethods.includes('iban')) {
        this.bank = this.elements.create('iban', options);
        this.createIban();
      }
    });
  }
  handleChange() {
    if (this.paymentMethods.includes('card')) {
      this.card.clear();
    }
    if (this.paymentMethods.includes('iban')) {
      this.bank.clear();
    }
  }
  createIban() {
    this.bank.mount(this.ibanInfo.nativeElement);
    this.bank.addEventListener('change', this.bankHandler);
  }
  createCard() {
    this.card.mount(this.cardInfo.nativeElement);
    this.card.addEventListener('change', this.cardHandler);
  }
  ngOnDestroy() {
    this.card.removeEventListener('change', this.cardHandler);
    this.card.destroy();
    this.bank.removeEventListener('change', this.cardHandler);
    this.bank.destroy();
  }

  onChange({ error }) {
    if (error) {
      this.error = error.message;
    } else {
      this.error = null;
    }
    this.cd.detectChanges();
  }
  async payAndPlaceOrder() {
    const owner = this.creatMockOwner();
    if (this.selectedPaymentMethod === 'buyerCredit') {
      console.log('handle on server');
      this.msg = 'handle on server';
    } else if (this.selectedPaymentMethod === 'card') {
      const { paymentIntent, error } = await this.stripe.handleCardPayment(
        this.client_secret, this.card, {
          source_data: {
            owner: owner
          }
        }
      );
      if (error) {
        console.log('error :', error['message']);
        this.msg = 'error :' + error;
        this.pmt.savePaymentRequest(this.createPaymentRequest(null, null, null, this.creatMockOwner().email))
          .subscribe((res) => console.log(res));
      } else {
        console.log('Success :', paymentIntent);
        this.pmt.savePaymentRequest(this.createPaymentRequest(paymentIntent['source'], null, paymentIntent['id'],
          this.creatMockOwner().email)).subscribe((res) => console.log(res));
        this.msg = 'redirect to confirmation page';
      }
    } else if (this.selectedPaymentMethod === 'iban') {
      const sourceData = {
        type: 'sepa_debit',
        currency: 'eur',
        owner: owner,
        mandate: {
          notification_method: 'email',
        }
      };
      const that = this;
      this.stripe.createSource(this.bank, sourceData).then(function (result) {
        if (result.error) {
          that.msg = 'error :' + result.error['message'];
        } else {
          that.pmt.savePaymentRequest(that.createPaymentRequest(result.source['id'], null, null,
            owner.email))
            .subscribe((res) => {
              console.log(res);
              that.msg = 'redirect to confirmation page';
            });

        }
      });
    }
  }
  private createPaymentRequest(sourceId: string, customeMsg: string,
    intentId: string, email: string) {
    const request: PaymentRequest = new PaymentRequest();
    request.currency = AppSettings.DEFAULT_CURRENCY;
    request.amount = this.cartSummary.totalAmount;
    request.paymentMethod = this.selectedPaymentMethod;
    request.chargeId = sourceId;
    request.customMessage = customeMsg;
    request.email = email;
    request.paymentIntentId = intentId;
    if (this.selectedPaymentMethod === 'iban') {
      request.capture = true;
    } else {
      request.capture = false;
    }
    return request;
  }
  private createMockDataIntent(data: any) {
    const request: IntentRequest = new IntentRequest();
    request.currency = AppSettings.DEFAULT_CURRENCY;
    request.amount = this.cartSummary.totalAmount;
    request.sources = data;
    return request;
  }
  private creatMockOwner() {
    const owner: BuyerDetails = new BuyerDetails();
    owner.name = 'Jane Doe';
    owner.email = 'janedoe@example.com';
    const address: Address = new Address();
    address.line1 = '123 Foo St.';
    address.postal_code = '94103';
    address.country = 'US';
    owner.address = address;
    return owner;
  }
}
