import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IncomeexpensesinputformComponent } from './incomeexpensesinputform.component';

describe('IncomeexpensesinputformComponent', () => {
  let component: IncomeexpensesinputformComponent;
  let fixture: ComponentFixture<IncomeexpensesinputformComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IncomeexpensesinputformComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IncomeexpensesinputformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
