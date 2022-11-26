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

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { AppState } from '@store/app.state';

import { SidebarTitleComponent } from './sidebar-title.component';

describe('SidebarTitleComponent', () => {
  let store: MockStore<AppState>;

  let component: SidebarTitleComponent;
  let fixture: ComponentFixture<SidebarTitleComponent>;

  beforeEach(async () => {
    const initialState = {};

    await TestBed.configureTestingModule({
      declarations: [ SidebarTitleComponent ],
      providers: [
        provideMockStore({ initialState })
      ]
    })
    .compileComponents();

    store = TestBed.inject(MockStore);

    fixture = TestBed.createComponent(SidebarTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
