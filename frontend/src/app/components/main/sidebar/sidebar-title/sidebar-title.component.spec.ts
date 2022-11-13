import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarTitleComponent } from './sidebar-title.component';

describe('SidebarTitleComponent', () => {
  let component: SidebarTitleComponent;
  let fixture: ComponentFixture<SidebarTitleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SidebarTitleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SidebarTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
