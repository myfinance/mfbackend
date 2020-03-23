import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CashflowtableComponent } from './cashflowtable.component';

describe('CashflowtableComponent', () => {
  let component: CashflowtableComponent;
  let fixture: ComponentFixture<CashflowtableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CashflowtableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CashflowtableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
