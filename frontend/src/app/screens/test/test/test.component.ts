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

import { Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { ThemeService } from '@root/app/services/sys/theme/theme.service';
import { AppState } from '@root/app/store/app.state';
import * as SessionActions from '@store/session/session.actions';

@Component({
  selector: 'ua-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.scss'],
})
export class TestComponent implements OnInit {

  errorMessages = {
    required: "Required field!",
    email: "Valid email required."
  }

  form = this.formBuilder.group({
    testText: ['', [Validators.required]],
    passTest: ['', [Validators.required]],
  });

  constructor(
    private readonly store: Store<AppState>,
    private themeService: ThemeService,
    private readonly formBuilder: NonNullableFormBuilder
  ) {}

  ngOnInit(): void {}

  lightTheme() {
    this.themeService.switchTheme('light');
  }

  darkTheme() {
    this.themeService.switchTheme('dark');
  }

  doSignoff() {
    this.store.dispatch(SessionActions.signoff());
  }

  test() {
    if (this.form.valid) {
      console.log(`Valid ${JSON.stringify(this.form.value)}`);
    } else {
      console.log('Invalid');
    }
  }
}
