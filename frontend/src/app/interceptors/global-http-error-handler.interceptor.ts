/*
 Copyright 2022 engilyin

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

import {
  HttpEvent,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse,
  HttpInterceptor,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { AppState } from '../store/app.state';
import { signoff } from '../store/session/session.actions';

@Injectable({
  providedIn: 'root',
})
export class GlobalHttpErrorHandlerInterceptor implements HttpInterceptor {
  constructor(private readonly store: Store<AppState>) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      retry(1),
      catchError((error: HttpErrorResponse) => {
        let errorMessage = '';
        console.error(error);

        if (this.handleServerSide(error)) {
          errorMessage = 'You should sign-in and repeat again.';
        } else {
          errorMessage = `Error Status: ${error.status} - ${error.statusText}\nMessage: ${error.message}`;
        }

        errorMessage = error?.error?.message ?? errorMessage;

        console.log('throw error back to to the subscriber');
        return throwError(() => errorMessage);
      })
    );
  }

  handleServerSide(error: HttpErrorResponse): boolean {
    switch (error.status) {
      case 401: //signin
        this.store.dispatch(signoff());
        console.log(`redirect to signin`);
        return true;
      case 403: //forbidden
        this.store.dispatch(signoff());
        console.log(`redirect to signin`);
        return true;
    }
    return false;
  }
}
