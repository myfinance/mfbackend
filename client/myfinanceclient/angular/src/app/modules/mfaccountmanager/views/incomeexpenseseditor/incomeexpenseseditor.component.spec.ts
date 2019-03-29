import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IncomeexpenseseditorComponent } from './incomeexpenseseditor.component';

describe('IncomeexpenseseditorComponent', () => {
  let component: IncomeexpenseseditorComponent;
  let fixture: ComponentFixture<IncomeexpenseseditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IncomeexpenseseditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IncomeexpenseseditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
