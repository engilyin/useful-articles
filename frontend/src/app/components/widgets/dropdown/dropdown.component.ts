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
import { Component, Input, ViewEncapsulation } from "@angular/core";
import { NG_VALIDATORS, NG_VALUE_ACCESSOR } from "@angular/forms";
import { BaseInput } from "../base-input/base-input.abstract";

@Component({
  selector: "ua-dropdown",
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
export class DropdownComponent extends BaseInput<string> {
  @Input() values!: { [key: string]: string };

  defaultValue(): string {
    return "";
  }

  get valueKeys() {
    return Object.keys(this.values);
  }
}
