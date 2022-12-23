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

import { Component, Input, ViewEncapsulation } from '@angular/core';
import {
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
} from '@angular/forms';
import { BaseInput } from '../base-input/base-input.abstract';

@Component({
  selector: 'ua-edit-text',
  template: `
    <div class="my-2">
      <label [for]="id" class="form-label">{{ label }}</label>
      <input
        [type]="type"
        class="form-control"
        [id]="id"
        [name]="id"
        [value]="value"
        [mask]="mask"
        [prefix]="prefix"
        [suffix]="suffix"
        [placeholder]="placeholder"
        [disabled]="disabled"
        (change)="onInputChange($event)"
        [class.is-invalid]="hasErrors"
      />
      <div class="invalid-feedback" *ngIf="hasErrors">{{errorMessage}}</div>
    </div>
  `,
  styles: [],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: EditTextComponent,
      multi: true,
    },
    {
      provide: NG_VALIDATORS,
      useExisting: EditTextComponent,
      multi: true,
    },
  ],
})
export class EditTextComponent extends BaseInput<string> {

  @Input() type = 'text';
  @Input() placeholder = '';
  @Input() mask = '';
  @Input() prefix = '';
  @Input() suffix = '';

  defaultValue(): string {
    return '';
  }

}
