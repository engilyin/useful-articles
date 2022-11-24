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
