import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstrumenteditorComponent } from './instrumenteditor.component';

describe('InstrumenteditorComponent', () => {
  let component: InstrumenteditorComponent;
  let fixture: ComponentFixture<InstrumenteditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstrumenteditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstrumenteditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
