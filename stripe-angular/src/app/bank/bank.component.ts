import { Component, OnDestroy, ViewChild, ChangeDetectorRef } from '@angular/core';
import { AfterViewInit, ElementRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { PaymentService } from '../services/payment/payment.service';
const style = {
  base: {
    // Add your base input styles here. For example:
    fontSize: '16px',
    color: "#32325d",
  }
};
const options = {
  style: style,
  supportedCountries: ['SEPA'],
  // If you know the country of the customer, you can optionally pass it to
  // the Element as placeholderCountry. The example IBAN that is being used
  // as placeholder reflects the IBAN format of that country.
  placeholderCountry: 'DE',
}
@Component({
  selector: 'app-bank',
  templateUrl: './bank.component.html',
  styleUrls: ['./bank.component.css']
})
export class BankComponent implements AfterViewInit, OnDestroy {
  @ViewChild('ibanInfo') ibanInfo: ElementRef;

  bank: any;
  bankHandler = this.onChange.bind(this);
  error: string;
  stripe: any;
  elements: any;
  orderAmt = 323;
  constructor(private cd: ChangeDetectorRef, private pmt: PaymentService) { }

  ngAfterViewInit() {
    this.stripe = this.pmt.stripe;
    var elements = this.stripe.elements();
    this.createIban(elements);
  }

  createIban(element: any) {
    this.bank = element.create('iban', options);
    this.bank.mount(this.ibanInfo.nativeElement);
    this.bank.addEventListener('change', this.bankHandler);
  }
  ngOnDestroy() {
    this.bank.removeEventListener('change', this.bankHandler);
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

  async onSubmit(form: NgForm) {
    var sourceData = {
      type: 'sepa_debit',
      currency: 'eur',
      mandate: {
        // Automatically send a mandate notification email to your customer
        // once the source is charged.
        notification_method: 'email',
      },
    };
    await this.stripe.createSource(this.bank, sourceData).then(function (result) {
      if (result.error) {
        console.log('Something is wrong:', result.error);
      } else {
        console.log('Success!', result.source);
        this.pmt.charge(result.source).subscribe((res) => {
          console.log("response:", res);
        });
      }
    });
  }
}