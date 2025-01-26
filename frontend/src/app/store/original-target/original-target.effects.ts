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
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { Action, Store } from "@ngrx/store";
import { Observable, of } from "rxjs";
import { switchMap, tap, withLatestFrom } from "rxjs/operators";
import { AppState } from "../app.state";
import * as OriginalTargeActions from "./original-target.actions";
import { selectOriginalTargetEndpoint } from "./original-target.selectors";

@Injectable()
export class OriginalTargetEffects {
  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private router: Router
  ) {}

  authorize$: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(OriginalTargeActions.authorize),
        tap((_) => this.router.navigate(["/signin"]))
      ),

    { dispatch: false }
  );

  allow$: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(OriginalTargeActions.allow),
        switchMap((_) =>
          this.store.select(selectOriginalTargetEndpoint).pipe(
            tap((target) => console.log(`Returning back to ${target}`)),
            tap((target) => this.router.navigate([target])),
            switchMap((_) => of(OriginalTargeActions.complete()))
          )
        )
      ),
    { dispatch: false }
  );
}
