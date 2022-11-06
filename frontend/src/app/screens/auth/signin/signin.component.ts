import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder } from '@angular/forms';
import { select, Store } from '@ngrx/store';
import { AppState } from '@root/app/store/app.state';
import { Observable, of } from 'rxjs';
import { signinFormModel } from './signin.form';
import { signin, signinFail } from '@store/session/session.actions'
import { selectAuthError } from '@root/app/store/session/session.selectors';

@Component({
  selector: 'ua-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  signinForm = signinFormModel(this.formBuilder);

  signinErrorMessage$?: Observable<string>;

  constructor(
    private readonly formBuilder: NonNullableFormBuilder,
    private readonly store: Store<AppState>,
    private cd: ChangeDetectorRef
  ) {
  }

  ngOnInit() {
    this.signinErrorMessage$ = this.store.pipe(select(selectAuthError));
  }

  doSignin(): void {
    console.log('Exec doSignin');
    if(this.signinForm.valid) {
      this.store.dispatch(signin(this.signinForm.value));
    } else {
      this.store.dispatch(signinFail({errorMessage: 'Please enter valid data!'}));
    }
    this.cd.detectChanges();
  }

}
