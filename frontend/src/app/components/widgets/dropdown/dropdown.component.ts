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
  AbstractControl,
  ControlValueAccessor,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator,
} from '@angular/forms';

@Component({
  selector: 'ua-dropdown',
  template: `
    <div class="my-2">
      <label for="category">Category</label>
      <select
        class="form-select"
        [id]="id"
        [name]="id"
        [disabled]="disabled"
        [class.is-invalid]="hasErrors"
        (change)="onInputChange($event)"
      >
        <option selected="" disabled="" value="">Choose...</option>
        <option [value]="key" *ngFor="let key of valueKeys">
          {{ values[key] }}
        </option>
      </select>
      <div class="invalid-feedback" *ngIf="hasErrors">{{ errorMessage }}</div>
    </div>
  `,
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: DropdownComponent,
      multi: true,
    },
    {
      provide: NG_VALIDATORS,
      useExisting: DropdownComponent,
      multi: true,
    },
  ],
})
export class DropdownComponent implements ControlValueAccessor, Validator {
  @Input() id!: string;
  @Input() label = '';
  @Input() placeholder = '';
  @Input() disabled = false;
  @Input() value = '';
  @Input() values!: { [key: string]: string };
  @Input() errors: { [key: string]: string } = {};

  hasErrors = false;
  touched = false;

  onChange: any = () => {};
  onTouched: any = () => {};

  errorMessage = '';

  get valueKeys() {
    return Object.keys(this.values);
  }

  onInputChange(event: any) {
    this.updateValue(event.target.value);
  }

  updateValue(val: any) {
    this.value = val;
    this.onChange(val);
    this.markAsTouched();
  }

  registerOnChange(onChange: any) {
    this.onChange = onChange;
  }

  registerOnTouched(onTouched: any) {
    this.onTouched = onTouched;
  }

  markAsTouched() {
    this.onTouched();
    this.touched = true;
    this.onChange(this.value);
  }

  writeValue(value: any) {
    if (value) {
      this.value = value;
    }
  }

  setDisabledState?(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  validate(control: AbstractControl<any, any>): ValidationErrors | null {
    this.hasErrors = control.touched && !!control.errors;
    this.errorMessage = '';
    if (this.hasErrors) {
      Object.keys(control.errors!).forEach((key) => {
        this.errorMessage += this.errors[key];
        +' ';
      });
    }
    return null;
  }
}
