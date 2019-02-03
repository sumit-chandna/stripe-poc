import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { PaymentService } from './services/payment/payment.service';
import { HttpClientModule } from '@angular/common/http';
import { AppErrorHandler } from './common/app-error-handler';

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule
    ],
    providers: [PaymentService, {
        provide: ErrorHandler,
        useClass: AppErrorHandler
    }],
    bootstrap: [AppComponent]
})
export class AppModule { }
