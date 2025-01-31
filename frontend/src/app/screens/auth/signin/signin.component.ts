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
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { NonNullableFormBuilder } from "@angular/forms";
import { select, Store } from "@ngrx/store";
import { AppState } from "@root/app/store/app.state";
import { Observable, of, takeUntil } from "rxjs";
import { signinFormModel } from "./signin.form";
import { signin, signinFail } from "@store/session/session.actions";
import { selectAuthError } from "@root/app/store/session/session.selectors";
import { ResourceHolder } from "@root/app/components/sys/resource-holder";

@Component({
  selector: "ua-signin",
  templateUrl: "./signin.component.html",
  styleUrls: ["./signin.component.scss"],
})
export class SigninComponent extends ResourceHolder implements OnInit {
  signinForm = signinFormModel(this.formBuilder);

  signinErrorMessage$?: Observable<string>;

  constructor(
    private readonly formBuilder: NonNullableFormBuilder,
    private readonly store: Store<AppState>,
    private cd: ChangeDetectorRef
  ) {
    super();
  }

  ngOnInit() {
    this.signinErrorMessage$ = this.store.pipe(
      select(selectAuthError),
      takeUntil(this.destroy$)
    );
  }

  doSignin(): void {
    console.log("Exec doSignin");
    if (this.signinForm.valid) {
      this.store.dispatch(signin(this.signinForm.value));
    } else {
      this.store.dispatch(
        signinFail({ errorMessage: "Please enter valid data!" })
      );
    }
    this.cd.detectChanges();
  }
}
