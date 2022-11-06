import { signinFail } from './session.actions';
import { busy, ready } from './../global-progress/global-progress.actions';
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Observable, of } from 'rxjs';
import { Action, Store } from '@ngrx/store';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { AppState } from '@store/app.state';
import { Router } from '@angular/router';
import * as SessionActions from "@store/session/session.actions";
import { SigninService } from '@root/app/services/auth/signin/signin.service';
import { allow } from '../original-target/original-target.actions';

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
      tap(action => this.store.dispatch(busy())),
      switchMap((action) => this.signService.signin({"username": action.username ?? '', "password": action.password ?? ''})
        .pipe(
          map(data => SessionActions.signinSuccess(data)),
          catchError(error => of(
            SessionActions.signinFail({errorMessage: error.message})))
        )))
  );

  signinSuccess$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(SessionActions.signinSuccess),
      tap(() => this.store.dispatch(ready())),
      tap(() => this.store.dispatch(allow()))
    ),
    { dispatch: false}
  );

  signinFail$: Observable<Action> = createEffect(() =>
  this.actions$.pipe(
    ofType(SessionActions.signinFail),
    tap(() => this.store.dispatch(ready()))
  ),
  { dispatch: false}
);


  signoff$: Observable<Action> = createEffect(() =>
  this.actions$.pipe(
    ofType(SessionActions.signoff),
    tap(() => this.router.navigate(["/signin"]))
  ),
  { dispatch: false}
);

}

