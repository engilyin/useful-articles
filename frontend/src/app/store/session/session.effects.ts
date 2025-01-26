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
import { signinFail } from "./session.actions";
import { busy, ready } from "./../global-progress/global-progress.actions";
import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { Observable, of } from "rxjs";
import { Action, Store } from "@ngrx/store";
import { catchError, map, switchMap, tap } from "rxjs/operators";
import { AppState } from "@store/app.state";
import { Router } from "@angular/router";
import * as SessionActions from "@store/session/session.actions";
import { SigninService } from "@root/app/services/auth/signin/signin.service";
import { allow } from "../original-target/original-target.actions";

@Injectable()
export class SessionEffects {
  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private router: Router,
    private signService: SigninService
  ) {}

  signin$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(SessionActions.signin),
      tap((action) => this.store.dispatch(busy())),
      switchMap((action) =>
        this.signService
          .signin({
            username: action.username ?? "",
            password: action.password ?? "",
          })
          .pipe(
            map((data) => SessionActions.signinSuccess(data)),
            catchError((errorMessage) =>
              of(SessionActions.signinFail({ errorMessage: errorMessage }))
            )
          )
      )
    )
  );

  signinSuccess$: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(SessionActions.signinSuccess),
        tap(() => this.store.dispatch(ready())),
        tap(() => this.store.dispatch(allow()))
      ),
    { dispatch: false }
  );

  signinFail$: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(SessionActions.signinFail),
        tap(() => this.store.dispatch(ready()))
      ),
    { dispatch: false }
  );

  signoff$: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(SessionActions.signoff),
        tap(() => this.router.navigate(["/signin"]))
      ),
    { dispatch: false }
  );
}
