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

import { authorize } from './../../store/original-target/original-target.actions';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from '@root/app/store/app.state';
import { selectLoggedin } from '@root/app/store/session/session.selectors';
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private readonly store: Store<AppState>,
    private readonly router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    return this.store.select(selectLoggedin).pipe(
      mergeMap(loggedIn => {
        if (!loggedIn) {
          let backTarget = this.router.getCurrentNavigation()?.extractedUrl.toString()!;
          console.log(`Authorization required. proceed to signin screen. Remember to return to: ${backTarget}`)
          this.store.dispatch(authorize({ target: backTarget}));
        }
        return of(loggedIn);
      })
    );
  }

}
