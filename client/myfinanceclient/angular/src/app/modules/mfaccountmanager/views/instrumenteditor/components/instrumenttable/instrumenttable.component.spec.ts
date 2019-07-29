import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstrumenttableComponent } from './instrumenttable.component';

describe('InstrumenttableComponent', () => {
  let component: InstrumenttableComponent;
  let fixture: ComponentFixture<InstrumenttableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstrumenttableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstrumenttableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
