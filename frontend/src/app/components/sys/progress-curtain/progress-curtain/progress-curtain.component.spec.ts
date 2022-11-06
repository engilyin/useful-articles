import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressCurtainComponent } from './progress-curtain.component';

describe('ProgressCurtainComponent', () => {
  let component: ProgressCurtainComponent;
  let fixture: ComponentFixture<ProgressCurtainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProgressCurtainComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProgressCurtainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
