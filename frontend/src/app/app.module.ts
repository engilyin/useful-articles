/*
 Copyright 2022-2025 engilyin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { SigninComponent } from "./screens/auth/signin/signin.component";
import { ProgressCurtainComponent } from "./components/sys/progress-curtain/progress-curtain/progress-curtain.component";
import { JwtInterceptor } from "./interceptors/jwt.interceptor";
import { RootStoreModule } from "./store/root-store.module";
import { MainComponent } from "./screens/main/_node/main.component";
import { HeaderComponent } from "./components/main/header/header/header.component";
import { FooterComponent } from "./components/main/footer/footer/footer.component";
import { SidebarComponent } from "./components/main/sidebar/sidebar/sidebar.component";
import { SidebarTitleComponent } from "./components/main/sidebar/sidebar-title/sidebar-title.component";
import { GlobalHttpErrorHandlerInterceptor } from "./interceptors/global-http-error-handler.interceptor";

@NgModule({
  declarations: [
    AppComponent,
    SigninComponent,
    ProgressCurtainComponent,
    MainComponent,
    HeaderComponent,
    FooterComponent,
    SidebarComponent,
    SidebarTitleComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    RootStoreModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: GlobalHttpErrorHandlerInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
