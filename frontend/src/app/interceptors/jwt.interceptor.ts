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
import {
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from "@angular/common/http";
import { HttpEvent } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AppState } from "@app/store/app.state";
import { selectToken } from "@app/store/session/session.selectors";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { first, mergeMap } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class JwtInterceptor implements HttpInterceptor {
  constructor(private readonly store: Store<AppState>) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return this.store.select(selectToken).pipe(
      first(),
      mergeMap((token) => {
        const authReq = !!token ? this.enhenceRequestWithAuth(req, token) : req;

        console.log(`Enhenced req: ${JSON.stringify(authReq)}`);
        return next.handle(authReq);
      })
    );
  }

  private enhenceRequestWithAuth(req: HttpRequest<any>, token: string) {
    const headers = req.headers;
    headers.set("Content-Type", "application/json");
    headers.set("Authorization", `Bearer ${token}`);
    return req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }
}
