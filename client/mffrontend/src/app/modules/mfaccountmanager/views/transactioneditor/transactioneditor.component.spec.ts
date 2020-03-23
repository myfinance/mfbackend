import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactioneditorComponent } from './transactioneditor.component';

describe('TransactioneditorComponent', () => {
  let component: TransactioneditorComponent;
  let fixture: ComponentFixture<TransactioneditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransactioneditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactioneditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
