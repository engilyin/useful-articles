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

import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { BaseInput } from '../base-input/base-input.abstract';

@Component({
  selector: 'ua-checkbox',
  template: `
    <div class="col-12">
      <div class="form-check">
        <input
          class="form-check-input"
          type="checkbox"
          [value]="value"
          [id]="id"
          [name]="id"
          [disabled]="disabled"
          [class.is-invalid]="hasErrors"
          (change)="onInputChange($event)"
        />
        <label class="form-check-label" [for]="id">
          {{ label }}
        </label>
        <div class="invalid-feedback" *ngIf="hasErrors">{{ errorMessage }}</div>
      </div>
    </div>
  `,
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: CheckboxComponent,
      multi: true,
    },
    {
      provide: NG_VALIDATORS,
      useExisting: CheckboxComponent,
      multi: true,
    },
  ],
})
export class CheckboxComponent extends BaseInput<boolean> {

  defaultValue(): boolean {
    return false;
  }

  override onInputChange(event: any) {
    this.updateValue(event.target.checked);
  }
}
