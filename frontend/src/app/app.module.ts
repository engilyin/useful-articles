import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';


import {ButtonModule} from 'primeng/button';
import {CheckboxModule} from 'primeng/checkbox';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SigninComponent } from './screens/auth/signin/signin.component';
import { ProgressCurtainComponent } from './components/sys/progress-curtain/progress-curtain/progress-curtain.component';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { TestComponent } from './screens/test/test/test.component';
import { RootStoreModule } from './store/root-store.module';

@NgModule({
  declarations: [
    AppComponent,
    SigninComponent,
    ProgressCurtainComponent,
    TestComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    RootStoreModule,
    ButtonModule,
    CheckboxModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptor,
    multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
