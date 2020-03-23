import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactiontableComponent } from './transactiontable.component';

describe('TransactiontableComponent', () => {
  let component: TransactiontableComponent;
  let fixture: ComponentFixture<TransactiontableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransactiontableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactiontableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
