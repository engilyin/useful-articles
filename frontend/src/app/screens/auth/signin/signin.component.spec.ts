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
import { FormGroup, ReactiveFormsModule } from "@angular/forms";
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

import { ComponentFixture, TestBed } from "@angular/core/testing";
import { NonNullableFormBuilder } from "@angular/forms";
import { MockStore, provideMockStore } from "@ngrx/store/testing";
import { AppState } from "@store/app.state";

import { SigninComponent } from "./signin.component";

describe("SigninComponent", () => {
  let formBuilder: NonNullableFormBuilder;
  let store: MockStore<AppState>;

  let component: SigninComponent;
  let fixture: ComponentFixture<SigninComponent>;

  beforeEach(async () => {
    const initialState = {};

    await TestBed.configureTestingModule({
      declarations: [SigninComponent],
      imports: [ReactiveFormsModule],
      providers: [NonNullableFormBuilder, provideMockStore({ initialState })],
    }).compileComponents();

    formBuilder = TestBed.inject(NonNullableFormBuilder); // get a handle on formBuilder

    store = TestBed.inject(MockStore);

    fixture = TestBed.createComponent(SigninComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
