import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InputformselectionComponent } from './inputformselection.component';

describe('InputformselectionComponent', () => {
  let component: InputformselectionComponent;
  let fixture: ComponentFixture<InputformselectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InputformselectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InputformselectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
