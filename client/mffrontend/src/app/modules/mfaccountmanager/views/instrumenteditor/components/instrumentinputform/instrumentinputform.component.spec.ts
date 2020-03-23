import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstrumentinputformComponent } from './instrumentinputform.component';

describe('InstrumentinputformComponent', () => {
  let component: InstrumentinputformComponent;
  let fixture: ComponentFixture<InstrumentinputformComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstrumentinputformComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstrumentinputformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
