import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { Action, Store } from "@ngrx/store";
import { Observable, of } from "rxjs";
import { switchMap, tap, withLatestFrom } from "rxjs/operators";
import { AppState } from "../app.state";
import * as OriginalTargeActions from './original-target.actions';
import { selectOriginalTargetEndpoint } from "./original-target.selectors";



@Injectable()
export class OriginalTargetEffects {

    constructor(
        private actions$: Actions,
        private store: Store<AppState>,
        private router: Router) { }

    authorize$: Observable<Action> = createEffect(() =>
        this.actions$.pipe(
            ofType(OriginalTargeActions.authorize),
            tap((_) => this.router.navigate(['/signin']))),

            {dispatch: false}
    );

    allow$: Observable<Action> = createEffect(() =>
        this.actions$.pipe(
            ofType(OriginalTargeActions.allow),
            switchMap((_) => this.store.select(selectOriginalTargetEndpoint).pipe(
              tap((target) => console.log(`Returning back to ${target}`)),
              tap((target) => this.router.navigate([target])),
              switchMap((_) => of(OriginalTargeActions.complete()))))
        ),
        {dispatch: false}
    );
}
